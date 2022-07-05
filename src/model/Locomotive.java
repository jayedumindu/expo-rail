package model;

import javafx.scene.control.Button;

public class Locomotive {
    String Reg_No;
    String Tag;
    String Type;
    int Compartments;
    int Seating;
    Object Classes;
    Button delete;
    boolean scheduled = false;

    public Locomotive(String reg_No, String tag, String type, int compartments, int seating, Object classes) {
        Reg_No = reg_No;
        Tag = tag;
        Type = type;
        Compartments = compartments;
        Seating = seating;
        Classes = classes;
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

    public String getReg_No() {
        return Reg_No;
    }

    public void setReg_No(String reg_No) {
        Reg_No = reg_No;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getCompartments() {
        return Compartments;
    }

    public void setCompartments(int compartments) {
        Compartments = compartments;
    }

    public int getSeating() {
        return Seating;
    }

    public void setSeating(int seating) {
        Seating = seating;
    }

    public Object getClasses() {
        return Classes;
    }

    public void setClasses(int[] classes) {
        Classes = classes;
    }
}
