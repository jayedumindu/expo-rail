package model;

public class Station {
    String tag;
    int st_no;
    double c1_price;
    double c2_price;
    double c3_price;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getSt_no() {
        return st_no;
    }

    public void setSt_no(int st_no) {
        this.st_no = st_no;
    }

    public double getC1_price() {
        return c1_price;
    }

    public void setC1_price(double c1_price) {
        this.c1_price = c1_price;
    }

    public double getC2_price() {
        return c2_price;
    }

    public void setC2_price(double c2_price) {
        this.c2_price = c2_price;
    }

    public double getC3_price() {
        return c3_price;
    }

    public void setC3_price(double c3_price) {
        this.c3_price = c3_price;
    }

    public Station(String tag, int st_no, double c1_price, double c2_price, double c3_price) {
        this.tag = tag;
        this.st_no = st_no;
        this.c1_price = c1_price;
        this.c2_price = c2_price;
        this.c3_price = c3_price;
    }
}
