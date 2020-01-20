package com.example.aakash.bmi_calc_app;

public class Result {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    private String name;
    private String age;
    private String phoneno;
    private String gender;
    private String status;
    private String bmi;

    public Result(String name, String age, String phoneno, String gender, String status, String bmi) {
        this.name = name;
        this.age = age;
        this.phoneno = phoneno;
        this.gender = gender;
        this.status = status;
        this.bmi = bmi;
    }
}
