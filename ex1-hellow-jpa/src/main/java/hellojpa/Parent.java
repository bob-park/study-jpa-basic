package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {
  @Id @GeneratedValue private Long id;

  private String name;

  // ! cascade 는 연관관계 맵핑과 아무 관계 없음, 영속화의 편리함 뿐임
  // 실무에서 ALL, PERSIST 만 쓰면 됨
  // ! 소유자가 1개일때 - 자식의 부모가 1개일때만 사용 해야함
  // ! 하지만, CHILD 를 Parent 뿐 아니라 다른곳에서 사용할 경우 사용하면 안됨
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Child> childList = new ArrayList<>();

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

  public List<Child> getChildList() {
    return childList;
  }

  public void addChild(Child child) {
    child.setParent(this);
    childList.add(child);
  }
}
