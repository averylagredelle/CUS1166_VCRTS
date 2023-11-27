import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Database {

    static Connection connection = null;
    static String url = " " ;
    static String username = "root";
    static String password = "password";

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

    public void addVehicle(Owner owner, Vehicle vehicle, String timeStamp){

        try{

            String sql = "INSERT INTO Vehicle" + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, owner.getUsername());
            statement.setString(2, vehicle.getMake());
            statement.setString(3, vehicle.getModel());
            statement.setString(4, vehicle.getLicensePlateNumber());
            statement.setInt(5, vehicle.getResidency());
            statement.setString(6, timeStamp);

            System.out.println("Executing: "+ statement.toString());
            int row = statement.executeUpdate();

            if (row > 0) {
                System.out.println("Data was inserted!" + row);
            }

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void addJob(Job job, String timeStamp, String jobOwner){

        String deadline = String.valueOf(job.getDeadline());

        try{

            String sql = "INSERT INTO Vehicle" + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, jobOwner); //placeholder for proper job owner (needs to be added)
            statement.setString(2, job.getTitle());
            statement.setString(3, job.getDescription());
            statement.setString(4, deadline);
            statement.setInt(5, job.getDurationTime());
            statement.setString(6, timeStamp);

            System.out.println("Executing: "+ statement.toString());
            int row = statement.executeUpdate();

            if (row > 0) {
                System.out.println("Data was inserted!" + row);
            }

        }catch (SQLException e){
            e.getMessage();
            e.printStackTrace();
        }

    }
}


