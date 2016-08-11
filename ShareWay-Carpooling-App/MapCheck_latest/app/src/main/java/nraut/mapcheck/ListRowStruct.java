package nraut.mapcheck;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by naina on 5/11/2016.
 */
public class ListRowStruct implements Serializable {
    String profile_pic;
    String personName;
    String origin;
    String destination;
    String email;
    String phone;
    String status;

    public ListRowStruct(String profile_pic, String personName, String origin,
                         String destination, String email, String phone, String status){
        this.profile_pic = profile_pic;
        this.personName = personName;
        this.origin = origin;
        this.destination = destination;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
