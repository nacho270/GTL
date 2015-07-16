package ar.com.textillevel.mobile.modules.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.UserHomeActivity;
import ar.com.textillevel.mobile.util.AppConfig;
import ar.com.textillevel.mobile.util.CallAPI;
import ar.com.textillevel.mobile.util.PortalUtils;

public class MainActivity extends Activity {

	private EditText username = null;
	private EditText password = null;

	public static final String USER = "USER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppConfig.getInstance().initConfig(this);

		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
	}

	public void login(View view) {
		if (username.getText().toString() != null && !username.getText().toString().isEmpty() && password.getText().toString() != null && !password.getText().toString().isEmpty()) {
			login(username.getText().toString(), password.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "Debe ingresar usuario y password", Toast.LENGTH_LONG).show();
		}
	}

	// @SuppressWarnings("unused")
	private void login(String usr, String pass) {
//		if (true) {
//			startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
//			return;
//		}
		Toast.makeText(getApplicationContext(), "Autenticando...", Toast.LENGTH_SHORT).show();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("user", usr));
		nameValuePairs.add(new BasicNameValuePair("pass", PortalUtils.getHash(pass, "MD5")));
		new CallAPI(AppConfig.getInstance().getServer() + AppConfig.API_URL_LOGIN, CallAPI.POST_METHOD, nameValuePairs, new LoginPostExecuteHandler(MainActivity.this)).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void loginExitoso() {
		username.setText("");
		password.setText("");
		startActivity(new Intent(this, UserHomeActivity.class));		
	}
}
