package com.tcf.sma.Models.RetrofitModels;

import com.tcf.sma.Helpers.DbTables.FeesCollection.AcademicSession;
import com.tcf.sma.Models.AcademicSessionModel;
import com.tcf.sma.Models.AreaModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.CampusModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.Fees_Collection.SessionInfoModel;
import com.tcf.sma.Models.LocationModel;
import com.tcf.sma.Models.RegionModel;
import com.tcf.sma.Models.ScholarshipCategoryModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.WithdrawalReasonModel;

import java.util.ArrayList;

/**
 * Created by Mohammad.Haseeb on 2/27/2017.
 */

public class MetaDataResponseModel {
    public ArrayList<CalendarsModel> Calendars;
    private ArrayList<RSchoolModel> School;
    private ArrayList<SchoolClassesModel> School_Classes;
    private ArrayList<SessionInfoModel> SchoolSSRSummary;
    private ArrayList<AcademicSessionModel> AcademicSession;
    //private ArrayList<SyncCashReceiptsModel> FeesReceipts;
    //private ArrayList<FeesInvoiceModel> FeesInvoices;
    //private ArrayList<CashDepositModel> CashDeposits;
    private int TargetFee;
    private ArrayList<ClassModel> TCFClass;
    private ArrayList<SectionModel> Section;
    private ArrayList<WithdrawalReasonModel> Withdrawal_Reason;
    private ArrayList<CampusModel> Campus;
    private ArrayList<LocationModel> Location;
    private ArrayList<AreaModel> Area;
    private ArrayList<ScholarshipCategoryModel> Scholarship_Category;
    private ArrayList<RegionModel> Region;
    private ArrayList<ReligionModel> Religion;
    private ArrayList<NationalityModel> Nationality;
    private ArrayList<ElectiveSubjectModel> ElectiveSubjects;
    public MetaDataResponseModel() {
    }
    public MetaDataResponseModel(ArrayList<RSchoolModel> school, ArrayList<ClassModel> TCFClass, ArrayList<SectionModel> section, ArrayList<SchoolClassesModel> school_Classes, ArrayList<WithdrawalReasonModel> withdrawal_Reason,
                                 //ArrayList<SyncCashReceiptsModel> feesReceipts, ArrayList<FeesInvoiceModel> feesInvoices, ArrayList<CashDepositModel> cashDeposits,
                                 ArrayList<CampusModel> campus, ArrayList<LocationModel> locations, ArrayList<AreaModel> area, ArrayList<RegionModel> region
                                 //, ArrayList<School_AllowedModulesModel> school_AllowedModules
    ) {
        School = school;
        //FeesReceipts = feesReceipts;
        //FeesInvoices = feesInvoices;
        //CashDeposits = cashDeposits;
        this.TCFClass = TCFClass;
        Section = section;
        School_Classes = school_Classes;
        Withdrawal_Reason = withdrawal_Reason;
        Campus = campus;
        Location = locations;
        Area = area;
        Region = region;
        //School_AllowedModules = school_AllowedModules;
    }

    public ArrayList<ScholarshipCategoryModel> getScholarship_Category() {
        return Scholarship_Category;
    }

    public void setScholarship_Category(ArrayList<ScholarshipCategoryModel> scholarship_Category) {
        Scholarship_Category = scholarship_Category;
    }
    //private ArrayList<School_AllowedModulesModel> School_AllowedModules;

    public ArrayList<SessionInfoModel> getSchoolSSRSummary() {
        return SchoolSSRSummary;
    }

    public void setSchoolSSRSummary(ArrayList<SessionInfoModel> schoolSSRSummary) {
        SchoolSSRSummary = schoolSSRSummary;
    }

    public ArrayList<CalendarsModel> getCalendars() {
        return Calendars;
    }

    public void setCalendars(ArrayList<CalendarsModel> calendars) {
        Calendars = calendars;
    }

    public ArrayList<RSchoolModel> getSchool() {
        return School;
    }

    public void setSchool(ArrayList<RSchoolModel> school) {
        School = school;
    }

    public ArrayList<ClassModel> getTCFClass() {
        return TCFClass;
    }

    public void setTCFClass(ArrayList<ClassModel> TCFClass) {
        this.TCFClass = TCFClass;
    }

    public ArrayList<SectionModel> getSection() {
        return Section;
    }

    public void setSection(ArrayList<SectionModel> section) {
        Section = section;
    }

    public ArrayList<SchoolClassesModel> getSchool_Classes() {
        return School_Classes;
    }

    public void setSchool_Classes(ArrayList<SchoolClassesModel> school_Classes) {
        School_Classes = school_Classes;
    }

    public ArrayList<WithdrawalReasonModel> getWithdrawal_Reason() {
        return Withdrawal_Reason;
    }

    public void setWithdrawal_Reason(ArrayList<WithdrawalReasonModel> withdrawal_Reason) {
        Withdrawal_Reason = withdrawal_Reason;
    }

//	public ArrayList<SyncCashReceiptsModel> getCashReceipts() {
//        return FeesReceipts;
//    }
//
//    public void setFeesReceipts(ArrayList<SyncCashReceiptsModel> feesReceipts) {
//        FeesReceipts = feesReceipts;
//    }
//
//	public ArrayList<FeesInvoiceModel> getFeesInvoices() {
//        return FeesInvoices;
//    }
//
//    public void setFeesInvoices(ArrayList<FeesInvoiceModel> feesInvoices) {
//        FeesInvoices = feesInvoices;
//    }
//
//	public ArrayList<CashDepositModel> getCashDeposits() {
//        return CashDeposits;
//    }
//
//    public void setCashDeposits(ArrayList<CashDepositModel> cashDeposits) {
//        CashDeposits = cashDeposits;
//    }

    public ArrayList<CampusModel> getCampuses() {
        return Campus;
    }

    public void setCampuss(ArrayList<CampusModel> campuses) {
        Campus = campuses;
    }

    public ArrayList<LocationModel> getLocation() {
        return Location;
    }

    public void setLocation(ArrayList<LocationModel> location) {
        Location = location;
    }

    public ArrayList<AreaModel> getArea() {
        return Area;
    }

    public void setArea(ArrayList<AreaModel> area) {
        Area = area;
    }

    public ArrayList<RegionModel> getRegion() {
        return Region;
    }

    public void setRegion(ArrayList<RegionModel> region) {
        Region = region;
    }

    public int getTargetFee() {
        return TargetFee;
    }

    public void setTargetFee(int targetFee) {
        TargetFee = targetFee;
    }
//	public ArrayList<School_AllowedModulesModel> getSchool_AllowedModules() {
//        return School_AllowedModules;
//    }
//
//    public void setSchool_AllowedModules(ArrayList<School_AllowedModulesModel> school_AllowedModules) {
//        School_AllowedModules = school_AllowedModules;
//    }


    public ArrayList<AcademicSessionModel> getAcademicSession() {
        return AcademicSession;
    }

    public void setAcademicSession(ArrayList<AcademicSessionModel> academicSession) {
        AcademicSession = academicSession;
    }

    public ArrayList<ReligionModel> getReligion() {
        return Religion;
    }

    public void setReligion(ArrayList<ReligionModel> religion) {
        Religion = religion;
    }

    public ArrayList<NationalityModel> getNationality() {
        return Nationality;
    }

    public void setNationality(ArrayList<NationalityModel> nationality) {
        Nationality = nationality;
    }

    public ArrayList<ElectiveSubjectModel> getElectiveSubjects() {
        return ElectiveSubjects;
    }

    public void setElectiveSubjects(ArrayList<ElectiveSubjectModel> electiveSubjects) {
        ElectiveSubjects = electiveSubjects;
    }
}
