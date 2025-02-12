package com.example.kmb.model;

import java.util.List;

public class stopETA {
    String stopName;
    List<String> etas;
    int seq;

    public stopETA(String stopName, List<String> etas, int seq) {
        this.stopName = stopName;
        this.etas = etas;
        this.seq = seq;
    }

    public String getStopName() {
        return stopName;
    }

    public int getSeq() {
        return seq;
    }

    public List<String> getEtas() {
        return etas;
    }
}
