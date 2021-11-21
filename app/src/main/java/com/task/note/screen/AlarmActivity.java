package com.task.note.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.task.note.R;
import com.task.note.service.BroadcastReceiver;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    Button alarmbutton, cancelButton;
    EditText text;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Intent intent;

    private int mDay, mHour, mMinute;
    public static TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmbutton = (Button) findViewById(R.id.button);
        cancelButton = (Button) findViewById(R.id.button2);
        text = (EditText) findViewById(R.id.editText);
        alarmbutton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        /* use this method if you want to start Alarm at particular time*/
        // Launch Time Picker Dialog
        timePickerDialog = new TimePickerDialog(AlarmActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        startAlertAtParticularTime(view);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void startAlert(View view) {
        if (!text.getText().toString().equals("")) {
            int i = Integer.parseInt(text.getText().toString());
            intent = new Intent(this, BroadcastReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(
                    this.getApplicationContext(), 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + (i * 1000), 10000
                    , pendingIntent);

            Toast.makeText(this, "Alarm will set in " + i + " seconds",
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"Please Provide time ",Toast.LENGTH_SHORT).show();
        }

    }


    private void startAlertAtParticularTime(TimePicker view) {
        // alarm first vibrate at 14 hrs and 40 min and repeat itself at ONE_HOUR interval
        intent = new Intent(this, BroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, view.getHour());
        calendar.set(Calendar.MINUTE, view.getMinute());

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR, pendingIntent);

        Toast.makeText(this, "Alarm will vibrate at time specified",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            startAlert(v);
        } else {
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Alarm Disabled !!",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}