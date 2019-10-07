package GameRentalSystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
  private Connection connection = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  @FXML private JFXTextField txtUserID;

  @FXML private JFXPasswordField txtUserPass;

  @FXML private JFXButton btnLogin;

  @FXML private Label lblError;

  @FXML private JFXButton SignUp;

  @FXML
  void handleLoginClicked(MouseEvent event) {
    String username = txtUserID.getText();
    String password = txtUserPass.getText();

    String sql = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

    try {
      // Create Prepared Statement using the sql query with user inputted username and password
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      resultSet = preparedStatement.executeQuery();

      // If the login failed
      if (!resultSet.next()) {
        lblError.setStyle("-fx-text-fill: red");
        lblError.setText("Incorrect Username/Password");
        System.out.println("Incorrect Username/Password");
      } else {
        System.out.println("Login Successful");

        // Get stage containing btnLogin and close it
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();

        // Create dashboard controller
        DashboardController dashboard = new DashboardController(username);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  void handleCreateAccount(MouseEvent event) {
    String username = txtUserID.getText();
    String password = txtUserPass.getText();
    String sql = "INSERT INTO USERS(USERNAME, PASSWORD) VALUES (?, ?)";

    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      preparedStatement.executeUpdate();
      System.out.println("New Account Created!");
      System.out.printf("Username: %s \t Password: %s \n", username, password);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void initialize() {
    // Initialize the database and store the connection for later use.
    connection = dbHandler.initializeDB();
  }
}
