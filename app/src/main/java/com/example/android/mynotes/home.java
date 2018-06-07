package com.example.android.mynotes;

import android.app.AlertDialog;
import android.app.LoaderManager;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import com.example.android.mynotes.data.contractClass.tasksTable;

public class home extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    FloatingActionButton newEntry ;
    ListView homelist ;
    private static final int LOAD_DATA = 0;
    CustomAdaper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        newEntry = (FloatingActionButton) findViewById(R.id.addNew);
        homelist = (ListView) findViewById(R.id.taskList);


        adapter = new CustomAdaper(null,this);
        homelist.setAdapter(adapter);
        homelist.setEmptyView(findViewById(R.id.emptyView));

        newEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this,addTask.class );
                startActivity(intent);
            }
        });

        homelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long idFinal = id;
                new AlertDialog.Builder(home.this).setTitle("DELETE").setMessage("Are you sure to delete? ")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri deluri = ContentUris.withAppendedId(tasksTable.CONTENT_URI,idFinal);
                                getContentResolver().delete(deluri,null,null);
                                getContentResolver().notifyChange(deluri,null);
                            }
                        }).create().show();
                return true;
            }
        });

        getLoaderManager().initLoader(LOAD_DATA,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOAD_DATA){
            String[] projection = {tasksTable._ID,tasksTable.TASK, tasksTable.TIME};
            return new CursorLoader(this,tasksTable.CONTENT_URI,projection,null,null,null);
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
