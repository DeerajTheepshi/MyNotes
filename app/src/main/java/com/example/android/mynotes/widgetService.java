package com.example.android.mynotes;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class widgetService extends RemoteViewsService {
    //SET ADAPTER TO SERVICE
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new remoteViewsFactory(this.getApplicationContext(), intent);
    }
}
