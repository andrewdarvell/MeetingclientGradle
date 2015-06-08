package ru.darvell.android.meetingclient.activitys;

import android.content.Context;
import android.content.Intent;

public class ActivityWorker {

    public static void showFriends(Context cntx){
        cntx.startActivity(new Intent(cntx, FriendsActivity.class));
    }
}
