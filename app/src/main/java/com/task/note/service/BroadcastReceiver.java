package com.task.note.service;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Time Up... Now Vibrating !!!",
                Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
