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

  // ! 1:N 양방향 시 - JPA 에서 1:N 양방향은 스펙 상 공식적으로 지원해주지 않음
  // ! 사용하지 않는 걸 권장
  // insertable, updatable 를 모두 false 로 하면 읽기 전용이 됨
  @ManyToOne
  @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
  private Team team;

  @OneToOne
  @JoinColumn(name = "LOCKER_ID") // ! 꼭 넣자 - 않넣으면 default Name 이 지저분함
  private Locker locker;

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

  public Team getTeam() {
    return team;
  }

  //  public void setTeam(Team team) {
  //    this.team = team;
  //  }

  public Locker getLocker() {
    return locker;
  }

  public void setLocker(Locker locker) {
    this.locker = locker;

    locker.setMember(this);
  }
}
