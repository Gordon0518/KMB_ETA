package com.example.kmb.model;

public class stopList {
    public String id,name_tc, name_en;
    public int seq;

    public stopList(String id, String name_en, String name_tc, int seq) {
        this.id = id;
        this.name_en = name_en;
        this.name_tc = name_tc;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_tc() {
        return name_tc;
    }

    public void setName_tc(String name_tc) {
        this.name_tc = name_tc;
    }
}
