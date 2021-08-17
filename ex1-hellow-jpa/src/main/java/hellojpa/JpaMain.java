package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
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

      Address address = new Address("city", "street", "zipcode");

      Member memberA = new Member();
      memberA.setName("memberA");
      memberA.setAddress(address);
      memberA.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));

      em.persist(memberA);

      Member memberB = new Member();
      memberB.setName("memberB");
      memberB.setAddress(address);

      em.persist(memberB);

      // ! Embedded Type 사용시 주의사항
      // 같은 객채를 수정해버린 경우 값이 공유되어, 해당 객채의 주소값을 가진 entity 들이 모두 변경될 수 있다.
      // 따라서, 불변객체로 만들어야 한다.
//      memberA.getAddress().setCity("newCity");

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
