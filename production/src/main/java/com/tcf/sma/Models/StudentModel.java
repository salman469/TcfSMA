package com.tcf.sma.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/2/2017.
 */
public class StudentModel {
    private static StudentModel instance;
    @SerializedName("student_id")
    private int Id;
    private transient String entryFee;
    private transient String monthlyfee;
    private transient int serverId;
    //    @SerializedName("current_session")
//    private int CurrentSession;
    @SerializedName("modified_by")
    private int ModifiedBy;
    @SerializedName("withdrawal_reason_id")
    private int WithdrawalReasonId;
    @SerializedName("schoolclass_id")
    private int SchoolClassId;
    //is_deleted => NOT is_active
//    @SerializedName("is_deleted") taimur bhai asked to do this. 27-09-2021
    @SerializedName("is_deleted") //redoed this.
    private Boolean IsActive;
    @SerializedName("is_withdrawl") //I have undoed this (Ahmad) -> //this cant be duplicated so i have put isActive in addStudent and updateStudent in isWithdrawl Field
    private boolean IsWithdrawal;
    @SerializedName("name")
    private String Name;
    @SerializedName("gender")
    private String Gender;
    @SerializedName("gr_no")
    private String GrNo;
    @SerializedName("previous_student_id")
    private int PreviousStudentID;
    @SerializedName("date_of_admission")
    private String EnrollmentDate;
    @SerializedName("date_of_birth")
    private String Dob;
    @SerializedName("student_NIC")
    private String FormB;
    @SerializedName("father_name")
    private String FathersName;
    @SerializedName("father_occupation")
    private String FatherOccupation;
    @SerializedName("father_nic_no")
    private String FatherNic;
    @SerializedName("mother_name")
    private String MotherName;
    @SerializedName("mother_occupation")
    private String MotherOccupation;
    @SerializedName("mother_nic_no")
    private String MotherNic;
    @SerializedName("guardian_name")
    private String GuardianName;
    @SerializedName("guardian_occupation")
    private String GuardianOccupation;
    @SerializedName("guardian_nic")
    private String GuardianNic;
    @SerializedName("previous_school_name")
    private String PreviousSchoolName;
    @SerializedName("previous_class")
    private String PreviousSchoolClass;
    @SerializedName("address")
    private String Address1;
    @SerializedName("phone")
    private String Address2;
    @SerializedName("mobile_no")
    private String ContactNumbers;
    private String CurrentClass;
    private String CurrentSection;
    @SerializedName("modified_on")
    private String ModifiedOn;
    //not coming from server but keep it in use for app
    private String UploadedOn;
    @SerializedName("withdrawl_date")
    private String WithdrawnOn;
    @SerializedName("approved_on")
    private String approved_on;
    @SerializedName("approver_comments")
    private String unapprovedComments;
    @SerializedName("is_approved")
    private boolean IsApproved;
    @SerializedName("approved_by")
    private int approved_by;
    @SerializedName("picture_filename")
    private String PictureName;
    transient private String PictureUploadedOn;
    @SerializedName("Scholarship_Category_ID")
    private int scholarshipCategoryId;
    @SerializedName("Actual_Fee")
    private int actualFees;
    private String AttendanceStatus;
    private ArrayList<StudentModel> smList;
    private StudentModel sm;
    transient private int openingBalance;
    private String religion;
    private String nationality;
    @SerializedName("electiveSubjectID")
    private int electiveSubjectId;
    @SerializedName("orphanStudent")
    private String isOrphan;
    @SerializedName("disability")
    private String isDisabled;
    private String email;
    private String deathCert_Image;
    private String deathCert_Image_UploadedOn;
    private String medicalCert_Image;
    private String medicalCert_Image_UploadedOn;
    private String bForm_Image;
    private String bForm_Image_UploadedOn;

    @SerializedName("review_status")
    private String student_promotionStatus;
    @SerializedName("review_comments")
    private String student_promotionComments;

    public static StudentModel getInstance() {
        return (instance == null) ? instance = new StudentModel() : instance;
    }

    public static void setInstance(StudentModel instance) {
        StudentModel.instance = instance;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getMonthlyfee() {
        return monthlyfee;
    }

    public void setMonthlyfee(String monthlyfee) {
        this.monthlyfee = monthlyfee;
    }

    public String getPictureUploadedOn() {
        return PictureUploadedOn;
    }

    public void setPictureUploadedOn(String pictureUploadedOn) {
        PictureUploadedOn = pictureUploadedOn;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getScholarshipCategoryId() {
        return scholarshipCategoryId;
    }

    public void setScholarshipCategoryId(int scholarshipCategoryId) {
        this.scholarshipCategoryId = scholarshipCategoryId;
    }

    public double getActualFees() {
        try {
            return Double.valueOf(actualFees);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setActualFees(double actualFees) {
        try {
            this.actualFees = (int) actualFees;
        } catch (Exception e) {
            this.actualFees = 0;
        }
    }

    public double getMax_allow_fees_admission() {
        return 0;
    }

    public void setMax_allow_fees_admission(double max_allow_fees_admission) {
//        this.max_allow_fees_admission = max_allow_fees_admission;
    }

    public double getMax_allow_fees_exam() {
        return 0;
    }

    public void setMax_allow_fees_exam(double max_allow_fees_exam) {
//        this.max_allow_fees_exam = max_allow_fees_exam;
    }

    public double getMax_allow_fees_tution() {
        return 0;
    }

    public void setMax_allow_fees_tution(double max_allow_fees_tution) {
    }

    public double getMax_allow_fees_uniform() {
        return 0;
    }

    public void setMax_allow_fees_uniform(double max_allow_fees_uniform) {
//        this.max_allow_fees_uniform = max_allow_fees_uniform;
    }

//    @SerializedName("maxallow_fees_others")
//    private double max_allow_fees_others;

    public double getMax_allow_fees_books() {
        return 0;
    }

    public void setMax_allow_fees_books(double max_allow_fees_books) {
//        this.max_allow_fees_books = max_allow_fees_books;
    }

    public double getMax_allow_fees_copies() {
        return 0;
    }

    public void setMax_allow_fees_copies(double max_allow_fees_copies) {
//        this.max_allow_fees_copies = max_allow_fees_copies;
    }

    public double getMax_allow_fees_others() {
        return 0;
    }

    public void setMax_allow_fees_others(double max_allow_fees_others) {
//        this.max_allow_fees_others = max_allow_fees_others;
    }

    //Getters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCurrentSession() {
        return 0;
    }

//    public int getSchoolId() {
//        return SchoolId;
//    }

    public void setCurrentSession(int currentSession) {
//        CurrentSession = currentSession;
    }

//    public boolean isHasGraduated() {
//        return HasGraduated;
//    }

    public String getCurrentClass() {
        return CurrentClass;
    }

    public void setCurrentClass(String currentClass) {
        CurrentClass = currentClass;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public Boolean isActive() {
        return IsActive;
    }

    public String getPictureName() {
        return PictureName;
    }

    public void setPictureName(String pictureName) {
        PictureName = pictureName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getGrNo() {
        return GrNo;
    }

    public void setGrNo(String grNo) {
        GrNo = grNo;
    }

    public String getEnrollmentDate() {
        return EnrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        EnrollmentDate = enrollmentDate;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getFormB() {
        return FormB;
    }

    public void setFormB(String formB) {
        FormB = formB;
    }

    public String getFathersName() {
        return FathersName;
    }

    public void setFathersName(String fathersName) {
        FathersName = fathersName;
    }

    public String getFatherOccupation() {
        return FatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        FatherOccupation = fatherOccupation;
    }

    public String getFatherNic() {
        return FatherNic;
    }

    public void setFatherNic(String fatherNic) {
        FatherNic = fatherNic;
    }

    //    public boolean isDeleted() {
//        return IsDeleted;
//    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getMotherOccupation() {
        return MotherOccupation;
    }

//    public String getProfilePicName() {
//        return ProfilePicName;
//    }

    public void setMotherOccupation(String motherOccupation) {
        MotherOccupation = motherOccupation;
    }

    public String getMotherNic() {
        return MotherNic;
    }

    public void setMotherNic(String motherNic) {
        MotherNic = motherNic;
    }

    public String getGuardianName() {
        return GuardianName;
    }

    public void setGuardianName(String guardianName) {
        GuardianName = guardianName;
    }

    public String getGuardianOccupation() {
        return GuardianOccupation;
    }

    public void setGuardianOccupation(String guardianOccupation) {
        GuardianOccupation = guardianOccupation;
    }
    //Setters

    public String getGuardianNic() {
        try {
            return String.valueOf(GuardianNic);
        } catch (Exception e) {
            return "";
        }
    }

    public void setGuardianNic(String guardianNic) {
        try {
            GuardianNic = guardianNic;
        } catch (Exception e) {
            GuardianNic = "";
        }

    }

    public String getPreviousSchoolName() {
        return PreviousSchoolName;
    }

    public void setPreviousSchoolName(String previousSchoolName) {
        PreviousSchoolName = previousSchoolName;
    }

//    public void setSchoolId(int schoolId) {
//        SchoolId = schoolId;
//    }

    public String getPreviousSchoolClass() {
        return PreviousSchoolClass;
    }

//    public void setHasGraduated(boolean hasGraduated) {
//        HasGraduated = hasGraduated;
//    }

    public void setPreviousSchoolClass(String previousSchoolClass) {
        PreviousSchoolClass = previousSchoolClass;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getContactNumbers() {
        return ContactNumbers;
    }

    public void setContactNumbers(String contactNumbers) {
        ContactNumbers = contactNumbers;
    }

    public StudentModel getSm() {
        return sm;
    }

    public void setSm(StudentModel sm) {
        this.sm = sm;
    }

    public int getWithdrawalReasonId() {
        return WithdrawalReasonId;
    }

    public void setWithdrawalReasonId(int withdrawalReasonId) {
        WithdrawalReasonId = withdrawalReasonId;
    }

    public int getSchoolClassId() {
        return SchoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        SchoolClassId = schoolClassId;
    }

    public String getUnapprovedComments() {
        return unapprovedComments;
    }

    public void setUnapprovedComments(String unapprovedComments) {
        this.unapprovedComments = unapprovedComments;
    }

    public boolean isWithdrawal() {
        return IsWithdrawal;
    }

    public void setWithdrawal(boolean withdrawal) {
        IsWithdrawal = withdrawal;
    }

    public ArrayList<StudentModel> getSmList() {
        return smList;
    }

    public void setSmList(ArrayList<StudentModel> smList) {
        this.smList = smList;
    }

    public String getCurrentSection() {
        return CurrentSection;
    }

    public void setCurrentSection(String currentSection) {
        CurrentSection = currentSection;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

//    public void setProfilePicName(String profilePicName) {
//        ProfilePicName = profilePicName;
//    }

    public void setModifiedBy(int modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getUploadedOn() {
        return UploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        UploadedOn = uploadedOn;
    }

    public String getWithdrawnOn() {
        return WithdrawnOn;
    }

    public void setWithdrawnOn(String withdrawnOn) {
        WithdrawnOn = withdrawnOn;
    }

    public String getAttendanceStatus() {
        return AttendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        AttendanceStatus = attendanceStatus;
    }

    public String getApproved_on() {
        return approved_on;
    }

    public void setApproved_on(String approved_on) {
        this.approved_on = approved_on;
    }

    public int getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(int approved_by) {
        this.approved_by = approved_by;
    }

    //    public void setDeleted(boolean deleted) {
//        IsDeleted = deleted;
//    }

    public int getPreviousStudentID() {
        return PreviousStudentID;
    }

    public void setPreviousStudentID(int previousStudentID) {
        PreviousStudentID = previousStudentID;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    public boolean isApproved() {
        return IsApproved;
    }

    public void setApproved(boolean approved) {
        IsApproved = approved;
    }

    public ArrayList<StudentModel> getStudentsList() {
        return smList;
    }

    public int getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(int openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getElectiveSubjectId() {
        return electiveSubjectId;
    }

    public void setElectiveSubjectId(int electiveSubjectId) {
        this.electiveSubjectId = electiveSubjectId;
    }

    public String isOrphan() {
        return isOrphan;
    }

    public void setOrphan(String orphan) {
        isOrphan = orphan;
    }

    public String isDisabled() {
        return isDisabled;
    }

    public void setDisabled(String disabled) {
        isDisabled = disabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeathCert_Image() {
        return deathCert_Image;
    }

    public void setDeathCert_Image(String deathCert_Image) {
        this.deathCert_Image = deathCert_Image;
    }

    public String getMedicalCert_Image() {
        return medicalCert_Image;
    }

    public void setMedicalCert_Image(String medicalCert_Image) {
        this.medicalCert_Image = medicalCert_Image;
    }

    public String getbForm_Image() {
        return bForm_Image;
    }

    public void setbForm_Image(String bForm_Image) {
        this.bForm_Image = bForm_Image;
    }

    public String getbForm_Image_UploadedOn() {
        return bForm_Image_UploadedOn;
    }

    public void setbForm_Image_UploadedOn(String bForm_Image_UploadedOn) {
        this.bForm_Image_UploadedOn = bForm_Image_UploadedOn;
    }

    public String getDeathCert_Image_UploadedOn() {
        return deathCert_Image_UploadedOn;
    }

    public void setDeathCert_Image_UploadedOn(String deathCert_Image_UploadedOn) {
        this.deathCert_Image_UploadedOn = deathCert_Image_UploadedOn;
    }

    public String getMedicalCert_Image_UploadedOn() {
        return medicalCert_Image_UploadedOn;
    }

    public void setMedicalCert_Image_UploadedOn(String medicalCert_Image_UploadedOn) {
        this.medicalCert_Image_UploadedOn = medicalCert_Image_UploadedOn;
    }

    public String getStudent_promotionStatus() {
        return student_promotionStatus;
    }

    public void setStudent_promotionStatus(String student_promotionStatus) {
        this.student_promotionStatus = student_promotionStatus;
    }

    public String getStudent_promotionComments() {
        return student_promotionComments;
    }

    public void setStudent_promotionComments(String student_promotionComments) {
        this.student_promotionComments = student_promotionComments;
    }

    //Methods
    public StudentModel setStudentsList(ArrayList<StudentModel> smList) {
        this.smList = smList;
        return this;
    }

    public StudentModel getStudent() {
        return this.sm;
    }

    public StudentModel setStudent(StudentModel sm) {
        this.sm = sm;
        return this;

    }

    //Parsing Methods
    public ArrayList<StudentModel> parseArray(String json) {
        smList = null;
        sm = null;

        try {
            JSONObject parentObj = new JSONObject(json);
            JSONArray pmarray = parentObj.getJSONArray("student");
            for (int i = 0; i < pmarray.length(); i++) {
                JSONObject childObject = pmarray.getJSONObject(i);

                if (childObject.has("id"))
                    sm.setId(childObject.getInt("id"));
                if (childObject.has("name"))
                    sm.setName(childObject.getString("name"));
                if (childObject.has("gender"))
                    sm.setGender(childObject.getString("gender"));
                if (childObject.has("grno"))
                    sm.setGrNo(childObject.getString("grno"));
                if (childObject.has("enrollment_date"))
                    sm.setEnrollmentDate(childObject.getString("enrollment_date"));
                if (childObject.has("dob"))
                    sm.setDob(childObject.getString("dob"));
                if (childObject.has("form_b"))
                    sm.setFormB(childObject.getString("form_b"));
                if (childObject.has("fathers_name"))
                    sm.setFathersName(childObject.getString("fathers_name"));
                if (childObject.has("fathers_occupation"))
                    sm.setFatherOccupation(childObject.getString("fathers_occupation"));
                if (childObject.has("fathers_nic"))
                    sm.setFatherNic(childObject.getString("fathers_nic"));
                if (childObject.has("mothers_name"))
                    sm.setMotherName(childObject.getString("mothers_name"));
                if (childObject.has("mothers_occupation"))
                    sm.setMotherOccupation(childObject.getString("mothers_occupation"));
                if (childObject.has("mothers_nic"))
                    sm.setMotherNic(childObject.getString("mothers_nic"));
                if (childObject.has("guardians_name"))
                    sm.setGuardianName(childObject.getString("guardians_name"));
                if (childObject.has("guardians_occupation"))
                    sm.setGuardianOccupation(childObject.getString("guardians_occupation"));
                if (childObject.has("guardians_nic"))
                    sm.setGuardianNic(childObject.getString("guardians_nic"));
                if (childObject.has("previous_school_name"))
                    sm.setPreviousSchoolName(childObject.getString("previous_school_name"));
                if (childObject.has("class_previous_school"))
                    sm.setPreviousSchoolClass(childObject.getString("class_previous_school"));
                if (childObject.has("address_1"))
                    sm.setAddress1(childObject.getString("address_1"));
                if (childObject.has("address_2"))
                    sm.setAddress2(childObject.getString("address_2"));
                if (childObject.has("contact_numbers"))
                    sm.setContactNumbers(childObject.getString("contact_numbers"));
                if (childObject.has("current_session"))
                    sm.setCurrentSession(childObject.getInt("current_session"));
//                if (childObject.has("current_class"))
//                    sm.setCurrentClass(childObject.getInt("current_class"));
//                if (childObject.has("current_section"))
//                    sm.setCurrentSection(childObject.getString("current_section"));
//                if (childObject.has("profile_picture_filename"))
//                    sm.setProfilePicName(childObject.getString("profile_picture_filename"));
                if (childObject.has("modified_by"))
                    sm.setModifiedBy(childObject.getInt("modified_by"));
                if (childObject.has("modified_on"))
                    sm.setModifiedOn(childObject.getString("modified_on"));
                if (childObject.has("uploaded_on"))
                    sm.setUploadedOn(childObject.getString("uploaded_on"));
//                if (childObject.has("school_id"))
//                    sm.setSchoolId(childObject.getInt("school_id"));
                if (childObject.has("withdrawn_on"))
                    sm.setWithdrawnOn(childObject.getString("withdrawn_on"));
//                if (childObject.has("is_active"))
//                    sm.setActive(childObject.getBoolean("is_active"));
//                if (childObject.has("has_graduated"))
//                    sm.setHasGraduated(childObject.getBoolean("has_graduated"));
                smList.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smList;
    }

    public StudentModel parseObject(String json) {
        sm = null;
        try {
            JSONObject childObject = new JSONObject(json);

            if (childObject.has("id"))
                sm.setId(childObject.getInt("id"));
            if (childObject.has("name"))
                sm.setName(childObject.getString("name"));
            if (childObject.has("gender"))
                sm.setGender(childObject.getString("gender"));
            if (childObject.has("grno"))
                sm.setGrNo(childObject.getString("grno"));
            if (childObject.has("enrollment_date"))
                sm.setEnrollmentDate(childObject.getString("enrollment_date"));
            if (childObject.has("dob"))
                sm.setDob(childObject.getString("dob"));
            if (childObject.has("form_b"))
                sm.setFormB(childObject.getString("form_b"));
            if (childObject.has("fathers_name"))
                sm.setFathersName(childObject.getString("fathers_name"));
            if (childObject.has("fathers_occupation"))
                sm.setFatherOccupation(childObject.getString("fathers_occupation"));
            if (childObject.has("fathers_nic"))
                sm.setFatherNic(childObject.getString("fathers_nic"));
            if (childObject.has("mothers_name"))
                sm.setMotherName(childObject.getString("mothers_name"));
            if (childObject.has("mothers_occupation"))
                sm.setMotherOccupation(childObject.getString("mothers_occupation"));
            if (childObject.has("mothers_nic"))
                sm.setMotherNic(childObject.getString("mothers_nic"));
            if (childObject.has("guardians_name"))
                sm.setGuardianName(childObject.getString("guardians_name"));
            if (childObject.has("guardians_occupation"))
                sm.setGuardianOccupation(childObject.getString("guardians_occupation"));
            if (childObject.has("guardians_nic"))
                sm.setGuardianNic(childObject.getString("guardians_nic"));
            if (childObject.has("previous_school_name"))
                sm.setPreviousSchoolName(childObject.getString("previous_school_name"));
            if (childObject.has("class_previous_school"))
                sm.setPreviousSchoolClass(childObject.getString("class_previous_school"));
            if (childObject.has("address_1"))
                sm.setAddress1(childObject.getString("address_1"));
            if (childObject.has("address_2"))
                sm.setAddress2(childObject.getString("address_2"));
            if (childObject.has("contact_numbers"))
                sm.setContactNumbers(childObject.getString("contact_numbers"));
            if (childObject.has("current_session"))
                sm.setCurrentSession(childObject.getInt("current_session"));
//            if (childObject.has("current_class"))
//                sm.setCurrentClass(childObject.getInt("current_class"));
//            if (childObject.has("current_section"))
//                sm.setCurrentSection(childObject.getString("current_section"));
//            if (childObject.has("profile_picture_filename"))
//                sm.setProfilePicName(childObject.getString("profile_picture_filename"));
            if (childObject.has("modified_by"))
                sm.setModifiedBy(childObject.getInt("modified_by"));
            if (childObject.has("modified_on"))
                sm.setModifiedOn(childObject.getString("modified_on"));
            if (childObject.has("uploaded_on"))
                sm.setUploadedOn(childObject.getString("uploaded_on"));
//            if (childObject.has("school_id"))
//                sm.setSchoolId(childObject.getInt("school_id"));
            if (childObject.has("withdrawn_on"))
                sm.setWithdrawnOn(childObject.getString("withdrawn_on"));
//            if (childObject.has("is_active"))
//                sm.setActive(childObject.getBoolean("is_active"));
//            if (childObject.has("has_graduated"))
//                sm.setHasGraduated(childObject.getBoolean("has_graduated"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sm;
    }

    @Override
    public boolean equals(Object obj) {
        StudentModel model = (StudentModel) obj;
        return getId() == model.getId();
    }
}
