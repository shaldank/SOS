package com.sos.db.emergencycontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sos.beans.EmergencyContact;
import com.sos.beans.EmergencyType;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for emergency contacts
 */
public class EmergencyContactDAO {
    private EmergencyContactReaderHelper emergencyContactReaderHelper;
//    private static String[] INITIAL_EMERGENCY_TYPES = {"HIGH", "MEDIUM", "LOW"};
//    private static String[] INITIAL_EMERGENCY_TYPE_DESCS = {"HIGH", "MEDIUM", "LOW"};

    public EmergencyContactDAO(Context context) {
        emergencyContactReaderHelper = new EmergencyContactReaderHelper(context);
    }

    public long insertContact(EmergencyContact contact) {
        SQLiteDatabase writableDatabase = emergencyContactReaderHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_CONTACT_NAME, contact.getName());
        contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_EMAILS, contact.getEmails());
        contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_PHONE_NUMBERS, contact.getPhoneNumbers());
        contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_TYPE_ID, contact.getEmergencyType());

        return writableDatabase.
                insert(EmergencyContactReaderContract.EmergencyContact.TABLE_NAME, EmergencyContactReaderContract.EmergencyContact.NULL_COLUMN_HACK, contentValues);
    }
/*
    public void doInitialInsert() {
        SQLiteDatabase writableDatabase = emergencyContactReaderHelper.getWritableDatabase();
        ContentValues contentValues;
        for (int i = 0; i < INITIAL_EMERGENCY_TYPES.length; i++) {
            contentValues = new ContentValues();
            contentValues.put(EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_NAME, INITIAL_EMERGENCY_TYPES[i]);
            contentValues.put(EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_DESC, INITIAL_EMERGENCY_TYPE_DESCS[i]);
            long typeId = writableDatabase.
                    insert(EmergencyContactReaderContract.EmergencyType.TABLE_NAME, EmergencyContactReaderContract.EmergencyType.NULL_COLUMN_HACK, contentValues);
            for (int j = 0; j < 3; j++) {
                contentValues = new ContentValues();
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_CONTACT_NAME, INITIAL_EMERGENCY_TYPES[i] + " temp contact " + j);
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_EMAILS, "email1,email2");
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_PHONE_NUMBERS, "number1,number2");
                contentValues.put(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_TYPE_ID, typeId);

                writableDatabase.
                        insert(EmergencyContactReaderContract.EmergencyContact.TABLE_NAME, EmergencyContactReaderContract.EmergencyContact.NULL_COLUMN_HACK, contentValues);
            }

        }

    }
*/
    public List<EmergencyType> getEmergencyTypes() {
        SQLiteDatabase readableDatabase = emergencyContactReaderHelper.getReadableDatabase();
        List<EmergencyType> types = new ArrayList<EmergencyType>();
        String[] projection = {
                EmergencyContactReaderContract.EmergencyType._ID,
                EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_NAME,
                EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_DESC
        };

        Cursor cursor = readableDatabase.query(
                EmergencyContactReaderContract.EmergencyType.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            types.add(new EmergencyType(cursor.getLong(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyType._ID)), cursor.getString(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_NAME)), cursor.getString(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyType.COLUMN_NAME_TYPE_DESC))));
            cursor.moveToNext();
        }
        return types;
    }

    public List<EmergencyContact> getEmergencyContacts(EmergencyType type) {
        SQLiteDatabase readableDatabase = emergencyContactReaderHelper.getReadableDatabase();
        List<EmergencyContact> contacts = new ArrayList<EmergencyContact>();
        String[] projection = {
                EmergencyContactReaderContract.EmergencyContact._ID,
                EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_CONTACT_NAME,
                EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_PHONE_NUMBERS,
                EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_EMAILS,
                EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_TYPE_ID,
        };
        String whereClause = EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_TYPE_ID + "=?";
        String[] whereValues = {String.valueOf(type.get_id())};
        Cursor cursor = readableDatabase.query(
                EmergencyContactReaderContract.EmergencyContact.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            contacts.add(new EmergencyContact(cursor.getLong(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyContact._ID)), cursor.getString(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_CONTACT_NAME)), cursor.getString(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_PHONE_NUMBERS)), cursor.getString(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_EMAILS)), cursor.getLong(cursor.getColumnIndex(EmergencyContactReaderContract.EmergencyContact.COLUMN_NAME_TYPE_ID))));
            cursor.moveToNext();
        }

        return contacts;
    }
}
