import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the server of the Vehicular Cloud System. It saves all the information entered in the GUI onto a 
 * text file, and is used to verify user data for logging in and signing up.
 * @author Software Engineering Capstone Project Group 1
 */
public class Server {
    // File for storing data
    private File database = new File("Database.txt");

    // String to store data
    private String data;

    // Lists of users, clients, owners, and completed jobs
    private ArrayList<User> users;
    private ArrayList<Client> clients;
    private ArrayList<Owner> owners;
    private ArrayList<Job> completedJobs;

    /**
     * Initializes a new Server object.
     */
    public Server() {
        // Initialize data and user lists
        data = "";
        users = new ArrayList<User>();
        clients = new ArrayList<Client>();
        owners = new ArrayList<Owner>();

        // Initialize the list of completed jobs
        completedJobs = new ArrayList<Job>();
    }

    /**
     * Checks to see if the given username belongs to a client in the server's saved list of clients. Returns {@code true} if 
     * a client has this username and {@code false} otherwise.
     * @param username the username to check to see if a client has it
     * @return {@code true} if the username belongs to a client, {@code false} otherwise
     */
    public boolean isClient(String username) {
        for (Client c : clients) {
            if (c.getUsername().equals(username))
                return true;
        }
        return false;
    }

    /**
     * Returns the Client with the given username. Returns {@code null} if the client cannot be found.
     * @param username the username of the desired Client
     * @return the Client with the given username, {@code null} if the Client is not in the server's list
     */
    public Client getClient(String username) {
        for (Client c : clients) {
            if (c.getUsername().equals(username))
                return c;
        }
        return null;
    }

    /**
     * Adds the given Client to the server's list of saved clients.
     * @param c the Client to add to the server's list
     */
    public void addClient(Client c) {
        clients.add(c);
    }

    /**
     * Returns an array containing all the clients saved on the server.
     * @return an array list of all the clients in the server's database
     */
    public Client[] getClients() {
        Client[] c = new Client[clients.size()];
        for (int i = 0; i < c.length; i++) {
            c[i] = clients.get(i);
        }
        return c;
    }

    /**
     * Checks if the given username belongs to an owner in the server's list of saved owners. Returns {@code true} if the 
     * username belongs to an owner and {@code false} otherwise.
     * @param username the username to check to see if an owner has it
     * @return {@code true} if the username belongs to an owner, {@code false} otherwise
     */
    public boolean isOwner(String username) {
        for (Owner o : owners) {
            if (o.getUsername().equals(username))
                return true;
        }
        return false;
    }

    /**
     * Returns the Owner with the given username. Returns {@code null} if the Owner cannot be located.
     * @param username the username of the desired Owner
     * @return the Owner with the given username, {@code null} if the Owner is not in the server's list
     */
    public Owner getOwner(String username) {
        for (Owner o : owners) {
            if (o.getUsername().equals(username))
                return o;
        }
        return null;
    }

    /**
     * Adds the given Owner to the server's list of saved owners.
     * @param o the Owner to add to the server's list
     */
    public void addOwner(Owner o) {
        owners.add(o);
    }

    /**
     * Checks if the given username belongs to a user who has logged into the Vehicular Cloud System before. This function 
     * can be used even if the user in question has not submitted a job or rented a vehicle yet.
     * @param username the username to check to see if it belongs to an already-existing user
     * @return {@code true} if the username belongs to an already-existing user, {@code false} otherwise
     */
    public boolean isUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username))
                return true;
        }
        return false;
    }

    /**
     * Returns the User with the given username.
     * @param username the username of the desired User
     * @return the User who has the given 
     */
    public User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Adds the given User to the server's list of users who have signed up/logged in to the Vehicular Cloud System.
     * @param thisUser the User to add to the server's list
     */
    public void addUser(User thisUser) {
        users.add(thisUser);
    }

    /**
     * Checks if an account with the given username and password exists in the server's database. Returns {@code true} 
     * if a user has signed up with the given username and password previously, and {@code false} otherwise.
     * @param username the username of the account whose existence is being checked
     * @param password the password of the account whose existence is being checked
     * @return {@code true} if the username and password belong to an existing user, {@code false} otherwise
     */
    public boolean accountFound(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return true;
        }
        return false;
    }

    /**
     * Transfers the given job to the server's database.
     * @param job the Job to be transferred
     */
    public void transferCompletedJob(Job job) {
        completedJobs.add(job);
    }

    /**
     * Updates the server's database text file with user actions and timestamps.
     * @param action the action performed by the user to be saved on the text file
     * @param user the User who performed the given action
     */
    public void updateDatabase(String action, User user) {
        Date d = new Date();
        String newData = action + "|" + user + "|Time: " + d + "\n";
        data = data.concat(newData);

        try {
            FileWriter myWriter = new FileWriter(database);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
