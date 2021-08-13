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

      Team teamA = new Team();
      teamA.setName("TeamA");

      em.persist(teamA);

      Member memberA = new Member();

      memberA.setName("MemberA");

      // ! 주의 양방향 연관관계 맵핑 시 주인이든 아니든, 양쪽 모두 다 값을 입력해야 side effect 를 예방할 수 있다.
      // * 주인쪽에서 연관관계 편의 메소드를 생성하여 편하게 사용하자!!
      // ! 연관 관계 편의 메소드는 한 곳에서만 생성하여 사용하자.
      //      teamA.getMembers().add(memberA);
      //      memberA.changeTeam(teamA);
      teamA.addMember(memberA);

      em.persist(memberA);

      // ! Persistence Context 를 비워주이 않고 사용한다면
      //      em.flush();
      //      em.clear();

      //      Member findMember = em.find(Member.class, memberA.getId());
      //
      //      for (Member m : findMember.getTeam().getMembers()) {
      //        System.out.println("m = " + m.getName());
      //      }

      // ! DB 에서 가져오는 것이 아닌, 1차 캐시(Persistence Context) 에 있던, team 를 가져오기 때문에, members 가 비워져 있다.
      Team findTeam = em.find(Team.class, teamA.getId());

      for (Member m : findTeam.getMembers()) {
        System.out.println("m : " + m.getName());
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
