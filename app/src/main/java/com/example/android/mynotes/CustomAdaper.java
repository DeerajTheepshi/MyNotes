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
        String[] arr = context.getResources().getStringArray(R.array.importanceList);
        TextView listTask = (TextView) view.findViewById(R.id.displayTask);
        TextView listTime = (TextView) view.findViewById(R.id.displayTime);
        TextView listDate = (TextView) view.findViewById(R.id.displayDate);
        TextView listPriority = (TextView) view.findViewById(R.id.displayPriority);
        listTask.setText(cursor.getString(cursor.getColumnIndex(tasksTable.TASK)));
        listTime.setText(cursor.getString(cursor.getColumnIndex(tasksTable.TIME)));
        listDate.setText(dateReFormat(cursor.getString(cursor.getColumnIndex(tasksTable.DATE))));
        listPriority.setText(arr[cursor.getInt(cursor.getColumnIndex(tasksTable.PRIORITY))]);
    }
    private String dateReFormat(String date){
        String day = date.substring(8);
        String month = date.substring(5,7);
        String year = date.substring(0,4);
        return day+"/"+month+"/"+year;
    }
}
