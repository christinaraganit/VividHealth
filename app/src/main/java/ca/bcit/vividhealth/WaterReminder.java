package ca.bcit.vividhealth;

public class WaterReminder extends Reminder {
    int waterReminderGlassesDrank;
    int waterReminderGlassesToDrink;

    public WaterReminder() {
    }

    public int getWaterReminderGlassesDrank() {
        return waterReminderGlassesDrank;
    }

    public void setWaterReminderGlassesDrank(int waterReminderGlassesDrank) {
        this.waterReminderGlassesDrank = waterReminderGlassesDrank;
    }

    public int getWaterReminderGlassesToDrink() {
        return waterReminderGlassesToDrink;
    }

    public void setWaterReminderGlassesToDrink(int waterReminderGlassesToDrink) {
        this.waterReminderGlassesToDrink = waterReminderGlassesToDrink;
    }

    public WaterReminder(int waterReminderGlassesDrank, int waterReminderGlassesToDrink) {
        this.waterReminderGlassesDrank = waterReminderGlassesDrank;
        this.waterReminderGlassesToDrink = waterReminderGlassesToDrink;
    }
}
