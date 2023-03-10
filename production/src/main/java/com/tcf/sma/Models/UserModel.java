package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohammad.Haseeb on 2/13/2017.
 */

public class UserModel {
    @SerializedName("userId")
    private int id;
    @SerializedName("")
    private int default_school_id;
    @SerializedName("roleId")
    private int roleId;
    @SerializedName("Department_Id")
    private int Department_Id;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("status")
    private String status;
    private String role;
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("designation")
    private String designation;
    @SerializedName("")
    private String lastpassword;
    @SerializedName("")
    private String lastlogin_time;
    @SerializedName("")
    private String metadata_sync_on;
    @SerializedName("token")
    private String session_token;
    private String fcm_token;
    //    @SerializedName("newpassword")
    private String lastpassword_1;
    private String lastpassword_2;
    private String lastpassword_3;
    private String password_change_on;
    private int password_change_on_login;
    private String last_uploaded_on;

    public UserModel() {
    }

    public UserModel(int id, int default_school_id, int roleId, int department_Id, String firstname, String lastname, String status, String role, String username, String email, String designation, String lastpassword, String lastlogin_time, String metadata_sync_on, String session_token, String fcm_token) {
        this.id = id;
        this.default_school_id = default_school_id;
        this.roleId = roleId;
        Department_Id = department_Id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.role = role;
        this.username = username;
        this.email = email;
        this.designation = designation;
        this.lastpassword = lastpassword;
        this.lastlogin_time = lastlogin_time;
        this.metadata_sync_on = metadata_sync_on;
        this.session_token = session_token;
        this.fcm_token = fcm_token;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDefault_school_id() {
        return default_school_id;
    }

    public void setDefault_school_id(int default_school_id) {
        this.default_school_id = default_school_id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getDepartment_Id() {
        return Department_Id;
    }

    public void setDepartment_Id(int department_Id) {
        Department_Id = department_Id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    //Setters

    public String getLastpassword() {
        return lastpassword;
    }

    public void setLastpassword(String lastpassword) {
        this.lastpassword = lastpassword;
    }

    public String getLastlogin_time() {
        return lastlogin_time;
    }

    public void setLastlogin_time(String lastlogin_time) {
        this.lastlogin_time = lastlogin_time;
    }

    public String getMetadata_sync_on() {
        return metadata_sync_on;
    }

    public void setMetadata_sync_on(String metadata_sync_on) {
        this.metadata_sync_on = metadata_sync_on;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getLastpassword_1() {
        return lastpassword_1;
    }

    public void setLastpassword_1(String lastpassword_1) {
        this.lastpassword_1 = lastpassword_1;
    }

    public String getLastpassword_2() {
        return lastpassword_2;
    }

    public void setLastpassword_2(String lastpassword_2) {
        this.lastpassword_2 = lastpassword_2;
    }

    public String getLastpassword_3() {
        return lastpassword_3;
    }

    public void setLastpassword_3(String lastpassword_3) {
        this.lastpassword_3 = lastpassword_3;
    }

    public String getPassword_change_on() {
        return password_change_on;
    }

    public void setPassword_change_on(String password_change_on) {
        this.password_change_on = password_change_on;
    }

    public int getPassword_change_on_login() {
        return password_change_on_login;
    }

    public void setPassword_change_on_login(int password_change_on_login) {
        this.password_change_on_login = password_change_on_login;
    }

    public String getLast_uploaded_on() {
        return last_uploaded_on;
    }

    public void setLast_uploaded_on(String last_uploaded_on) {
        this.last_uploaded_on = last_uploaded_on;
    }

    //Parsing methods

//    public ArrayList<UserModel> parseArray(String json) {
//        ArrayList<UserModel> umList = new ArrayList<UserModel>();
//        UserModel um = null;
//
//
//        try {
//            JSONObject parentObj = new JSONObject(json);
//            JSONArray pmarray = parentObj.getJSONArray("profiles");
//            for (int i = 0; i < pmarray.length(); i++) {
//                JSONObject childObject = pmarray.getJSONObject(i);
//
//                um = new UserModel();
//                if (childObject.has("id"))
//                    um.setId(childObject.getInt("id"));
//                if (childObject.has("name"))
//                    um.setName(childObject.getString("name"));
//                if (childObject.has("role"))
//                    um.setRole(childObject.getString("role"));
//                if (childObject.has("default_school_id"))
//                    um.setDefault_school_id(childObject.getInt("default_school_id"));
//                if (childObject.has("username"))
//                    um.setUsername(childObject.getString("username"));
//                if (childObject.has("lastpassword"))
//                    um.setLastpassword(childObject.getString("lastpassword"));
//                if (childObject.has("lastlogin_time"))
//                    um.setLastlogin_time(childObject.getString("lastlogin_time"));
//                if (childObject.has("metadata_sync_on"))
//                    um.setMetadata_sync_on(childObject.getString("metadata_sync_on"));
//                if (childObject.has("session_token"))
//                    um.setSession_token(childObject.getString("session_token"));
//                umList.add(um);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return umList;
//    }
//
//    public UserModel parseObject(String json) {
//        UserModel um = new UserModel();
//        try {
//            JSONObject childObject = new JSONObject(json);
//
//            if (childObject.has("id"))
//                um.setId(childObject.getInt("id"));
//            if (childObject.has("name"))
//                um.setName(childObject.getString("name"));
//            if (childObject.has("role"))
//                um.setRole(childObject.getString("role"));
//            if (childObject.has("default_school_id"))
//                um.setDefault_school_id(childObject.getInt("default_school_id"));
//            if (childObject.has("username"))
//                um.setUsername(childObject.getString("username"));
//            if (childObject.has("lastpassword"))
//                um.setLastpassword(childObject.getString("lastpassword"));
//            if (childObject.has("lastlogin_time"))
//                um.setLastlogin_time(childObject.getString("lastlogin_time"));
//            if (childObject.has("metadata_sync_on"))
//                um.setMetadata_sync_on(childObject.getString("metadata_sync_on"));
//            if (childObject.has("session_token"))
//                um.setSession_token(childObject.getString("session_token"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return um;
//    }

}
