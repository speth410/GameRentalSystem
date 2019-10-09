package GameRentalSystem;

public class CartItem {
  private String title;

  public CartItem(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public boolean equals(Object item) {
    return item instanceof CartItem && ((CartItem) item).title.equals(title);
  }
}
