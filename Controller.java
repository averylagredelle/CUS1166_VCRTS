import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;


import javax.swing.JButton;
import javax.swing.JDialog;
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
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 // GUI components

  private JFrame frame = new JFrame();
  private JPanel jobsPanel = new JPanel(new GridLayout());
  private JPanel rentalsPanel = new JPanel(new GridLayout());
  private JDialog messageBox = new JDialog();
  private boolean acceptChosen = false;
  private final int APP_WIDTH = 480;
  private final int APP_HEIGHT = 600;
  private Color backgroundColor;
  private Color buttonColor;
  private Color textColor;
  private float buttonSize = 15;
  private float textSize = 20;
  /**
     * Controller class constructor. initializes and configures data structures
     * the graphical user interface with predetermined dimensions and colors.
     */


  public Controller() {
    database = new Server();
    completionTimes = new HashMap<Job, Integer>();

    // Set GUI color scheme 
    backgroundColor = new Color(187, 242, 184);//background color
    buttonColor = new Color(80,192,217);//button color
    textColor = new Color(0,0,0);//text color


    //completed jobs output
    //completedJobsOutput into new file "completedjobs.txt"
    //throws FileNotFoundException

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new CardLayout());
    frame.setTitle("Vehicular Cloud Real Time Controller");
    frame.setSize(APP_WIDTH, APP_HEIGHT);
    frame.setResizable(false);
    frame.setLocation(850, 100);

    

    messageBox.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    messageBox.setLayout(new GridLayout(5, 1));
    messageBox.setSize(300, 400);
    messageBox.setResizable(false);
    messageBox.setModalityType(ModalityType.APPLICATION_MODAL);

    startApp();
    frame.setVisible(true);
    messageBox.setLocationRelativeTo(frame);
    jobs = database.getJobs(completionTimes);
    minutesFromStart = completionTimes.get(jobs.get(jobs.size() - 1));
    updateJobsPanel();
    vehicles = database.getVehicles();
    updateRentalsPanel();

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
 /**
  * Starts the Vehicular Cloud Real Time Controller application by creating and displaying
  * the introductory screen, jobs list screen, and rentals list screen.
  */
  public void startApp() { 
    createIntroScreen(); // Create and display the introductory screen
    createJobsListScreen();  // Create and display the jobs list screen
    createRentalsListScreen(); // Create and display the rentals list screen
  }
  /**
 * Creates the introductory screen with a welcome message, system description, and
 * buttons to show jobs and vehicles.
 */
  public void createIntroScreen() {
    // Set up the main panel with a centered flow layout
    JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    // Set up the text panel with a border layout and vertical gap
    JPanel textPanel = new JPanel(new BorderLayout(0, 50));
    // Set up the title panel with a centered flow layout
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel title = new JLabel("Welcome to the Controller for the Vehicular Cloud Real Time System");
    // Set up the description panel with a border layout
    JPanel descriptionPanel = new JPanel(new BorderLayout());
    JTextArea description = new JTextArea("On this page, you are able to view jobs that have been submitted to the vehicular cloud system with their completion times. You can also see how many vehicles are currently in the VC System. To begin, press \"Show Jobs\" or \"Show Vehicles\" below.");
    // Set up the button panel with a centered flow layout and horizontal gap
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50));
    JButton showJobs = new JButton("Show Jobs");
    JButton showVehicles = new JButton("Show Vehicles");

   // Set up the title panel by adding the title label and configuring its background color 
    titlePanel.add(title);
    titlePanel.setBackground(backgroundColor);
    title.setBackground(backgroundColor);

    // Configure settings for the description text area
    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    description.setEditable(false);
    description.setMinimumSize(new Dimension(APP_WIDTH - 50, 200));
    description.setFocusable(false);
    description.setBackground(backgroundColor);
    description.setFont(description.getFont().deriveFont(textSize));


    // Set the size and add the description text area to the description panel
    descriptionPanel.setSize(APP_WIDTH, 100);
    descriptionPanel.add(description);
    descriptionPanel.setBackground(backgroundColor);

    textPanel.add(titlePanel, BorderLayout.NORTH);
    textPanel.add(descriptionPanel, BorderLayout.CENTER);
    textPanel.setBackground(backgroundColor);


    buttonPanel.add(showJobs);
    buttonPanel.add(showVehicles);
    buttonPanel.setBackground(backgroundColor);;

    showJobs.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Job List Screen");
    });
    showJobs.setForeground(textColor);
    showJobs.setBackground(buttonColor);
    showJobs.setBorderPainted(false);
    showJobs.setOpaque(true);
    showJobs.setFont(showJobs.getFont().deriveFont(buttonSize));

    showVehicles.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Rental List Screen");
    });
    showVehicles.setForeground(textColor);
    showVehicles.setBackground(buttonColor);
    showVehicles.setBorderPainted(false);
    showVehicles.setOpaque(true);
    showVehicles.setFont(showVehicles.getFont().deriveFont(buttonSize));

    mainPanel.add(textPanel);
    mainPanel.add(buttonPanel);
    mainPanel.setBackground(backgroundColor);

    frame.add(mainPanel, "Intro Screen");
  }
  /**
 * Creates the jobs list screen with a title, job container, and buttons to show jobs and vehicles.
 */
  public void createJobsListScreen() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel title = new JLabel("Current Jobs Requested From Users");
    JScrollPane jobContainer;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50));
    JButton showJobs = new JButton("Show Jobs");
    JButton showVehicles = new JButton("Show Vehicles");

    titlePanel.add(title);
    titlePanel.setBackground(backgroundColor);
    title.setFont(title.getFont().deriveFont(textSize));


    jobContainer = new JScrollPane(jobsPanel);
    jobContainer.setBorder(null);
    jobContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    showVehicles.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Rental List Screen");
    });
    showVehicles.setForeground(textColor);
    showVehicles.setBackground(buttonColor);
    showVehicles.setBorderPainted(false);
    showVehicles.setOpaque(true);
    showVehicles.setFont(showVehicles.getFont().deriveFont(buttonSize));

    showJobs.setForeground(textColor);
    showJobs.setBackground(buttonColor);
    showJobs.setBorderPainted(false);
    showJobs.setOpaque(true);
    showJobs.setFont(showJobs.getFont().deriveFont(buttonSize));
    showJobs.setBounds(0, 40, APP_WIDTH, APP_HEIGHT);




    buttonPanel.add(showJobs);
    buttonPanel.add(showVehicles);
    buttonPanel.setBackground(backgroundColor);

    mainPanel.add(titlePanel, BorderLayout.NORTH);
    mainPanel.add(jobContainer, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    mainPanel.setBackground(backgroundColor);
    frame.add(mainPanel, "Job List Screen");
  }

  /**
 * Creates the rentals list screen with a title, rental container, and buttons to show jobs and vehicles.
 */
  public void createRentalsListScreen() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel title = new JLabel("Current Vehicles Rented to VC System by Users");
    JScrollPane rentalContainer;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50));
    JButton showJobs = new JButton("Show Jobs");
    JButton showVehicles = new JButton("Show Vehicles");

    titlePanel.add(title);
    titlePanel.setBackground(backgroundColor);
    //title.setBackground(backgroundColor);
    title.setFont(title.getFont().deriveFont(textSize));

    

    rentalContainer = new JScrollPane(rentalsPanel);
    rentalContainer.setBorder(null);
    rentalContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    showVehicles.setForeground(textColor);
    showVehicles.setBackground(buttonColor);
    showVehicles.setBorderPainted(false);
    showVehicles.setOpaque(true);
    showVehicles.setFont(showVehicles.getFont().deriveFont(buttonSize));


    showJobs.addActionListener(e -> {
      ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), "Job List Screen");
    });
    showJobs.setForeground(textColor);
    showJobs.setBackground(buttonColor);
    showJobs.setBorderPainted(false);
    showJobs.setOpaque(true);
    showJobs.setFont(showJobs.getFont().deriveFont(buttonSize));

    buttonPanel.add(showJobs);
    buttonPanel.add(showVehicles);
    buttonPanel.setBackground(backgroundColor);

    mainPanel.add(titlePanel, BorderLayout.NORTH);
    mainPanel.add(rentalContainer, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    mainPanel.setBackground(backgroundColor);
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
      jobsPanel.add(new JLabel("                               Job Owner: " + this.jobs.get(i).getJobOwner()));
      jobsPanel.add(new JLabel("                               Time Completed: " + completionTimes.get(this.jobs.get(i))));
    }
    JPanel totalTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    totalTimePanel.add(new JLabel("*****Total Run Time: " + minutesFromStart + "*****"));
    jobsPanel.add(totalTimePanel);
    frame.validate();
  }
 /**
 * Updates the jobs panel with information about each job, including title, description,
 * duration time, deadline, job owner, and time completed. It also displays the total run time.
 */
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
      rentalsPanel.add(new JLabel("                               Vehicle Arrival Time: " + formatter.format(vehicles.get(i).getArrivalTime())));
      rentalsPanel.add(new JLabel("                               Vehicle Owner: " + vehicles.get(i).getVehicleOwner()));
    }
    frame.validate();
  }
  /**
 * Displays a message in a dialog box regarding an incoming job submission request.
 * The message includes details such as job title, description, and duration time,
 * and provides options to accept or reject the job.
 * 
 * The Job object representing the incoming job submission request.
 */
  public void showMessage(Job j) {
    messageBox.getContentPane().removeAll();
    JLabel row1 = new JLabel("             Incoming Job Submission Request");
    JLabel row2 = new JLabel("             Job Title: " + j.getTitle());
    JLabel row3 = new JLabel("             Job Description: " + j.getDescription());
    JLabel row4 = new JLabel("             Job Duration Time: " + j.getDurationTime() + " minutes");
    JPanel row5 = new JPanel(new FlowLayout(FlowLayout.CENTER));

    JButton accept = new JButton("Accept");
    JButton reject = new JButton("Reject");

    accept.addActionListener(e -> {
      acceptChosen = true;
      messageBox.dispose();
    });

    reject.addActionListener(e -> {
      acceptChosen = false;
      messageBox.dispose();
    });

    row5.add(accept);
    row5.add(reject);

    messageBox.add(row1);
    messageBox.add(row2);
    messageBox.add(row3);
    messageBox.add(row4);
    messageBox.add(row5);
    messageBox.validate();
    messageBox.setVisible(true);
  }

  /**
 * Displays a message in a dialog box regarding an incoming vehicle rental request.
 * The message includes details such as vehicle make, model, and residency time,
 * and provides options to accept or reject the vehicle rental.
 * 
 * The Vehicle object representing the incoming vehicle rental request.
 */
  public void showMessage(Vehicle v) {
    messageBox.getContentPane().removeAll();
    JLabel row1 = new JLabel("             Incoming Vehicle Rental Request");
    JLabel row2 = new JLabel("             Vehicle Make: " + v.getMake());
    JLabel row3 = new JLabel("             Vehicle Model: " + v.getModel());
    JLabel row4 = new JLabel("             Vehicle Residency Time: " + v.getResidency() + " days");
    JPanel row5 = new JPanel(new FlowLayout(FlowLayout.CENTER));

    JButton accept = new JButton("Accept");
    JButton reject = new JButton("Reject");

    accept.addActionListener(e -> {
      acceptChosen = true;
      messageBox.dispose();
    });

    reject.addActionListener(e -> {
      acceptChosen = false;
      messageBox.dispose();
    });

    row5.add(accept);
    row5.add(reject);

    messageBox.add(row1);
    messageBox.add(row2);
    messageBox.add(row3);
    messageBox.add(row4);
    messageBox.add(row5);
    messageBox.validate();
    messageBox.setVisible(true);
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
          outputStream.writeBoolean(database.addUser(newUser));
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
          outputStream.writeBoolean(database.recordNewLogin(username));
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

          showMessage(job);
          boolean updateSuccessful = false;
          if(acceptChosen) {
            if(!database.isClient(username)){
              database.setUserClient(username);
            }
            c = database.getClient(username);
            c.submitJob(job,this);
            updateSuccessful = database.addJob(job);
            updateJobsPanel();
          }
          
          outputStream.writeBoolean(acceptChosen && updateSuccessful);
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
          break;
        }
        catch(IOException e) {
          System.out.println("An IO exception occurred while trying to calculate job completion time");
          break;
        }
        catch(NumberFormatException n) {
          System.out.println("A number format exception occurred while trying to calculate job completion time");
          break;
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
          
          showMessage(vehicle);
          boolean rentalSuccessful = false;
          if(acceptChosen) {
            if(!database.isOwner(username)){
              database.setUserOwner(username);
            }
            o = database.getOwner(username);
            o.rentVehicle(vehicle,this);
            rentalSuccessful = database.addVehicle(vehicle); 
            updateRentalsPanel();
          }

          outputStream.writeBoolean(acceptChosen && rentalSuccessful);
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



