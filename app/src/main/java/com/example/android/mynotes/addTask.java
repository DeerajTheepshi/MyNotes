package com.example.android.mynotes;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.mynotes.data.contractClass.tasksTable;
import java.util.Calendar;

public class addTask extends AppCompatActivity {
    EditText taskEntry,timeEntry;
    String task_details, time_details;
    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        taskEntry = (EditText)findViewById(R.id.task);
        timeEntry= (EditText) findViewById(R.id.time);

        final TimePickerDialog.OnTimeSetListener listenTime = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formatHour = ""+hourOfDay,formatMinute = ""+minute;
                if(hourOfDay<10)
                    formatHour = "0"+hourOfDay;
                if(minute<10)
                    formatMinute = "0"+minute;
                timeEntry.setText(formatHour+":"+formatMinute);

            }

        };

        timeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("kaala","im here");
                 new TimePickerDialog(addTask.this, listenTime,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),
                        android.text.format.DateFormat.is24HourFormat(getApplicationContext())).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                save();
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(){
        task_details = taskEntry.getText().toString().trim();
        time_details = timeEntry.getText().toString().trim();
        ContentValues val = new ContentValues();
        val.put(tasksTable.TASK,task_details);
        val.put(tasksTable.TIME,time_details);
        Uri uri =getContentResolver().insert(tasksTable.CONTENT_URI,val);
        if(uri != null)
            finish();
    }
}
