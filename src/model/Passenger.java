package model;


import javafx.scene.control.Button;

public class Passenger {
    String NIC;
    String Name;
    String TP_Number;
    String Address;
    Button delete = new Button("delete");

    public Passenger(String NIC, String name, String TP_Number, String address) {
        this.NIC = NIC;
        Name = name;
        this.TP_Number = TP_Number;
        Address = address;
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
