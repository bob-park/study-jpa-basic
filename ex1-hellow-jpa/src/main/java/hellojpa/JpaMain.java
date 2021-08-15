package hellojpa;

import hellojpa.mappedsuper.Member;
import hellojpa.mapping.Item;
import hellojpa.mapping.Movie;

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

      /*
       * 조인 전략
       */
      Movie movie = new Movie();
      movie.setName("movieA");
      movie.setDirector("A");
      movie.setActor("A");
      movie.setPrice(10000);

      em.persist(movie);

      em.flush();
      em.clear();

      //      Movie findMovie = em.find(Movie.class, movie.getId());
      //      System.out.println("findMovie = " + findMovie);

      // ! 상속관계 맵핑이 Single Table 인 경우 문제점
      /*
       * 부모클래스로 조회하는 경우 부모클래스를 상속받은 모든 entity 객체를 모두 찾음 - 너무 비효율적으로 동작함
       */
      Item findItem = em.find(Item.class, movie.getId());
      System.out.println("findItem = " + findItem);

      // ! MappedSuperClass
      Member member = new Member();

      member.setName("memberA");
      member.setCreatedBy("system");
      member.setCreatedDate(LocalDateTime.now());

      em.persist(member);

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
