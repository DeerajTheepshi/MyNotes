package com.example.android.mynotes;

        import android.app.DatePickerDialog;
        import android.app.TimePickerDialog;
        import android.appwidget.AppWidgetManager;
        import android.content.ComponentName;
        import android.content.ContentValues;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import com.example.android.mynotes.data.contractClass.tasksTable;
        import java.util.Calendar;

public class addTask extends AppCompatActivity {

    EditText taskEntry;                                                                             //ALL GLOBAL VARIABLES DECLARATIONS
    Spinner priority;
    TextView timeEntry,dateEntry;
    String task_details, time_details,dateSqlFormat;
    int priorityVal;
    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        taskEntry = (EditText)findViewById(R.id.task);
        timeEntry= (TextView) findViewById(R.id.time);
        dateEntry = (TextView) findViewById(R.id.date);
        priority = (Spinner) findViewById(R.id.importanceSpinner);

        final TimePickerDialog.OnTimeSetListener listenTime = new TimePickerDialog.OnTimeSetListener(){ //INITIATE A TIME PICKER LISTENER
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

        timeEntry.setOnClickListener(new View.OnClickListener() {                                   //INITATE A TIME PICKER AND SET LISTENER
            @Override
            public void onClick(View v) {
                new TimePickerDialog(addTask.this, listenTime,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),
                        android.text.format.DateFormat.is24HourFormat(getApplicationContext())).show();
            }
        });
        final DatePickerDialog.OnDateSetListener listenDate = new DatePickerDialog.OnDateSetListener() { //INITIATE A DATE PICKER LISTENER
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String Dayformat = ""+dayOfMonth, monthFormat = ""+month;
                if(dayOfMonth<10)
                    Dayformat = "0"+dayOfMonth;
                if(month<10)
                    monthFormat = "0"+month;
                dateSqlFormat = year+"/"+monthFormat+"/"+Dayformat;
                dateEntry.setText(Dayformat+"/"+monthFormat+"/"+year);
            }
        };
        dateEntry.setOnClickListener(new View.OnClickListener() {                                   //INITIATE DATE PICKER AND ATTACH LISTENER
            @Override
            public void onClick(View v) {
                new DatePickerDialog(addTask.this,listenDate,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        initializeSpinner();                                                                        //SET UP ALL THE SPINNER

    }

    public void initializeSpinner(){
        final ArrayAdapter priorityAdapter = ArrayAdapter.createFromResource                        //CREATE ARRAY FROM RESOURCE AND BIND IT
                (this, R.array.importanceList,android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        priority.setAdapter(priorityAdapter);


        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {               //SET ON ITEM SELECTED LISTENER
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                switch (selected){
                    case "! ! !":
                        priorityVal = 0;
                        break;
                    case "! !":
                        priorityVal = 1;
                        break;
                    case "!":
                        priorityVal = 2;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                 //CREATE THE OPTIONS MENU
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                           //PERFORM THE SAVE ACTION WHEN SELECTED
        switch (item.getItemId()){
            case R.id.save:
                save();
        }
        return super.onOptionsItemSelected(item);
    }



    public void save(){                                                                             //CREATE CONTENT VALUES AND UPLOAD IT TO DATABASE
        task_details = taskEntry.getText().toString().trim();
        time_details = timeEntry.getText().toString().trim();
        ContentValues val = new ContentValues();
        val.put(tasksTable.TASK,task_details);
        val.put(tasksTable.TIME,time_details);
        val.put(tasksTable.DATE,dateSqlFormat);
        val.put(tasksTable.PRIORITY,priorityVal);
        Uri uri =getContentResolver().insert(tasksTable.CONTENT_URI,val);
        if(uri != null) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(this);                              //NOTIFY WIDGET BROADCAST LISTENERS ABOUT DATA SET CHANGED,
            ComponentName cn = new ComponentName(this, NotesWidget.class);                     // CALLS ON DATASETCHANGED METHOD OF WIDGET PROVIDER CLASS
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.appwidget_list);
            finish();                                                                               //CLOSE ACTIVITY , MOVE TO PARENT ACTIVITY
        }

    }
}
