package ca.bcit.vividhealth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ClosingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        System.out.println("Closing the Application");

        // Handle application closing
        fireClosingNotification();

        // Destroy the service
        stopSelf();
    }

    public void fireClosingNotification(){
        AppNotification.sendNotification(getApplicationContext(), "Closing App");
    }
}

