package hellojpa.mappedsuper;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // 단순히 공통 매핑 정보가 필요 할때 사용 - 따라서, 생성이나 다른 곳에서 사용할 수 없도록, 추상클래스 적용
public abstract class BaseEntity {

  private String createdBy;
  private LocalDateTime createdDate;
  private String modifiedBy;
  private LocalDateTime modifiedDate;

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }
}
