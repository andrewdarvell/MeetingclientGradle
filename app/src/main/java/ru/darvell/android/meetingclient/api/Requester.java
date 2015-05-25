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
}
