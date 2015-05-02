package ar.com.textillevel.mobile.modules.cheque;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.cheque.to.ChequeFullTO;
import ar.com.textillevel.mobile.modules.common.FragmentDocumentWithController;

public class ChequeFragment extends FragmentDocumentWithController<ChequeFullTO, ChequeFragmentController>{

	public ChequeFragment(ChequeFullTO cheque) {
		super(cheque);
		super.setController(new ChequeFragmentController(this));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.cheque_fragment, container, false);
		setDatosCabecera(new DatosCabeceraChequeRenderer(getDocument(), rootView));
		return rootView;
	}
}
