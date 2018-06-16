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

//CLASS THAT BINDS CURSOR TO LISTVIEW

public class remoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;

    public remoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }
    private void initCursor(){                                                                      //UPDATES OR INITATES A CURSOR
        if (mCursor != null) {
            mCursor.close();
        }


        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = tasksTable.CONTENT_URI;
        String[] projection = {tasksTable._ID,tasksTable.TASK,tasksTable.TIME};
        String selection = tasksTable.DATE + "=?";
        String[] selctionArgs = new String[]{getDate()};                                            //MATCHES DATE OF CURRENT DAY TO SORT OUT DATA
        mCursor = mContext.getContentResolver().query(uri,
                projection,
                selection,
                selctionArgs,
                tasksTable.TIME+ " ASC");
        Binder.restoreCallingIdentity(identityToken);
    }

    private String getDate(){                                                                       //DATE GETTER
        Calendar cal = Calendar.getInstance();
        String yr=cal.get(Calendar.YEAR)+"", monthformat=cal.get(Calendar.MONTH)+"", dayformat=cal.get(Calendar.DAY_OF_MONTH)+"";
        if(cal.get(Calendar.MONTH)<10)
            monthformat = "0" + cal.get(Calendar.MONTH);
        if(cal.get(Calendar.DAY_OF_MONTH)<10)
            dayformat = "0" + cal.get(Calendar.DAY_OF_MONTH);
        return yr+"/"+monthformat+"/"+dayformat;
    }
    @Override
    public void onDataSetChanged() {                                                                //CALLED WHENEVER DATA SET IS CHANGED, INITIATES A NEW CURSOR
        initCursor();

    }

    @Override
    public void onDestroy() {                                                                       //WHEN WIDGET IS DESTROYED
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {                                                                         //ABSTRACT METHOD IMPLEMENTATIONS
        return  mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {                                                    //BINDS VIEWS TO CURSOR DATA

        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {

            return null;
        }
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
