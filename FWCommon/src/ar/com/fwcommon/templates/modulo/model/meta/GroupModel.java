package ar.com.fwcommon.templates.modulo.model.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Grupo de modelos
 * 
 * 
 *
 * @param <T> Tipo de modelo
 */
public final class GroupModel<T> extends Model<T> {
	private final String name;
	private final List<T> models;
	
	
	/**
	 * Crea un grupo de modelos
	 */
	public GroupModel(final String name, final List<T> models) {
		super();
		this.name = name;
		this.models = Collections.unmodifiableList(new ArrayList<T>(models));		
	}
	
	/**
	 * Crea un grupo de modelos
	 */
	public GroupModel(final String name, T... models) {
		super();
		this.name = name;
		this.models = Collections.unmodifiableList(Arrays.asList(models));
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.model.meta.Model#isGroupModel()
	 */
	@Override
	public final boolean isGroupModel() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.model.meta.Model#isSingleModel()
	 */
	@Override
	public final boolean isSingleModel() {
		return false;
	}

	/**
	 * Devuelve el nombre del grupo
	 * @return Nombre del grupo
	 */
	public String getName() {
		return name;
	}

	/**
	 * Devuelve la lista de grupos
	 * @return Lista de grupos
	 */
	public List<T> getModels() {
		return models;
	}

	/*
	 * (non-Javadoc)
	 * java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}
}
