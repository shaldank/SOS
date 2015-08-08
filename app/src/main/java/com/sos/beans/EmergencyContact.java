package com.sos.beans;


public class EmergencyContact {
    private long _id;
    private String name;
    private String phoneNumbers;
    private String emails;
    private long emergencyType;

    public EmergencyContact() {
    }

    public EmergencyContact(long _id, String name, String phoneNumbers, String emails, long emergencyType) {
        this._id = _id;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.emails = emails;
        this.emergencyType = emergencyType;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public long getEmergencyType() {
        return emergencyType;
    }
    public void setEmergencyType(long type) {
        this.emergencyType = type;
    }

    @Override
    public String toString() {
        return "EmergencyContact{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", phoneNumbers='" + phoneNumbers + '\'' +
                ", emails='" + emails + '\'' +
                ", emergencyType=" + emergencyType +
                '}';
    }
}
