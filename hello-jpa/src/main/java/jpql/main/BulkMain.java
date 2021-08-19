package jpql.main;

import jpql.domain.Member;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BulkMain {
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

      //      em.flush();
      //      em.clear();

      /*
       * 벌크 연산
       */
      // * 벌크 연산 실행 전 flush 자동 호출 된다.
      // update - delete 지원
      int resultCount = em.createQuery("update Member m set m.age = 20").executeUpdate();

      System.out.println("resultCount = " + resultCount);

      // * 주의
      // ! JPA 영속성 컨텍스트를 무시하고 DB 에 직접 Query 실행
      // * 해결방법
      // -> 1. 영속성 컨텍스트 초기화 전 벌크 연산 먼저 실행
      // -> 2. 벌크 연산 수행 후 영속성 컨텍스트 초기화
      Member findMember =
          em.find(Member.class, 2L); // 영속성 컨텍스트에서 가져온 member 이기 때문에, 벌크 연산이 적용이 안된 member 를 가져옴

      System.out.println("findMember = " + findMember);

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
