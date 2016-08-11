package nraut.mapcheck;

/**
 * Created by naina on 5/20/2016.
 */
public class DriverTable {
    String email;
    double origin_lat;
    double origin_lng;
    double dest_lat;
    double dest_lng;
    String passengerEmail;
    String status;

    public String getEmail() {
        return  email;
    }

    public void setEmail(String email) {
        this. email = email;
    }

    public double getOrigin_lat() {
        return origin_lat;
    }

    public void setOrigin_lat(double origin_lat) {
        this.origin_lat = origin_lat;
    }

    public double getOrigin_lng() {
        return origin_lng;
    }

    public void setOrigin_lng(double origin_lng) {
        this.origin_lng = origin_lng;
    }

    public double getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(double dest_lat) {
        this.dest_lat = dest_lat;
    }

    public double getDest_lng() {
        return dest_lng;
    }

    public void setDest_lng(double dest_lng) {
        this.dest_lng = dest_lng;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
