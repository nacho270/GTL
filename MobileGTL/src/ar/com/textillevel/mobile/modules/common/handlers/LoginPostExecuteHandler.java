package ar.com.textillevel.mobile.modules.common.handlers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import ar.com.textillevel.mobile.UserHomeActivity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.SessionData;
import ar.com.textillevel.mobile.modules.common.to.UsuarioSistemaTO;

public class LoginPostExecuteHandler extends AbstractPostExecuteHandler<UsuarioSistemaTO>{

	public LoginPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(UsuarioSistemaTO usuario) {
		SessionData.getInstance().setUsuarioEnSesion(usuario);
		getActivity().startActivity(new Intent(getActivity(), UserHomeActivity.class));
	}

	@Override
	public void handleError(ErrorTO error) {
		Toast.makeText(getActivity().getApplicationContext(), error.getMensaje(),Toast.LENGTH_SHORT).show();
	}
}
