package ru.darvell.android.meetingclient.api;

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

	public static Map<String, String> prepareLogin(String login, String pass){
		Map<String, String> params = new HashMap<String, String>();

        params.put("method","auth");
        params.put("apiKey", Conf.apiKey);
        params.put("login", login);
        params.put("passw", pass);

		return params;
	}

	public static Map<String, String> preapareRegister(String login, String pass, String email){
		Map<String, String> params = new HashMap<String, String>();

		params.put("method", "users");
		params.put("action", "register");
		params.put("login", login);
		params.put("pass", pass);
		params.put("email", email);
		return params;
	}


	public static Map<String, String> parseParams(String str){
		Map<String, String> result = new HashMap<String, String>();
		try {
			String rawPars[] = str.split(";");
			for (int i = 0; i < rawPars.length-1; i++) {
				String keyValue[] = rawPars[i].split(":");
				result.put(keyValue[0], keyValue[1]);
			}
			return result;
		}catch (Exception e){
			result.put("code", "-1");
			e.printStackTrace();
			return result;
		}
	}

	public static JSONObject sendPost(Map<String, String> params){
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Conf.apiUrl+params.get("method"));

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
			e.printStackTrace();
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

//	curl --data "action=getKey&login=ron&pass=1&api_key=bcbe3365e6ac95ea2c0343a2395834dd" http://env-5340213.jelastic.regruhosting.ru/meeting/secur

}
