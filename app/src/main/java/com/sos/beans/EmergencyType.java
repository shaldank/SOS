package com.sos.beans;

public class EmergencyType {
    private long _id;
    private String name;
    private String desc;

    public EmergencyType() {
    }

    public EmergencyType(long _id, String name, String desc) {
        this._id = _id;
        this.name = name;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "EmergencyType{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
