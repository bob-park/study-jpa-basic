package jpql.main;

import jpql.domain.Member;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class NamedMain {
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
      * Named Query
      *
      ! Application 로딩 시점에서 쿼리 검증
      */
      // * 특징
      // -> 1. 정적쿼리만 사용 가능
      // -> 2. 재사용 가능
      List<Member> resultList01 =
          em.createNamedQuery("Member.findByUserName", Member.class)
              .setParameter("username", memberA.getUsername())
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
