package GameRentalSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbHandler {
  Connection conn = null;

  public static Connection initializeDB() {

    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/GameRentalSystem";

    final String USER = "";
    final String PASS = "";

    try {
      Class.forName(JDBC_DRIVER);
      Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

      return conn;

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
