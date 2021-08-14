package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

  @Id @GeneratedValue private Long id;
  private String username;

  // ! N:1 맵핑시
//  @ManyToOne
//  @JoinColumn(name = "TEAM_ID")
//  private Team team;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

//  public Team getTeam() {
//    return team;
//  }
//
//  public void setTeam(Team team) {
//    this.team = team;
//  }
}
