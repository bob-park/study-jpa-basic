package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

      Member member = new Member();
      member.setUsername("memberA");

      em.persist(member);

      em.flush();
      em.clear();

      // * JPQL
      List<Member> resultList =
          em.createQuery("select m from Member m where m.username like '%A%'", Member.class)
              .getResultList();

      for (Member mem : resultList) {
        System.out.println("mem = " + mem);
      }

      // * Criteria
      CriteriaBuilder cb = em.getCriteriaBuilder();

      CriteriaQuery<Member> query = cb.createQuery(Member.class);

      // 루트 클래스 - 조회 시작할 클래스
      Root<Member> m = query.from(Member.class);

      // query 생성
      CriteriaQuery<Member> cq = query.select(m).where(cb.like(m.get("username"), "%A%"));

      List<Member> findMemberList = em.createQuery(cq).getResultList();

      for (Member item : findMemberList) {
        System.out.println("item = " + item);
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
