package ca.bcit.vividhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcaster extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Remember in the SetAlarm file we made an intent to this, this is way this work, otherwise you would have to put an action
        //and here listen to the action, like in a normal receiver
        createNotification(context, intent.getExtras().get("message").toString());

    }

    public void createNotification(Context context, String message) {
        NotificationTrigger.starNotification(context, message);
    }

}