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

      Team team = new Team();
      team.setName("teamA");

      em.persist(team);

      Member member = new Member();
      member.setName("memberA");

      team.addMember(member);

      em.persist(member);

      em.flush();
      em.clear();

      // * JPA 에서 member 를 가져오는 쿼리에 team 까지 같이 가져오는 쿼리(Join Query)를 실행해버린다.
      //      Member findMember = em.find(Member.class, member.getId());

      // * getRefernece() 만 실행하면, DB Query 를 실행하지 않은다. - 실제 member 가 사용되는 시점에 쿼리가 실행된다.
      Member findMember = em.getReference(Member.class, member.getId());

      // ! member 와 Team 을 출력할 경우
      //      printMemberAndTeam(findMember);

      // ! member 만 출력할 경우
      printMember(findMember);

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

  private static void printMember(Member findMember) {

    // ! JPA Proxy 에서 생성한 가짜 Entity - DB Query 실행 후 진짜 entity 생성하여 method() 를 실행하게 된다.
    // ! Proxy 가 변경되는 것이 아님 - 진짜 entity 에 접근하는 거임
    // ! 타입 체크 시 주의 - equals() or == X , instanceof O
    System.out.println("findMember = " + findMember.getClass());
    System.out.println(
        "findMember.id = " + findMember.getId()); // JPA 조회 시 ID 로 조회하기 때문에 ID 는 들어간다.

    // ! getReference() 사용시 이때 DB Query 가 실행된다.
    // getName() -> Proxy -> Persistence Context 초기화 요청 -> DB Query 실행 -> 실제 Entity 생성 ->
    // Entity.getName() 실행
    System.out.println("name = " + findMember.getName());
  }

  private static void printMemberAndTeam(Member findMember) {

    System.out.println("name = " + findMember.getName());
    System.out.println("team = " + findMember.getTeam());
  }
}
