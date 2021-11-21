package com.task.note.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.task.note.R;


public class Preference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Preference(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.sh_preference_file), context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setViewList(int view) {
        editor.putInt(String.valueOf(R.string.sh_preference_view), view);
        editor.commit();
    }

    public int getViewList() {
        return sharedPreferences.getInt(String.valueOf(R.string.sh_preference_view), 0);
    }

    public void setView(Boolean view) {
        editor.putBoolean(String.valueOf(R.string.sh_preference_linear), view);
        editor.commit();
    }

    public boolean getView() {
        return sharedPreferences.getBoolean(String.valueOf(R.string.sh_preference_linear), false);
    }

    public void setDescription(Boolean description) {
        editor.putBoolean(String.valueOf(R.string.sh_preference_description), description);
        editor.commit();
    }

    public boolean getDescription() {
        return sharedPreferences.getBoolean(String.valueOf(R.string.sh_preference_description), false);
    }

    public void setOpen(Boolean open) {
        editor.putBoolean(String.valueOf(R.string.sh_preference_open), open);
        editor.commit();
    }

    public boolean getOpen() {
        return sharedPreferences.getBoolean(String.valueOf(R.string.sh_preference_open), false);
    }

    public void clearData(String remove) {
//        editor.clear();
        editor.remove(remove);
    }
}
