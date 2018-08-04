package com.auk.wakey.model;

import java.util.Calendar;

public class Alarm {

    private String uid;

    private boolean[] repeats;

    private boolean isOn;

    private Calendar alarmDate;

    private String ringtoneUrl;

    private boolean avoidGovHolildays;

    public Alarm(String uid, boolean isOn, Calendar alarmDate, boolean[] repeats, String ringtoneUrl, boolean avoidGovHolildays) {
        this.alarmDate = alarmDate;
        this.ringtoneUrl = ringtoneUrl;
        this.avoidGovHolildays = avoidGovHolildays;
        this.repeats = repeats;
        this.isOn = isOn;
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean[] getRepeats() {
        return this.repeats;
    }

    public void setRepeats(boolean[] repeats) {
        this.repeats = repeats;
    }

    public boolean getIsOn() {
        return this.isOn;
    }

    public void setIsOn(Boolean isOn) {
        this.isOn = isOn;
    }

    public Calendar getAlarmDate() {
        return this.alarmDate;
    }

    public void setAlarmDate(Calendar alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getRingtoneUrl() {
        return this.ringtoneUrl;
    }

    public void setRingtoneUrl(String ringtoneUrl) {
        this.ringtoneUrl = ringtoneUrl;
    }

    public boolean getAvoidGovHolildays() {
        return this.avoidGovHolildays;
    }

    public void setAvoidGovHolildays(Boolean avoidGovHolildays) {
        this.avoidGovHolildays = avoidGovHolildays;
    }
}
