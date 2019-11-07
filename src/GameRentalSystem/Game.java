package GameRentalSystem;

import javafx.scene.image.Image;

import java.io.FileInputStream;

public class Game {
  private String gameTitle;
  private GameGenre genre;
  private FileInputStream fileIn;
  private Image gameImage;
  private GameRating rating;
  private String price;

  public Game(String gameTitle, Image gameImage) {
    this.gameTitle = gameTitle;
    this.gameImage = gameImage;
  }

  public Image getGameImage() {
    return gameImage;
  }

  public Game(
      String gameTitle, GameGenre genre, GameRating rating, String price, FileInputStream fileIn) {
    this.gameTitle = gameTitle;
    this.genre = genre;
    this.fileIn = fileIn;
    this.rating = rating;
    this.price = price;
  }

  public String getGameTitle() {
    return gameTitle;
  }

  public void setGameTitle(String gameTitle) {
    this.gameTitle = gameTitle;
  }

  public GameGenre getGameGenre() {
    return genre;
  }

  public void setGameGenre(GameGenre genre) {
    this.genre = genre;
  }

  public FileInputStream getFileIn() {
    return fileIn;
  }

  public void setFileIn(FileInputStream fileIn) {
    this.fileIn = fileIn;
  }

  public GameRating getRating() {
    return rating;
  }

  public void setRating(GameRating rating) {
    this.rating = rating;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public void print() {
    System.out.println(
        "Title: " + gameTitle + " Genre: " + genre + " Rating: " + rating + " Price: " + price);
  }
}
