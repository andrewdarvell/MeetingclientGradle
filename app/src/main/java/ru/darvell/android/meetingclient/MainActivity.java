package ru.darvell.android.meetingclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.adapters.ScheduleAdapter;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.MeetingApi;
import ru.darvell.android.meetingclient.api.Requester;
import ru.darvell.android.meetingclient.api.entitys.Schedule;
import ru.darvell.android.meetingclient.database.DBFabric;
import ru.darvell.android.meetingclient.utils.FileWorkerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    public final static int ACT_ID = 3;
    public final static int REQUEST_IMAGE = 5;
    final String LOG_TAG = "meeting_main";

	ArrayList<Schedule> schedulesData;
    ScheduleAdapter scheduleAdapter;
    ListView schedulesList;
    MenuItem miActionProgressItem;
    BroadcastReceiver br;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ImageView imageAvatar;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        schedulesData = new ArrayList<>();

        scheduleAdapter = new ScheduleAdapter(this, schedulesData);
        schedulesList = (ListView) findViewById(R.id.schedulesList);
        schedulesList.setAdapter(scheduleAdapter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        imageAvatar = (ImageView) findViewById(R.id.imageViewMain);

        String[] menuStr = {"111", "222"};
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, menuStr));

        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(REQUEST_IMAGE);
            }
        });

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
    protected void onPause() {
        FileWorkerFactory.getWorker(this).storeConfig();
        super.onPause();
    }

    @Override
    protected void onStart() {
        FileWorkerFactory.getWorker(this).updateConfig();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        FileWorkerFactory.getWorker(this).storeConfig();
        super.onDestroy();
    }

    public void openGallery(int req_code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), req_code);
    }

    public void updateDataSource(){
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

    void updateAvatar(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (mediaStorageDir.exists()){

        }

    }

    void setVisiblePB(boolean visible){
        if (visible){
            miActionProgressItem.setVisible(true);//progressBar.setVisibility(View.VISIBLE);
        }else{
            miActionProgressItem.setVisible(false);//progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (requestCode == REQUEST_IMAGE){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    Bitmap dstBmp;
                    if (bitmap.getWidth() >= bitmap.getHeight()){
                        dstBmp = Bitmap.createBitmap(
                                bitmap,
                                bitmap.getWidth()/2 - bitmap.getHeight()/2,
                                0,
                                bitmap.getHeight(),
                                bitmap.getHeight()
                        );
                    }else{
                        dstBmp = Bitmap.createBitmap(
                                bitmap,
                                0,
                                bitmap.getHeight()/2 - bitmap.getWidth()/2,
                                bitmap.getWidth(),
                                bitmap.getWidth()
                        );
                    }

                    imageAvatar.setImageBitmap(dstBmp);
//                    FileOutputStream fo = openFileOutput("avatar.jpg", this.MODE_PRIVATE);

                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                            + "/Android/data/"
                            + getApplicationContext().getPackageName()
                            + "/Files/avatar.jpg");
                    FileOutputStream fo = new FileOutputStream(mediaStorageDir);
                    dstBmp.compress(Bitmap.CompressFormat.JPEG, 50, fo);

                    fo.flush();
                    fo.close();

                } catch (IOException e) {
                    Log.d(LOG_TAG, e.toString());
                }
            }
        }
    }
}
