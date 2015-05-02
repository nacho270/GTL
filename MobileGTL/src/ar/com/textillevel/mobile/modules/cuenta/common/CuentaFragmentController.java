package ar.com.textillevel.mobile.modules.cuenta.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ar.com.textillevel.mobile.modules.common.FragmentController;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.CuentaPostExecuteHandler;
import ar.com.textillevel.mobile.util.AppConfig;
import ar.com.textillevel.mobile.util.CallAPI;

public class CuentaFragmentController extends FragmentController<AbstractCuentaFragment> {

	public CuentaFragmentController(AbstractCuentaFragment f) {
		super(f);
	}

	public void buscarMovimientos(final int tipoCuenta, Integer cantidadMovimientos, String txtCliente) throws Exception {
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("tipoCuenta", String.valueOf(tipoCuenta)));
		nameValuePairs.add(new BasicNameValuePair("cuentaABuscar", txtCliente));
		nameValuePairs.add(new BasicNameValuePair("cantidadMovimientos", String.valueOf(cantidadMovimientos)));
		new CallAPI(AppConfig.getInstance().getServer() + AppConfig.API_URL_CUENTA, CallAPI.GET_METHOD, nameValuePairs, new CuentaPostExecuteHandler(getFragment().getActivity(), getFragment())).execute();
    }

}