package ar.com.textillevel.mobile.modules.common;

import android.view.View;
import android.widget.TextView;

public abstract class DatosCabeceraRenderer<D> {
	
	private D document;
	private View rootView;
	
	public DatosCabeceraRenderer(D document, View rootView){
		this.document = document;
		this.rootView = rootView;
	}
	
	public abstract void render();
	
	protected void setTextOnTextView(int idTextView, String text, View rootView){
		TextView txt = (TextView) rootView.findViewById(idTextView);
		txt.setText(text);
	}
	
	public D getDocument() {
		return document;
	}

	public void setDocument(D docuement) {
		this.document = docuement;
	}

	public View getRootView() {
		return rootView;
	}

	public void setRootView(View rootView) {
		this.rootView = rootView;
	}

}
