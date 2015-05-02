package ar.com.textillevel.mobile.modules.common.handlers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.widget.Toast;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.login.MainActivity;
import ar.com.textillevel.mobile.util.json.JSONParser;

public abstract class AbstractPostExecuteHandler<T> implements PostExecuteHandler{

	private Activity activity;
	
	public AbstractPostExecuteHandler(Activity activity) {
		this.activity = activity;
	}

	protected void showFragment(Fragment f, final Activity activity){
		FragmentManager fragmentManager = activity.getFragmentManager();
		fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, f).commit();
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void handle(String result) {
		try{
			ErrorTO error = JSONParser.fromJson(result, ErrorTO.class);
			if(error.getMensaje() == null){
				handleResult(createResult(result));
			}else if(error.getNumero()!=403){
				handleError(error);
			}else{
				handleNoSession();
			}
		}catch(Exception e){
			handleResult(createResult(result));
		}
	}
	
	protected void handleNoSession(){
		getActivity().startActivity( new Intent(getActivity(), MainActivity.class));
		Toast.makeText(getActivity(), "La sesión ha expirado", Toast.LENGTH_LONG).show();
	}

	@SuppressWarnings("unchecked")
	private T createResult(String result) {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		return JSONParser.fromJson(result,(Class<T>)actualTypeArguments[0]);
	}

	public abstract void handleResult(T result);
	public abstract void handleError(ErrorTO error);
}
