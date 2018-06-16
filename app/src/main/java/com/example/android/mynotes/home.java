package com.example.android.mynotes;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import com.example.android.mynotes.data.contractClass.tasksTable;

public class home extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    FloatingActionButton newEntry ;
    ListView homelist ;
    private static final int LOAD_DATA = 0,LOAD_DATA_PRIORITY=1;
    CustomAdaper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        newEntry = (FloatingActionButton) findViewById(R.id.addNew);
        homelist = (ListView) findViewById(R.id.taskList);


        adapter = new CustomAdaper(null,this);                                       //CREATE LISTVIEW, ATTACH ADAPTER AND EMPTY VIEWS TO IT
        homelist.setAdapter(adapter);
        homelist.setEmptyView(findViewById(R.id.emptyView));

        newEntry.setOnClickListener(new View.OnClickListener() {                                    //FLOATING BUTTON ACTION LISTENER
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this,addTask.class );
                startActivity(intent);
            }
        });

                homelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {     //DELETE OPTION FOR LONG PRESS EVENT
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long idFinal = id;
                new AlertDialog.Builder(home.this).setTitle("DELETE").setMessage("Are you sure to delete? ") //BRING UP ALERT DIALOG
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri deluri = ContentUris.withAppendedId(tasksTable.CONTENT_URI,idFinal);
                                getContentResolver().delete(deluri,null,null);
                                getContentResolver().notifyChange(deluri,null);                         //NOTIFY DATABASE PROVIDER LISTENERS
                                AppWidgetManager mgr = AppWidgetManager.getInstance(home.this);                 //NOTIFY WIDGET BROADCAST LISTENERS
                                ComponentName cn = new ComponentName(home.this, NotesWidget.class);
                                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.appwidget_list);
                            }
                        }).create().show();
                return true;
            }
        });

        String data = null;                                                                         //FOLLOWNG OPERATES ON SORTING OF LIST BASED ON VARIOUS OPTIONS
        if(getIntent().getExtras()!=null)
            data = getIntent().getExtras().getString("menuControl");
        if(data!=null) {

            switch (data) {
                case "priority":
                    setTitle("MyNotes - Priority");
                    getLoaderManager().initLoader(LOAD_DATA_PRIORITY, null, this);
                    getLoaderManager().restartLoader(LOAD_DATA_PRIORITY, null, this);
                    break;
                case "date":
                    getLoaderManager().restartLoader(LOAD_DATA, null, this);
                    getLoaderManager().initLoader(LOAD_DATA,null,this);
                    break;
            }
        }
        else {
            getLoaderManager().restartLoader(LOAD_DATA, null, this);
            getLoaderManager().initLoader(LOAD_DATA,null,this);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                 //LOAD OPTIONS MENU
        getMenuInflater().inflate(R.menu.sort_choice,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                           //SET ON CLICK LISTENERS FOR MENU ITEMS
        int itemID = item.getItemId();
        Intent intent = new Intent(this,home.class);
        switch (itemID){
            case R.id.sortTime:
                intent.putExtra("menuControl","date");
                startActivity(intent);
                break;
            case R.id.sortPriority:
                intent.putExtra("menuControl","priority");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {                                     //CURSOR LOADER ABSTRACT METHOD IMPLEMENTATIONS
        if(id == LOAD_DATA){
            String[] projection = {tasksTable._ID,tasksTable.TASK, tasksTable.TIME,tasksTable.DATE,tasksTable.PRIORITY};
            return new CursorLoader(this,tasksTable.CONTENT_URI,projection,null,null,
                    tasksTable.DATE + " ASC, " + tasksTable.TIME + " ASC");
        }
        if(id == LOAD_DATA_PRIORITY){
            String[] projection = {tasksTable._ID,tasksTable.TASK, tasksTable.TIME,tasksTable.DATE,tasksTable.PRIORITY};
            return new CursorLoader(this,tasksTable.CONTENT_URI,projection,null,null,
                    tasksTable.PRIORITY + " ASC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
