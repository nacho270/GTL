package ar.com.textillevel.mobile.modules.common;

import android.app.Fragment;

public abstract class FragmentController<F extends Fragment> {

	private F fragment;
	
	public FragmentController(F f) {
		this.fragment = f;
	}
	
	public F getFragment() {
		return fragment;
	}
}
