package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

  @Id @GeneratedValue private Long id;

  private String name;

  // ! N:1 맵핑시
  //  @OneToMany(mappedBy = "team")
  //  private List<Member> members = new ArrayList<>();

  // ! 1:N 맵핑시
  @OneToMany
  // ! 필수 - 사옹하지 않을 경우 "조인 테이블" 이 중간에 생김
  // "조인 테이블"을 사용시 DB 테이블의 복잡도가 올라가기 떄문에, 유지보수에 어려움이 있을 수 있음
  @JoinColumn(name = "TEAM_ID")
  private List<Member> members = new ArrayList<>();

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

  //  public List<Member> getMembers() {
  //    return members;
  //  }
  //
  //  public void addMembers(Member member) {
  //    member.setTeam(this);
  //
  //    members.add(member);
  //  }

  public List<Member> getMembers() {
    return members;
  }

  public void setMembers(List<Member> members) {
    this.members = members;
  }
}
