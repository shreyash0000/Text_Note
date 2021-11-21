package com.task.note.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.task.note.R;
import com.task.note.adapter.model.Notes;
import com.task.note.databaseHandler.DatabaseHandler;
import com.task.note.utils.LinedEditText;
import com.task.note.utils.Utils;

public class AddNoteActivity extends AppCompatActivity {
    private EditText title;
    private LinedEditText description;
    private TextView save;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // findView Class
        findViewClass();

        // run time change font
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf");
        description.setTypeface(face);

        // on change description title add by description
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // on click button on save
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save notes
                saveNoteClass();
            }
        });

    }

    // on back press saved
    @Override
    public void onBackPressed() {
        // save notes
        saveNoteClass();
    }

    // save notes
    private void saveNoteClass() {
        Notes note = new Notes();
        note.setTitle(title.getText().toString());
        note.setDescription(description.getText().toString());
        long now = System.currentTimeMillis();
        note.setModify(now);
        databaseHandler.addNote(note);

        Toast.makeText(AddNoteActivity.this, "Save", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(AddNoteActivity.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        },500);
    }

    // findView Class
    private void findViewClass() {
        save = (TextView) findViewById(R.id.save); // btn save
        title = (EditText) findViewById(R.id.title); // title
        description = (LinedEditText) findViewById(R.id.description); // description inside add btn

        databaseHandler = new DatabaseHandler(this);
    }

}