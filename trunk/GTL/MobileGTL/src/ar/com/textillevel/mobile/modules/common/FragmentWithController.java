package ar.com.textillevel.mobile.modules.common;

import android.app.Fragment;

public abstract class FragmentWithController<C extends FragmentController<? extends Fragment>> extends Fragment{

	private C controller;

	public C getController() {
		return controller;
	}

	public void setController(C controller) {
		this.controller = controller;
	}
}
