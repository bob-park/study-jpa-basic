package jpql.main;

import jpql.domain.*;

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

      Team teamA = new Team();
      teamA.setName("teamA");

      em.persist(teamA);

      for (int i = 0; i < 100; i++) {
        Member member = new Member();
        //        member.setUsername("member" + i);
        member.setAge(i);

        em.persist(member);
      }

      Member memberA = new Member();
      memberA.setUsername("memberA");
      memberA.setAge(10);

      teamA.addMember(memberA);

      em.persist(memberA);

      em.flush();
      em.clear();

      // * Type Query - Query
      TypedQuery<Member> typeQuery1 =
          em.createQuery(
              "select m from Member m where m.username='memberA'", Member.class); // Type 이 명확할 때 사용
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

      /*
       * Join
       */
      // * inner join
      List<Member> findResult6 =
          em.createQuery("select m from Member m inner join m.team t", Member.class)
              .getResultList();

      for (Member member : findResult6) {
        System.out.println("member = " + member);
      }

      // * outer join
      List<Member> findResult7 =
          em.createQuery("select m from Member m left outer join m.team t", Member.class)
              .getResultList();

      for (Member member : findResult7) {
        System.out.println("member = " + member);
      }

      // * 세타 조인
      List<Member> findResult8 =
          em.createQuery("select m from Member m, Team t where m.username = t.name", Member.class)
              .getResultList();

      for (Member member : findResult8) {
        System.out.println("member = " + member);
      }

      // * on 절

      em.createQuery("select m from Member m left join m.team t on t.name='teamA'").getResultList();

      // * 연관관계 없는 외부 조인
      em.createQuery("select m from Member m left join Team t on m.username=t.name", Member.class)
          .getResultList();

      /*
       * 서브 쿼리
       */
      // ! from 절에서 서브 쿼리가 불가능 -> 조인으로 풀 수 있을 경우 풀어서 해결해야 한다.
      em.createQuery("select (select avg(m1) from Member m1) as avgAge from Member m")
          .getResultList();

      /*
       * JPQL 타입 표현
       */
      // * enum - 패키지를 모두 적어주어야 한다.
      em.createQuery("select m from Member m where m.memberType=jpql.domain.MemberType.USER", Member.class)
          .getResultList();
      // 파라미터 바인딩을 하면 편하다.
      em.createQuery("select m from Member m where m.memberType= :memberType", Member.class)
          .setParameter("memberType", MemberType.USER)
          .getResultList();

      em.flush();
      em.clear();

      Book bookA = new Book();

      bookA.setName("bookA");
      bookA.setAuthor("hwpark");

      em.persist(bookA);

      // * entity type
      List<Item> findResult9 =
          em.createQuery("select i from Item i where type(i) = Book", Item.class).getResultList();

      for (Item item : findResult9) {
        System.out.println("item = " + item);
      }

      em.flush();
      em.clear();

      /*
       * 조건식 - CASE
       */
      // * CASE
      List<String> findResult10 =
          em.createQuery(
                  "select "
                      + "case when m.age <= 10 then '학생 요금' "
                      + "when m.age >= 60 then '경로 요금' "
                      + "else '일반 요금' "
                      + "end "
                      + "from Member m where m.team is not null",
                  String.class)
              .getResultList();

      for (String s : findResult10) {
        System.out.println("result = " + s);
      }

      // * Coalesce - 하나씩 조회해서 null 이 아니면 반환
      List<String> findResult11 =
          em.createQuery("select coalesce(m.username, '이름 없는 회원') from Member m", String.class)
              .getResultList();

      for (String s : findResult11) {
        System.out.println("result = " + s);
      }

      // * nullIf - 두 값이 같으면 null, 다르면 첫번쨰 값 반환
      List<String> findResult12 =
          em.createQuery("select nullif(m.username, 'memberA') from Member m", String.class)
              .getResultList();

      for (String s : findResult12) {
        System.out.println("result = " + s);
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