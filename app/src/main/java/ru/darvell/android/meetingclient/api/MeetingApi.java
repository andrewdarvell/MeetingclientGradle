package ru.darvell.android.meetingclient.api;

import android.content.Intent;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.*;

/**
 * Основной класс для работы с сервером
 */
public class MeetingApi {

	public static final String SECUR_METHOD = "secur";
    final static String LOG_TAG = "meeting_api";
    static Map<Integer, String> urlsString;
    public static final int LOGIN = 1;
    public static final int REGISTER = 2;
    public static final int ALL_USER_SCHEDULES = 3;
    public static final int USER_INFO = 4;

    static {
        urlsString = new HashMap<>();
        urlsString.put(LOGIN, "auth");
        urlsString.put(REGISTER, "auth/register");
        urlsString.put(ALL_USER_SCHEDULES, "schedules/get_all/");
        urlsString.put(USER_INFO, "user/get_info/");
    }

	public static Map<String, String> prepareLogin(Intent intent){
		Map<String, String> params = new HashMap<String, String>();
        params.put("method", urlsString.get(LOGIN));
        params.put("apiKey", Conf.apiKey);
        params.put("login", intent.getStringExtra("login"));
        params.put("passw", intent.getStringExtra("pass"));
		return params;
	}

    public static String prepareRegister(){
        return urlsString.get(REGISTER);
    }

    public static Map<String, String> prepareGetAllSchedules(Intent intent){
        Map<String, String> params = new HashMap<>();
        params.put("method", urlsString.get(ALL_USER_SCHEDULES)+Conf.userId);
        params.put("sessionKey", Conf.sessKey);
        params.put("apiKey", Conf.apiKey);
        return params;
    }

//    public static Map<String, String> prepareGetAllSchedulesFriends(){
//        Map<String, String> params = new HashMap<>();
//        params.put("request", "friends");
//        params.put("method", "schedules/get_all/"+Conf.userId);
//        params.put("sessionKey", Conf.sessKey);
//        params.put("apiKey", Conf.apiKey);
//        return params;
//    }

    public static Map<String, String> prepareUpdateUser(Intent intent){
        Map<String, String> params = new HashMap<>();
        params.put("method", urlsString.get(USER_INFO)+Conf.userId);
        params.put("sessionKey", Conf.sessKey);
        params.put("apiKey", Conf.apiKey);
        return params;
    }

	public static JSONObject sendPost(Map<String, String> params){
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Conf.apiUrl+params.get("method"));
            Log.i("URL", httpPost.getURI().toString());

			Set keys = params.keySet();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(keys.size());
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()){
				String tmpStr = iterator.next();
				if (!tmpStr.equals("method")) {
					nameValuePairs.add(new BasicNameValuePair(tmpStr, params.get(tmpStr)));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String responseRaw = httpclient.execute(httpPost, new BasicResponseHandler());
			return new JSONObject(responseRaw);
		}catch (Exception e){
			Log.e(LOG_TAG, e.toString());
			return null;
		}
	}

    public static JSONObject sendPostJson(JSONObject jsonBody, String url){
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Conf.apiUrl+url);
            Log.i("url", httpPost.toString());
            httpPost.setEntity(new StringEntity(jsonBody.toString()));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            String responseRaw = httpclient.execute(httpPost, new BasicResponseHandler());
            return new JSONObject(responseRaw);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

	public static String sendGet(Map<String, String> params){
		try{
			HttpClient httpClient = new DefaultHttpClient();
			StringBuilder stringBuilder = new StringBuilder();
			Set keys = params.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()){
				String tmpStr = iterator.next();
				if (!tmpStr.equals("method")) {
					stringBuilder.append(tmpStr + "=" + params.get(tmpStr) + "&");
				}
			}
			HttpGet httpGet = new HttpGet(Conf.apiUrl+params.get("method")+"?"+stringBuilder.toString());
			String responseRaw = httpClient.execute(httpGet, new BasicResponseHandler());
			return responseRaw;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
