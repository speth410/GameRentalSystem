package GameRentalSystem;

public enum GameRating {
  E(1),
  E10(10),
  T(13),
  M(17);

  public int minAge;

  GameRating(int minAge) {
    this.minAge = minAge;
  }

  public int getMinAge() {
    return minAge;
  }
}
