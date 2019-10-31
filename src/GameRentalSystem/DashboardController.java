package GameRentalSystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.LabelView;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DashboardController extends LoginController {
  @FXML
  private TilePane tpGames;
  @FXML
  private ScrollPane spGames;
  @FXML
  private HBox hBox;
  @FXML
  private TableView<CartItem> tvCart;
  @FXML
  private TableColumn<CartItem, String> colTitle;
  @FXML
  private TableColumn<?, ?> colRemove;
  @FXML
  private Button btnCheckout;
  @FXML
  private Label lblUsername;

  private Connection connection = null;
  private String currentUser = null;
  private File selectedFile;
  private Stage dashboardStage = new Stage();
  private FileChooser fileChooser = new FileChooser();
  private Game game;
  public Text txtDisplayUserName;

  @FXML
  public BorderPane borderpane; //belongs to UserInterface

  @FXML
  public Text txtDisplayFirstName;
  @FXML
  public Text txtDisplayLastName;
  @FXML
  public Text txtDisplayAge;
  @FXML
  public Text txtDisplayGender;
  @FXML
  public Text txtDisplayEmail;

  private ArrayList<Game> games = new ArrayList<>();

  public DashboardController(String username) {
    // Save the current users username
    currentUser = username;


    // Create new stage
    Stage dashboardStage = new Stage();

    // Load the FXML file
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInterface.fxml"));

      // Set this class as the controller
      loader.setController(this);

      // Load the scene
      dashboardStage.setScene(new Scene(loader.load()));

      // Setup the window/stage
      dashboardStage.setTitle("Dashboard");
      dashboardStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void initialize() throws SQLException {

    // Get Connection from dbHandler
    connection = dbHandler.initializeDB();


    // Test to show that the dashboard knows who is logged in.
    System.out.println("Dashboard Controller -> Logged in as: " + currentUser);

//    getAccountInfo();


  }


  @FXML
  void loadGameList(MouseEvent event) {
    loadUI("GameList");
  }

  @FXML
  void loadReturn(MouseEvent event) {
    borderpane.setCenter(null);
  }

  private void loadUI(String ui) {
    Parent root = null;

    try {
      root = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    borderpane.setCenter(root);
  }


  @FXML
  void handleRemoveClicked(MouseEvent event) {
    CartItem item = tvCart.getSelectionModel().getSelectedItem();
    tvCart.getItems().remove(item);
  }


  public void getAccountInfo() throws SQLException {
    String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, currentUser);
    ResultSet resultSetGetAccountInfo = stmt.executeQuery();

    while (resultSetGetAccountInfo.next()) {
      txtDisplayUserName.setText(currentUser);
      txtDisplayFirstName.setText(resultSetGetAccountInfo.getString("FIRST_NAME"));
    }
  }
}

