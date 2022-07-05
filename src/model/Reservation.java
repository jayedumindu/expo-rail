package model;

import javafx.scene.control.Button;

public class Reservation {
    String NIC;
    String Reg_No;
    String tag;
    int seating;
    String begin;
    String end;
    double value;
    Button delete;

    public Reservation(String NIC, String reg_No, String tag, int seating, String begin, String end, double value) {
        this.NIC = NIC;
        Reg_No = reg_No;
        this.tag = tag;
        this.seating = seating;
        this.begin = begin;
        this.end = end;
        this.value = value;
        delete = new Button("delete");
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

    public String getReg_No() {
        return Reg_No;
    }

    public void setReg_No(String reg_No) {
        Reg_No = reg_No;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getSeating() {
        return seating;
    }

    public void setSeating(int seating) {
        this.seating = seating;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
