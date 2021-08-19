package jpql.main;

import jpql.domain.Member;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      Team teamA = new Team();
      teamA.setName("teamA");

      em.persist(teamA);

      Team teamB = new Team();
      teamB.setName("teamB");

      em.persist(teamB);

      for (int i = 1; i < 3; i++) {
        Member member = new Member();
        member.setUsername("member" + i);
        member.setAge(10);

        teamA.addMember(member);

        em.persist(member);
      }

      Member member = new Member();
      member.setUsername("member3");
      member.setAge(10);

      teamB.addMember(member);

      em.persist(member);

      em.flush();
      em.clear();

      /*
       * 패치 조인
       */
      // 일반 - team 이 지연 로딩인 경우 team 이 이름을 가져올때 team query 가 실행된다.
      List<Member> resultList01 =
          em.createQuery("select m from Member m join m.team", Member.class).getResultList();

      for (Member item : resultList01) {
        System.out.println("member = " + item);
      }

      // ! 패치 조인 시 - 지연 로딩을 설정해도 지연로딩을 무시하고 즉시 로딩된다.
      // DB Query 실행시 join 은 하지만, 해당 entity 만 조회한다.
      List<Member> resultList02 =
          em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();

      for (Member item : resultList02) {
        System.out.println("member = " + item);
      }

      // * 컬렉션 패치 조인
      // 1:N 관계 - 데이터가 더 많이 나올 수 있다.
      // DB join Query 가 실행되기 때문에, 어쩔 수 없다.
      // JPA 에서 같은 데이터인 경우 영속성 컨텍스트에서 관리하기 때문에 하나로 관리되지만, 결과는 같은 데이터를 두번 내려준다.
      List<Team> resultList03 =
          em.createQuery("select t from Team t join fetch t.members", Team.class).getResultList();

      for (Team team : resultList03) {
        System.out.println("team = " + team.getName() + "|members=" + team.getMembers().size());
      }

      // * distinct - entity 중복 제거
      // 기능 2개  :
      // -> 1. DB SQL 에 distinct 추가 - DB 에서는 데이터가 모두 같아야 중복이 제거된다.(team 만 같다고 제거되진 않음)
      // -> 2. Application 에서 entity 중복 제거 - 같은 식별자를 가진 entity 를 제거하고 결과 리턴
      List<Team> resultList04 =
          em.createQuery("select distinct t from Team t join fetch t.members", Team.class)
              .getResultList();

      for (Team team : resultList04) {
        System.out.println("team = " + team.getName() + "|members=" + team.getMembers().size());
      }

      /*
       * 패치 조인의 특징과 한계
       */
      // * 패치 조인 대상에 별칭을 줄 수 없음
      // ! hibernate 에서는 가능하나, 가급적 사용 X
      // team 를 가진 member 가 5명인데, 조건으로 age > 3인 member 를 조회하여 3명이 조회되면, team.members() 도 3명이 나옴.
      // 하지만, 객체 지향적으로 봤을 떄, team.members 가 해당 team 를 가진 모든 member 를 가지게 설계되어 있으므로,
      // 이런 상황에서 JPA 영속성 컨텍스트가 보장해주지 않기 때문
      // * 이런 상황에서는 따로 query 를 실행해야함
      em.createQuery("select t from Team t join fetch t.members m where m.age > 3", Team.class)
          .getResultList();

      // * 컬렉션 패치 조인할 경우 페이징 API 를 사용할 수 없다.
      // ! 1:N 연관관계인 경우 데이터가 항상 대상의 개수 이상으로 나오기 때문에, 페이징 할 수 없다.
      // ! 경고를 남기고, 메모리에서 페이징 처리함(매우 위험)
      // DB query 실행 시 페이징 쿼리가 없음
      em.createQuery("select t from Team t join fetch t.members", Team.class)
          .setFirstResult(1)
          .setMaxResults(1)
          .getResultList();

      em.flush();
      em.clear();

      // * 컬렉션 패치 조인 페이징
      // -> 1. @BatchSize 이용
      // list 의 team 의 id 를 DB query 중 in 키워드를 사용하여 @BatchSize 개수 만큼 처리하여 member 를 가져온다.
      // @BatchSize 보다 team 이 많은 경우 DB query 를 @BatchSize 만큼 실행 한 후 나머지 team 에 대해서 Query 를 실행한다.
      // ! @BatchSize 처럼 Local 로 설정할 수 있지만, 대부분 Global 로 설정한다. (hibernate.default_batch_fetch_size)
      // ! size 는 1000 이하로 설정해야 한다.
      // -> 2. 대상 entity 를 변경한다.(team -> member)
      // -> 3. DTO 를 만들어서 해당 데이터를 가공한다.
      List<Team> resultList05 =
          em.createQuery("select t from Team t", Team.class)
              .setFirstResult(0)
              .setMaxResults(2)
              .getResultList();

      for (Team team : resultList05) {
        System.out.println("team = " + team.getName() + "| members=" + team.getMembers().size());
        for (Member mItem : team.getMembers()) {
          System.out.println("mItem = " + mItem);
        }
      }

      tx.commit();
    } catch (Exception e) {
      e.printStackTrace();
      tx.rollback();
    } finally {
      // close
      em.close();
    }

    emf.close();
  }
}
