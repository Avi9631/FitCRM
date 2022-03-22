package com.crm.myapplication.Models;

import java.io.Serializable;

public class Member implements Serializable {

    private String name;
    private String id;
    private String picurl;
    private String docurl;
    private String joindate;
    private String feepaydate;
    private String expdate;
    private String mob;
    private String email;
    private String address;
    private String gender;
    private String dob;
    private String details;
    private String status;
    private String batch;
    private String batchname;

    public Member(String name, String id, String picurl, String docurl, String joindate, String feepaydate, String expdate, String mob, String email, String address, String gender, String dob, String details, String status, String batch, String batchname) {
        this.name = name;
        this.id = id;
        this.picurl = picurl;
        this.docurl = docurl;
        this.joindate = joindate;
        this.feepaydate = feepaydate;
        this.expdate = expdate;
        this.mob = mob;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.details = details;
        this.status = status;
        this.batch = batch;
        this.batchname = batchname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getFeepaydate() {
        return feepaydate;
    }

    public void setFeepaydate(String feepaydate) {
        this.feepaydate = feepaydate;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }
}


