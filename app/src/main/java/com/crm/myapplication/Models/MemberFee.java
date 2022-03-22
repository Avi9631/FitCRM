package com.crm.myapplication.Models;

public class MemberFee {

    private String id;
    private String mid;
    private String plan;
    private String fee;
    private String discount;
    private String admission;
    private String tax;
    private String datetime;

    public MemberFee(String id, String mid, String plan, String fee, String discount, String admission, String tax, String datetime) {
        this.id = id;
        this.mid = mid;
        this.plan = plan;
        this.fee = fee;
        this.discount = discount;
        this.admission = admission;
        this.tax = tax;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
