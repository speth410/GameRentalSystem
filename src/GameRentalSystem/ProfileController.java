package GameRentalSystem;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;///
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProfileController {

    private String currentUser = null;
    private Connection connection = null;
    public Text txtDisplayUserName;
    @FXML public BorderPane borderpane; //belongs to UserInterface
    @FXML public Text txtDisplayFirstName;
    @FXML public Text txtDisplayLastName;
    @FXML public Text txtDisplayAge;
    @FXML public Text txtDisplayGender;
    @FXML public Text txtDisplayEmail;

    @FXML
    public void initialize() throws SQLException {
        // Initialize the database and store the connection for later use.
        connection = dbHandler.initializeDB();
        getAccountInfo();

    }

    public void getAccountInfo() throws SQLException {
        String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, currentUser);
        ResultSet resultSetGetAccountInfo = stmt.executeQuery();

        while (resultSetGetAccountInfo.next()) {
            txtDisplayUserName.setText(currentUser);
            txtDisplayFirstName.setText(resultSetGetAccountInfo.getString("FIRST_NAME"));
            txtDisplayLastName.setText(resultSetGetAccountInfo.getString("LAST_NAME"));
            txtDisplayAge.setText(resultSetGetAccountInfo.getString("AGE"));
            txtDisplayGender.setText(resultSetGetAccountInfo.getString("GENDER"));
            txtDisplayEmail.setText(resultSetGetAccountInfo.getString("EMAIL"));
        }
    }

    @FXML
    void changeEmail(MouseEvent event) {
        Dialog<ButtonType> emailChange = new Dialog<>();
        emailChange.setTitle("Change Email");
        emailChange.setHeaderText("Enter a new email");

        // labels to instruct the user to enter information
        Label lblOldPassword = new Label("old password: ");
        Label lblNewPassword = new Label("new password: ");

        // text fields to obtain the user input for each
        TextField txtOldPassword = new TextField();
        TextField txtNewPassword = new TextField();

        GridPane grid = new GridPane();
        grid.add(lblOldPassword, 1, 1);
        grid.add(txtOldPassword, 2, 1);
        grid.add(lblNewPassword, 1, 2);
        grid.add(txtNewPassword, 2, 2);

        // attaching the grid pane to the dialog pane
        emailChange.getDialogPane().setContent(grid);

        // creates the submit button
        ButtonType btnSubmit = new ButtonType("Submit");

        emailChange.getDialogPane().getButtonTypes().add(btnSubmit);
        emailChange
                .getDialogPane()
                .getButtonTypes()
                .add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

        Optional<ButtonType> result = emailChange.showAndWait();
    }
}
