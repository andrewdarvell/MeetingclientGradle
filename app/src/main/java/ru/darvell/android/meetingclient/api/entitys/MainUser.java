package ru.darvell.android.meetingclient.api.entitys;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.utils.FileWorkerFactory;

public class MainUser {

    final String LOG_TAG = "meeting_main_user";

    private Long userId;
    private String login;
    private String pass;
    private String sessionKey;
    private String email;

    public MainUser(Long id, String login, String pass, String sessionKey, String email) {
        this.userId = id;
        this.login = login;
        this.pass = pass;
        this.sessionKey = sessionKey;
        this.email = email;
    }

    public MainUser(){

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getsessionKey() {
        return sessionKey;
    }

    public void setsessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("login", login);
            jsonObject.put("pass", pass);
            jsonObject.put("email", email);
            jsonObject.put("sessionKey", sessionKey);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return jsonObject;
    }

    boolean updateFromJsonFile(String str){
        try{
            JSONObject jsonObject = new JSONObject(str);
            this.userId = jsonObject.getLong("userId");
            this.login = jsonObject.getString("login");
            this.pass = jsonObject.getString("pass");
            this.sessionKey = jsonObject.getString("sessionKey");
            this.email = jsonObject.getString("email");
            return true;
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString());
            return false;
        }
    }

    public void saveMainUser(Context context){
        JSONObject jsonObject = toJson();
        FileWorkerFactory.getWorker(context).writeString(jsonObject.toString());
    }

    public boolean loadMainUser(Context context){
         return updateFromJsonFile(FileWorkerFactory.getWorker(context).readString());
    }

    public void UpdateFromJson(String rawJson){
        try {
            JSONObject jsonObject = new JSONObject(rawJson);
            JSONObject user = jsonObject.getJSONObject("user");
            login = jsonObject.getString("login");
            email = user.getString("email");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }
}
