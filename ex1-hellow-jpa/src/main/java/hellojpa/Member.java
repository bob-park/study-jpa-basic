package hellojpa;

import javax.persistence.*;

@Entity
public class Member {

  @Id @GeneratedValue private Long id;

  private String name;

  private int age;

  @ManyToOne
  @JoinColumn(name = "TEAM_ID")
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

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Team getTeam() {
    return team;
  }

  /**
   * ! setter 메소드는 초기화 외 사용하는 것은 부적절하므로, naming 을 set 아닌 다른 것으로 하는 것을 추천
   *
   * @param team
   */
  public void changeTeam(Team team) {
    this.team = team;

    team.getMembers().add(this);
  }
}
