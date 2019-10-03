package GameRentalSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Button btnCreateAccount;

    @FXML
    void handleLoginClicked(MouseEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        String sql = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

        try {
            // Create Prepared Statement using the sql query with user inputted username and password
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            // If the login failed
            if (!resultSet.next()) {
                System.out.println("Login failed");
            } else {
                System.out.println("Login Successful");

                // Get stage containing btnLogin and close it
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.close();

                // Create new Stage and scene for the main program.
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Dashboard.fxml")));
                Stage dialogStage = new Stage();
                dialogStage.setScene(scene);
                dialogStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCreateAccount(MouseEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
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
