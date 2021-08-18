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

      for (int i = 0; i < 100; i++) {
        Member member = new Member();
        member.setUsername("member" + i);
        member.setAge(i);
        em.persist(member);
      }

      Member memberA = new Member();
      memberA.setUsername("memberA");
      memberA.setAge(10);

      em.persist(memberA);

      // * Type Query - Query
      TypedQuery<Member> typeQuery1 =
          em.createQuery("select m from Member m where m.id=1", Member.class); // Type 이 명확할 때 사용
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

      em.flush();
      em.clear();

      // * entity 프로젝션 - 영속성 관리가 됨
      List<Member> findResult1 =
          em.createQuery("select m from Member m", Member.class).getResultList();

      Member findMember = findResult1.get(0);
      findMember.setAge(15);

      // ! 이런식으로 사용하면 예측하기 어렵기 때문에 사용하지 말고, join 을 명확하게 사용하자
      em.createQuery("select m.team from Member m", Team.class)
          .getResultList(); // join query 가 실행된다.

      // 이런식으로 사용하자
      em.createQuery("select t from Member m join m.team t", Team.class).getResultList();

      // * Embedded type Projection
      em.createQuery("select o.address from Order o", Address.class).getResultList();

      // * Scala type Projection
      em.createQuery("select m.username, m.age from Member m").getResultList();

      /*
       * Projection - 여러값 조회
       */
      // * Query 타입 조회
      List findResult2 = em.createQuery("select m.username, m.age from Member m").getResultList();

      Object o = findResult2.get(0);
      Object[] result1 = (Object[]) o;

      System.out.println("username = " + result1[0]);
      System.out.println("age = " + result1[1]);

      // * Object[] 타입  조회
      List<Object[]> findResult3 =
          em.createQuery("select m.username, m.age from Member m").getResultList();
      Object[] result2 = findResult3.get(0);

      System.out.println("username = " + result2[0]);
      System.out.println("age = " + result2[1]);

      // * new 명령어로 조회
      // ! 반드시 생성자가 필요함
      List<MemberDTO> findResult4 =
          em.createQuery(
                  "select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
              .getResultList();

      for (MemberDTO memberDTO : findResult4) {
        System.out.println("memberDTO.username = " + memberDTO.getUsername());
        System.out.println("memberDTO.age = " + memberDTO.getAge());
      }

      /*
       * Pagination
       */
      List<Member> findResult5 =
          em.createQuery("select m from Member m order by m.age desc", Member.class)
              .setFirstResult(1)
              .setMaxResults(10)
              .getResultList(); // 페이징 시 order by 를 꼭 넣어야 확인 가능

      System.out.println("findResult5.size = " + findResult5.size());

      for (Member member : findResult5) {
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
