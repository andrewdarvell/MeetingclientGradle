package ru.darvell.android.meetingclient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.Requester;
import ru.darvell.android.meetingclient.database.DBFabric;

import java.util.Map;

/**
 * Регистрация нового пользователя
 */
public class RegisterActivity extends Activity {

	Button registerBtn;
	Button cancelBtn;
	EditText loginText;
	EditText passText;
	EditText emailText;
	Context context;

    BroadcastReceiver br;
    final String LOG_TAG = "meeting_register";
    public final static int ACT_ID = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerlayaut);

		registerBtn = (Button) findViewById(R.id.b_apply_register);
		cancelBtn = (Button) findViewById(R.id.b_cancel_register);
		loginText = (EditText) findViewById(R.id.t_login_register);
		passText = (EditText) findViewById(R.id.t_pass_register);
		emailText = (EditText) findViewById(R.id.t_email_register);
		context = this;

		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doRegister();
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelRegister();
			}
		});

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("actId", -1) == ACT_ID){
                    Log.d(LOG_TAG, "gotRequest");
//                    setVisiblePB(false);
                    Map<String,String> map = DBFabric.getDBWorker(context).getRequests(ACT_ID);
//                    ckeckLogin(map.get("result"));
                    Log.d(LOG_TAG, map.get("result"));
                    processingRegister(map.get("result"));

                }
            }
        };
        IntentFilter intFilt = new IntentFilter(Conf.BROADCAST_ACTION);
        registerReceiver(br, intFilt);

	}

    void processingRegister(String jsonStr){
        try{
            JSONObject response = new JSONObject(jsonStr);
            if (response.getInt("exitCode") == 0) {
                Toast.makeText(context, "Success registered", Toast.LENGTH_LONG).show();
                showLoginForm();
                Log.i("debug", "Success Register");
            } else if (response.get("code").equals("-15")) {
                Toast.makeText(context, "Login already ...", Toast.LENGTH_LONG).show();
            } else if (response.get("code").equals("-16")) {
                Toast.makeText(context, "Email already ...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "I can't register you", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            Log.d(LOG_TAG, e.toString());
        }
    }

	void doRegister(){
//		MyTask myTask  = new MyTask();
		if ((!loginText.getText().toString().equals(""))||
				(!passText.getText().toString().equals(""))||
				(!emailText.getText().toString().equals(""))) {

            JSONObject user = new JSONObject();
            try {
                user.put("login", loginText.getText());
                user.put("password", passText.getText());
                user.put("email", "temp");
                user.put("apiKey", Conf.apiKey);
//                myTask.execute(user);
                Requester requester = new Requester();
                requester.doRegister(this, user, ACT_ID);
            }catch (Exception e){

            }

		}else {
			Toast.makeText(context, "Empty fields", Toast.LENGTH_LONG).show();
		}

	}

	void cancelRegister(){

	}

	void showLoginForm(){
		startActivity(new Intent(context, AuthActivity.class));
	}

//	class MyTask extends AsyncTask<JSONObject, Integer, JSONObject>{
//
//		@Override
//		protected JSONObject doInBackground(JSONObject ... jsonBody) {
//			try {
//				Log.i("debug", "Send Get!!!");
//				return MeetingApi.sendPostJson(jsonBody[0], "auth/register");
//			}catch (Exception e){
//				e.printStackTrace();
//				return null;
//			}
//		}
//
//		@Override
//		protected void onPostExecute(JSONObject response) {
//			if (response == null) {
//				Toast.makeText(context, "Problem with connection?", Toast.LENGTH_LONG).show();
//				Log.i("debug", "Error!!!");
//			}else {
//				try {
//                    if (response.getInt("exitСode") == 0) {
//                        Toast.makeText(context, "Success registered", Toast.LENGTH_LONG).show();
//                        showLoginForm();
//                        Log.i("debug", "Success Register");
//                    } else if (response.get("code").equals("-15")) {
//                        Toast.makeText(context, "Login already ...", Toast.LENGTH_LONG).show();
//                    } else if (response.get("code").equals("-16")) {
//                        Toast.makeText(context, "Email already ...", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(context, "I can't register you", Toast.LENGTH_LONG).show();
//                    }
//                }catch (Exception e){
//                    Log.e("requests", e.toString());
//                }
//			}
//		}
//	}

}