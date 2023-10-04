public class Owner extends User {
    private String firstName;
    private String lastname;
    private String email;
    private double phoneNumber;
    private String vehicleInfo;
    private String licensePlate;
    private int residencyTime;

    public Owner(String firstName, String lastName, String email, Long phoneNumber, String vehicleInfo, 
    String licensePlate, int residencyTime, String username, String password) {
        super(username, password);
        this.firstName = firstName;
        this.lastname = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.vehicleInfo = vehicleInfo;
        this.licensePlate = licensePlate;
        this.residencyTime = residencyTime;
    }

    public Owner(String username, String password) {
        super(username, password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(double phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getResidencyTime() {
        return residencyTime;
    }

    public void setResidencyTime(int residencyTime) {
        this.residencyTime = residencyTime;
    }

    @Override
    public String toString() {
        return "Owner Information{" +
                "\n First Name: " + firstName +
                "\n Last Name: " + lastname +
                "\n Email: " + email +
                "\n Phone Number: " + phoneNumber +
                "\n Vehicle Info: " + vehicleInfo +
                "\n License Plate: " + licensePlate +
                "\n Residency Time: " + residencyTime + " hours" +
                "\n}";
    }
}