package model;


import javafx.scene.control.Button;

public class Schedule {
    String v_num;
    String tag;
    String driver;
    String departure;
    String begin;
    String end;
    String type;
    Object stops;
    int passengers;
    Button delete;

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public Schedule(String v_num, String tag, String driver, String departure, String begin, String end, String type, Object stops, int passengers) {
        this.v_num = v_num;
        this.tag = tag;
        this.driver = driver;
        this.departure = departure;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.stops = stops;
        this.passengers = passengers;
        this.delete = new Button("delete");
    }

    public Schedule(String v_num, String tag, String driver, String departure, String begin, String end, String type, int passengers) {
        this.v_num = v_num;
        this.tag = tag;
        this.driver = driver;
        this.departure = departure;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.passengers = passengers;
        this.delete = new Button("delete");
    }

    public String getV_num() {
        return v_num;
    }

    public void setV_num(String v_num) {
        this.v_num = v_num;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getStops() {
        return stops;
    }

    public void setStops(Object stops) {
        this.stops = stops;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
}
