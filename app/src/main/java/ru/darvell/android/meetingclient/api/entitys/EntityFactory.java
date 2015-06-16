package ru.darvell.android.meetingclient.api.entitys;

import android.content.Context;
import org.json.JSONObject;

public class EntityFactory {
    public static MainUser getMainUser(Context context){
        MainUser mainUser = new MainUser();
        mainUser.loadMainUser(context);
        return mainUser;
    }

    public static FriendRequest getRequestFrom(JSONObject jsonObject) throws Exception{
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(jsonObject.getLong("request_id"));
        friendRequest.setFromMember(new Member(jsonObject.getJSONObject("to_user")));
        return friendRequest;
    }

    public static FriendRequest getRequestTo(JSONObject jsonObject) throws Exception{
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(jsonObject.getLong("request_id"));
        friendRequest.setToMember(new Member(jsonObject.getJSONObject("from_user")));
        return friendRequest;
    }

}
