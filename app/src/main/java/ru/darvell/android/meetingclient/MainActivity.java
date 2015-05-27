package ru.darvell.android.meetingclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.adapters.ScheduleAdapter;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.MeetingApi;
import ru.darvell.android.meetingclient.api.Requester;
import ru.darvell.android.meetingclient.api.Schedule;
import ru.darvell.android.meetingclient.database.DBFabric;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends ActionBarActivity {


	ArrayList<Schedule> schedulesData;
    ScheduleAdapter scheduleAdapter;
    ListView schedulesList;
//    ProgressBar progressBar;
    MenuItem miActionProgressItem;
    BroadcastReceiver br;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    public final static int ACT_ID = 3;
    final String LOG_TAG = "meeting_main";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        schedulesData = new ArrayList<>();
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        scheduleAdapter = new ScheduleAdapter(this, schedulesData);
        schedulesList = (ListView) findViewById(R.id.schedulesList);
        schedulesList.setAdapter(scheduleAdapter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//        progressBar = (ProgressBar) findViewById(R.id.miActionProgress);

        String[] menuStr = {"111", "222"};
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, menuStr));


        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("actId", -1) == ACT_ID){
                    Log.d(LOG_TAG, "gotRequest");
                    setVisiblePB(false);
                    Map<String,String> map = DBFabric.getDBWorker(context).getRequests(ACT_ID);
                    int type = Integer.parseInt(map.get("type"));
                    switch (type){
                        case MeetingApi.ALL_USER_SCHEDULES: updateSchedules(map.get("result"));
                            break;
                    }
                    Log.d(LOG_TAG, map.get("result"));
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(Conf.BROADCAST_ACTION);
        registerReceiver(br, intFilt);

//        getAllSchedulesUser();

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        setVisiblePB(false);
        getAllSchedulesUser();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getAllSchedulesUser();
    }



    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }

    synchronized void updateDataSource(){
        scheduleAdapter.notifyDataSetChanged();
    }

    void getAllSchedulesUser(){
        new Requester().doGetAllUserSchedules(this, ACT_ID);
        setVisiblePB(true);

    }

    void updateSchedules(String jsonStr){
        try{
            JSONObject response = new JSONObject(jsonStr);
            if (response.getInt("exitCode") == 0) {
                JSONArray schedulesJson = response.getJSONArray("schedules");
                for (int i=0; i < schedulesJson.length(); i++){
                    JSONObject scheduleJson = (JSONObject) schedulesJson.get(i);
                    Schedule schedule = new Schedule(scheduleJson);
                    schedulesData.add(schedule);
                    updateDataSource();
                }
            }

        }catch (Exception e){
            Log.d(LOG_TAG, e.toString());
        }
    }

    void setVisiblePB(boolean visible){
        if (visible){
            miActionProgressItem.setVisible(true);//progressBar.setVisibility(View.VISIBLE);
        }else{
            miActionProgressItem.setVisible(false);//progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
