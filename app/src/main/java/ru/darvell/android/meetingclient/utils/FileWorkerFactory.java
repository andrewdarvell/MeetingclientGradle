package ru.darvell.android.meetingclient.utils;

import android.content.Context;

public class FileWorkerFactory {

    public static FileWorker getWorker(Context context){
        return new FileWorkerImpl(context);
    }
}
