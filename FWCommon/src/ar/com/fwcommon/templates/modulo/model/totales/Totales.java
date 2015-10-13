package ar.com.fwcommon.templates.modulo.model.totales;

import java.util.List;

import ar.com.fwcommon.templates.modulo.model.meta.GroupModel;
import ar.com.fwcommon.templates.modulo.model.meta.Model;
import ar.com.fwcommon.templates.modulo.model.meta.ModelSet;
import ar.com.fwcommon.templates.modulo.model.meta.SingleModel;

/**
 * Modelo que agrupa multiples totales
 * 
 * 
 * 
 * @param <T> Tipo de datos que se va a totalizar
 */
public final class Totales<T> extends ModelSet<Total<T>>{
	
	public Totales() {
		super();
	}

	/**
	 * Acumula en todos los totales un nuevo objeto
	 * 
	 * @param item Objeto a totalizar
	 */
    public synchronized void totalizar(T item) {
        for (Model<Total<T>> element : getElements()) {
        	if (element.isSingleModel()) {
        		final SingleModel<Total<T>> model = (SingleModel<Total<T>>)element;
        		model.getModel().totalizar(item);
        	} else {
        		final GroupModel<Total<T>> model = (GroupModel<Total<T>>)element;
        		List<Total<T>> totales = model.getModels();
        		for (Total<T> total : totales) {
        			total.totalizar(item);
				}
        	}
        }
    }

	/**
	 * Totaliza una lista de objetos determinada<br>
	 * Es decir, que inicializa los totales y luego acumula cada elemento
	 * 
	 * @param items Items a totalizar
	 */
    public synchronized void totalizar(List<T> items) {
    	inicializar();
        for (T item: items) {
        	totalizar(item);
        }
    }
    
    /**
	 * Reinicializa el valor de todos los totales
	 */
    public synchronized void inicializar() {
        for (Model<Total<T>> element : getElements()) {
        	if (element.isSingleModel()) {
        		final SingleModel<Total<T>> model = (SingleModel<Total<T>>)element;
        		model.getModel().setValue(0);
        	} else {
        		final GroupModel<Total<T>> model = (GroupModel<Total<T>>)element;
        		List<Total<T>> totales = model.getModels();
        		for (Total<T> total : totales) {
        			total.setValue(0);
				}
        	}
        }
    }
    
    //TODO OPtimizar Este Metodo para Evitar el INSTANCEOF de TotalSeleccionadas 
    public synchronized void totalizarSeleccionadas(Integer totalSeleccionadas) {
    	for (Model<Total<T>> element : getElements()) {
        	if (element.isSingleModel()) {
        		final SingleModel<Total<T>> model = (SingleModel<Total<T>>)element;
        		if(model.getModel() instanceof  TotalSeleccionadas) {
        			model.getModel().setValue(totalSeleccionadas);
				}
        	} else {
        		final GroupModel<Total<T>> model = (GroupModel<Total<T>>)element;
        		List<Total<T>> totales = model.getModels();
        		for (Total<T> total : totales) {
        			if(total instanceof  TotalSeleccionadas) {
            			total.setValue(totalSeleccionadas);
						
					}

				}
        	}
        }
	}
}
