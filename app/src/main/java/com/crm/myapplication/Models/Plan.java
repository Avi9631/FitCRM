package com.crm.myapplication.Models;

public class Plan {

    private String planid;
    private String planname;
    private String planfee;
    private String planduration;
    private String plandurationtype;
    private String plandesc;
    private String status;
    private String planTimestamp;

    public Plan(String planid, String planname, String planfee, String planduration, String plandurationtype, String plandesc, String status, String planTimestamp) {
        this.planid = planid;
        this.planname = planname;
        this.planfee = planfee;
        this.planduration = planduration;
        this.plandurationtype = plandurationtype;
        this.plandesc = plandesc;
        this.status = status;
        this.planTimestamp=planTimestamp;
    }

    public String getPlanTimestamp() {
        return planTimestamp;
    }

    public void setPlanTimestamp(String planTimestamp) {
        this.planTimestamp = planTimestamp;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getPlanfee() {
        return planfee;
    }

    public void setPlanfee(String planfee) {
        this.planfee = planfee;
    }

    public String getPlanduration() {
        return planduration;
    }

    public void setPlanduration(String planduration) {
        this.planduration = planduration;
    }

    public String getPlandurationtype() {
        return plandurationtype;
    }

    public void setPlandurationtype(String plandurationtype) {
        this.plandurationtype = plandurationtype;
    }

    public String getPlandesc() {
        return plandesc;
    }

    public void setPlandesc(String plandesc) {
        this.plandesc = plandesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
