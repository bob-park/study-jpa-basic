package hellojpa;

import javax.persistence.*;

public class CascadeMain {

  public static void main(String[] args) {
    // DB 당 1개만 생성됨 - Container 가 생성 시점에 생성됨
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    // * 트랜잭션 단위, 또는 작업 단위마다 EntityManager 를 생성하여 사용해야한다.
    // * 쓰레드간 공유가 되지 않는다.
    EntityManager em = emf.createEntityManager();

    // * Persistence Util
    PersistenceUnitUtil puu = emf.getPersistenceUnitUtil();

    // * JPA 의 모든 변경은 트랜잭션 안에서 실행되어야 한다.
    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      Parent parent = new Parent();
      parent.setName("parent1");

      Child child1 = new Child();
      child1.setName("child1");

      Child child2 = new Child();
      child2.setName("child2");

      parent.addChild(child1);
      parent.addChild(child2);

      // * 이것이 기본
      em.persist(parent); // Cascade 가 ALL 로 되어 있을 경우, 자식들도 persist 됨

      em.flush();
      em.clear();

      Parent findParent = em.find(Parent.class, parent.getId());

      findParent
          .getChildList()
          .remove(0); // ! orphanRemoval 이 true 인 경우 delete query 가 실행된다. 사용시 유의해야한다.

      em.remove(findParent); // ! orphanRemoval 이 true 인 경우 부모 뿐 아니라, 자식도 모두 delete 되니, 사용에 유의해야한다.

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
