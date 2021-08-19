package jpql.domain;

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

  public Long getId() {
    return id;
  }

  @OneToMany(mappedBy = "team")
  private List<Member> members = new ArrayList<>();

  public Team setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Team setName(String name) {
    this.name = name;
    return this;
  }

  public List<Member> getMembers() {
    return members;
  }

  public void addMember(Member member) {
    member.setTeam(this);
    members.add(member);
  }

  @Override
  public String toString() {
    return "Team{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
