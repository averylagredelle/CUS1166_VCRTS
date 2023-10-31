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

  public void triggerCheckpoint(Vehicle v) {

    String currentComputationState = v.getComputationState();
    createCheckpoint(currentComputationState);

    System.out.println("Checkpoint triggered for Vehicle " + v.getLicensePlateNumber());

  }

  public void recruitNewVehicle(Vehicle oldV, Vehicle newV, Job j) {
    if (j != null) {
      // newV.loadCheckpoint(checkpoint);
      // Vehicle.add(newV);
      newV.getComputationState();
  } else {
      System.out.println("No checkpoint available for recruitment.");
  }
}
  

  public void setJobCompleted(Job j) {


    
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


