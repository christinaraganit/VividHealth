package ca.bcit.vividhealth;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class ReminderListAdapter extends ArrayAdapter<Reminder> {
    public ReminderListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
