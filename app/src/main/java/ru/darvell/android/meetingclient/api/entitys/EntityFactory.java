package ru.darvell.android.meetingclient.api.entitys;

import android.content.Context;

public class EntityFactory {
    public static MainUser getMainUser(Context context){
        MainUser mainUser = new MainUser();
        mainUser.loadMainUser(context);
        return mainUser;
    }

}
