import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Controller {
  private ArrayList<Vehicle> vehicles;
  private ArrayList<Job> jobs;
  private Server database;
  //private int redundancyLevel;

  private static ServerSocket serverSocket;
  private static Socket socket;
  private static DataInputStream inputStream;
  private static DataOutputStream outputStream;
  private static boolean controllerConnectionOn = true;

  private int minutesFromStart;
  private HashMap<Job, Integer> completionTimes;

  private JFrame frame = new JFrame();
  private JPanel jobsPanel = new JPanel(new GridLayout());
  private JPanel rentalsPanel = new JPanel(new GridLayout());
  private final int APP_WIDTH = 480;
  private final int APP_HEIGHT = 600;

  public Controller() {
    jobs = new ArrayList<Job>();
    vehicles = new ArrayList<Vehicle>();
    minutesFromStart = 0;
    completionTimes = new HashMap<Job, Integer>();
    database = new Server();
    //completed jobs output
    //completedJobsOutput into new file "completedjobs.txt"
    //throws FileNotFoundException

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new CardLayout());
    frame.setTitle("Vehicular Cloud Real Time Controller");
    frame.setSize(APP_WIDTH, APP_HEIGHT);
    frame.setResizable(false);
    frame.setLocation(600, 100);

    startApp();
    frame.setVisible(true);

    try{
      serverSocket = new ServerSocket(1000);
      socket = serverSocket.accept();
      inputStream = new DataInputStream(socket.getInputStream());
      outputStream = new DataOutputStream(socket.getOutputStream());
    }
    catch(IOException e) {
      System.out.println("An error occurred, could not connect");
    }

    String request = "";

    while(controllerConnectionOn) {
      try {
        request = inputStream.readUTF();
        parseRequest(request);
      }
      catch(IOException e) {
        System.out.println("An error occurred while trying to read requests");
      }
    }
  }

  public static void main(String[] args) {
    new Controller();
  }

  public void startApp() {
    createIntroScreen();
    createJobsListScreen();
    createRentalsListScreen();
  }

  public void createIntroScreen() {
    JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JPanel textPanel = new JPanel(new BorderLayout(0, 50));
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel title = new JLabel("Welcome to the Controller for the Vehicular Cloud Real Time System");
    JPanel descriptionPanel = new JPanel(new BorderLayout());
    JTextArea description = new JTextArea("On this page, you are able to view jobs that have been submitted to the vehicular cloud system with their completion times. You can also see how many vehicles are currently in the VC System. To begin, press \"Show Jobs\" or \"Show Vehicles\" below.");
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50));
    JButton showJobs = new JButton("Show Jobs");
    JButton showVehicles = new JButton("Show Vehicles");

    titlePanel.add(title);

    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    description.setEditable(false);
    description.setMinimumSize(new Dimension(APP_WIDTH - 50, 200));
    description.setFocusable(false);

    descriptionPanel.setSize(APP_WIDTH, 100);
    descriptionPanel.add(description);

    textPanel.add(titlePanel, BorderLayout.NORTH);
    textPanel.add(descriptionPanel, BorderLayout.CENTER);

    buttonPanel.add(showJobs);
    buttonPanel.add(showVehicles);

    showJobs.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Job List Screen");
    });

    showVehicles.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Rental List Screen");
    });

    mainPanel.add(textPanel);
    mainPanel.add(buttonPanel);
    frame.add(mainPanel, "Intro Screen");
  }

  public void createJobsListScreen() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel title = new JLabel("Current Jobs Requested From Users");
    JScrollPane jobContainer;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50));
    JButton showJobs = new JButton("Show Jobs");
    JButton showVehicles = new JButton("Show Vehicles");

    titlePanel.add(title);

    jobContainer = new JScrollPane(jobsPanel);
    jobContainer.setBorder(null);
    jobContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    showVehicles.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Rental List Screen");
    });

    buttonPanel.add(showJobs);
    buttonPanel.add(showVehicles);

    mainPanel.add(titlePanel, BorderLayout.NORTH);
    mainPanel.add(jobContainer, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    frame.add(mainPanel, "Job List Screen");
  }

  public void createRentalsListScreen() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel title = new JLabel("Current Vehicles Rented to VC System by Users");
    JScrollPane rentalContainer;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50));
    JButton showJobs = new JButton("Show Jobs");
    JButton showVehicles = new JButton("Show Vehicles");

    titlePanel.add(title);

    rentalContainer = new JScrollPane(rentalsPanel);
    rentalContainer.setBorder(null);
    rentalContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    showJobs.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Job List Screen");
    });

    buttonPanel.add(showJobs);
    buttonPanel.add(showVehicles);

    mainPanel.add(titlePanel, BorderLayout.NORTH);
    mainPanel.add(rentalContainer, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    frame.add(mainPanel, "Rental List Screen");
  }

  public void updateJobsPanel() {
    jobsPanel.removeAll();
    ((GridLayout)jobsPanel.getLayout()).setRows(jobs.size() * 8);
    ((GridLayout)jobsPanel.getLayout()).setColumns(1);
    for(int i = 0; i < this.jobs.size(); i++) {
      jobsPanel.add(new JLabel(".............................................................................................................................."));
      jobsPanel.add(new JLabel("                               Job " + String.valueOf(i + 1) + ": " + this.jobs.get(i).getTitle()));
      jobsPanel.add(new JLabel("                               Description: " + this.jobs.get(i).getDescription()));
      jobsPanel.add(new JLabel("                               Duration Time: " + this.jobs.get(i).getDurationTime() + " minutes"));
      jobsPanel.add(new JLabel("                               Deadline: " + this.jobs.get(i).getDeadline()));
      jobsPanel.add(new JLabel("                               Job Owner: " + this.jobs.get(i).getJobOwner().getUsername()));
      jobsPanel.add(new JLabel("                               Time Completed: " + completionTimes.get(this.jobs.get(i))));
    }
    JPanel totalTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    totalTimePanel.add(new JLabel("*****Total Run Time: " + minutesFromStart + "*****"));
    jobsPanel.add(totalTimePanel);
    frame.validate();
  }

  public void updateRentalsPanel() {
    rentalsPanel.removeAll();
    ((GridLayout)rentalsPanel.getLayout()).setRows(vehicles.size() * 7);
    ((GridLayout)rentalsPanel.getLayout()).setColumns(1);
    for(int i = 0; i < vehicles.size(); i++) {
      rentalsPanel.add(new JLabel(".............................................................................................................................."));
      rentalsPanel.add(new JLabel("                               Vehicle " + String.valueOf(i + 1) + " Make: " + vehicles.get(i).getMake()));
      rentalsPanel.add(new JLabel("                               Model: " + vehicles.get(i).getModel()));
      rentalsPanel.add(new JLabel("                               License Plate Number: " + vehicles.get(i).getLicensePlateNumber()));
      rentalsPanel.add(new JLabel("                               Residency: " + vehicles.get(i).getResidency() + " days"));
      rentalsPanel.add(new JLabel("                               Vehicle Arrival Time: " + vehicles.get(i).getArrivalTime()));
      rentalsPanel.add(new JLabel("                               Vehicle Owner: " + vehicles.get(i).getVehicleOwner().getUsername()));
    }
    frame.validate();
  }

  public void assignJob(Job j) {
    jobs.add(j);
    completionTimes.put(j, minutesFromStart + j.getDurationTime());
    minutesFromStart += j.getDurationTime();
  }

  // public void createCheckpoint(String checkpointImage) {
  //   // checkpoint = new Checkpoint(checkpointImage);
  // }

  // Trigger a checkpoint for a vehicle
  public void triggerCheckpoint(Vehicle v) {
    // Implement the logic for triggering a checkpoint for a vehicle
  }

  // Recruit a new vehicle to replace an old one
  public void recruitNewVehicle(Vehicle oldV, Vehicle newV) {
    // Implement the logic for recruiting a new vehicle to replace an old one
  }

  // Mark a job as completed
  public void setJobCompleted(Job j) {
    // Implement the logic for marking a job as completed
  }

  public void addVehicle(Vehicle v) {
    vehicles.add(v);
  }

  public void removeVehicle(Vehicle v) {
    vehicles.remove(v);
  }


  public int calculateJobCompletionTime(Job j){
    return minutesFromStart + j.getDurationTime();
  }

  public void parseRequest(String request) {
    switch(request) {
      case "database isUser": {
        try {
          outputStream.writeUTF("send username");
          //System.out.println("send username sent in output stream");
          String username = "";
          username = inputStream.readUTF();
          //System.out.println("username received from input stream");
          outputStream.writeBoolean(database.isUser(username));
          //System.out.println("boolean sent in output stream");
          break;
        }
        catch(IOException e) {
          System.out.println("An IO error occurred with database.isUser()");
          break;
        }
      }

      case "database accountFound": {
        try {
          String username, password;
          outputStream.writeUTF("send username and password");
          String params = inputStream.readUTF();
          String[] paramsList = params.split(",");
          username = paramsList[0];
          password = paramsList[1];
          outputStream.writeBoolean(database.accountFound(username, password));
          break;
        }
        catch(IOException e) {
          System.out.println("An error occurred with database.accountFound()");
          break;
        }
      }

      case "database addUser": {
        try {
          String username, password;
          outputStream.writeUTF("send username and password");
          String params = inputStream.readUTF();
          String[] paramsList = params.split(",");
          username = paramsList[0];
          password = paramsList[1];
          User newUser = new User(username, password);
          database.addUser(newUser);
          database.updateDatabase("New Sign Up", newUser);
          outputStream.writeBoolean(true);
          break;
        }
        catch(IOException e) {
          System.out.println("An error occurred with database.addUser()");
          break;
        }
      }
    
      case "recordNewLogin": {
        try {
          String username;
          outputStream.writeUTF("send username");
          username = inputStream.readUTF();
          User user = database.getUser(username);
          database.updateDatabase("New Login", user);
          outputStream.writeBoolean(true);
          break;
        }
        catch(IOException e) {
          System.out.println("An error occurred with recordNewLogin()");
          break;
        }
      }

      case "database sendJobRequest": {
        try {
          String jobTitle, jobDescription, deadline, username;
          int jobDurationTime = 0;

          outputStream.writeUTF("send job fields");
          String params = inputStream.readUTF();
          String[] paramsList = params.split(",");
          jobTitle = paramsList[0];
          jobDescription = paramsList[1];
          jobDurationTime = Integer.parseInt(paramsList[2]);
          deadline = paramsList[3];
          username = paramsList[4];

          Job job = new Job(jobTitle, jobDescription, jobDurationTime, LocalDate.parse(deadline));
          Client c;

          if(database.isClient(username)){
            c = database.getClient(username);
            job.setJobOwner(c);
            c.submitJob(job,this);
          }
          else {
            c = new Client(database.getUser(username).getUsername(),database.getUser(username).getPassword());
            job.setJobOwner(c);
            database.addClient(c);
            c.submitJob(job,this);
          }
          database.updateDatabase("New Job Submitted", c);
          updateJobsPanel();

          break;
        }
        catch(IOException e) {
          System.out.println("An error occurred with recordsendJobRequest()");
          break;
        } catch(NumberFormatException e) {
          System.out.println("An error occurred with Integer.parseInt");
          break;
        }
      }

      case "controller calculateJobCompletionTime": {
        try {
          String title, description;
          int durationTime = -1;
          LocalDate deadline;

          outputStream.writeUTF("send job params");
          String params = inputStream.readUTF();
          String[] paramsList = params.split(",");

          title = paramsList[0];
          description = paramsList[1];
          durationTime = Integer.parseInt(paramsList[2]);
          deadline = LocalDate.parse(paramsList[3]);

          outputStream.writeInt(calculateJobCompletionTime(new Job(title, description, durationTime, deadline)));
        }
        catch(IOException e) {
          System.out.println("An IO exception occurred while trying to calculate job completion time");
        }
        catch(NumberFormatException n) {
          System.out.println("A number format exception occurred while trying to calculate job completion time");
        }
      }
        case "database sendRentalRequest": {
        try {
          String make, model, licensePlateNumber, username;
          int residency = 0;
          outputStream.writeUTF("send vehicle fields");
          String params = inputStream.readUTF();
          String[] paramsList = params.split(",");

          make = paramsList[0];
          model = paramsList[1];
          licensePlateNumber = paramsList[2];
          residency = Integer.parseInt(paramsList[3]);
          username = paramsList[4];
         
          Vehicle vehicle = new Vehicle(make, model, licensePlateNumber, residency);
          Owner o;
          
          if(database.isOwner(username)){
            o = database.getOwner(username);
            vehicle.setVehicleOwner(o);
            vehicle.setArrivalTime(new Date());
            o.rentVehicle(vehicle,this);
          }
          else {
            o = new Owner(database.getUser(username).getUsername(),database.getUser(username).getPassword());
            database.addOwner(o);
            vehicle.setVehicleOwner(o);
            vehicle.setArrivalTime(new Date());
            o.rentVehicle(vehicle,this);
          }
          database.updateDatabase("New Vehice Rented", o); 
          updateRentalsPanel();

          break;
        }
        catch(IOException e) {
          System.out.println("An error occurred with recordsendJobRequest()");
          break;
        } catch(NumberFormatException e) {
          System.out.println("An error occurred with Integer.parseInt");
          break;
        }
      }         
    }
  }
}



