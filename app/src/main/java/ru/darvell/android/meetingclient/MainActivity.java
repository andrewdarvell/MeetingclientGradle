package ru.darvell.android.meetingclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.adapters.ScheduleAdapter;
import ru.darvell.android.meetingclient.api.MeetingApi;
import ru.darvell.android.meetingclient.api.Schedule;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity {


	ArrayList<Schedule> schedulesData;
    ScheduleAdapter scheduleAdapter;
    ListView schedulesList;
    ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        schedulesData = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        scheduleAdapter = new ScheduleAdapter(this, schedulesData);
        schedulesList = (ListView) findViewById(R.id.schedulesList);
        schedulesList.setAdapter(scheduleAdapter);
        getAllSchedulesUser();

	}

    @Override
    protected void onStart() {
        super.onStart();
        updateDataSource();
    }

    synchronized void updateDataSource(){
        scheduleAdapter.notifyDataSetChanged();
    }

    void getAllSchedulesUser(){
        setVisiblePB(true);
        MyTask myTask = new MyTask();
        myTask.execute(MeetingApi.prepareGetAllSchedules());
    }

    void setVisiblePB(boolean visible){
        if (visible){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    //Класс посылает запрос в другом потоке. Не GUI
    class MyTask extends AsyncTask<Map<String,String>, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(Map<String, String>... params) {
            try {
                Log.i("debug", "Send Post!!!");
                return MeetingApi.sendPost(params[0]);

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            setVisiblePB(false);
            if (response == null) {
                Log.i("debug", "Error!!!");
            }else {
                try {
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
                    Log.e("error", e.toString());
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }

}
