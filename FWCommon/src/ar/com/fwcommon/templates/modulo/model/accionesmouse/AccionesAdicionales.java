package ar.com.fwcommon.templates.modulo.model.accionesmouse;

import java.util.List;

import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.templates.modulo.model.meta.GroupModel;
import ar.com.fwcommon.templates.modulo.model.meta.Model;
import ar.com.fwcommon.templates.modulo.model.meta.ModelSet;
import ar.com.fwcommon.templates.modulo.model.meta.SingleModel;


public class AccionesAdicionales<T> extends ModelSet<AccionAdicional<T>> {
	
	public AccionesAdicionales() {
		super();
	}
	
	public void update(final AccionEvent<T> e) {
        for (Model<AccionAdicional<T>> element : getElements()) {
        	if (element.isSingleModel()) {
        		final SingleModel<AccionAdicional<T>> model = (SingleModel<AccionAdicional<T>>)element;
        		model.getModel().update(e);
        	} else {
        		final GroupModel<AccionAdicional<T>> model = (GroupModel<AccionAdicional<T>>)element;
        		List<AccionAdicional<T>> statuses = model.getModels();
        		for (AccionAdicional<T> status : statuses) {
        			status.update(e);
				}
        	}
        }
	}
	
}
