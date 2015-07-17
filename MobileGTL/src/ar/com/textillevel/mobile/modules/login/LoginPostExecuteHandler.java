package ar.com.textillevel.mobile.modules.login;

import android.app.Activity;
import android.widget.Toast;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.SessionData;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.common.to.UsuarioSistemaTO;

public class LoginPostExecuteHandler extends AbstractPostExecuteHandler<UsuarioSistemaTO>{

	public LoginPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(UsuarioSistemaTO usuario) {
		SessionData.getInstance().setUsuarioEnSesion(usuario);
		((MainActivity)getActivity()).loginExitoso();
	}

	@Override
	public void handleError(ErrorTO error) {
		Toast.makeText(getActivity().getApplicationContext(), error.getMensaje(),Toast.LENGTH_SHORT).show();
	}
}
