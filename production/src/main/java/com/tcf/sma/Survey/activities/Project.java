package com.tcf.sma.Survey.activities;

import android.os.Parcel;
import android.os.Parcelable;

import com.tcf.sma.Models.SchoolModel;

import java.util.EmptyStackException;
import java.util.Stack;

public class Project implements Parcelable {

    //private variables
    private int _key;
    private String _name;
    private String showMapScreen;
    private String showBuildingScreen;
    private String needCategoryScreen;
    private String totalSurveys, today, weeek, month;
    private SchoolModel selectedSchool;
    private int selectedEmployeeId;
    private boolean showSchoolSelection, showEmployeeSelection, showCourseSelection;
    public  Stack<Class> classStack = new Stack<>();

    float progress = 0;
    String current_download;

    // Empty constructor
    public Project() {

    }

    // constructor
    public Project(int key, String value, String buildingScreen, String mapScreen, String categoryScreen) {
        this._key = key;
        this._name = value;
        this.showBuildingScreen = buildingScreen;
        this.showMapScreen = mapScreen;
        this.needCategoryScreen = categoryScreen;
    }

    public Project(int key, String value, String buildingScreen, String mapScreen, String totalSurvey, String today, String week, String month, String categoryScreen) {
        this._key = key;
        this._name = value;
        this.showBuildingScreen = buildingScreen;
        this.showMapScreen = mapScreen;
        this.totalSurveys = totalSurvey;
        this.today = today;
        this.weeek = week;
        this.month = month;
        this.needCategoryScreen = categoryScreen;
    }

    public Project(int key, String value, String buildingScreen, String mapScreen, String totalSurvey, String today, String week, String month, String categoryScreen, boolean shouldShowSchool, boolean shouldShowEmployee, boolean shouldShowCourse, boolean isSetStack) {
        this._key = key;
        this._name = value;
        this.showBuildingScreen = buildingScreen;
        this.showMapScreen = mapScreen;
        this.totalSurveys = totalSurvey;
        this.today = today;
        this.weeek = week;
        this.month = month;
        this.needCategoryScreen = categoryScreen;
        showSchoolSelection = shouldShowSchool;
        showEmployeeSelection = shouldShowEmployee;
        showCourseSelection = shouldShowCourse;
        if (isSetStack)
            setClassStack();
    }

    public SchoolModel getSelectedSchool() {
        return selectedSchool;
    }

    public Project setSelectedSchool(SchoolModel selectedSchool) {
        this.selectedSchool = selectedSchool;
        return this;
    }

    public String getCurrent_download() {
        return current_download;
    }

    public void setCurrent_download(String current_download) {
        this.current_download = current_download;
    }

    public String isShowMapScreen() {
        return showMapScreen;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setShowMapScreen(String showMapScreen) {
        this.showMapScreen = showMapScreen;
    }

    public String isShowBuildingScreen() {
        return showBuildingScreen;
    }

    public void setShowBuildingScreen(String showBuildingScreen) {
        this.showBuildingScreen = showBuildingScreen;
    }

    // getting name
    public int getKey() {
        return this._key;
    }

    // setting name
    public void setKey(int key) {
        this._key = key;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String value) {
        this._name = value;
    }

    public String getTotalSurveys() {
        return totalSurveys;
    }

    public void setTotalSurveys(String totalSurveys) {
        this.totalSurveys = totalSurveys;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getWeeek() {
        return weeek;
    }

    public void setWeeek(String weeek) {
        this.weeek = weeek;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int get_key() {
        return _key;
    }

    public void set_key(int _key) {
        this._key = _key;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String getShowMapScreen() {
        return showMapScreen;
    }

    public String getShowBuildingScreen() {
        return showBuildingScreen;
    }

    public String getNeedCategoryScreen() {
        return needCategoryScreen;
    }

    public void setNeedCategoryScreen(String needCategoryScreen) {
        this.needCategoryScreen = needCategoryScreen;
    }

    public boolean isShowSchoolSelection() {
        return showSchoolSelection;
    }

    public void setShowSchoolSelection(boolean showSchoolSelection) {
        this.showSchoolSelection = showSchoolSelection;
    }

    public boolean isShowEmployeeSelection() {
        return showEmployeeSelection;
    }

    public void setShowEmployeeSelection(boolean showEmployeeSelection) {
        this.showEmployeeSelection = showEmployeeSelection;
    }

    public boolean isShowCourseSelection() {
        return showCourseSelection;
    }

    public void setShowCourseSelection(boolean showCourseSelection) {
        this.showCourseSelection = showCourseSelection;
    }

    public int getSelectedEmployeeId() {
        return selectedEmployeeId;
    }

    public Project setSelectedEmployeeId(int selectedEmployeeId) {
        this.selectedEmployeeId = selectedEmployeeId;
        return this;
    }

    public Class popActivityFromStack() throws EmptyStackException {
        if (!classStack.empty())
            return classStack.pop();
        else
            throw new EmptyStackException();
    }

    public void setClassStack() {
//        if (!showCourseSelection && !showEmployeeSelection && !showSchoolSelection) {
//            classStack.push(DashboardActivity.class);
//            classStack.push(CourseSelector.class);
//            classStack.push(EmployeeSelector.class);
//            classStack.push(SelectSchoolActivity.class);
//            return;
//        }

        classStack.push(DashboardActivity.class);
        if (showCourseSelection)
            classStack.push(CourseSelector.class);
        if (showEmployeeSelection)
            classStack.push(EmployeeSelector.class);
        if (showSchoolSelection)
            classStack.push(SelectSchoolActivity.class);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._key);
        dest.writeString(this._name);
        dest.writeString(this.showMapScreen);
        dest.writeString(this.showBuildingScreen);
        dest.writeString(this.needCategoryScreen);
        dest.writeString(this.totalSurveys);
        dest.writeString(this.today);
        dest.writeString(this.weeek);
        dest.writeString(this.month);
        dest.writeByte(this.showSchoolSelection ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showEmployeeSelection ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showCourseSelection ? (byte) 1 : (byte) 0);
        dest.writeList(this.classStack);
        dest.writeFloat(this.progress);
        dest.writeString(this.current_download);
    }

    protected Project(Parcel in) {
        this._key = in.readInt();
        this._name = in.readString();
        this.showMapScreen = in.readString();
        this.showBuildingScreen = in.readString();
        this.needCategoryScreen = in.readString();
        this.totalSurveys = in.readString();
        this.today = in.readString();
        this.weeek = in.readString();
        this.month = in.readString();
        this.showSchoolSelection = in.readByte() != 0;
        this.showEmployeeSelection = in.readByte() != 0;
        this.showCourseSelection = in.readByte() != 0;
        this.classStack = new Stack<>();
        in.readList(this.classStack, Class.class.getClassLoader());
        this.progress = in.readFloat();
        this.current_download = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
