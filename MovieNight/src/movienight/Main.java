package movienight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.connect();
            System.out.println("Successfully connected to the MovieNight_DB!");
            
            // Initialize the database (create tables)
            // DatabaseInitializer.initialize(connection);
            
            Statement stmt = connection.createStatement();
            while (true) {
            	stmt.execute("Select * from movie");
            }
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
	}
}
