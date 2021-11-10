package com.example.MainService.Alarm.activities;


public class Time {
    private String tv_memo, tv_repeat, am_pm, tv_qt, hour, minute, tv_nq , tv_type, check_switch;


    public Time( String tv_memo, String am_pm, String tv_repeat, String tv_qt, String hour, String minute, String tv_type, String tv_nq, String check_switch){

        this.tv_memo = tv_memo;
        this.am_pm = am_pm;
        this.tv_repeat = tv_repeat;
        this.tv_qt = tv_qt;
        this.hour = hour;
        this.minute = minute;
        this.tv_nq = tv_nq;
        this.tv_type = tv_type;
        this.check_switch = check_switch;
    }
    public String getTv_memo() {
        return tv_memo;
    }
    public void setTv_memo(String tv_memo) {
        this.tv_memo = tv_memo;
    }
    public String getAm_pm() {
        return am_pm;
    }
    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }
    public String getTv_repeat() {
        return tv_repeat;
    }
    public void setTv_repeat(String tv_repeat) {
        this.tv_repeat = tv_repeat;
    }
    public String getTv_qt() { return tv_qt; }
    public void setTv_qt(String tv_qt) {
        this.tv_qt = tv_qt;
    }
    public String getHour() {
        return hour;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }
    public String getMinute() {
        return minute;
    }
    public void setMinute(String minute) {
        this.minute = minute;
    }
    public String getTv_type() { return tv_type; }
    public void setTv_type(String tv_type) {
        this.tv_type = tv_type;
    }
    public String getTv_nq() { return tv_nq; }
    public void setTv_nq(String tv_nq) {
        this.tv_nq = tv_nq;
    }
    public void setSwitch(String check_switch) { this.check_switch = check_switch;}
    public String getSwitch() { return check_switch;}
}