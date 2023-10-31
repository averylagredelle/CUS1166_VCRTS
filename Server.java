import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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

    public Server() {
        // Initialize data and user lists
        data = "";
        users = new ArrayList<User>();
        clients = new ArrayList<Client>();
        owners = new ArrayList<Owner>();

        // Initialize the list of completed jobs
        completedJobs = new ArrayList<Job>();
    }

    // Check if a user is a client
    public boolean isClient(String username) {
        for (Client c : clients) {
            if (c.getUsername().equals(username))
                return true;
        }
        return false;
    }

    // Get a client by username
    public Client getClient(String username) {
        for (Client c : clients) {
            if (c.getUsername().equals(username))
                return c;
        }
        return null;
    }

    // Add a client to the list
    public void addClient(Client c) {
        clients.add(c);
    }

    // Get an array of all clients
    public Client[] getClients() {
        Client[] c = new Client[clients.size()];
        for (int i = 0; i < c.length; i++) {
            c[i] = clients.get(i);
        }
        return c;
    }

    // Check if a user is an owner
    public boolean isOwner(String username) {
        for (Owner o : owners) {
            if (o.getUsername().equals(username))
                return true;
        }
        return false;
    }

    // Get an owner by username
    public Owner getOwner(String username) {
        for (Owner o : owners) {
            if (o.getUsername().equals(username))
                return o;
        }
        return null;
    }

    // Add an owner to the list
    public void addOwner(Owner o) {
        owners.add(o);
    }

    // Check if a user exists
    public boolean isUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username))
                return true;
        }
        return false;
    }

    // Get a user by username
    public User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    // Add a user to the list
    public void addUser(User thisUser) {
        users.add(thisUser);
    }

    // Check if a user account with a given username and password exists
    public boolean accountFound(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return true;
        }
        return false;
    }

    // Add a completed job to the list of completed jobs
    public void transferCompletedJob(Job job) {
        completedJobs.add(job);
    }

    // Update the database with user actions and timestamps
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
