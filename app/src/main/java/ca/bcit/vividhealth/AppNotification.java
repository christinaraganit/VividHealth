package ca.bcit.vividhealth;



import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;



public class Notification extends Application {

    static String CHANNEL_ID = "Reminders";


    public void setNotification(Context context, String message){

        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat b = new NotificationCompat.Builder(context, CHANNEL_ID);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());


    }


}
