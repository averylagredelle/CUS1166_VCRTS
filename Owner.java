import java.util.ArrayList;

public class Owner extends User {
    private ArrayList<Vehicle> rentals;

    public Owner(String username, String password, String name, String email, String phoneNumber) {
        super(username, password, name, email, phoneNumber);
        rentals = new ArrayList<Vehicle>();
    }

    public Owner(String username, String password) {
        super(username, password);
        rentals = new ArrayList<Vehicle>();
    }

    public void rentVehicle(Vehicle v, Controller c) {
        rentals.add(v);
        c.addVehicle(v);
    }

    public void removeVehicle(Vehicle v, Controller c) {
        rentals.remove(v);
        c.removeVehicle(v);
    }

    public String getRentals() {
        String carRentals = "";
        
        for(Vehicle c: rentals) {
            carRentals = carRentals.concat(String.valueOf(c));
        }

        return carRentals;
    }

    @Override
    public String toString() {
        return "Owner ID: " + this.getUsername() + getRentals();
    }
}

