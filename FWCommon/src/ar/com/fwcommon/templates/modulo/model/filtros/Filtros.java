package ar.com.fwcommon.templates.modulo.model.filtros;

import java.util.LinkedList;
import java.util.List;

import ar.com.fwcommon.templates.modulo.model.meta.GroupModel;
import ar.com.fwcommon.templates.modulo.model.meta.Model;
import ar.com.fwcommon.templates.modulo.model.meta.ModelSet;
import ar.com.fwcommon.templates.modulo.model.meta.SingleModel;

/**
 * Agrupador de filtros
 * 
 * 
 *
 * @param <T>
 */
public final class Filtros<T> extends ModelSet<Filtro<T, ?>> {
	
	public Filtros() {
		super();
	}

	/**
	 * Filtra una lista de elementos utilizando los filtros.
	 * @param items Lista de elementos a filtrar
	 * @return Lista de elementos filtrada
	 */
	public List<T> filtrar(List<T> items) {
		List<T> filteredItems = new LinkedList<T>();
		if(items != null) {
			for(T item : items) {
				if(filtrar(item)) filteredItems.add(item);
			}
		}
		return filteredItems;
	}
	
	/**
	 * Dice si un elemento determinado pasa todos los filtros o no.
	 * 
	 * @param item el item a filtrar.
	 * @return <code>TRUE</code> si pasó todos los filtros y
	 *         <code>FALSE</code> en caso contrario
	 */
    public boolean filtrar(T item) {
        for (Model<Filtro<T,?>> element : getElements()) {
        	if (element.isSingleModel()) {
        		final SingleModel<Filtro<T,?>> model = (SingleModel<Filtro<T,?>>)element;
        		if (!model.getModel().filtrar(item))
        			return false;
        	} else {
        		final GroupModel<Filtro<T,?>> model = (GroupModel<Filtro<T,?>>)element;
        		List<Filtro<T,?>> filtros = model.getModels();
        		for (Filtro<T, ?> filtro : filtros) {
        			if (!filtro.filtrar(item))
        				return false;
				}
        	}
        }
        return true;
    }

	/**
	 * Coloca todos los filtros en su valor por defecto
	 */
	public void resetFiltros() {
        for (Model<Filtro<T,?>> element : getElements()) {
        	if (element.isSingleModel()) {
        		final SingleModel<Filtro<T,?>> model = (SingleModel<Filtro<T,?>>)element;
        		model.getModel().resetFilter();
        	} else {
        		final GroupModel<Filtro<T,?>> model = (GroupModel<Filtro<T,?>>)element;
        		List<Filtro<T,?>> filtros = model.getModels();
        		for (Filtro<T, ?> filtro : filtros) {
        			filtro.resetFilter();
				}
        	}
        }
	}
}
