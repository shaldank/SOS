package com.sos.db.emergencycontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for emergency contacts
 */
public class EmergencyContactReaderHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Sos3.db";
    private static String[] INITIAL_EMERGENCY_TYPES = {"HIGH", "MEDIUM", "LOW"};
    private static String[] INITIAL_EMERGENCY_TYPE_DESCS = {"HIGH", "MEDIUM", "LOW"};

    public EmergencyContactReaderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EmergencyContactReaderContract.SQL_CREATE_EMERGENCYTYPES);
        db.execSQL(EmergencyContactReaderContract.SQL_CREATE_EMERGENCYCONTACTS);
        doInitialInsert(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: complete this if needed
    }

    public void doInitialInsert(SQLiteDatabase writableDatabase) {
        //SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues contentValues;
        for (int i = 0; i < INITIAL_EMERGENCY_TYPES.length; i++) {
            contentValues = new ContentValues();
            contentValues.put(EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_NAME, INITIAL_EMERGENCY_TYPES[i]);
            contentValues.put(EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_DESC, INITIAL_EMERGENCY_TYPE_DESCS[i]);
            long typeId = writableDatabase.
                    insert(EmergencyContactReaderContract.EmergencyType.TABLE_NAME, EmergencyContactReaderContract.EmergencyType.NULL_COLUMN_HACK, contentValues);
           /*
            for (int j = 0; j < 3; j++) {
                contentValues = new ContentValues();
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_CONTACT_NAME, INITIAL_EMERGENCY_TYPES[i] + " temp contact " + j);
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_EMAILS, "email1,email2");
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_PHONE_NUMBERS, "number1,number2");
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_TYPE_ID, typeId);

                writableDatabase.
                        insert(EmergencyContactReaderContract.EmergencyContact.TABLE_NAME, EmergencyContactReaderContract.EmergencyContact.NULL_COLUMN_HACK, contentValues);
            }
            */

        }

    }
}
