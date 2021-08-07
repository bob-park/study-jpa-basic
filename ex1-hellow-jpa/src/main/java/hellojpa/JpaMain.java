package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

      // 비영속 상태
      //      Member member = new Member();
      //      member.setId(100L);
      //      member.setName("HelloJPA");

      // 영속
      // * DB 저장 X
      // * Query 를 쌓아놈
      //      System.out.println("=== BEFORE ===");
      //      em.persist(member);
      //      System.out.println("=== AFTER ===");

      // * Entity Manager 에서 제거 - 준영속상태
      //      em.detach(member);

      // DB 에서 가져오지 않고, Entity Manager 에서 가져옴 - 없으면 DB 조회 후 Entity Manager 에 추가하여 가져옴
      //      Member findMember = em.find(Member.class, 100L);
      //
      //      System.out.println("findMember.id" + findMember.getId());
      //      System.out.println("findMember.name" + findMember.getName());

      // * 쓰기 지연
      //      em.persist(new Member(103L, "HelloJPA! 103"));
      //      em.persist(new Member(104L, "HelloJPA! 104"));
      //

      // * 변경 감지
//      Member member = em.find(Member.class, 104L);
//
//      member.setName("ZZZZ");

      // 이거 쓰면 안됨 - 써도 아무 이득이 없음
      //      em.persist(member);

      // * 플러쉬(flush)
      Member member = new Member(200L, "member200");

      em.persist(member);

      // 바로 SQL 실행됨
      em.flush();

      System.out.println("============");

      // * 쌓아논 Query 를 실행함
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
