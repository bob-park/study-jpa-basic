package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

  @Id @GeneratedValue private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "PARENT_ID")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "CATEGORY_ITEM",
      // 여기서 조인하는 컬럼
      joinColumns = @JoinColumn(name = "CATEGORY_ID"),
      // 반대편 조인하는 컬럼
      inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
  private List<Item> items = new ArrayList<>();
}
