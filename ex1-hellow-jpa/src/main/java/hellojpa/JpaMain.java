package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.Arrays;
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

      // * 객체를 새로 생성해야한다.
      Address newAddress = new Address("new City", address.getStreet(), address.getZipcode());

      memberB.setAddress(newAddress);

      // * Embedded Type 비교
      // equals 를 override 하지 않는 경우 당연히 false
      Address address1 = new Address(address.getCity(), address.getStreet(), address.getZipcode());
      System.out.println("address == address1 : " + (address == address1)); // false
      System.out.println("address == address1 : " + (address.equals(address1))); // true

      // * Collection Type 사용
      Address addressC = new Address("cityC", "streetC", "zipcodeC");

      Member memberC = new Member();

      memberC.setName("memberC");
      memberC.setAddress(addressC);

      memberC.getFavoriteFoods().add("치킨");
      memberC.getFavoriteFoods().add("족발");

      memberC.getAddressHistory().add(new Address("cityA", "streetA", "zipcodeA"));
      memberC.getAddressHistory().add(new Address("cityB", "streetB", "zipcodeB"));
      memberC.getAddressHistory().add(addressC);

      em.persist(memberC);

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
