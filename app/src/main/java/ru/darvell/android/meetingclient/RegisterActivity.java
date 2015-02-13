package ru.darvell.android.meetingclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import ru.darvell.android.meetingclient.api.MeetingApi;

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
	ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerlayaut);

		registerBtn = (Button) findViewById(R.id.b_apply_register);
		cancelBtn = (Button) findViewById(R.id.b_cancel_register);
		loginText = (EditText) findViewById(R.id.t_login_register);
		passText = (EditText) findViewById(R.id.t_pass_register);
		emailText = (EditText) findViewById(R.id.t_email_register);
		progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);
		setVisiblePB(false);

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

	}

	void doRegister(){
		MyTask myTask  = new MyTask();
		if ((!loginText.getText().toString().equals(""))||
				(!passText.getText().toString().equals(""))||
				(!emailText.getText().toString().equals(""))) {
			setVisiblePB(true);
			myTask.execute(MeetingApi.preapareRegister(loginText.getText().toString(),
					passText.getText().toString(),
					emailText.getText().toString()
					));
		}else {
			Toast.makeText(context, "Empty fields", Toast.LENGTH_LONG).show();
		}

	}

	void cancelRegister(){

	}

	void showLoginForm(){
		startActivity(new Intent(context, AuthActivity.class));
	}

	void setVisiblePB(boolean visible){
		if (visible){
			progressBar.setVisibility(View.VISIBLE);
		}else {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}

	class MyTask extends AsyncTask<Map<String,String>, Integer, String>{

		@Override
		protected String doInBackground(Map<String, String>... params) {
			try {
				Log.i("debug", "Send Get!!!");
				return MeetingApi.sendGet(params[0]);
			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String s) {
			setVisiblePB(false);
			if (s == null) {
				Toast.makeText(context, "Problem with connection?", Toast.LENGTH_LONG).show();
				Log.i("debug", "Error!!!");
			}else {
				Map<String, String> response = MeetingApi.parseParams(s);
				if (response.get("code").equals("0")){
					Toast.makeText(context, "Success registered", Toast.LENGTH_LONG).show();
					showLoginForm();
					Log.i("debug", "Success Register");
				}else if (response.get("code").equals("-15")){
					Toast.makeText(context, "Login already ...", Toast.LENGTH_LONG).show();
				}else if (response.get("code").equals("-16")){
					Toast.makeText(context, "Email already ...", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(context, "I can't register you", Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}
	}

}