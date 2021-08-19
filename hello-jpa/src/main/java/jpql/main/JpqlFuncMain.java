package jpql.main;

import jpql.domain.Member;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlFuncMain {

  public static void main(String[] args) {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      Team teamA = new Team();
      teamA.setName("teamA");

      em.persist(teamA);

      for (int i = 0; i < 100; i++) {
        Member member = new Member();
        member.setUsername("member" + i);
        member.setAge(i);

        teamA.addMember(member);

        em.persist(member);
      }

      em.flush();
      em.clear();

      /*
       * JPQL Function
       */
      // concat, substring, trim, lower, upper, length, locate, abs, sqrt, mod
      // ! size, index - JPA 에서만
      em.createQuery("select concat('a', 'b') from Member m", String.class).getResultList();
      em.createQuery("select substring(m.username, 2, 3) from Member m", String.class)
          .getResultList();
      // ... 기본적인 DB 에서 제공하는 함수는 사용할 수 있다.

      List<Integer> resultList1 =
          em.createQuery("select size(t.members) from Team t", Integer.class)
              .getResultList(); // list 의 사이즈 반환

      for (Integer integer : resultList1) {
        System.out.println("result = " + integer);
      }

      // * 사용자 정의 함수
      List<String> resultList2 =
          em.createQuery("select function('group_concat', m.username) from Member m", String.class)
              .getResultList();
      // hibernate 인 경우
      em.createQuery("select group_concat(m.username) from Member m")
          .getResultList(); // JPA 문법에 맞지 않음, 하지만 실행됨 (JPA 는 hibernate 를 확장한 것이기 때문)

      for (String s : resultList2) {
        System.out.println("result = " + s);
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
