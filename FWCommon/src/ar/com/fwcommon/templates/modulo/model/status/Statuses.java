package ar.com.fwcommon.templates.modulo.model.status;

import java.util.List;

import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.templates.modulo.model.meta.GroupModel;
import ar.com.fwcommon.templates.modulo.model.meta.Model;
import ar.com.fwcommon.templates.modulo.model.meta.ModelSet;
import ar.com.fwcommon.templates.modulo.model.meta.SingleModel;

/**
 * Modelo que agrupa multiples Status
 * 
 * 
 * 
 * @param <T> Tipo de datos que se encuentra en la tabla
 */
public final class Statuses<T> extends ModelSet<Status<T>>{
	
	public Statuses() {
		super();
	}
	
	public void update(final AccionEvent<T> e) {
        for (Model<Status<T>> element : getElements()) {
        	if (element.isSingleModel()) {
        		final SingleModel<Status<T>> model = (SingleModel<Status<T>>)element;
        		model.getModel().update(e);
        	} else {
        		final GroupModel<Status<T>> model = (GroupModel<Status<T>>)element;
        		List<Status<T>> statuses = model.getModels();
        		for (Status<T> status : statuses) {
        			status.update(e);
				}
        	}
        }
	}
}
