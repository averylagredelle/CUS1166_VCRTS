import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    static Connection connection = null;
    static String url = " " ;
    static String username = "root";
    static String password = "password";
}

public void connectDatabase() {
    try {
        //declares connection to database
        connection = DriverManager.getConnection(url, username, password);
        
    } catch (SQLException e) {
        e.getMessage();

    }

    //public void addVehicle
    //public void addJob
}


