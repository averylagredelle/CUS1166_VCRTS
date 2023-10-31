import java.util.ArrayList;

public class Client extends User {
  private ArrayList<Job> requestedJobs;

  public Client(String username, String password) {
    super(username, password);
    requestedJobs = new ArrayList<Job>();
  }

  public Client(String username, String password, String name, String email, String phoneNumber) {
    super(username, password, name, email, phoneNumber);
    requestedJobs = new ArrayList<Job>();
  }

  public void submitJob(Job j) {
    requestedJobs.add(j);
    //c.processJob(j); // You can add logic here to process the job using the provided controller.
  }

  public String requestCheckpoint(Job j, Controller c) {
    // You can add logic here to request a checkpoint for the specified job using the provided controller.
    // Return a message or status string.
    //return "Checkpoint requested for job: " + j.getJobID();

    return "";
  }

  public String getQueuedJobs() {
    StringBuilder allJobs = new StringBuilder();
    for (Job job : requestedJobs) {
      allJobs.append(job.toString());
    }
    return allJobs.toString();
  }

  @Override
  public String toString() {
    return "Client ID: " + this.getUsername() + getQueuedJobs();
  }
}
