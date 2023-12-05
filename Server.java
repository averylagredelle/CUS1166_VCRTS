import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the server of the Vehicular Cloud System. It saves all the information entered in the GUI onto a 
 * text file, and is used to verify user data for logging in and signing up.
 * @author Software Engineering Capstone Project Group 1
 */
public class Server {
    private Connection connection = null;
    private String url = "jdbc:mysql://localhost:3306/VCRTS?useTimezone=true&serverTimezone=UTC";
    private String username = "root";
    private String password = "NewYork2003!";
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Initializes a new Server object.
     */
    public Server() {
        // // Initialize data and user lists
        // data = "";
        // users = new ArrayList<User>();
        // clients = new ArrayList<Client>();
        // owners = new ArrayList<Owner>();

        // // Initialize the list of completed jobs
        // completedJobs = new ArrayList<Job>();

        try {
            //declares connection to database
            connection = DriverManager.getConnection(url, username, password);
            
        } catch (SQLException e) {
            System.out.println("There was an error connecting to the database");
            e.getMessage();
        }

        
    }

    /**
     * Checks to see if the given username belongs to a client in the server's saved list of clients. Returns {@code true} if 
     * a client has this username and {@code false} otherwise.
     * @param username the username to check to see if a client has it
     * @return {@code true} if the username belongs to a client, {@code false} otherwise
     */
    public boolean isClient(String username) {
        String isClientQuery = "SELECT * FROM vcrts.user WHERE username='" + username + "' AND isClient='true'";

        try{
            return queryDatabase(isClientQuery, "An error occurred while trying to verify the client username").first();
        }
        catch(SQLException e) {
            System.out.println("An error occurred while trying to verify the client username");
            return false;
        }
        catch(NullPointerException e) {
            System.out.println("An error occurred while trying to query the database for client information");
            return false;
        }
    }

    public boolean setUserClient(String username) {
        String setUserClientStatement = "UPDATE user SET isClient='true' WHERE username='" + username + "'";

        return updateDatabase(setUserClientStatement, "There was an error setting user as client");
    }

    public Client getClient(String username) {
        String getClientQuery = "SELECT * FROM vcrts.user WHERE username='" + username + "' AND isClient='true'";

        try {
            ResultSet resultSet = queryDatabase(getClientQuery, "An error occurred while trying to find requested client.");
            if(resultSet.first()) {
                String password = resultSet.getString("userPassword");
                Client c = new Client(username, password);
                return c;
            }
            else {
                System.out.println("Could not find client with username " + username);
                return null;
            }
        }
        catch(SQLException e) {
            System.out.println("A SQL exception occurred while trying to get the client " + username);
            return null;
        }
        catch(NullPointerException e) {
            System.out.println("Could not get client because result set was null");
            return null;
        }
    }

    /**
     * Checks if the given username belongs to an owner in the server's list of saved owners. Returns {@code true} if the 
     * username belongs to an owner and {@code false} otherwise.
     * @param username the username to check to see if an owner has it
     * @return {@code true} if the username belongs to an owner, {@code false} otherwise
     */
    public boolean isOwner(String username) {
        String isOwnerQuery = "SELECT * FROM vcrts.user WHERE username='" + username + "' AND isOwner='true'";

        try{
            return queryDatabase(isOwnerQuery, "An error occurred while trying to verify the owner username").first();
        }
        catch(SQLException e) {
            System.out.println("An error occurred while trying to verify the owner username");
            return false;
        }
        catch(NullPointerException e) {
            System.out.println("An error occurred while trying to query the database for owner information");
            return false;
        }
    }

    public Owner getOwner(String username) {
        String getOwnerQuery = "SELECT * FROM vcrts.user WHERE username='" + username + "' AND isOwner='true'";

        try {
            ResultSet resultSet = queryDatabase(getOwnerQuery, "An error occurred while trying to find requested owner.");
            if(resultSet.first()) {
                String password = resultSet.getString("userPassword");
                Owner o = new Owner(username, password);
                return o;
            }
            else {
                System.out.println("Could not find owner with username " + username);
                return null;
            }
        }
        catch(SQLException e) {
            System.out.println("A SQL exception occurred while trying to get the owner " + username);
            return null;
        }
        catch(NullPointerException e) {
            System.out.println("Could not get owner because result set was null");
            return null;
        }
    }

    /**
     * Returns the Owner with the given username. Returns {@code null} if the Owner cannot be located.
     * @param username the username of the desired Owner
     * @return the Owner with the given username, {@code null} if the Owner is not in the server's list
     */
    public boolean setUserOwner(String username) {
        String setUserClientStatement = "UPDATE user SET isOwner='true' WHERE username='" + username + "'";

        return updateDatabase(setUserClientStatement, "There was an error setting user as owner");
    }

    /**
     * Checks if the given username belongs to a user who has logged into the Vehicular Cloud System before. This function 
     * can be used even if the user in question has not submitted a job or rented a vehicle yet.
     * @param username the username to check to see if it belongs to an already-existing user
     * @return {@code true} if the username belongs to an already-existing user, {@code false} otherwise
     */
    public boolean isUser(String username) {
        String isUserQuery = "SELECT * FROM vcrts.user WHERE username='" + username + "'";

        try{
            return queryDatabase(isUserQuery, "An error occurred while trying to verify user").first();
        }
        catch(SQLException e) {
            System.out.println("An error occurred while trying to verify the user");
            return false;
        }
        catch(NullPointerException e) {
            System.out.println("An error occurred while trying to query the database for user information");
            return false;
        }
    }

    /**
     * Returns the User with the given username.
     * @param username the username of the desired User
     * @return the User who has the given 
     */
    // public User getUser(String username) {
    //     for (User u : users) {
    //         if (u.getUsername().equals(username)) {
    //             return u;
    //         }
    //     }
    //     return null;
    // }

    public boolean recordNewLogin(String username) {
        String currentTime = formatter.format(LocalDateTime.now());
        String recordNewLoginStatement = "UPDATE user SET lastSignIn='" + currentTime + "' WHERE username='" + username + "'";

        return updateDatabase(recordNewLoginStatement, "An error occurred while trying to record new login in database");
    }

    /**
     * Adds the given User to the server's list of users who have signed up/logged in to the Vehicular Cloud System.
     * @param thisUser the User to add to the server's list
     */
    public boolean addUser(User thisUser) {
        String currentTime = formatter.format(LocalDateTime.now());
        String addUserStatement = "INSERT INTO vcrts.user(username,userPassword,dateCreated,lastSignIn) VALUES('" + thisUser.getUsername() + "','" + thisUser.getPassword() + "','" + currentTime + "','" + currentTime + "')";

        return updateDatabase(addUserStatement, "An error occurred while trying to add user " + thisUser.getUsername() + " to database");
    }

    /**
     * Checks if an account with the given username and password exists in the server's database. Returns {@code true} 
     * if a user has signed up with the given username and password previously, and {@code false} otherwise.
     * @param username the username of the account whose existence is being checked
     * @param password the password of the account whose existence is being checked
     * @return {@code true} if the username and password belong to an existing user, {@code false} otherwise
     */
    public boolean accountFound(String username, String password) {
        String findAccountQuery = "SELECT * FROM vcrts.user WHERE username='" + username + "' AND userPassword='" + password + "'";
        try {
            return queryDatabase(findAccountQuery, "An error occurred while trying to find the account for " + username).first();
        }
        catch(SQLException e) {
            System.out.println("Could not access the account for " + username + " in the database");
            return false;
        }
        catch(NullPointerException e) {
            System.out.println("Could not verify account because result set was null");
            return false;
        }
    }

    /**
     * Transfers the given job to the server's database.
     * @param job the Job to be transferred
     */
    public void setJobCompleted(Job job) {
        //completedJobs.add(job);

        String setJobCompletedStatement = "UPDATE vcrts.job SET completed='true' WHERE jobOwner='" + job.getJobOwner() + "' AND jobTitle='" + job.getTitle() + "' AND jobDescription='" + job.getDescription() + "'";
        if(!updateDatabase(setJobCompletedStatement, "An error occurred while trying to set job completed")) {
            System.out.println("Failed to set job completed");
        }
    }

    public boolean addJob(Job j) {
        String dateRequested = formatter.format(LocalDateTime.now());
        String addJobStatement = "INSERT INTO vcrts.job VALUES('" + j.getJobOwner() + "','" + j.getTitle() + "','" + j.getDescription() + "','" + j.getDeadline() + "'," + j.getDurationTime() + ",'false','" + dateRequested + "')";

        return updateDatabase(addJobStatement, "An error occurred while trying to add the " + j.getTitle() + " job to the database");
    }

    public boolean addVehicle(Vehicle v) {
        String arrivalTime = formatter.format(v.getArrivalTime());
        String addVehicleStatement = "INSERT INTO vcrts.vehicle VALUES('" + v.getVehicleOwner() + "','" + v.getMake() + "','" + v.getModel() + "','" + v.getLicensePlateNumber() + "'," + v.getResidency() + ",'" + arrivalTime + "')";

        return updateDatabase(addVehicleStatement, "An error occurred while trying to add rental " + v.getLicensePlateNumber() + " to the database");
    }

    public ArrayList<Job> getJobs(HashMap<Job, Integer> completionTimes) {
        int minutesFromStart = 0;
        ArrayList<Job> allJobs = new ArrayList<Job>();
        String getJobsRequest = "SELECT * FROM vcrts.job";

        try {
            ResultSet resultSet = queryDatabase(getJobsRequest, "An error occurred while trying to fetch all jobs from the database");
            while(resultSet.next()) {
                String jobOwner = resultSet.getString("jobOwner");
                String jobTitle = resultSet.getString("jobTitle");
                String jobDescription = resultSet.getString("jobDescription");
                LocalDate jobDeadline = LocalDate.parse(resultSet.getString("jobDeadline"));
                int jobDuration = resultSet.getInt("jobDuration");

                Job newJob = new Job(jobTitle, jobDescription, jobDuration, jobDeadline);
                newJob.setJobOwner(jobOwner);
                completionTimes.put(newJob, minutesFromStart + newJob.getDurationTime());
                minutesFromStart = minutesFromStart + newJob.getDurationTime();
                allJobs.add(newJob);
            }
            return allJobs;
        }
        catch(SQLException e) {
            System.out.println("A SQL exception occurred while trying to get the jobs from the result set");
            return null;
        }
        catch(NullPointerException e) {
            System.out.println("Could not get all jobs from the database because the result set was null");
            return allJobs;
        }
    }

    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> allVehicles = new ArrayList<Vehicle>();
        String getVehiclesRequest = "SELECT * FROM vcrts.vehicle";

        try {
            ResultSet resultSet = queryDatabase(getVehiclesRequest, "An error occurred while trying to fetch all vehicles from the database");
            while(resultSet.next()) {
                String vehicleOwner = resultSet.getString("vehicleOwner");
                String make = resultSet.getString("vehicleMake");
                String model = resultSet.getString("vehicleModel");
                String licencePlateNumber = resultSet.getString("vehiclePlate");
                int residency = resultSet.getInt("residencyTime");
                LocalDateTime arrivalTime = LocalDateTime.parse(resultSet.getString("arrivalTime"), formatter);

                Vehicle newCar = new Vehicle(make, model, licencePlateNumber, residency);
                newCar.setVehicleOwner(vehicleOwner);
                newCar.setArrivalTime(arrivalTime);

                allVehicles.add(newCar);
            }
            return allVehicles;
        }
        catch(SQLException e) {
            System.out.println("A SQL exception occurred while trying to get the vehicles from the result set");
            return null;
        }
        catch(NullPointerException e) {
            System.out.println("Could not get all vehicles from the database because the result set was null");
            return allVehicles;
        }
    }

    public ResultSet queryDatabase(String query, String errorMessage) {
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        }
        catch(SQLException e) {
            System.out.println(errorMessage);
            return null;
        }
    }

    /**
     * Updates the server's database text file with user actions and timestamps.
     * @param action the action performed by the user to be saved on the text file
     * @param user the User who performed the given action
     */
    public boolean updateDatabase(String sqlStatement, String errorMessage) {
        // Date d = new Date();
        // String newData = action + "|" + user + "|Time: " + d + "\n";
        // data = data.concat(newData);

        // try {
        //     FileWriter myWriter = new FileWriter(database);
        //     myWriter.write(data);
        //     myWriter.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        try{
            Statement statement = connection.createStatement();
            int row = statement.executeUpdate(sqlStatement);
            return row > 0;
        }
        catch(SQLException e) {
            System.out.println(errorMessage);
            System.out.println(e.getMessage());
            return false;
        }
    }

    //insert JobtoDatabase(Job, job)
    
    //insertOwnertoDatabase(owner, owner)
}
