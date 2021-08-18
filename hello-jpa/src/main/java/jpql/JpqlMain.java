package jpql;

import javax.persistence.*;
import java.util.List;

public class JpqlMain {

  public static void main(String[] args) {
    // DB 당 1개만 생성됨 - Container 가 생성 시점에 생성됨
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    // * 트랜잭션 단위, 또는 작업 단위마다 EntityManager 를 생성하여 사용해야한다.
    // * 쓰레드간 공유가 되지 않는다.
    EntityManager em = emf.createEntityManager();

    // * JPA 의 모든 변경은 트랜잭션 안에서 실행되어야 한다.
    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      Member memberA = new Member();
      memberA.setUsername("memberA");
      memberA.setAge(10);

      em.persist(memberA);

      // * Type Query - Query
      TypedQuery<Member> typeQuery1 =
          em.createQuery("select m from Member m", Member.class); // Type 이 명확할 때 사용
      Query query1 = em.createQuery("select m.username, m.age from Member m"); // Type 이 명확하지 않을 때

      // * 결과 조회
      List<Member> resultList1 = typeQuery1.getResultList(); // 하나 이상일때, 결과가 없는 경우 empty list 반환

      // 없는 경우 -> NoResultException - 결과가 없는 경우를 사용하고 싶을 경우 try ~ catch 해야함
      // 두개 이상인 경우 -> NonUniqueResultException
      Member singleResult1 = typeQuery1.getSingleResult(); // 정확히 하나일 때 - 따라서 값이 보장될 때 사용해야한다.

      // * 파라미터 바인딩
      TypedQuery<Member> typeQuery2 =
          em.createQuery("select m from Member m where m.username=:username", Member.class);
      typeQuery2.setParameter("username", "memberA");

      Member findMemberA = typeQuery2.getSingleResult();

      System.out.println("findMemberA.name = " + findMemberA.getUsername());

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
