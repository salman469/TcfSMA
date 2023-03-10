package com.tcf.sma.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 12/28/2016.
 */

public class SchoolModel implements Parcelable {
    private int schoolId;
    private int prinicipalId;
    private String name;
    private String region;
    private String district;
    private String area;
    private String campusId;
    private String Target_Amount;
    private String AllowedModule_App;
    @SerializedName("SchoolYear_Start_Date")
    private String start_date;
    @SerializedName("SchoolYear_End_Date")
    private String end_date;
    @SerializedName("AcademicSession_Name")
    private String academic_session;
    @SerializedName("AcademicSession_Id")
    private int academic_Session_Id;
    @SerializedName("SchoolYear_Id")
    private int school_yearId;
    @SerializedName("Employee_Code")
    private String EMIS;
    private ArrayList<SchoolModel> smList;
    private SchoolModel sm;
    @SerializedName("TypeOfSchool")
    private String typeOfSchool;
    private int provinceId;
    private String provinceName;
    @SerializedName("FirstName")
    private String principalFirstName;
    @SerializedName("LastName")
    private String principalLastName;

    @SerializedName("shift")
    private String schoolShift;

    public SchoolModel() {
    }

    public SchoolModel(int schoolId, String name) {
        this.schoolId = schoolId;
        this.name = name;
    }

    public SchoolModel(int schoolId, int prinicipalId, String name, String region, String district, String area, String EMIS) {
        this.schoolId = schoolId;
        this.prinicipalId = prinicipalId;
        this.name = name;
        this.region = region;
        this.district = district;
        this.area = area;
        this.EMIS = EMIS;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getAllowedModule_App() {
        return AllowedModule_App;
    }

    public void setAllowedModule_App(String allowedModule_App) {
        AllowedModule_App = allowedModule_App;
    }

    public String getTarget_Amount() {
        return Target_Amount;
    }

    public void setTarget_Amount(String target_Amount) {
        Target_Amount = target_Amount;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getAcademic_session() {
        return academic_session;
    }

    public void setAcademic_session(String academic_session) {
        this.academic_session = academic_session;
    }

    public int getAcademic_Session_Id() {
        return academic_Session_Id;
    }


    //Constructor

    public void setAcademic_Session_Id(int academic_Session_Id) {
        this.academic_Session_Id = academic_Session_Id;
    }

    public int getSchool_yearId() {
        return school_yearId;
    }

    public void setSchool_yearId(int school_yearId) {
        this.school_yearId = school_yearId;
    }

    // Getters
    public int getId() {
        return schoolId;
    }

    // Setters
    public void setId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEMIS() {
        return EMIS;
    }

    public void setEMIS(String EMIS) {
        this.EMIS = EMIS;
    }

    public int getPrinicipalId() {
        return prinicipalId;
    }

    public void setPrinicipalId(int prinicipalId) {
        this.prinicipalId = prinicipalId;
    }

    public ArrayList<SchoolModel> getSchoolsList() {
        return smList;
    }

    //Methods
    public SchoolModel setSchoolsList(ArrayList<SchoolModel> smList) {
        this.smList = smList;
        return this;
    }

    public SchoolModel getSchool() {
        return this.sm;
    }

    public SchoolModel setSchool(SchoolModel sm) {
        this.sm = sm;
        return this;

    }

    public String getTypeOfSchool() {
        return typeOfSchool;
    }

    public void setTypeOfSchool(String typeOfSchool) {
        this.typeOfSchool = typeOfSchool;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getSchoolShift() {
        return schoolShift;
    }

    public void setSchoolShift(String schoolShift) {
        this.schoolShift = schoolShift;
    }

    //Parsing Methods
    public ArrayList<SchoolModel> parseArray(String json) {
        ArrayList<SchoolModel> smList = new ArrayList<SchoolModel>();
        sm = null;


        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("schools");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                sm = new SchoolModel();
                if (childObject.has("id"))
                    sm.setId(childObject.getInt("id"));
                if (childObject.has("name"))
                    sm.setName(childObject.getString("name"));
                if (childObject.has("region"))
                    sm.setRegion(childObject.getString("region"));
                if (childObject.has("district"))
                    sm.setDistrict(childObject.getString("district"));
                if (childObject.has("area"))
                    sm.setArea(childObject.getString("area"));
                if (childObject.has("emis"))
                    sm.setEMIS(childObject.getString("emis"));

                smList.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smList;
    }



    public SchoolModel parseObject(String json) {
        sm = new SchoolModel();
        try {
            JSONObject childObject = new JSONObject(json);

            this.sm = new SchoolModel();
            if (childObject.has("id"))
                this.sm.setId(childObject.getInt("id"));
            if (childObject.has("name"))
                this.sm.setName(childObject.getString("name"));
            if (childObject.has("region"))
                this.sm.setRegion(childObject.getString("region"));
            if (childObject.has("district"))
                this.sm.setDistrict(childObject.getString("district"));
            if (childObject.has("area"))
                this.sm.setArea(childObject.getString("area"));
            if (childObject.has("emis"))
                this.sm.setEMIS(childObject.getString("emis"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sm;
    }

    //to display object as a string in spinner
    public String toString() {
        return (getId() <= 0) ? getName() : getId() + "-" + getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.schoolId);
        dest.writeInt(this.prinicipalId);
        dest.writeString(this.name);
        dest.writeString(this.region);
        dest.writeString(this.district);
        dest.writeString(this.area);
        dest.writeString(this.campusId);
        dest.writeString(this.Target_Amount);
        dest.writeString(this.AllowedModule_App);
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
        dest.writeString(this.academic_session);
        dest.writeInt(this.academic_Session_Id);
        dest.writeInt(this.school_yearId);
        dest.writeString(this.EMIS);
        dest.writeList(this.smList);
        dest.writeParcelable(this.sm, flags);
    }

    protected SchoolModel(Parcel in) {
        this.schoolId = in.readInt();
        this.prinicipalId = in.readInt();
        this.name = in.readString();
        this.region = in.readString();
        this.district = in.readString();
        this.area = in.readString();
        this.campusId = in.readString();
        this.Target_Amount = in.readString();
        this.AllowedModule_App = in.readString();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.academic_session = in.readString();
        this.academic_Session_Id = in.readInt();
        this.school_yearId = in.readInt();
        this.EMIS = in.readString();
        this.smList = new ArrayList<SchoolModel>();
        in.readList(this.smList, SchoolModel.class.getClassLoader());
        this.sm = in.readParcelable(SchoolModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<SchoolModel> CREATOR = new Parcelable.Creator<SchoolModel>() {
        @Override
        public SchoolModel createFromParcel(Parcel source) {
            return new SchoolModel(source);
        }

        @Override
        public SchoolModel[] newArray(int size) {
            return new SchoolModel[size];
        }
    };

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getPrincipalFirstName() {
        return principalFirstName;
    }

    public void setPrincipalFirstName(String principalFirstName) {
        this.principalFirstName = principalFirstName;
    }

    public String getPrincipalLastName() {
        return principalLastName;
    }

    public void setPrincipalLastName(String principalLastName) {
        this.principalLastName = principalLastName;
    }
}
