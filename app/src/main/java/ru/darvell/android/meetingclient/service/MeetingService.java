package ru.darvell.android.meetingclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.MeetingApi;
import ru.darvell.android.meetingclient.database.DBFabric;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MeetingService extends Service{

    final String LOG_TAG = "meeting_service";
    Set<Integer> queue = new HashSet<>();

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
        switch (intent.getIntExtra("method", -1)){
            case MeetingApi.LOGIN : startRequest(intent);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void startRequest(Intent intent){
        new SenderRequest(MeetingApi.prepareLogin(intent), this, intent.getIntExtra("method", -1), intent.getIntExtra("actId", -1));
    }

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
            Intent intent = new Intent(Conf.BROADCAST_ACTION);
            intent.putExtra("actId", act_id);
            queue.remove(new Integer(type));
            service.sendBroadcast(intent);
            service.stopSelf();
        }
    }

}
