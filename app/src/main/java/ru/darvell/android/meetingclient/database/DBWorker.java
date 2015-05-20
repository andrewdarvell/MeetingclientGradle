package ru.darvell.android.meetingclient.database;

import java.util.Map;

public interface DBWorker {

    void putRequest(String result, int type, int act_id );
    Map<String, String> getRequests(int act_id);
    void delRequest(int id);
}
