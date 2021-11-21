package com.task.note.screen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import com.task.note.R;
import com.task.note.adapter.model.Notes;
import com.task.note.databaseHandler.DatabaseHandler;
import com.task.note.utils.LinedEditText;

public class EditNoteActivity extends AppCompatActivity {

    // custom view && database
    private EditText title;
    private LinedEditText description;
    private TextView save;
    private TextView tvDate;
    private DatabaseHandler databaseHandler;

    // get Intent
    private String getTitle;
    private int getId;
    private String getDescription;

    // notification
    private NotificationManager manager;
    private Notification myNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // findView Class
        findViewClass();

        //get Value from Adapter
        getIntentClass();

        // change font run time
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf");
        description.setTypeface(face);

        // take action during onChanging text
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // change title as per Description.
                title.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // on save
        save.setText("Update");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save data to database
                sendRequestToSave();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // save data to database
        sendRequestToSave();
    }

    // save data to database
    private void sendRequestToSave() {
        Notes note = new Notes(); // notes model class
        note.setTitle(title.getText().toString());
        note.setDescription(description.getText().toString());
        long now = System.currentTimeMillis();
        note.setModify(now);
        databaseHandler.editNote(note, getTitle); // send request for edit Note

        Toast.makeText(EditNoteActivity.this, "Save", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(EditNoteActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        }, 500);
    }

    private void getIntentClass() {
        getId = getIntent().getExtras().getInt("id"); // get Title
        getTitle = getIntent().getExtras().getString("title", "try again"); // get Title
        getDescription = getIntent().getExtras().getString("description", "try again"); // get Description
        String getTime = getIntent().getExtras().getString("modify", "try again"); // get Time

        title.setText(getTitle); // set Title
        description.setText(getDescription); // set Description
        tvDate.setText(getTime); // set Time
    }

    private void findViewClass() {
        save = (TextView) findViewById(R.id.save); // btn save
        title = (EditText) findViewById(R.id.title); // title
        tvDate = (TextView) findViewById(R.id.tvDate); // title
        description = (LinedEditText) findViewById(R.id.description); // description inside add btn

        databaseHandler = new DatabaseHandler(this); // init database
    }

    @SuppressLint("RestrictedApi")
    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(EditNoteActivity.this, view);//View will be an anchor for PopupMenu
        popupMenu.inflate(R.menu.custom_menu);
        Menu menu = popupMenu.getMenu();

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("Delete")) {
                    // Custom Delete Alert Dialog
                    RequestDeleteDialog();

                }

                if (item.getTitle().equals("Reminder")) {

                    // Generate Notification of Reminder
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    /* direction of activity which activity you want to open */
                    Intent intent = new Intent("com.task.note.screen.MainActivity");

                    PendingIntent pendingIntent = PendingIntent.getActivity(EditNoteActivity.this, 1, intent, 0);
                    Notification.Builder builder = new Notification.Builder(EditNoteActivity.this);

                    builder.setAutoCancel(false);
                    builder.setTicker(getTitle);
                    builder.setContentTitle(getTitle);
                    builder.setContentText(getDescription);
                    builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                    builder.setContentIntent(pendingIntent);
                    builder.setOngoing(true);
                    builder.setSubText("Reminder");   //API level 16
                    builder.setNumber(100);
                    builder.build();

                    myNotification = builder.getNotification();
                    manager.notify(11, myNotification);

                   // for canceling notification
                     /*   for cancel notification from status bar
                     *                   manager.cancel(11); */
                }
                return false;
            }
        });
        popupMenu.show();
    }


    // Custom Delete Alert Dialog
    private void RequestDeleteDialog() {

        final Dialog dialog = new Dialog(EditNoteActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_exit);

        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView message = (TextView) dialog.findViewById(R.id.message);
        final Button cancel = (Button) dialog.findViewById(R.id.btn_close);
        final Button exit = (Button) dialog.findViewById(R.id.btn_exist);

        title.setText("Delete Text Note");
        message.setText("Are You Sure,you want to Delete This Text Note?");
        exit.setText("DELETE");
        cancel.setText("CANCEL");

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete from database
                Notes notes = new Notes();
                notes.setId(getId);
                databaseHandler.deleteNote(notes);
                Toast.makeText(EditNoteActivity.this, "Delete SuccessFull", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent in = new Intent(EditNoteActivity.this, MainActivity.class);
                        startActivity(in);
                    }
                }, 300);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}