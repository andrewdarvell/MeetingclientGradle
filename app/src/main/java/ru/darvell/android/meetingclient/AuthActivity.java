package ru.darvell.android.meetingclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import ru.darvell.android.meetingclient.api.Conf;
import ru.darvell.android.meetingclient.api.MeetingApi;

import java.util.Map;

/**
 * Форма авторизации и регистрации
 */
public class AuthActivity extends Activity {

	MyTask mt;

	EditText loginText;
	EditText passText;
	ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authlayout);

		Button button = (Button) findViewById(R.id.button);
		Button button2 = (Button) findViewById(R.id.button2);
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
				doLogin(loginText.getText().toString(), passText.getText().toString());
			}
		});
	}

	void doLogin(String login, String pass){
		mt = new MyTask();
		mt.execute(MeetingApi.prepareLogin(login, pass));
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
		if (visible){
			progressBar.setVisibility(View.VISIBLE);
		}else {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}

	//Класс посылает запрос в другом потоке. Не GUI
	class MyTask extends AsyncTask<Map<String,String>, Integer, String> {
		@Override
		protected String doInBackground(Map<String, String>... params) {
			try {
				Log.i("debug", "Send Post!!!");
				return MeetingApi.sendPost(params[0]);

			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String s) {
			setVisiblePB(false);
			if (s == null) {
				Log.i("debug", "Error!!!");
			}else {
				Map<String, String> response = MeetingApi.parseParams(s);
				if (response.get("code").equals("0")){
					Conf.sessKey = response.get("main");
					Conf.login = loginText.getText().toString();
					Conf.pass = passText.getText().toString();
					Conf.exist = true;
					Log.i("debug", Conf.sessKey);
					showMain();
				}
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}
	}

}