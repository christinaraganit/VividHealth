package ca.bcit.vividhealth;

import java.util.List;

public class User {
    String userId;
    String userName;
    String userEmail;
    int userAge;
    int userWeight;
    List<Reminder> reminderList;

    public User() {}

    public User(String userId, String userEmail, int userAge, int userWeight, String userName) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userAge = userAge;
        this.userWeight = userWeight;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public int getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(int userWeight) {
        this.userWeight = userWeight;
    }

    public List<Reminder> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
