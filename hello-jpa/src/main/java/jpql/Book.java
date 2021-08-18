package jpql;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BOOK")
public class Book extends Item {

  private String author;

  public String getAuthor() {
    return author;
  }

  public Book setAuthor(String author) {
    this.author = author;
    return this;
  }
}
