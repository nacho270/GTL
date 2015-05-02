package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.cheque.ChequeFragment;
import ar.com.textillevel.mobile.modules.cheque.to.ChequeFullTO;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.util.json.JSONParser;

public class ChequePostExecuteHandler extends AbstractPostExecuteHandler<ChequeFullTO> {

	public ChequePostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handle(String result) {
		ChequeFullTO cheque = JSONParser.fromJson(result, ChequeFullTO.class);
		ChequeFragment cfragment = new ChequeFragment(cheque);
		showFragment(cfragment, getActivity());		
	}

	@Override
	public void handleError(ErrorTO error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleResult(ChequeFullTO document) {
		ChequeFragment cfragment = new ChequeFragment(document);
		showFragment(cfragment, getActivity());		
	}
}
