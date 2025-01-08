package movienightgui;

import java.sql.Connection;
import java.sql.SQLException;
import utils.DatabaseConnection;

public class MovieNightGUI {
	
	public static void main(String[] args) {
		Connection connection;
		try {
			connection = DatabaseConnection.connect();
			new MainFrame(new Database(connection)).setVisible(true);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
        
	}
}
