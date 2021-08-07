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
      // * 추가
      Member member = new Member();
      member.setId(2L);
      member.setName("HelloB");

      em.persist(member);

      // * 조회
      Member findMember = em.find(Member.class, 1L);

      System.out.println("findMember.id = " + findMember.getId());
      System.out.println("findMember.name = " + findMember.getName());

      // * 삭제
      //            em.remove(findMember);

      // * 수정
      // ! 수정시 객체의 value만 변경하면, JPA 가 커밋 시점에 체크하고 변경부분이 있는 경우 update query 를 생성후 커밋 시점에 실행한다.
      findMember.setName("HelloJPA");

      // * 전체 조회 (JPQL - 객체 지향 쿼리)
      // Table 이 아닌, 객체가 대상이다.
      List<Member> result =
          em.createQuery("select m from Member as m", Member.class)
              // pagination start
              .setFirstResult(1)
              .setMaxResults(10)
              // pagination end
              .getResultList();

      for (Member m : result) {
        System.out.println("member.name = " + m.getName());
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
