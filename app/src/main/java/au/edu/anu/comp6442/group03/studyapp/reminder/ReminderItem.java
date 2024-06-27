package au.edu.anu.comp6442.group03.studyapp.reminder;

import com.google.firebase.Timestamp;

public class ReminderItem {
    private String reminderText;
    private int year, month, day, hour, minute;

    private Timestamp timestamp;

    public ReminderItem(){}
    public ReminderItem(String reminderText, int year, int month, int day, int hour, int minute) {
        this.reminderText = reminderText;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour){
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }

    public Timestamp getTimestamp(){return timestamp;}
    public void setTimestamp(Timestamp timestamp){this.timestamp = timestamp;}
    @Override
    public String toString() {
        return reminderText; // This is what will be displayed in the ListView
    }
}
