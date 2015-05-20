package ru.darvell.android.meetingclient.database;

import android.content.Context;

public class DBFabric {
    public static DBWorker getDBWorker(Context context){
        return new DBWorkerImpl(context);
    }
}
