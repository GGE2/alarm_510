package com.example.MainService.WAN.fragments;

public class Worg_data {


    private String tv_date;
    private String tv_category;
    private String tv_problem_text;
    private String problem_id;
    private String user_uid;

    public Worg_data(String tv_date, String tv_category, String tv_problem_text, String problem_id, String user_uid) {
        this.tv_date = tv_date;
        this.tv_category = tv_category;
        this.tv_problem_text = tv_problem_text;
        this.problem_id = problem_id;
        this.user_uid = user_uid;

    }

    public  String getUser_uid(){return  user_uid;}

    public void  setUser_uid(String user_uid){this.user_uid = user_uid;}

    public  String getProblem_id(){return  problem_id;}

    public void  setProblem_id(String problem_id){this.problem_id = problem_id;}

    public String getTv_date() {
        return tv_date;
    }

    public void setTv_date(String tv_date) {
        this.tv_date = tv_date;
    }

    public String getTv_category() {
        return tv_category;
    }

    public void setTv_category(String tv_category) {
        this.tv_category = tv_category;
    }

    public String getTv_problem_text() {
        return tv_problem_text;
    }

    public void setTv_problem_text(String tv_problem_text) {
        this.tv_problem_text = tv_problem_text;
    }
}
