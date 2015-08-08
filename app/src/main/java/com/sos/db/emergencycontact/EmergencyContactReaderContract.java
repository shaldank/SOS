package com.sos.db.emergencycontact;

import android.provider.BaseColumns;

/**
 * Contract class to define URIs,tables and columns
 * Should be used globally to access any tables and it's columns
 */
public final class EmergencyContactReaderContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_EMERGENCYTYPES =
            "CREATE TABLE " + EmergencyType.TABLE_NAME + " (" +
                    EmergencyType._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    EmergencyType.COLUMN_NAME_TYPE_NAME + TEXT_TYPE + COMMA_SEP +
                    EmergencyType.COLUMN_NAME_TYPE_DESC + TEXT_TYPE + " )";

    public static final String SQL_CREATE_EMERGENCYCONTACTS =
            "CREATE TABLE " + EmergencyContact.TABLE_NAME + " (" +
                    EmergencyContact._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    EmergencyContact.COLUMN_NAME_CONTACT_NAME + TEXT_TYPE + COMMA_SEP +
                    EmergencyContact.COLUMN_NAME_PHONE_NUMBERS + TEXT_TYPE + COMMA_SEP +
                    EmergencyContact.COLUMN_NAME_EMAILS + TEXT_TYPE + COMMA_SEP +
                    EmergencyContact.COLUMN_NAME_TYPE_ID + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + EmergencyContact.COLUMN_NAME_TYPE_ID + ") REFERENCES " + EmergencyType.TABLE_NAME + "(" + EmergencyType._ID + ")" +
                    " )";

    public static final String SQL_DELETE_EMERGENCYTYPES =
            "DROP TABLE IF EXISTS " + EmergencyType.TABLE_NAME;

    public static final String SQL_DELETE_EMERGENCYCONTACTS =
            "DROP TABLE IF EXISTS " + EmergencyContact.TABLE_NAME;

    public EmergencyContactReaderContract() {
    }

    public static abstract class EmergencyType implements BaseColumns {
        public static final String TABLE_NAME = "emergencytype";
        //public static final String COLUMN_NAME_TYPE_ID = "typeid";
        public static final String COLUMN_NAME_TYPE_NAME = "typename";
        public static final String COLUMN_NAME_TYPE_DESC = "typedesc";
        public static final String NULL_COLUMN_HACK = null;
    }

    public static abstract class EmergencyContact implements BaseColumns {
        public static final String TABLE_NAME = "emergencycontact";
        //public static final String COLUMN_NAME_CONTACT_ID = "contactid";
        public static final String COLUMN_NAME_CONTACT_NAME = "contactname";
        public static final String COLUMN_NAME_PHONE_NUMBERS = "phonenumers"; // store as comma separated string
        public static final String COLUMN_NAME_EMAILS = "emails"; // store as comma separated string
        public static final String COLUMN_NAME_TYPE_ID = "typeid";
        public static final String NULL_COLUMN_HACK = null;
    }

    public static void createEmergencyTypes() {

    }
}
