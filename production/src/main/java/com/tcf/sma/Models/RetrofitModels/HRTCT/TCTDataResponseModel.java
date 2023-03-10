package com.tcf.sma.Models.RetrofitModels.HRTCT;

import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDesignationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveTypeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeePositionModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeQualificationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignTypeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSchoolModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeTeacherAttendanceModel;

import java.util.ArrayList;

public class TCTDataResponseModel {

    public ArrayList<TCTPhaseModel> Phase;
    public ArrayList<TCTEmpSubjTagReasonModel> SubjectTaggingReasons;
    public ArrayList<TCTEmpSubjectTaggingModel> EmployeesSubjectTagging;
    public ArrayList<TCTSubjectsModel> Subjects;
    public ArrayList<TCTDesginationModel> Designations;
    public ArrayList<TCTLeaveTypeModel> LeaveTypes;

    public ArrayList<TCTEmpSubjTagReasonModel> getSubjectTaggingReasons() {
        return SubjectTaggingReasons;
    }

    public void setSubjectTaggingReasons(ArrayList<TCTEmpSubjTagReasonModel> subjectTaggingReasons) {
        SubjectTaggingReasons = subjectTaggingReasons;
    }

    public ArrayList<TCTPhaseModel> getPhase() {
        return Phase;
    }

    public void setPhase(ArrayList<TCTPhaseModel> phase) {
        Phase = phase;
    }

    public ArrayList<TCTEmpSubjectTaggingModel> getEmployeesSubjectTagging() {
        return EmployeesSubjectTagging;
    }

    public void setEmployeesSubjectTagging(ArrayList<TCTEmpSubjectTaggingModel> employeesSubjectTagging) {
        EmployeesSubjectTagging = employeesSubjectTagging;
    }

    public ArrayList<TCTSubjectsModel> getSubjects() {
        return Subjects;
    }

    public void setSubjects(ArrayList<TCTSubjectsModel> subjects) {
        Subjects = subjects;
    }

    public ArrayList<TCTDesginationModel> getDesignations() {
        return Designations;
    }

    public void setDesignations(ArrayList<TCTDesginationModel> designations) {
        Designations = designations;
    }

    public ArrayList<TCTLeaveTypeModel> getLeaveTypes() {
        return LeaveTypes;
    }

    public void setLeaveTypes(ArrayList<TCTLeaveTypeModel> leaveTypes) {
        LeaveTypes = leaveTypes;
    }
}
