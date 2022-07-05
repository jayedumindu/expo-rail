package model;

import javafx.scene.control.Button;;

public class Driver {
    String NIC;
    String ID;
    int Age;
    String Name;
    String TP_Number;
    String Address;
    Button delete;
    boolean scheduled = false;

    public Driver(String NIC, String ID, int age, String name, String TP_Number, String address) {
        this.NIC = NIC;
        this.ID = ID;
        Age = age;
        Name = name;
        this.TP_Number = TP_Number;
        Address = address;
        delete = new Button("delete");
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTP_Number() {
        return TP_Number;
    }

    public void setTP_Number(String TP_Number) {
        this.TP_Number = TP_Number;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
