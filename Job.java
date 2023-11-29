import java.time.LocalDate;
///Users/tanvirahmed/gui/CUS1166_VCRTS/Job.java
public class Job {
    private boolean inProgress;
    private String title;
    private String description;
    private int durationTime;
    private LocalDate deadline;
    private String jobOwner;

    public Job() {
        this.title = "";
        this.description = "";
        this.durationTime = -1;
        this.deadline = LocalDate.parse("2000-01-01");
    }

    public Job(String title, String description, int durationTime, LocalDate deadline) {/* getters and setters for all parameters*/
        this.title = title;
        this.description = description;
        this.durationTime = durationTime;
        this.deadline = deadline;
        this.inProgress = true;
    }

    public String getTitle() {/* title of job  */
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {/* description of job  */
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationTime() {/* time the job will take to be completed   */
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public LocalDate getDeadline() { /* dead line for job time  */
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isInProgress() { /*boolean that sends true or false statement fro whether the job in is progress  */
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public String getJobOwner() { /* user who requested the job */
        return jobOwner;
    }

    public void setJobOwner(String jobOwner) {
        this.jobOwner = jobOwner;
    }

    @Override
    public String toString() {  //to string method will add all information into database txt file
        return "|Title: " + title + "|Description: " + description + "|Duration Time: " + 
            durationTime + " minutes |Deadline: " + deadline;
    }
}
