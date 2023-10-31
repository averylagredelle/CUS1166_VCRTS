import java.util.ArrayList;
import java.time.Duration;

public class Controller {
  private ArrayList<Vehicle> vehiclesArrayList;
  private ArrayList<Job> jobs;
  private Server server;
  private int redundancyLevel;
  private boolean checkpoint;
 
  


  public void assignJob(Job j) {

    jobs.add(j);
    

  }

  public void triggerCheckpoint(Vehicle v) {

    String currentComputationState = v.getComputationState();

  }

  public void recruitNewVehicle(Vehicle oldV, Vehicle newV, Job j) {
   
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


