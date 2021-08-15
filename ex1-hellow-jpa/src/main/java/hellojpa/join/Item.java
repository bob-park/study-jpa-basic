package hellojpa.join;

import javax.persistence.*;

/**
 * 조인 전략
 *
 * <pre>
 *  !JPA 의 상속 맵핑의 default 가 single table 전략이다.
 * </pre>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Item {

  @Id @GeneratedValue private Long id;

  private String name;
  private int price;

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

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }
}
