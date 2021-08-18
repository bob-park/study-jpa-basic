package jpql;

import javax.persistence.*;

@Entity
public class Member {

  @Id @GeneratedValue private Long id;

  private String username;
  private int age;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  public Long getId() {
    return id;
  }

  public Member setId(Long id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Member setUsername(String username) {
    this.username = username;
    return this;
  }

  public int getAge() {
    return age;
  }

  public Member setAge(int age) {
    this.age = age;
    return this;
  }

  public Team getTeam() {
    return team;
  }

  public Member setTeam(Team team) {
    this.team = team;
    return this;
  }
}
