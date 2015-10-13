package ar.com.fwcommon.templates.modulo.model.meta;

/**
 * Clase abstracta que agrupa a todos los modelos de componentes individuales
 * 
 * 
 */
public abstract class Model<T> {
	
	/**
	 * Crea un modelo. El contructor se hizo de package para que solo existan
	 * dos subclases de la misma
	 */
	Model() {
		super();
	}
	
	/**
	 * Dice si se trata de un solo modelo o de un grupo de modelos
	 * 
	 * @return <code>true</code> si es un grupo de modelos. <code>false</code>
	 *         si es un único modelo
	 */
	public abstract boolean isGroupModel();
	
	/**
	 * Dice si se trata de un solo modelo o de un grupo de modelos
	 *  
	 * @return <code>true</code> si es un único modelo. <code>false</code>
	 *         si es un grupo de modelos
	 */
	public abstract boolean isSingleModel();
}
