package ca.bcit.vividhealth;

import android.text.format.DateUtils;

import java.sql.Time;
import java.util.Date;

public class Reminder {
    String reminderId;
    String reminderTitle;
    String reminderType;
    Time reminderTime;
    Date reminderDate;
    Date reminderFrequency;
    boolean reminderIsDone;

    public Reminder() {}

    public Reminder(String reminderId, String reminderTitle, String reminderType, Time reminderTime, Date reminderDate, Date reminderFrequency, boolean reminderIsDone) {
        this.reminderId = reminderId;
        this.reminderTitle = reminderTitle;
        this.reminderType = reminderType;
        this.reminderTime = reminderTime;
        this.reminderDate = reminderDate;
        this.reminderFrequency = reminderFrequency;
        this.reminderIsDone = reminderIsDone;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public Time getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Time reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Date getReminderFrequency() {
        return reminderFrequency;
    }

    public void setReminderFrequency(Date reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    public boolean isReminderIsDone() {
        return reminderIsDone;
    }

    public void setReminderIsDone(boolean reminderIsDone) {
        this.reminderIsDone = reminderIsDone;
    }
}
