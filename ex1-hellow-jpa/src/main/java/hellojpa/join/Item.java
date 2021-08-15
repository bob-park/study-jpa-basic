package hellojpa.join;

import javax.persistence.*;

/**
 * 조인 전략
 *
 * <pre>
 *  !JPA 의 상속 맵핑의 default 가 single table 전략이다.
 * </pre>
 *
 * <p>조인 전략인 경우 @{@link DiscriminatorColumn} 이 있을 필욘 없지만, 유지보수 측면에서 명확히 하려면 필요하다.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
@DiscriminatorColumn // 부모 테이블에 type 전략 추가
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
