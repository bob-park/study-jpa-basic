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
