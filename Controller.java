import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Controller {
  private ArrayList<Vehicle> vehicles;
  private ArrayList<Job> jobs;
  private Server database;
  private int redundancyLevel;

  private static ServerSocket serverSocket;
  private static Socket socket;
  private static DataInputStream inputStream;
  private static DataOutputStream outputStream;
  private static boolean controllerConnectionOn = true;

  private int minutesFromStart;
  private HashMap<Job, Integer> completionTimes;

  private JFrame frame = new JFrame();
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
  }

  public void createIntroScreen() {
    JPanel introPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50));
    JLabel title = new JLabel("Welcome to the Controller for the Vehicular Cloud Real Time System");
    JTextArea description = new JTextArea("From this page, you are able to view jobs that have been submitted to the vehicular                         cloud system as well as their completion times.");

    description.setLineWrap(true);
    description.setEditable(false);
    description.setSize(APP_WIDTH - 50, APP_HEIGHT - 50);
    description.setFocusable(false);    

    introPanel.add(title);
    introPanel.add(description);
    frame.add(introPanel);
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

        case "sendJobRequest": {
          try {
            String jobTitle, jobDescription, deadline, username;
            int jobDurationTime = 0;

            outputStream.writeUTF("send job field");
            String params = inputStream.readUTF();
            String[] paramsList = params.split(",");
            jobTitle = paramsList[0];
            jobDescription = paramsList[1];
            jobDurationTime = Integer.valueOf(paramsList[2]);
            deadline = paramsList[3];
            username = paramsList[4];


            break;
          }
        catch(IOException e) {
          System.out.println("An error occurred with recordNewLogin()");
          break;
        }
      }
    }
    }
  }


