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
import android.widget.ProgressBar;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.Requester;
import ru.darvell.android.meetingclient.database.DBFabric;

import java.util.Map;

/**
 * Форма авторизации и регистрации
 */
public class AuthActivity extends Activity {

	EditText loginText;
	EditText passText;
	ProgressBar progressBar;
    Button button;
    Button button2;

    BroadcastReceiver br;

    final String LOG_TAG = "meeting_auth";
    public final static int ACT_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authlayout);

		button = (Button) findViewById(R.id.button);
		button2 = (Button) findViewById(R.id.button2);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE);

		loginText = (EditText) findViewById(R.id.t_login_auth);
		passText = (EditText) findViewById(R.id.t_pass_auth);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showRegister();
			}
		});

		//Действие на кнопку логин
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setVisiblePB(true);
				sendLogin(loginText.getText().toString(), passText.getText().toString());
			}
		});

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("actId", -1) == ACT_ID){
                    Log.d("AuthAct", "gotRequest");
                    setVisiblePB(false);
                    Map<String,String> map = DBFabric.getDBWorker(context).getRequests(ACT_ID);
                    ckeckLogin(map.get("result"));
                    Log.d("AuthAct", map.get("result"));
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(Conf.BROADCAST_ACTION);
        registerReceiver(br, intFilt);
	}

	void sendLogin(String login, String pass){

        DBFabric.getDBWorker(this).delRequests(ACT_ID);
        Requester requester = new Requester();
        requester.doLogin(this, login, pass, ACT_ID);
	}

	//Вызывает основную форму приложения
	void showMain(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	//Видимость прогресс-бара
	void showRegister(){
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	void setVisiblePB(boolean visible){
        button.setEnabled(!visible);
        button2.setEnabled(!visible);
        if (visible){
			progressBar.setVisibility(View.VISIBLE);
		}else {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}

    void ckeckLogin(String result){
        try{
            JSONObject resultJson = new JSONObject(result);
            if (resultJson.getInt("exitCode") == 0) {
                Conf.sessKey = resultJson.getString("sessionKey");
                JSONObject user = (JSONObject) resultJson.get("user");
                Conf.userId = user.getInt("userId");
                Conf.login = loginText.getText().toString();
                Conf.pass = passText.getText().toString();
                Conf.exist = true;
                Log.i(LOG_TAG, Conf.sessKey);
                showMain();
            }
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }
}