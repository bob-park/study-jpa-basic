package jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class Delivery {

  @Id @GeneratedValue private Long id;

  @Embedded private Address address;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Address getAddress() {
    return address;
  }

  public Delivery setAddress(Address address) {
    this.address = address;
    return this;
  }

  public DeliveryStatus getStatus() {
    return status;
  }

  public void setStatus(DeliveryStatus status) {
    this.status = status;
  }
}
