package ru.darvell.android.meetingclient.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Conf {

    //TODO Сделать блять с этим чтонибудь

	public static final String apiUrl = "http://meeting.darvell.ru/";
	public static final String apiKey = "bcbe3365e6ac95ea2c0343a2395834dd";
    public final static String BROADCAST_ACTION = "ru.darvell.android.meetingclient";

	public static String sessKey = "";

	public static boolean exist = false;
	public static String login = "";
	public static String pass = "";
	public static String email = "";


	public static int userId = -1;

    public static void UpdateFromJson(String rawJson){
        try {
            JSONObject jsonObject = new JSONObject(rawJson);
            JSONObject user = jsonObject.getJSONObject("user");
//            login = jsonObject.getString("login");
            email = user.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
