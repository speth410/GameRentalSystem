package GameRentalSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private int age;
  private String gender;
  private String email;
  private ArrayList<Game> cartList = new ArrayList<>();

  public User(
      String username,
      String password,
      String firstName,
      String lastName,
      int age,
      String gender,
      String email) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.gender = gender;
    this.email = email;
  }

  public User (String username, String password) {
    this.username = username;
    this.password = password;

    getUserInfo();
  }

  private void getUserInfo() {
    Connection conn = dbHandler.initializeDB();

    try {
      String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();
      while(rs.next()) {
        username = rs.getString("USERNAME");
        password = rs.getString("PASSWORD");
        firstName = rs.getString("FIRST_NAME");
        lastName = rs.getString("LAST_NAME");
        age = rs.getInt("AGE");
        gender = rs.getString("GENDER");
        email = rs.getString("EMAIL");
      }
      dbHandler.close(rs);
      dbHandler.close(ps);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    dbHandler.close(conn);
  }

  public ArrayList<Game> getCartList() {
    return cartList;
  }

  public void setCartList(ArrayList<Game> cartList) {
    this.cartList = cartList;
    System.out.println("CartList: " + cartList.size());
  }

  public void clearCartList() {
    cartList.clear();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
