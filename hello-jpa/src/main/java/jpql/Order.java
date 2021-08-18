package jpql;

import javax.persistence.*;

@Entity
@Table(name = "Orders")
public class Order {

  @Id @GeneratedValue private Long id;

  private int orderAmount;

  @Embedded private Address address;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  public Long getId() {
    return id;
  }

  public Order setId(Long id) {
    this.id = id;
    return this;
  }

  public int getOrderAmount() {
    return orderAmount;
  }

  public Order setOrderAmount(int orderAmount) {
    this.orderAmount = orderAmount;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public Order setAddress(Address address) {
    this.address = address;
    return this;
  }

  public Product getProduct() {
    return product;
  }

  public Order setProduct(Product product) {
    this.product = product;
    return this;
  }
}
