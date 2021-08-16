package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

  @Id @GeneratedValue private Long id;

  private String name;

//  @ManyToOne(fetch = FetchType.LAZY)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "team_id")
  private Team team;

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

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  @Override
  public String toString() {
    return "Member{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", team=" + team + '}';
  }
}
