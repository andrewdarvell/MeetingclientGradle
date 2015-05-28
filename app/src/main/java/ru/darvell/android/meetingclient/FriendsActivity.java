package ru.darvell.android.meetingclient;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ListView;
import ru.darvell.android.meetingclient.adapters.MembersAdapter;
import ru.darvell.android.meetingclient.api.entitys.Member;

import java.util.ArrayList;

public class FriendsActivity extends ActionBarActivity {

    public final static int ACT_ID = 4;
    final String LOG_TAG = "meeting_friends";
    BroadcastReceiver br;

    ListView friendList;
    ArrayList<Member> membersData;
    MembersAdapter membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        friendList = (ListView) findViewById(R.id.friend_list);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
