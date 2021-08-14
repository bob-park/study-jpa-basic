package jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class Delivery {

  @Id @GeneratedValue private Long id;

  private String city;

  private String street;
  private String zipcode;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public DeliveryStatus getStatus() {
    return status;
  }

  public void setStatus(DeliveryStatus status) {
    this.status = status;
  }
}
