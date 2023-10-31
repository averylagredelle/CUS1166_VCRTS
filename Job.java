import java.time.LocalDate;
///Users/tanvirahmed/gui/CUS1166_VCRTS/Job.java
public class Job {
    private boolean inProgress;
    private String title;
    private String description;
    private int durationTime;
    private LocalDate deadline;
    private Client jobOwner;

    public Job() {
        this.title = "";
        this.description = "";
        this.durationTime = -1;
        this.deadline = LocalDate.parse("2000-01-01");
    }

    public Job(String title, String description, int durationTime, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.durationTime = durationTime;
        this.deadline = deadline;
        this.inProgress = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Client getJobOwner() {
        return jobOwner;
    }

    public void setJobOwner(Client jobOwner) {
        this.jobOwner = jobOwner;
    }

    @Override
    public String toString() {
        return "|Title: " + title + "|Description: " + description + "|Duration Time: " + 
            durationTime + " minutes |Deadline: " + deadline;
    }
}
