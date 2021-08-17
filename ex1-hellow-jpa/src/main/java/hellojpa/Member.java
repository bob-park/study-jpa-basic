package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {

  @Id @GeneratedValue private Long id;

  private String name;

  // Period
  @Embedded private Period workPeriod;

  // address
  @Embedded private Address address;

  // * 같은 값을 다시 사용하고 싶을 경우
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "city", column = @Column(name = "delivery_city")),
    @AttributeOverride(name = "street", column = @Column(name = "delivery_street")),
    @AttributeOverride(name = "zipcode", column = @Column(name = "delivery_zipcode"))
  })
  private Address deliveryAddress;

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

  public Period getWorkPeriod() {
    return workPeriod;
  }

  public void setWorkPeriod(Period workPeriod) {
    this.workPeriod = workPeriod;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Address getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(Address deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }
}
