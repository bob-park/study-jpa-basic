package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

  public static void main(String[] args) {

    // DB 당 1개만 생성됨 - Container 가 생성 시점에 생성됨
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    // * 트랜잭션 단위, 또는 작업 단위마다 EntityManager 를 생성하여 사용해야한다.
    // * 쓰레드간 공유가 되지 않는다.
    EntityManager em = emf.createEntityManager();

    // * JPA 의 모든 변경은 트랜잭션 안에서 실행되어야 한다.
    EntityTransaction tx = em.getTransaction();
    tx.begin();

    // code
    try {

      Team teamA = new Team();
      teamA.setName("TeamA");

      em.persist(teamA);

      Member memberA = new Member();

      memberA.setName("MemberA");
      memberA.setTeam(teamA);

      em.persist(memberA);

      // DB 에서 바로 가져오는 쿼리를 보고 싶을때
      em.flush();
      em.clear();

      Member findMember = em.find(Member.class, memberA.getId());

      for (Member m : findMember.getTeam().getMembers()) {
        System.out.println("m = " + m.getName());
      }

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      // close
      em.close();
    }

    emf.close();
  }
}
