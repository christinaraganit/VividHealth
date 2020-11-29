package ca.bcit.vividhealth;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class NotificationTrigger extends IntentService {

    private static final String ALARM = "your.package.ALARM";

    public NotificationTrigger() {
        super("NotificationTrigger");
    }

    public static void starNotification(Context context) {
        Intent intent = new Intent(context, NotificationTrigger.class);
        intent.setAction(ALARM);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ALARM.equals(action)) {

            }
        }
    }
}
