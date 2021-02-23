package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExecutor {

  public static void main(String[] args) {
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "hplussport", "postgres", "password");
    try {
      Connection connection = dcm.getConnection();
//      Statement statement = connection.createStatement();
//      String query = "SELECT COUNT(*) FROM CUSTOMER";
//      ResultSet resultSet = statement.executeQuery(query);
//      while (resultSet.next()) {
//        System.out.println(resultSet.getInt(1));;
//      }
      CustomerDAO customerDAO = new CustomerDAO(connection);
      Customer customer = new Customer();

      customerDAO.create(customer);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
