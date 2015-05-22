package ru.darvell.android.meetingclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.AuthActivity;
import ru.darvell.android.meetingclient.api.MeetingApi;
import ru.darvell.android.meetingclient.database.DBFabric;

import java.util.HashMap;
import java.util.Map;

public class MeetingService extends Service{

    final String LOG_TAG = "meeting_service";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand.");
        Map<String, String> params = new HashMap<>();
        switch (intent.getIntExtra("method", -1)){
            case MeetingApi.LOGIN:new SenderRequest(MeetingApi.prepareLogin(intent), this, intent.getIntExtra("method", -1), 1);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

//    Map<String, String> =

    class SenderRequest extends Thread{

        Map<String, String> parameters;
        Service service;
        int type;
        int act_id;

        SenderRequest(Map<String, String> parameters, Service service, int type, int act_id){
            this.parameters = parameters;
            this.service = service;
            this.type = type;
            this.act_id = act_id;
            Log.d("THREAD", "Start");
            start();
        }

        @Override
        public void run() {
            JSONObject s = MeetingApi.sendPost(parameters);
            DBFabric.getDBWorker(service).putRequest(s.toString(), type, act_id);
            Log.d("THREAD", s.toString());
            Intent intent = new Intent(AuthActivity.BROADCAST_ACTION);
            intent.putExtra("actId", 1);
            service.sendBroadcast(intent);
//            service.sendBroadcast();
            service.stopSelf();
        }
    }

}
