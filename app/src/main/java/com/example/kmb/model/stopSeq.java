package com.example.kmb.model;

public class stopSeq {
    public String stopid;
    public int seq;

    public stopSeq(String stopid, int seq) {
        this.stopid = stopid;
        this.seq = seq;
    }

    public  String getStopid() {
        return stopid;
    }

    public int getSeq() {
        return seq;
    }
}
