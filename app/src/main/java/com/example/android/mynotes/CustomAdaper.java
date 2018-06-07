package com.example.android.mynotes;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.mynotes.data.contractClass.tasksTable;

public class CustomAdaper extends CursorAdapter {

    public CustomAdaper(Cursor cursor, Context context){
        super(context,cursor,0);

    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView listTask = (TextView) view.findViewById(R.id.displayTask);
        TextView listTime = (TextView) view.findViewById(R.id.displayTime);

        listTask.setText(cursor.getString(cursor.getColumnIndex(tasksTable.TASK)));
        listTime.setText(cursor.getString(cursor.getColumnIndex(tasksTable.TIME)));
    }
}
