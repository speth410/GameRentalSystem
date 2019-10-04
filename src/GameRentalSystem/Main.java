package GameRentalSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
    primaryStage.setTitle("Game Rental System");
    primaryStage.setScene(new Scene(root, 875, 475));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
