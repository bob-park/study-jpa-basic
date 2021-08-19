package jpql.main;

import jpql.domain.Member;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Jpql2Main {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      Team teamA = new Team();
      teamA.setName("teamA");

      em.persist(teamA);

      Member memberA = new Member();
      memberA.setUsername("memberA");
      memberA.setAge(10);

      teamA.addMember(memberA);

      em.persist(memberA);

      em.flush();
      em.clear();

      /*
      * 경로 표현식
      *
      ! 항상 명시적 조인을 사용해야 한다.
      */
      // * 상태 필드 - 경로 탐색 X
      em.createQuery("select m.username from Member m").getResultList();

      // * 단일값 연관 경로
      // team 은 단일 연관 경로이므로 탐색 가능
      // 묵시적 내부 조인이 발생 - 내부 조인만 가능
      // ! 조심히 사용해야한다. - 직관적이지 않기 때문에, 튜닝하기 어렵다.
      em.createQuery("select m.team from Member m").getResultList();
      em.createQuery("select m.team.name from Member m").getResultList();

      // * 컬렉션 값 연관 경로
      // ! 탐색 X
      em.createQuery("select t.members from Team t").getResultList(); // return Type - Collection

      // ! 명시적 조인인 경우 탐색 O
      em.createQuery("select m.username from Team t join t.members m")
          .getResultList(); // 별칭을 얻어 사용할 수 있음

      /*
       * Entity 직접 사용
       */
      // * entity 기본키 사용 시 - DB query 실행 시 entity 의 ID 로 변환되어 실행됨
      Member findMember =
          em.createQuery("select m from Member m where m = :member", Member.class)
              .setParameter("member", memberA)
              .getSingleResult();

      System.out.println("findMember = " + findMember);

      // * entity 외래키 사용시
      List<Member> resultList01 =
          em.createQuery("select m from Member m where m.team = :team", Member.class)
              .setParameter("team", teamA)
              .getResultList();

      for (Member member : resultList01) {
        System.out.println("member = " + member);
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
