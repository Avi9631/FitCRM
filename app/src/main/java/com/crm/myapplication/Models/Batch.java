package com.crm.myapplication.Models;

public class Batch {

    private String batchid;
    private String batchname;
    private String batchdesc;
    private String batchtot;
    private String batchMaxStrength;
    private String batchtimestamp;
    private String status;


    public Batch(String batchid, String batchname, String batchdesc, String batchtot, String batchMaxStrength, String batchtimestamp, String status) {
        this.batchid = batchid;
        this.batchname = batchname;
        this.batchdesc = batchdesc;
        this.batchtot = batchtot;
        this.batchMaxStrength = batchMaxStrength;
        this.batchtimestamp = batchtimestamp;
        this.status = status;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    public String getBatchdesc() {
        return batchdesc;
    }

    public void setBatchdesc(String batchdesc) {
        this.batchdesc = batchdesc;
    }

    public String getBatchtot() {
        return batchtot;
    }

    public void setBatchtot(String batchtot) {
        this.batchtot = batchtot;
    }

    public String getBatchMaxStrength() {
        return batchMaxStrength;
    }

    public void setBatchMaxStrength(String batchMaxStrength) {
        this.batchMaxStrength = batchMaxStrength;
    }

    public String getBatchtimestamp() {
        return batchtimestamp;
    }

    public void setBatchtimestamp(String batchtimestamp) {
        this.batchtimestamp = batchtimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
