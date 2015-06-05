package ru.darvell.android.meetingclient.database;

import java.util.Map;

public interface DBWorker {

    long putRequest(String result, int type, int act_id );
    Map<String, String> getRequests(int act_id);
    void delRequest(long id);
    void delRequests(int atc_id);
}
