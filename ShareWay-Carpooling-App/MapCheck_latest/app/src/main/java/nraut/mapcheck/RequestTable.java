package nraut.mapcheck;

/**
 * Created by naina on 5/23/2016.
 */
public class RequestTable {
    public String email;
    public String type;
    public String requestedEmailAddress;
    public String status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestedEmailAddress() {
        return requestedEmailAddress;
    }

    public void setRequestedEmailAddress(String requestedEmailAddress) {
        this.requestedEmailAddress = requestedEmailAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
