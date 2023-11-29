import java.time.LocalDateTime;

public class Owner extends User {

    public Owner(String username, String password, String name, String email, String phoneNumber) {
        super(username, password, name, email, phoneNumber);
    }

    public Owner(String username, String password) {
        super(username, password);
    }

    public void rentVehicle(Vehicle v, Controller c) {
        v.setVehicleOwner(getUsername());
        v.setArrivalTime(LocalDateTime.now());
        c.addVehicle(v);
    }

    public void removeVehicle(Vehicle v, Controller c) {
        if(v.getVehicleOwner().equals(getUsername())) {
            c.removeVehicle(v);
        }
    }

    public String getRentals() {
        String carRentals = "";
        
        // for(Vehicle c: rentals) {
        //     carRentals = carRentals.concat(String.valueOf(c));
        // }

        return carRentals;
    }

    @Override
    public String toString() {
        return "Owner ID: " + this.getUsername() + getRentals();
    }
}

