import java.time.LocalDateTime;

public class Vehicle {
    private String make;
    private String model;
    private String licensePlateNumber;
    private int residency;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private Owner vehicleOwner;
    private String computationState;

    public Vehicle() {
        make = "";
        model = "";
        licensePlateNumber = "";
    }

    public Vehicle(String make, String model, String licensePlateNumber, int residency) {
        this.make = make;
        this.model = model;
        this.licensePlateNumber = licensePlateNumber;
        this.residency = residency;
    }

    // Getters and setters for the new fields

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public int getResidency() {
        return residency;
    }

    public void setResidency(int residency) {
        this.residency = residency;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Owner getVehicleOwner() {
        return vehicleOwner;
    }

    public void setVehicleOwner(Owner vehicleOwner) {
        this.vehicleOwner = vehicleOwner;
    }
    
    public String getComputationState() {
        return computationState;
    }

    public void setComputationState(String computationState) {
        this.computationState = computationState;
    }

    public void completeJob(Job j) {
        // Implementation for completing a job
    }

    public void startComputationFromCheckpoint(Job j) {
        // Implementation for starting computation from a checkpoint
    }

    public void copyComputationImage(Vehicle targetVehicle, Job j) {
        // Implementation for copying computation image to a target vehicle
    }

    public void eraseAllComputationData() {
        // Implementation for erasing all computation data
    }

    @Override
    public String toString() {
        return "|Make: " + make + "|Model: " + model + "|License Plate Number: " + licensePlateNumber + "|Residency Time: " + 
        residency + " days";
    }
}

