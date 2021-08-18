package jpql;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@DiscriminatorColumn
public class Item {

  @Id @GeneratedValue private Long id;

  private String name;

  private int amount;

  public Long getId() {
    return id;
  }

  public Item setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Item setName(String name) {
    this.name = name;
    return this;
  }

  public int getAmount() {
    return amount;
  }

  public Item setAmount(int amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public String toString() {
    return "Item{" + "id=" + id + ", name='" + name + '\'' + ", amount=" + amount + '}';
  }
}
