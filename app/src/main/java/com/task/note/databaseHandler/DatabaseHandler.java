package com.task.note.databaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.task.note.adapter.model.Notes;
import com.task.note.fragment.Note;
import com.task.note.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // database name
    private static final String DATABASE_NAME = "Notes.db";

    // table name
    private static final String TABLE_NOTE = "table_note"; // default products

    //order id
    private static final String COLUMN_NOTE_ID = "note_id";
    private static final String COLUMN_NOTE_TITLE = "note_title";
    private static final String COLUMN_NOTE_DESCRIPTION = "note_description";
    private static final String COLUMN_NOTE_CREATED = "note_created";
    private static final String COLUMN_NOTE_MODIFY = "note_modify";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //all rate table
    private String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
            + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOTE_TITLE + " TEXT,"
            + COLUMN_NOTE_DESCRIPTION + " TEXT,"
            + COLUMN_NOTE_MODIFY + " TEXT,"
            + COLUMN_NOTE_CREATED + " TEXT"
            + ")";

    // drop table sql query
    private String DROP_NOTE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NOTE;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_NOTE_TABLE);
    }

    //table Note

    //__________________| Note section |_________________

    // add note
    public void addNote(Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_DESCRIPTION, note.getDescription());
        values.put(COLUMN_NOTE_MODIFY, note.getModify());
        values.put(COLUMN_NOTE_CREATED, Utils.getDateTime());

        // Inserting Row
        db.insert(TABLE_NOTE, null, values);
        db.close();
    }

    // get All Notes
    public List<Notes> getAllNotes() {
        String[] columns = {
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TITLE,
                COLUMN_NOTE_DESCRIPTION,
                COLUMN_NOTE_MODIFY,
                COLUMN_NOTE_CREATED
        };

        String sortOrder =
                COLUMN_NOTE_ID + " ASC";

        List<Notes> notelist = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_NOTE, //Table to query
                columns,    //columns to return
                null,                  //columns for the WHERE clause
                null,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        if (cursor.moveToFirst()) {
            do {
                Notes note = new Notes();
                note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DESCRIPTION)));
                note.setModify(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_MODIFY)));
                note.setCreated(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CREATED)));
                // Adding user record to list
                notelist.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return notelist;
    }

    public List<Notes> getCreatedNotes() {
        String[] columns = {
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TITLE,
                COLUMN_NOTE_DESCRIPTION,
                COLUMN_NOTE_MODIFY,
                COLUMN_NOTE_CREATED
        };

        String sortOrder =
                COLUMN_NOTE_CREATED + " ASC";

        List<Notes> notelist = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_NOTE, //Table to query
                columns,    //columns to return
                null,                  //columns for the WHERE clause
                null,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        if (cursor.moveToFirst()) {
            do {
                Notes note = new Notes();
                note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DESCRIPTION)));
                note.setModify(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_MODIFY)));
                note.setCreated(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CREATED)));
                // Adding user record to list
                notelist.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return notelist;
    }

    // edit Note
    public void editNote(Notes note, String oldTitle) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_DESCRIPTION, note.getDescription());
        values.put(COLUMN_NOTE_MODIFY, note.getModify());

        db.update(
                TABLE_NOTE,
                values,
                COLUMN_NOTE_TITLE + " = ?",
                new String[]{oldTitle}
        );

        db.close();
    }


    // delete notes
    public void deleteNote(Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
