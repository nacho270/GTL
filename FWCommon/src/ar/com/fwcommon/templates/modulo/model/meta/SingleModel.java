package ar.com.fwcommon.templates.modulo.model.meta;

/**
 * 
 *
 */
public final class SingleModel<T> extends Model<T> {
	private final T model;
	/**
	 * 
	 */
	public SingleModel(final T model) {
		super();
		this.model = model;
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.model.meta.Model#isGroupModel()
	 */
	@Override
	public final boolean isGroupModel() {
		return false;
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.model.meta.Model#isSingleModel()
	 */
	@Override
	public final boolean isSingleModel() {
		return true;
	}

	/**
	 * Devuelve el modelo.
	 * 
	 * @return Modelo
	 */
	public T getModel() {
		return model;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SingleModel) {
			return model.equals(((SingleModel)obj).getModel());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getModel().hashCode();
	}	
}
