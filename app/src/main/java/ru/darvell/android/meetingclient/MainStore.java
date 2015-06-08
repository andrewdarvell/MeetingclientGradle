package ru.darvell.android.meetingclient;

import ru.darvell.android.meetingclient.api.entitys.MainUser;

public class MainStore {

    public static final String apiUrl = "http://meeting.darvell.ru/";
    public static final String apiKey = "bcbe3365e6ac95ea2c0343a2395834dd";
    public final static String BROADCAST_ACTION = "ru.darvell.android.meetingclient";

    MainUser mainUser;
    MainStore mainStore;

    public MainStore(MainUser mainUser){
        mainStore = this;
        this.mainUser = mainUser;
    }

    protected String getSessionKey(){
        return mainUser.getSesionKey();
    }

    protected String getUserEmail(){
        return mainUser.getEmail();
    }



}
