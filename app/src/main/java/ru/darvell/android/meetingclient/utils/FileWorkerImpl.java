package ru.darvell.android.meetingclient.utils;

import android.content.Context;
import android.util.Log;
import ru.darvell.android.meetingclient.api.Conf;

import java.io.*;



public class FileWorkerImpl implements FileWorker {

    Context context;
    private static final String FILENAME = "config";
    private final String LOG_TAG = "meeting_file";

    public FileWorkerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void storeConfig() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(context.openFileOutput(FILENAME, context.MODE_PRIVATE)));
            bw.write("userId:"+Conf.userId+"\n");
            bw.write("pass:"+Conf.pass+"\n");
            bw.write("login:"+Conf.login+"\n");
            bw.write("sessKey:"+Conf.sessKey+"\n");
            bw.write("email:"+Conf.email+"\n");
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateConfig() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                switch (str.split(":")[0]){
                    case "userId":Conf.userId = Integer.parseInt(str.split(":")[1]);
                        break;
                    case "pass":Conf.pass = str.split(":")[1];
                        break;
                    case "login":Conf.login = str.split(":")[1];
                        break;
                    case "sessKey":Conf.sessKey = str.split(":")[1];
                        break;
                    case "email":Conf.email = str.split(":")[1];
                        break;
                }
            }
            br.close();
            Log.d(LOG_TAG, Conf.pass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
