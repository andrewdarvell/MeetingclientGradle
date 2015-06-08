package ru.darvell.android.meetingclient.activitys;

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
import org.json.JSONException;
import org.json.JSONObject;
import ru.darvell.android.meetingclient.R;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.Requester;
import ru.darvell.android.meetingclient.api.entitys.MainUser;
import ru.darvell.android.meetingclient.database.DBFabric;
import ru.darvell.android.meetingclient.service.MeetingService;
import ru.darvell.android.meetingclient.utils.FileWorkerFactory;

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
    Context context;

    final String LOG_TAG = "meeting_auth";
    public final static int ACT_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authlayout);
        context = this;
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
                    Log.d(LOG_TAG, "gotRequest");
                    setVisiblePB(false);
                    if (intent.getIntExtra("result", -2) == 0){
                        Map<String,String> map = DBFabric.getDBWorker(context).getRequests(ACT_ID);
                        DBFabric.getDBWorker(context).delRequest(intent.getLongExtra("id", -10));
                        checkLogin(map.get("result"));
                    }
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(Conf.BROADCAST_ACTION);
        registerReceiver(br, intFilt);
	}

	void sendLogin(String login, String pass){
        DBFabric.getDBWorker(this).delRequests(ACT_ID);
        new Requester().doLogin(this, login, pass, ACT_ID);
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

    void checkLogin(String result){
        try{
            JSONObject resultJson = new JSONObject(result);
            if (resultJson.getInt("exitCode") == 0) {
                JSONObject user = (JSONObject) resultJson.get("user");
                MainUser mainUser = new MainUser(
                                        user.getLong("userId"),
                                        loginText.getText().toString(),
                                        passText.getText().toString(),
                                        resultJson.getString("sessionKey"),
                                        user.getString("email")
                                    );
                mainUser.saveMainUser(this);
                Log.i(LOG_TAG, mainUser.getsessionKey());
                showMain();
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        stopService(new Intent(this, MeetingService.class));
        super.onDestroy();
    }
}