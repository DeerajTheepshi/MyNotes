package com.example.android.mynotes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.android.mynotes.data.contractClass.tasksTable;

public class provider extends ContentProvider {

    private static final int ENTIRE_TABLE = 50;
    private static final int SINGLE_TABLE = 51;
    private static final UriMatcher matchUris = new UriMatcher(UriMatcher.NO_MATCH);
    private helperClass dbhelper;

    static{
        matchUris.addURI(contractClass.CONTENT_AUTHORITY,contractClass.PATH,ENTIRE_TABLE);
        matchUris.addURI(contractClass.CONTENT_AUTHORITY,contractClass.PATH+"/#",SINGLE_TABLE);
    }

    @Override
    public boolean onCreate() {
        dbhelper = new helperClass(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor=null;
        if(matchUris.match(uri)==ENTIRE_TABLE){
            Log.v("kaala","vantenu sollu1");
            cursor = db.query(tasksTable.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
            Log.v("kaala","vantenu sollu2");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        Log.v("kaala","vantenu sollu");
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long id=0;
        if(matchUris.match(uri)==ENTIRE_TABLE){
            if(values.getAsString(tasksTable.TASK).isEmpty() || values.getAsString(tasksTable.TIME).isEmpty()){
                id = -1;
            }
            else
                id = db.insert(tasksTable.TABLE_NAME,null,values);
        }
        if(id!=-1) {
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        }
        else
            Toast.makeText(getContext(), "Make Sure You Enter All Information",Toast.LENGTH_SHORT).show();
            return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        if(matchUris.match(uri)==SINGLE_TABLE){
            selection = tasksTable._ID + "=?";
            selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
            return db.delete(tasksTable.TABLE_NAME,selection,selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
