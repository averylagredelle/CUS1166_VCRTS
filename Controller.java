import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
  private ArrayList<Vehicle> vehicles;
  private ArrayList<Job> jobs;
  private Server server;
  private int redundancyLevel;
  //private boolean checkpoint;
  
  private int minutesFromStart;
  private HashMap<Job, Integer> completionTimes;

  public Controller() {
    jobs = new ArrayList<Job>();
    minutesFromStart = 0;
    completionTimes = new HashMap<Job, Integer>();
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

