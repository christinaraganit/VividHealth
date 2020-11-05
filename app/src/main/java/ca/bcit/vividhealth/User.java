package ca.bcit.vividhealth;

import java.util.List;

public class User {
    String userId;
    String userName;
    int userAge;
    int userWeight;
    List<Reminder> reminderList;

    public User() {}

    public User(String userId, String userName, int userAge, int userWeight) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userWeight = userWeight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
