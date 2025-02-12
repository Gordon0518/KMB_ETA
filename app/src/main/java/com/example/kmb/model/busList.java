package com.example.kmb.model;

public class busList {
    String busNum, start, dest;

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public busList(String busNum, String start, String dest) {
        this.busNum = busNum;
        this.start = start;
        this.dest = dest;

    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
