package com.example.android.mynotes.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class contractClass {

    public contractClass(){}

    public final static String CONTENT_AUTHORITY = "com.example.android.mynotes";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public final static String PATH = "tasks";

    public final static class tasksTable implements BaseColumns{
        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH);
        public final static String TABLE_NAME = "tasks";
        public final static String _ID = BaseColumns._ID;
        public final static String TASK = "task";
        public final static String DATE = "date";
        public final static String TIME = "time";
    }


}
