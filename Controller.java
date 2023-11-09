import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Controller {
  private ArrayList<Vehicle> vehicles;
  private ArrayList<Job> jobs;
  private Server server;
  private int redundancyLevel;

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
    JTextArea description = new JTextArea("From this page, you are able to view jobs that have been submitted to the vehicular cloud system as well as their completion times.");

    description.setLineWrap(true);
    description.setWrapStyleWord(true);
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

}

