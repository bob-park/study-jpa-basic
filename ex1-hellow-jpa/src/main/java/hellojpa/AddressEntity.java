package hellojpa;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

  @Id @GeneratedValue private Long id;

  private Address address;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  public AddressEntity() {}

  public AddressEntity(Address address) {
    this.address = address;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Member getMember() {
    return member;
  }

  public void setMebmer(Member member) {
    this.member = member;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddressEntity that = (AddressEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(address, that.address)
        && Objects.equals(member, that.member);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, address, member);
  }
}
