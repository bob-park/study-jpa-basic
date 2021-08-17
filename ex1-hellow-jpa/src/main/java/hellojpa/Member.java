package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

  // ! Collection Type
  // 관계형 DB 는 Collection 를 저장하지 못하기 때문에, 별도의 테이블을 생성하여 관리(1:N 관계)
  @ElementCollection
  @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "member_id"))
  @Column(name = "food_name")
  private Set<String> favoriteFoods = new HashSet<>();

  //  @ElementCollection
  //  @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "member_id"))
  //  private List<Address> addressHistory = new ArrayList<>();

  // * Collection Type 을 1:N 관계로 변경
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AddressEntity> addressHistory = new ArrayList<>();

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

  public Set<String> getFavoriteFoods() {
    return favoriteFoods;
  }

  public void setFavoriteFoods(Set<String> favoriteFoods) {
    this.favoriteFoods = favoriteFoods;
  }

  public List<AddressEntity> getAddressHistory() {
    return addressHistory;
  }

  public void addAddressHistory(AddressEntity address) {

    address.setMebmer(this);

    addressHistory.add(address);
  }
}
