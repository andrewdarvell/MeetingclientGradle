package ru.darvell.android.meetingclient.utils;

import android.content.Context;
import android.util.Log;

import java.io.*;



public class FileWorkerImpl implements FileWorker {

    Context context;
    private static final String FILENAME = "config";
    private final String LOG_TAG = "meeting_file";

    public FileWorkerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void writeString(String str) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(context.openFileOutput(FILENAME, context.MODE_PRIVATE)));
            bw.write(str);
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readString() {
        String str = "11";
        Log.d(LOG_TAG, "Start reading file");
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(FILENAME)));
            // читаем содержимое
            str = br.readLine();
            if (str == null){
                Log.d(LOG_TAG,"Null string");
            }
            Log.d(LOG_TAG, str);
            br.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }

        Log.d(LOG_TAG, str);
        return str;
    }


}
