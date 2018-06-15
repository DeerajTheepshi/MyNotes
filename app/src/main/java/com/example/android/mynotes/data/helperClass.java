package com.example.android.mynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.mynotes.data.contractClass.tasksTable;

public class helperClass extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "taskManager.db";
    public final static int DATABASE_VERSION = 1;

    public helperClass(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String QUERY = "CREATE TABLE " + tasksTable.TABLE_NAME + " ("
                + tasksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + tasksTable.TASK + " TEXT NOT NULL, "
                + tasksTable.TIME + " TEXT NOT NULL, "
                + tasksTable.DATE + " DATE NOT NULL, "
                + tasksTable.PRIORITY +" INTEGER NOT NULL );";

        db.execSQL(QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
