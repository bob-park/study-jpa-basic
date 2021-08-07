package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
// * 테이블 이름이 다를 경우 추가
// @Table(name = "USER")
public class Member {

  @Id private Long id;

  //  * 컬럼 명이 다를 경우 추가
  //  @Column(name = "name_str")
  private String name;

  protected Member() {}

  public Member(Long id, String name) {
    this.id = id;
    this.name = name;
  }

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
}
