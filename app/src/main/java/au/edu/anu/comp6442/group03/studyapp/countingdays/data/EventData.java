package au.edu.anu.comp6442.group03.studyapp.countingdays.data;

import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EventData {
    private String name;
    private long date;
    private String dateString;
    private String documentId;

    public EventData(String name, long date) {
        this.name = name;
        this.date = date;
        this.dateString = calculateDateString(date);
    }

    private String calculateDateString(long dateMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long differenceInMillis = dateMillis - currentTimeMillis;
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);
        if (differenceInDays > 0) {
            return differenceInDays + " days remaining";
        } else if (differenceInDays == 0) {
            return "Due today";
        } else {
            return "Expired for " + Math.abs(differenceInDays) + " days";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        this.dateString = calculateDateString(date);
    }

    public String getDateString() {
        return dateString;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public boolean isEventToday() {
        return calculateRemainingDays() == 0;
    }

    private long calculateRemainingDays() {
        long currentTimeMillis = System.currentTimeMillis();
        long differenceInMillis = this.date - currentTimeMillis;
        return TimeUnit.MILLISECONDS.toDays(differenceInMillis);

    }
}