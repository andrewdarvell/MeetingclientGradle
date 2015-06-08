package ru.darvell.android.meetingclient.api.entitys;

import android.content.Context;

public class EntityFactory {
    static MainUser getMainUser(Context context){
        return new MainUser();
    }

}
