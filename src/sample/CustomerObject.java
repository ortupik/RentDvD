package sample;

import javafx.beans.property.StringProperty;

/**
 * Created by Chris on 3/19/2018.
 */
public class CustomerObject {
    private String id;
    private String fname;
    private String lname;
    private String phone;
    private String address;
    private String time;

    public CustomerObject(String id, String fname, String lname, String phone, String address) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
