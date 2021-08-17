package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

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

      //      memberC.getFavoriteFoods().add("치킨");
      //      memberC.getFavoriteFoods().add("족발");

      //      memberC
      //          .getAddressHistory()
      //          .add(new AddressEntity(new Address("cityA", "streetA", "zipcodeA")));
      //      memberC
      //          .getAddressHistory()
      //          .add(new AddressEntity(new Address("cityB", "streetB", "zipcodeB")));
      //      memberC.getAddressHistory().add(new AddressEntity(addressC));

      AddressEntity addressEntity = new AddressEntity(addressC);

      memberC.addAddressHistory(new AddressEntity(new Address("cityA", "streetA", "zipcodeA")));
      memberC.addAddressHistory(new AddressEntity(new Address("cityB", "streetB", "zipcodeB")));
      memberC.addAddressHistory(addressEntity);

      em.persist(memberC);

      em.flush();
      em.clear();

      System.out.println("========== START ==========");

      // * Collection Type 은 기본이 지연 로딩
      Member findMember = em.find(Member.class, memberC.getId());

      //      for (Address item : findMember.getAddressHistory()) {
      //        System.out.println("address=" + item.getCity());
      //      }
      //
      //      for (String food : findMember.getFavoriteFoods()) {
      //        System.out.println("food=" + food);
      //      }

      // * 값 타입 수정 (Collection 포함)
      //      findMember.getAddress().setCity("new City"); // ! 이렇게 하면 절대 안됨. side effect 가 무조건 생김
      findMember.setAddress(
          new Address(
              "new City",
              addressC.getStreet(),
              addressC.getZipcode())); // 이렇게 불변 객체로 만들어서, 값을 아예 새로 생성해야함

      // collection type 은 지우고 새로 넣어야한다.
      //      findMember.getFavoriteFoods().remove("치킨");
      //      findMember.getFavoriteFoods().add("짬뽕");

      // Collection Type 인데 Embedded Type 의 Collection 인 경우
      /*
      ! 주의
      * JPA 에서 객체의 Collection Type 인 경우 모두 지우고 새로 insert 한다. - 변경에 대한 추적이 되지 않기 때문
      ! 따라서, 이걸 실무에서 사용하면 절대 안됨
      * 하지만, 방법은 있다 - @OrderColumn 을 사용하면 순서가 들어가서 되지만, 개발자의 의도와 상관없이 DB Query 가 실행될 수 있다.
      ! 실무에서는 Type 에 따라서 무조건 1:N 관계를 고려해야 한다.
      */
      boolean remove =
          findMember
              .getAddressHistory()
              .remove(
                  findMember.getAddressHistory().stream()
                      .filter(history -> addressEntity.getId() == history.getId())
                      .findAny()
                      .orElse(null)); // 지우고 싶은 객체의 모든 값을 동일하게 하여 객체 생성하여 지움
      //      findMember
      //          .getAddressHistory()
      //          .add(
      //              new Address(
      //                  "new City", addressC.getStreet(), addressC.getZipcode())); // 객체를 새로 생성하여
      // 넣어야함

      // * Collection Type 을 1:N 관계로 변경할 경우
      findMember.addAddressHistory(
          new AddressEntity(new Address("new City", addressC.getStreet(), addressC.getZipcode())));

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
