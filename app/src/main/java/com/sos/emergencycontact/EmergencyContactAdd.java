package com.sos.emergencycontact;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.sos.beans.EmergencyContact;
import com.sos.beans.EmergencyType;
import com.sos.db.emergencycontact.EmergencyContactDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shahharshil46.sos.R;

public class EmergencyContactAdd extends Activity {
    private static final String DEBUG_TAG = "DEBUG:";
    EmergencyContactDAO dao;
    private static final int CONTACT_PICKER_RESULT = 1001;
    EmergencyType selectedType = null;
    TabHost tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_add);
        dao = new EmergencyContactDAO(getApplicationContext());
        //dao.doInitialInsert();
        tabs = (TabHost) findViewById(R.id.addContactTabs);
        createTabsFromEmergencyType(tabs);
    }

    private void createTabsFromEmergencyType(TabHost tabHost) {
        List<EmergencyType> types = dao.getEmergencyTypes();
        tabHost.setup();
        for (final EmergencyType type : types) {
            TabHost.TabSpec newSpec = tabHost.newTabSpec(type.getName());
            newSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return getContactListView(type);
                }
            });
            newSpec.setIndicator(type.getName());
            tabHost.addTab(newSpec);
        }
    }

    private View getContactListView(final EmergencyType type) {
        RelativeLayout layout = new RelativeLayout(EmergencyContactAdd.this);
        layout.setTag("relativeLayout");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        ListView contactList = getRelatedContactListView(type);
        addListViewToRelativeLayout(layout,contactList);
        Button addContact = new Button(EmergencyContactAdd.this);
        addContact.setText("Add contact");
        RelativeLayout.LayoutParams addContactParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        addContactParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType = type;
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CONTACT_PICKER_RESULT);
            }
        });

        layout.addView(addContact,addContactParams);

        return layout;
    }

    private ListView getRelatedContactListView(EmergencyType type) {
        List<EmergencyContact> contacts = dao.getEmergencyContacts(type);
        List<String> listContent = new ArrayList<String>();
        for (EmergencyContact contact : contacts) {
            listContent.add(contact.getName());
        }

        MyListAdaptor adaptor = new MyListAdaptor(getApplicationContext(), listContent);
        ListView contactList = new ListView(EmergencyContactAdd.this);
        contactList.setTag("contactList");
        contactList.setAdapter(adaptor);

        return contactList;
    }
    private void addListViewToRelativeLayout(RelativeLayout layout, ListView contactList)
    {
        RelativeLayout.LayoutParams contactListParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        contactListParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layout.addView(contactList, contactListParams);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results
                    handleContact(data.getData());
                    break;
            }

        } else {
            // gracefully handle failure
            Log.w(DEBUG_TAG, "Warning: activity result not ok");
        }
    }

    private void handleContact(Uri result) {
        ArrayList<String> allContacts = new ArrayList<String>();
        String id = result.getLastPathSegment();
        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                ContactsContract.Contacts._ID + "=?",
                new String[]{id}, null
        );
        String contactName = null;
        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext()) {
                if(contactName == null)
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        int index = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String number = pCur.getString(index);
                        Log.v("DEBUG: ", "Number: " + " == ["
                                + number + "]");
                        allContacts.add(number);
                    }
                    pCur.close();
                }

                Cursor emailCur = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    // This would allow you get several email addresses
                    // if the email addresses were stored in an array
                    String email = emailCur.getString(
                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    allContacts.add(email);
                }
                emailCur.close();
            }
        }

        String[] arr = new String[allContacts.size()];
        allContacts.toArray(arr);

        saveContactFromSelectBox(contactName, arr, selectedType);
    }
    private class MyListAdaptor extends ArrayAdapter<String> {

        public MyListAdaptor(Context context, List<String> listContent) {
            super(context, -1, -1, listContent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LinearLayout listLayout = new LinearLayout(EmergencyContactAdd.this);
            listLayout.setLayoutParams(new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.WRAP_CONTENT,
                    AbsListView.LayoutParams.WRAP_CONTENT));
            listLayout.setId((int) (Math.random() * 1000));

            TextView listText = new TextView(EmergencyContactAdd.this);
            listText.setId((int) (Math.random() * 1000));

            listLayout.addView(listText);

            listText.setText(super.getItem(position));

            return listLayout;
        }
    }

    private void saveContactFromSelectBox(final String contactName, final String[] contacts, final EmergencyType type)
    {
        final ArrayList<String> selected = new ArrayList<String>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contactName);
        builder.setMultiChoiceItems(contacts, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                           selected.add(contacts[indexSelected]);
                        } else if (selected.contains(indexSelected)) {
                            selected.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }) .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (!selected.isEmpty()) {
                    ArrayList<String> numbers = new ArrayList<String>();
                    ArrayList<String> emails = new ArrayList<String>();
                    for (String selectedContact : selected) {
                        if (isEmail(selectedContact)) emails.add(selectedContact);
                        else numbers.add(selectedContact);
                    }

                    EmergencyContact contact = new EmergencyContact();
                    contact.setName(contactName);
                    contact.setEmails(getCSVFromArrayList(emails));
                    contact.setPhoneNumbers(getCSVFromArrayList(numbers));
                    contact.setEmergencyType(type.get_id());
                    //Toast.makeText(getApplicationContext(), getCSVFromArrayList(emails), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), getCSVFromArrayList(numbers), Toast.LENGTH_LONG).show();
                    Log.v("DEBUG: ", contact.toString());
                    dao.insertContact(contact);
                    Log.v("DEBUG: ", "tabs " + tabs.getCurrentView().toString());
                    refreshListForCurrentTab();
                }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private boolean isEmail(String email) {
        String emailPattern =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    private String getCSVFromArrayList(ArrayList<String> stringArrayList) {
        if (stringArrayList.isEmpty())  return "";
        String csv = stringArrayList.toString();

        return csv.substring(1, csv.length() - 2);
    }

    private void refreshListForCurrentTab() {
        RelativeLayout layout = (RelativeLayout) tabs.getCurrentView();
        ListView contactList = (ListView) layout.findViewWithTag("contactList");
        Log.v("DEBUG: ", "tabs " + contactList.toString());
        layout.removeView(contactList);
        addListViewToRelativeLayout(layout, getRelatedContactListView(selectedType));
    }
}


