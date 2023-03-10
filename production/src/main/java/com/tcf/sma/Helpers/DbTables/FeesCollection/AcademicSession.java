package com.tcf.sma.Helpers.DbTables.FeesCollection;

public class AcademicSession {

    public static final String ACADEMIC_SESSION_TABLE = "AcademicSession";


    public static final String SESSION_ID = "session_id";
    public static final String SESSION = "session";
    public static final String START_YEAR = "start_year";

    public static final String CREATE_ACADEMIC_SESSION_TABLE = "CREATE TABLE " + ACADEMIC_SESSION_TABLE
            + " (" + SESSION_ID + "  INTEGER PRIMARY KEY ,"
            + SESSION + "  TEXT ,"
            + START_YEAR + "  INTEGER);";

}
