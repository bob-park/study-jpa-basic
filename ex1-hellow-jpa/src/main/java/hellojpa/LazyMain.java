package hellojpa;

import javax.persistence.*;
import java.util.List;

public class LazyMain {

  public static void main(String[] args) {
    // DB 당 1개만 생성됨 - Container 가 생성 시점에 생성됨
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    // * 트랜잭션 단위, 또는 작업 단위마다 EntityManager 를 생성하여 사용해야한다.
    // * 쓰레드간 공유가 되지 않는다.
    EntityManager em = emf.createEntityManager();

    // * Persistence Util
    PersistenceUnitUtil puu = emf.getPersistenceUnitUtil();

    // * JPA 의 모든 변경은 트랜잭션 안에서 실행되어야 한다.
    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      Team team = new Team();
      team.setName("teamB");

      em.persist(team);

      Member member = new Member();
      member.setName("memberB");

      team.addMember(member);

      em.persist(member);

      em.flush();
      em.clear();

      // ! Fetch Type 이 Lazy - 지연로딩인 경우 - 비지니스 로직 중 member 의 team 을 아주 가끔 사용한다면 이거 씀
      // DB query 는 member 만 조회된다. (더 이상 team 과 join 하지 않는다)

      // ! 하지만, 실무에서는 Lazy 를 사용하길 권장
      Member findMember = em.find(Member.class, member.getId());

      System.out.println("findMember.name = " + findMember.getName());
      System.out.println("team = " + findMember.getTeam().getClass()); // Proxy 가 반환된다.

      em.flush();
      em.clear();

      // ! 즉시로딩인 경우, DB Query 가 N (부가 Query) + 1(초기 Query) 실행된다.
      // 이 문제의 해결방법
      // 1. Lazy Fetch 로 변경
      // 2. Fetch Join (JPQL) - 필요한 부분만 초기에 조인한다
      // 3. Batch Size.... Entity Graph....머 있단다. (나중에 나옴)
      List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

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
