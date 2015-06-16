package ru.darvell.android.meetingclient.api;

import android.content.Context;
import android.content.Intent;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.service.MeetingService;

public class Requester {

    public void doLogin(Context context, String login, String pass, int actId){

        Intent intent = new Intent(context, MeetingService.class);
        intent.putExtra("method", MeetingApi.LOGIN);
        intent.putExtra("actId", actId);

        intent.putExtra("login", login);
        intent.putExtra("pass", pass);
        context.startService(intent);
    }

    public void doRegister(Context context, JSONObject jsonObject, int actId){
        Intent intent = new Intent(context, MeetingService.class);
        intent.putExtra("method", MeetingApi.REGISTER);
        intent.putExtra("actId", actId);
        intent.putExtra("json", jsonObject.toString());
        context.startService(intent);
    }

    public void doGetAllUserSchedules(Context context, int actId){
        Intent intent = new Intent(context, MeetingService.class);
        intent.putExtra("method", MeetingApi.ALL_USER_SCHEDULES);
        intent.putExtra("actId", actId);
        context.startService(intent);
    }

    public void doGetUserInfo(Context context, int actId, long userId){
        Intent intent = new Intent(context, MeetingService.class);
        intent.putExtra("method", MeetingApi.USER_INFO);
        intent.putExtra("actId", actId);
        intent.putExtra("userId", userId);
        context.startService(intent);
    }

    public void doGetFriends(Context context, int actId, long userId){
        Intent intent = new Intent(context, MeetingService.class);
        intent.putExtra("method", MeetingApi.FRIENDS_ALL);
        intent.putExtra("actId", actId);
        intent.putExtra("userId", actId);
        context.startService(intent);
    }

    public void doGetRequestFromMe(Context context, int actId, long userId){
        Intent intent = new Intent(context, MeetingService.class);
        intent.putExtra("method", MeetingApi.FRIENDS_REQ_FROM);
        intent.putExtra("actId", actId);
        intent.putExtra("userId", actId);
        context.startService(intent);
    }

    public void doGetRequestToMe(Context context, int actId, long userId){
        Intent intent = new Intent(context, MeetingService.class);
        intent.putExtra("method", MeetingApi.FRIENDS_REQ_TO);
        intent.putExtra("actId", actId);
        intent.putExtra("userId", actId);
        context.startService(intent);
    }

}
