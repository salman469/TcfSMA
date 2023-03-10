package com.tcf.sma.Models.RetrofitModels;

import com.tcf.sma.Models.AttendanceModel;
import com.tcf.sma.Models.AttendanceStudentModel;
import com.tcf.sma.Models.EnrollmentImageModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.PromotionDBModel;
import com.tcf.sma.Models.PromotionStudentDBModel;
import com.tcf.sma.Models.SchoolAuditModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.WithdrawalModel;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/28/2017.
 */

public class StudentDataResponseModel {
    private ArrayList<StudentModel> Student;
    private ArrayList<AttendanceModel> Attendance;
    private ArrayList<AttendanceStudentModel> Student_Attendance;
    private ArrayList<EnrollmentModel> Enrollment;
    private ArrayList<EnrollmentImageModel> Enrollment_Image;
    private ArrayList<PromotionDBModel> Promotion;
    private ArrayList<PromotionStudentDBModel> Promotion_Student;
    private ArrayList<WithdrawalModel> Withdrawal;
    private ArrayList<SchoolAuditModel> SchoolAudit;

    //Constructor


    public StudentDataResponseModel() {
    }

    public StudentDataResponseModel(ArrayList<StudentModel> student, ArrayList<AttendanceModel> attendance, ArrayList<AttendanceStudentModel> student_Attendance, ArrayList<EnrollmentModel> enrollment, ArrayList<EnrollmentImageModel> enrollment_Image, ArrayList<PromotionDBModel> promotion, ArrayList<PromotionStudentDBModel> promotion_Student, ArrayList<WithdrawalModel> withdrawal, ArrayList<SchoolAuditModel> school_audit) {
        Student = student;
        Attendance = attendance;
        Student_Attendance = student_Attendance;
        Enrollment = enrollment;
        Enrollment_Image = enrollment_Image;
        Promotion = promotion;
        Promotion_Student = promotion_Student;
        Withdrawal = withdrawal;
        SchoolAudit = school_audit;

    }

    //Getters

    public ArrayList<StudentModel> getStudent() {
        return Student;
    }

    public void setStudent(ArrayList<StudentModel> student) {
        Student = student;
    }

    public ArrayList<AttendanceModel> getAttendance() {
        return Attendance;
    }

    public void setAttendance(ArrayList<AttendanceModel> attendance) {
        Attendance = attendance;
    }

    public ArrayList<AttendanceStudentModel> getStudent_Attendance() {
        return Student_Attendance;
    }

    public void setStudent_Attendance(ArrayList<AttendanceStudentModel> student_Attendance) {
        Student_Attendance = student_Attendance;
    }

    public ArrayList<EnrollmentModel> getEnrollment() {
        return Enrollment;
    }

    public void setEnrollment(ArrayList<EnrollmentModel> enrollment) {
        Enrollment = enrollment;
    }

    public ArrayList<EnrollmentImageModel> getEnrollment_Image() {
        return Enrollment_Image;
    }

    //Setters

    public void setEnrollment_Image(ArrayList<EnrollmentImageModel> enrollment_Image) {
        Enrollment_Image = enrollment_Image;
    }

    public ArrayList<PromotionDBModel> getPromotion() {
        return Promotion;
    }

    public void setPromotion(ArrayList<PromotionDBModel> promotion) {
        Promotion = promotion;
    }

    public ArrayList<PromotionStudentDBModel> getPromotion_Student() {
        return Promotion_Student;
    }

    public void setPromotion_Student(ArrayList<PromotionStudentDBModel> promotion_Student) {
        Promotion_Student = promotion_Student;
    }

    public ArrayList<WithdrawalModel> getWithdrawal() {
        return Withdrawal;
    }

    public void setWithdrawal(ArrayList<WithdrawalModel> withdrawal) {
        Withdrawal = withdrawal;
    }

    public ArrayList<SchoolAuditModel> getSchoolAudit() {
        return SchoolAudit;
    }

    public void setSchoolAudit(ArrayList<SchoolAuditModel> schoolAudit) {
        SchoolAudit = schoolAudit;
    }
}
