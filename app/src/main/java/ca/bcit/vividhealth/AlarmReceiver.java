package ca.bcit.vividhealth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class AlarmReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public AlarmReceiver(final Context context, final int hour, final int min, final String message,
                         final String repeat, final Calendar calendar){




        //This will set the alarm time
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        if (now.compareTo(calendar) > calendar.compareTo(now)){
            if (repeat.equals("Every day")){
                calendar.add(Calendar.DAY_OF_MONTH, 1);

            } else if (repeat.equals("Every 3 days")){
                calendar.add(Calendar.DAY_OF_MONTH, 3);

            } else if (repeat.equals("Every week")){
                calendar.add(Calendar.DAY_OF_MONTH, 7);

            } else if (repeat.equals("Every 12 hours")){
                calendar.add(Calendar.HOUR_OF_DAY, 12);

            } else if (repeat.equals("Every 3 hours")){
                calendar.add(Calendar.HOUR_OF_DAY, 3);

            }

        }

        System.out.println("Date: " + calendar.getTime().toString());


        //Now create the time and schedule it
        Timer timer = new Timer();

        //Use this if you want to execute it once
        timer.schedule(new AppNotification() {
            @Override
            public void run() {
                AppNotification.sendNotification(context, message);
                new AlarmReceiver(context,hour,min,message, repeat, calendar);
            }
        }, calendar.getTime());




    }
}
