package hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

  @Id @GeneratedValue private Long id;

  private String name;

  /** 읽기만 가능해야한다. 즉, 주인은 Member 가 된다. 왜래키가 있는 곳이 주인이 되야된다. */
  @OneToMany(mappedBy = "team")
  private List<Member> members = new ArrayList<>(); // 관례 상 초기화 해준다.

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Fetch Type 이 Lazy(default) 인 경우 이 메소드를 사용할 때, JPA 에서 query를 실행함.
   *
   * @return
   */
  public List<Member> getMembers() {
    return members;
  }

  public void setMembers(List<Member> members) {
    this.members = members;
  }

  public void addMember(Member member) {

    member.setTeam(this);

    members.add(member);
  }
}
