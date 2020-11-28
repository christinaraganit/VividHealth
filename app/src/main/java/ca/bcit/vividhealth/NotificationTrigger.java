package ca.bcit.vividhealth;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class NotificationTrigger extends IntentService {

    private static final String ALARM = "your.package.ALARM";

    public NotificationTrigger() {
        super("NotificationTrigger");
    }

    public static void starNotification(Context context, String message) {
        Intent intent = new Intent(context, NotificationTrigger.class);
        intent.putExtra("message", message);
        intent.setAction(ALARM);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Bundle message = intent.getExtras();
            if (ALARM.equals(action)) {
                handleAlarm(message.get("message").toString());
            }
        }
    }

    private void handleAlarm(String message) {

        // Get id & message
        int notificationId = 0;

        // Call MainActivity when notification is tapped.
        Intent mainIntent = new Intent(getApplicationContext(), HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, 0);

        // NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence channelName = "My Notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("CHANNEL_SAMPLE", channelName, importance);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_SAMPLE")
                .setSmallIcon(R.drawable.logo_white)
                .setContentTitle("Vivid Health")
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Notify
        notificationManager.notify(notificationId, builder.build());
    }


}