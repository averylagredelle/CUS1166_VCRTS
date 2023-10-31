import java.util.ArrayList;

public class Controller {
  private ArrayList<Vehicle> vehiclesArrayList;
  private ArrayList<Job> jobs;
  private Server server;
  private int redundancyLevel;
  private boolean checkpoint;
 
  


  public void assignJob(Job j) {
    jobs.add(j);
    

  }
  public void createCheckpoint(String checkpointImage) {
    // checkpoint = new Checkpoint(checkpointImage);
}

  // Trigger a checkpoint for a vehicle
  public void triggerCheckpoint(Vehicle v) {
    // Implement the logic for triggering a checkpoint for a vehicle
  }

  // Recruit a new vehicle to replace an old one
  public void recruitNewVehicle(Vehicle oldV, Vehicle newV, Job j) {
    // Implement the logic for recruiting a new vehicle to replace an old one
  }

  // Mark a job as completed
  public void setJobCompleted(Job j) {
    // Implement the logic for marking a job as completed
  }


  public void calculateJobCompletionTime( Job j){
      int currentTime = 0;
        for (Job job : jobs) {
            job.setDurationTime(currentTime);
            currentTime += job.getDurationTime();
            //job.setDeadline(currentTime);
        }
        for (Job job : jobs) {
          System.out.println("Job ID: " + job.getDescription() + " - Completion Time: " + job.getDeadline());
      }
    }

    
  }

