package GameRentalSystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {
  private Connection connection = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  @FXML private JFXTextField txtUserID;
  @FXML private JFXPasswordField txtUserPass;
  @FXML private JFXButton btnLogin;
  @FXML private Label lblError;
  @FXML private JFXButton btnCreateAccount;


  @FXML
  void handleLoginEnter(KeyEvent event) {
    connection = dbHandler.initializeDB();

    if (event.getCode() == KeyCode.ENTER) {
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

          User currentUser = new User(username, password);

          // Get stage containing btnLogin and close it
          Stage stage = (Stage) btnLogin.getScene().getWindow();
          stage.close();

          // Create dashboard controller
          DashboardController dashboard = new DashboardController(currentUser);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      dbHandler.close(resultSet);
      dbHandler.close(preparedStatement);
      dbHandler.close(connection);
    }
  }

  @FXML
  void handleLoginClicked(MouseEvent event) {
    connection = dbHandler.initializeDB();
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

        User currentUser = new User(username, password);

        // Get stage containing btnLogin and close it
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();

        // Create dashboard controller
        DashboardController dashboard = new DashboardController(currentUser);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    dbHandler.close(resultSet);
    dbHandler.close(preparedStatement);
    dbHandler.close(connection);
  }

  @FXML
  void handleCreateAccount(MouseEvent event) {
    connection = dbHandler.initializeDB();

    Dialog<ButtonType> createAccount = new Dialog<>();
    createAccount.setTitle("Create New Account");
    createAccount.setHeaderText("Enter your personal information for this account.");

    // labels to instruct the user to enter information
    Label lblUserName = new Label("Username: ");
    Label lblPassWord = new Label("Password: ");
    Label lblFirstName = new Label("First Name: ");
    Label lblLastName = new Label("Last Name: ");
    Label lblAge = new Label("Age: ");
    Label lblGender = new Label("Gender: ");
    Label lblEmail = new Label("Email: ");

    // text fields to obtain the user input for each
    TextField txtUserName = new TextField();
    TextField txtPassword = new TextField();
    TextField txtFirstName = new TextField();
    TextField txtLastName = new TextField();
    TextField txtAge = new TextField();
    TextField txtGender = new TextField();
    TextField txtEmail = new TextField();

    GridPane grid = new GridPane();
    grid.add(lblUserName, 1, 1);
    grid.add(txtUserName, 2, 1);
    grid.add(lblPassWord, 1, 2);
    grid.add(txtPassword, 2, 2);
    grid.add(lblFirstName, 1, 3);
    grid.add(txtFirstName, 2, 3);
    grid.add(lblLastName, 1, 4);
    grid.add(txtLastName, 2, 4);
    grid.add(lblAge, 1, 5);
    grid.add(txtAge, 2, 5);
    grid.add(lblGender, 1, 6);
    grid.add(txtGender, 2, 6);
    grid.add(lblEmail, 1, 7);
    grid.add(txtEmail, 2, 7);

    // attaching the grid pane to the dialog pane
    createAccount.getDialogPane().setContent(grid);

    // creates the submit button
    ButtonType btnSubmit = new ButtonType("Submit");

    createAccount.getDialogPane().getButtonTypes().add(btnSubmit);
    createAccount
        .getDialogPane()
        .getButtonTypes()
        .add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

    Optional<ButtonType> result = createAccount.showAndWait();
    if (result.isPresent() && result.get() == btnSubmit) {
      try {
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String age = txtAge.getText();
        String gender = txtGender.getText();
        String email = txtEmail.getText();

        String sql =
            "INSERT INTO USERS(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, AGE, GENDER, EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?)";

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);
        preparedStatement.setString(5, age);
        preparedStatement.setString(6, gender);
        preparedStatement.setString(7, email);
        preparedStatement.executeUpdate();
        System.out.println("New Account Created!");
        System.out.printf("Username: %s \t Password: %s \n", username, password);

      } catch (Exception e) {
        e.printStackTrace();
      }
      dbHandler.close(preparedStatement);
      dbHandler.close(connection);
    }
  }

  @FXML
  public void initialize() {
  }


}
