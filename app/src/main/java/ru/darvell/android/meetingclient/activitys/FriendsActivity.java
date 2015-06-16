package ru.darvell.android.meetingclient.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.R;
import ru.darvell.android.meetingclient.adapters.FriendRequestsAdapter;
import ru.darvell.android.meetingclient.adapters.MembersAdapter;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.MeetingApi;
import ru.darvell.android.meetingclient.api.Requester;
import ru.darvell.android.meetingclient.api.entitys.EntityFactory;
import ru.darvell.android.meetingclient.api.entitys.FriendRequest;
import ru.darvell.android.meetingclient.api.entitys.MainUser;
import ru.darvell.android.meetingclient.api.entitys.Member;
import ru.darvell.android.meetingclient.database.DBFabric;

import java.util.ArrayList;
import java.util.Map;

public class FriendsActivity extends ActionBarActivity {

    public final static int ACT_ID = 4;
    final String LOG_TAG = "meeting_friends";
    BroadcastReceiver br;
    MainUser mainUser = new MainUser();
    ListView mDrawerList;

    ListView friendList;
    ArrayList<Member> membersData;
    MembersAdapter membersAdapter;

    ListView toList;
    ArrayList<FriendRequest> toMembersData;
    FriendRequestsAdapter toMembersAdapter;

    ListView fromList;
    ArrayList<FriendRequest> fromMembersData;
    FriendRequestsAdapter fromMembersAdapter;

    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        mainUser.loadMainUser(this);

        membersData = new ArrayList<>();
        friendList = (ListView) findViewById(R.id.friend_list);
        membersAdapter = new MembersAdapter(this, membersData);
        friendList.setAdapter(membersAdapter);

        toMembersData = new ArrayList<>();
        toList = (ListView) findViewById(R.id.requestToList);
        toMembersAdapter = new FriendRequestsAdapter(this, toMembersData);
        toList.setAdapter(toMembersAdapter);

        fromMembersData = new ArrayList<>();
        fromList = (ListView) findViewById(R.id.requestFromList);
        fromMembersAdapter = new FriendRequestsAdapter(this, fromMembersData);
        fromList.setAdapter(fromMembersAdapter);

        String[] menuStr = {"111", "Друзья"};
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, menuStr));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("actId", -1) == ACT_ID){
                    Log.d(LOG_TAG, "gotRequest");
                    setVisiblePB(false);
                    Map<String,String> map = DBFabric.getDBWorker(context).getRequests(ACT_ID);
                    int type = Integer.parseInt(map.get("type"));
                    switch (type){
                        case MeetingApi.FRIENDS_ALL: updateMembers(map.get("result"));
                            break;
                        case MeetingApi.FRIENDS_REQ_FROM: updateReqFromUser(map.get("result"));
                            break;
                        case MeetingApi.FRIENDS_REQ_TO: updateReqToUser(map.get("result"));
                            break;
                    }
                    Log.d(LOG_TAG, map.get("result"));
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(Conf.BROADCAST_ACTION);
        registerReceiver(br, intFilt);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Друзья");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Исходящие");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Заявки");
        tabSpec.setContent(R.id.tab3);
        tabHost.addTab(tabSpec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                Log.d(LOG_TAG, "Change tab");
                Log.d(LOG_TAG, s);
                switch (s){
                    case "tag1":getFriends();
                        break;
                    case "tag2":getRequestFrom();
                        break;
                    case "tag3":getRequestTo();
                        break;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        mainUser.saveMainUser(this);
        super.onPause();
    }

    @Override
    protected void onStart() {
        mainUser.loadMainUser(this);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        mainUser.saveMainUser(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mainUser.loadMainUser(this);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        getFriends();
        return super.onPrepareOptionsMenu(menu);
    }

    void getFriends(){
        setVisiblePB(true);
        new Requester().doGetFriends(this, ACT_ID, mainUser.getUserId());
    }

    void getRequestTo(){
        setVisiblePB(true);
        new Requester().doGetRequestToMe(this, ACT_ID, mainUser.getUserId());
    }

    void getRequestFrom(){
        setVisiblePB(true);
        new Requester().doGetRequestFromMe(this, ACT_ID, mainUser.getUserId());
    }

    void updateDataSource(){
        membersAdapter.notifyDataSetChanged();
    }

    /**
     * Разбор списка друзей
     * @param jsonStr
     */
    void updateMembers(String jsonStr){
        try{
            JSONObject membersRespJson = new JSONObject(jsonStr);
            if (membersRespJson.getInt("exitCode") == 0){
                JSONArray friendsJson = membersRespJson.getJSONArray("friends");
                membersData.clear();
                for (int i=0; i<friendsJson.length(); i++){
                    Member member = new Member((JSONObject) friendsJson.get(i));
                    membersData.add(member);
                    updateDataSource();
                }
            }
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString());
        }
    }

    /**
     * Разбор запросов к пользователю
     * @param jsonStr
     */
    void updateReqToUser(String jsonStr){
        try{
            JSONObject membersRespJson = new JSONObject(jsonStr);
            if (membersRespJson.getInt("exitCode") == 0){
                JSONArray friendsReqJson = membersRespJson.getJSONArray("requests");
                toMembersData.clear();
                for (int i=0; i<friendsReqJson.length(); i++){
                    FriendRequest friendRequest = EntityFactory.getRequestTo((JSONObject) friendsReqJson.get(i));
                    toMembersData.add(friendRequest);
                    toMembersAdapter.notifyDataSetChanged();
                }
            }
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString());
        }
    }

    /**
     * Разбор запросов ОТ пользователя
     * @param jsonStr
     */
    void updateReqFromUser(String jsonStr){
        try{
            JSONObject membersRespJson = new JSONObject(jsonStr);
            if (membersRespJson.getInt("exitCode") == 0){
                JSONArray friendsReqJson = membersRespJson.getJSONArray("requests");
                fromMembersData.clear();
                for (int i=0; i<friendsReqJson.length(); i++){
                    FriendRequest friendRequest = EntityFactory.getRequestFrom((JSONObject) friendsReqJson.get(i));
                    fromMembersData.add(friendRequest);
                    fromMembersAdapter.notifyDataSetChanged();
                }
            }
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString());
        }
    }

    void setVisiblePB(boolean visible){
        if (visible){
            miActionProgressItem.setVisible(true);
        }else{
            miActionProgressItem.setVisible(false);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 1: openFriends();
                    break;
            }
        }
    }

    private void openFriends(){
        ActivityWorker.showFriends(this);
    }
}
