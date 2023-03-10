package com.tcf.sma.Models.RetrofitModels.HR;

import java.util.ArrayList;

public class EmployeeDataResponseModel {

    public ArrayList<EmployeeModel> User;
    public ArrayList<EmployeeSchoolModel> UserSchool;
    public ArrayList<EmployeeTeacherAttendanceModel> Teacher_Attendance;
    public ArrayList<EmployeeQualificationDetailModel> QualificationHistory;
    public ArrayList<EmployeePositionModel> PositionHistory;
    public ArrayList<EmployeeLeaveModel> TeacherLeave;
    public ArrayList<EmployeeSeparationModel> ResignReceived;
    public ArrayList<EmployeeDesignationModel> Designation;
    public ArrayList<EmployeeLeaveTypeModel> LeaveType;
    public ArrayList<EmployeeResignReasonModel> ResignReason;
    public ArrayList<EmployeeResignTypeModel> ResignType;
    public ArrayList<EmployeeSeparationDetailModel> separationApproval;
    public ArrayList<SeparationAttachmentsModel> ResignReceivedImages;
//    public ArrayList<HRLeaveType> LeaveType ;

//    public ArrayList<HRResignStatus> ResignStatus ;
//    public ArrayList<HRLeaveStatus> LeaveStatus ;
//    public ArrayList<HRAttendanceType> AttendanceType ;


    public ArrayList<EmployeeModel> getUser() {
        return User;
    }

    public void setUser(ArrayList<EmployeeModel> user) {
        User = user;
    }

    public ArrayList<EmployeeSchoolModel> getUserSchool() {
        return UserSchool;
    }

    public void setUserSchool(ArrayList<EmployeeSchoolModel> userSchool) {
        UserSchool = userSchool;
    }

    public ArrayList<EmployeeTeacherAttendanceModel> getTeacher_Attendance() {
        return Teacher_Attendance;
    }

    public void setTeacher_Attendance(ArrayList<EmployeeTeacherAttendanceModel> teacher_Attendance) {
        Teacher_Attendance = teacher_Attendance;
    }

    public ArrayList<EmployeeQualificationDetailModel> getQualificationHistory() {
        return QualificationHistory;
    }

    public void setQualificationHistory(ArrayList<EmployeeQualificationDetailModel> qualificationHistory) {
        QualificationHistory = qualificationHistory;
    }

    public ArrayList<EmployeePositionModel> getPositionHistory() {
        return PositionHistory;
    }

    public void setPositionHistory(ArrayList<EmployeePositionModel> positionHistory) {
        PositionHistory = positionHistory;
    }

    public ArrayList<EmployeeLeaveModel> getTeacherLeave() {
        return TeacherLeave;
    }

    public void setTeacherLeave(ArrayList<EmployeeLeaveModel> teacherLeave) {
        TeacherLeave = teacherLeave;
    }

    public ArrayList<EmployeeSeparationModel> getResignReceived() {
        return ResignReceived;
    }

    public void setResignReceived(ArrayList<EmployeeSeparationModel> resignReceived) {
        ResignReceived = resignReceived;
    }

    public ArrayList<EmployeeDesignationModel> getDesignation() {
        return Designation;
    }

    public void setDesignation(ArrayList<EmployeeDesignationModel> designation) {
        Designation = designation;
    }

    public ArrayList<EmployeeLeaveTypeModel> getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(ArrayList<EmployeeLeaveTypeModel> leaveType) {
        LeaveType = leaveType;
    }

    public ArrayList<EmployeeResignReasonModel> getResignReason() {
        return ResignReason;
    }

    public void setResignReason(ArrayList<EmployeeResignReasonModel> resignReason) {
        ResignReason = resignReason;
    }

    public ArrayList<EmployeeResignTypeModel> getResignType() {
        return ResignType;
    }

    public void setResignType(ArrayList<EmployeeResignTypeModel> resignType) {
        ResignType = resignType;
    }

    public ArrayList<EmployeeSeparationDetailModel> getSeparationApproval() {
        return separationApproval;
    }

    public void setSeparationApproval(ArrayList<EmployeeSeparationDetailModel> separationApproval) {
        this.separationApproval = separationApproval;
    }

    public ArrayList<SeparationAttachmentsModel> getResignReceivedImages() {
        return ResignReceivedImages;
    }

    public void setResignReceivedImages(ArrayList<SeparationAttachmentsModel> resignReceivedImages) {
        ResignReceivedImages = resignReceivedImages;
    }
}
