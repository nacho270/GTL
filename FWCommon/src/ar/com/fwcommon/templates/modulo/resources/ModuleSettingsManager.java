package ar.com.fwcommon.templates.modulo.resources;

import ar.com.fwcommon.boss.BossIdiomas;

/**
 * 
 *
 */
public abstract class ModuleSettingsManager {
	public static final int RESOURCE_STRING_FILTOS = 0;
	public static final int RESOURCE_STRING_TODOS = 1;
	private static ModuleSettingsManager instance = new DefaultModuleSettingsManager();
	
	/**
	 * Devuelve la instancia default del {@link ModuleSettingsManager}
	 * @return Instancia default
	 */
	public static ModuleSettingsManager getInstance() {
		return instance;
	}

	/**
	 * Registra un nuevo {@link ModuleSettingsManager}
	 * @param instance {@link ModuleSettingsManager} a utilizar
	 */
	public static void setInstance(ModuleSettingsManager instance) {
		ModuleSettingsManager.instance = instance;
	}
	
	
	/**
	 * Devuelve el string correspondiente al resource
	 * @param resource Numero de recurso
	 * @return String correspondiente
	 */
	public abstract String getStringResource(int resource);

	/**
	 * Devuelve si el sistema esta utilizando la fuente grande o no 
	 * @return <code>true</code> si el sistema está utilizando la fuente grande. <code>false</code> en caso contrario
	 */
	public abstract boolean isBigFont();

	
	private static class DefaultModuleSettingsManager extends ModuleSettingsManager {
		@Override
		public String getStringResource(int resource) {
			switch (resource) {
			case RESOURCE_STRING_FILTOS:
				return BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("fwcommon.ModuloTemplate.filtros");
				
			case RESOURCE_STRING_TODOS:
				return BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("fwcommon.ModuloTemplate.todos");
			}
			throw new IllegalArgumentException("Illegal Argument");
		}

		@Override
		public boolean isBigFont() {
			return System.getProperty("tamanioFuente") != null && 
					System.getProperty("tamanioFuente").equalsIgnoreCase("Grande");
		}
	}
}
