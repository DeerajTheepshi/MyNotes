package com.example.android.mynotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.android.mynotes.data.contractClass.tasksTable;

import java.util.Calendar;

public class remoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;

    public remoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }
    private void initCursor(){
        if (mCursor != null) {
            mCursor.close();
        }


        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = tasksTable.CONTENT_URI;
        String[] projection = {tasksTable._ID,tasksTable.TASK,tasksTable.TIME};
        String selection = tasksTable.DATE + "=?";
        String[] selctionArgs = new String[]{getDate()};
        Log.v("mama",getDate());
        mCursor = mContext.getContentResolver().query(uri,
                projection,
                selection,
                selctionArgs,
                tasksTable.TIME+ " ASC");
        Log.v("Kaala","InitCursor()");
        Binder.restoreCallingIdentity(identityToken);
    }

    private String getDate(){
        Calendar cal = Calendar.getInstance();
        String yr=cal.get(Calendar.YEAR)+"", monthformat=cal.get(Calendar.MONTH)+"", dayformat=cal.get(Calendar.DAY_OF_MONTH)+"";
        if(cal.get(Calendar.MONTH)<10)
            monthformat = "0" + cal.get(Calendar.MONTH);
        if(cal.get(Calendar.DAY_OF_MONTH)<10)
            dayformat = "0" + cal.get(Calendar.DAY_OF_MONTH);
        return yr+"/"+monthformat+"/"+dayformat;
    }
    @Override
    public void onDataSetChanged() {
        Log.v("Kaala","OnDataSetChanged");
        initCursor();

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return  mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {

            return null;
        }
        Log.v("Kaala","getView");
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        rv.setTextViewText(R.id.text1,"* \t"+mCursor.getString(1));
        rv.setTextViewText(R.id.time1,"  \t "+mCursor.getString(2));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return  position;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }
}
