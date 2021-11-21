package com.task.note.fragment;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.task.note.R;
import com.task.note.service.BroadcastReceiver;

import java.text.DateFormat;
import java.util.Calendar;

public class Search extends Fragment {
    private CalendarView calendar;
    private TextView date_view;

    //__________| Alarm
    private int mDay, mHour, mMinute;
    private Intent intent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    //_____________| static
    public static TimePickerDialog timePickerDialog;

    // empty Contructor
    public Search() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ((TextView) view.findViewById(R.id.action_bar_title)).setText("Calendar");
        calendar = (CalendarView) view.findViewById(R.id.calendar);
        date_view = (TextView) view.findViewById(R.id.date_view);
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Add Listener in calendar
        // get the value of DAYS, MONTH, YEARS
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;

                Toast.makeText(getActivity(), "testing" + Date, Toast.LENGTH_SHORT).show();

                dialogAddNote(Date); // dialog for pick time
                // set this date in TextView for Display
                date_view.setText(Date);
            }
        });

        return view;
    }

    // Show Dialog
    public void dialogAddNote(String Date) {
        // create dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_add_note);

        // ini
        final Button add = (Button) dialog.findViewById(R.id.btn_add);
        final Button cancel = (Button) dialog.findViewById(R.id.btn_close);
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView message = (TextView) dialog.findViewById(R.id.message);

        //set Text
        title.setText(Date + " Selected !");
        message.setText("Please Add Time");
        add.setText("Add Time");
        cancel.setText("Cancel");

        // on click add
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                startAlertAtParticularTime(view);
                                message.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        // dismiss dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void startAlertAtParticularTime(TimePicker view) {
        // alarm first vibrate at select and repeat itself at ONE_HOUR interval
        intent = new Intent(getActivity(), BroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                getActivity().getApplicationContext(), 280192, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // calender accounting take time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, view.getHour());
        calendar.set(Calendar.MINUTE, view.getMinute());

        // get Alarm System Service
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR, pendingIntent);
        Toast.makeText(getActivity(), "Alarm will vibrate at Note Time",
                Toast.LENGTH_SHORT).show();
    }

    /*  @Override
    public void onDestroy() {
        super.onDestroy();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }*/

}