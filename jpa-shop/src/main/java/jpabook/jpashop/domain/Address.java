package jpabook.jpashop.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {
  private String city;
  private String street;
  private String zipcode;

  public String getCity() {
    return city;
  }

  public String getStreet() {
    return street;
  }

  public String getZipcode() {
    return zipcode;
  }

  private void setCity(String city) {
    this.city = city;
  }

  private void setStreet(String street) {
    this.street = street;
  }

  private void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  // ! 항상 IDE 에서 제공하는 자동 완성 기능을 사용하는데, option 중 getter 를 호출 기능을 사용하자
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return Objects.equals(getCity(), address.getCity())
        && Objects.equals(getStreet(), address.getStreet())
        && Objects.equals(getZipcode(), address.getZipcode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCity(), getStreet(), getZipcode());
  }
}
