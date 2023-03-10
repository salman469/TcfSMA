package com.tcf.sma.Models.RetrofitModels;

import com.tcf.sma.Models.SchoolModel;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class LoginResponseModel {
    public String token;
    public String status;
    public int userId;
    public int roleId;
    public String firstname;
    public String lastname;
    public String email;
    public String designation;
    public int Department_Id;
    public ArrayList<SchoolModel> Schools;
    //    public ArrayList<CalendarsModel> Calendars;
    public String password_change_onlogin;
    public String LastPassword_3;
    public String LastPassword_2;
    public String LastPassword_1;
    private ArrayList<AppModulesModel> appModules;
    private String UserPicture;
    private int NetworkConnectionFrequency;


    public LoginResponseModel() {
    }

    public LoginResponseModel(String token, String status,
                              int userId, int roleId,
                              String firstname, String lastname,
                              String email, String designation,
                              int department_Id, ArrayList<SchoolModel> schools) {
        this.token = token;
        this.status = status;
        this.userId = userId;
        this.roleId = roleId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.designation = designation;
        Department_Id = department_Id;
        Schools = schools;
    }

    //Getters


    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public int getNetworkConnectionFrequency() {
        return NetworkConnectionFrequency;
    }

    public void setNetworkConnectionFrequency(int networkConnectionFrequency) {
        NetworkConnectionFrequency = networkConnectionFrequency;
    }

    public ArrayList<AppModulesModel> getAppModules() {
        return appModules;
    }

    public void setAppModules(ArrayList<AppModulesModel> appModules) {
        this.appModules = appModules;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    //    public ArrayList<CalendarsModel> getCalendars() {
//        return Calendars;
//    }

    //Setters

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getDepartment_Id() {
        return Department_Id;
    }

    public void setDepartment_Id(int department_Id) {
        Department_Id = department_Id;
    }

    public ArrayList<SchoolModel> getSchools() {
        return Schools;
    }

    public void setSchools(ArrayList<SchoolModel> schools) {
        Schools = schools;
    }

    public String getPassword_change_onlogin() {
        return password_change_onlogin;
    }

    public void setPassword_change_onlogin(String password_change_onlogin) {
        this.password_change_onlogin = password_change_onlogin;
    }

    public String getLastPassword_3() {
        return LastPassword_3;
    }

    public void setLastPassword_3(String lastPassword_3) {
        LastPassword_3 = lastPassword_3;
    }

    public String getLastPassword_2() {
        return LastPassword_2;
    }

    public void setLastPassword_2(String lastPassword_2) {
        LastPassword_2 = lastPassword_2;
    }

    public String getLastPassword_1() {
        return LastPassword_1;
    }

    public void setLastPassword_1(String lastPassword_1) {
        LastPassword_1 = lastPassword_1;
    }

    //    public void setCalendars(ArrayList<CalendarsModel> calendars) {
//        Calendars = calendars;
//    }
}
