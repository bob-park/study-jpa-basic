package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

  @Id @GeneratedValue private String id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  public String getId() {
    return id;
  }

  public void setId(String id) {
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
}
