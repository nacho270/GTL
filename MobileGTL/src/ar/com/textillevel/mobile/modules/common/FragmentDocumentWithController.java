package ar.com.textillevel.mobile.modules.common;

import android.app.Fragment;

public abstract class FragmentDocumentWithController<D,T extends FragmentController<? extends Fragment>> extends FragmentWithController<T>{

	private D document;
	
	public FragmentDocumentWithController(D document){
		this.document = document;
	}
	
	public void setDatosCabecera(DatosCabeceraRenderer<D> renderer){
		renderer.render();
	}

	public D getDocument() {
		return document;
	}

	public void setDocument(D document) {
		this.document = document;
	}
}
