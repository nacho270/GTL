package ar.com.fwcommon.templates.main.skin;

import javax.swing.Icon;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.GuiUtil;

/**
 * Clase abstracta y base para la creación de un Skin.
 */
public abstract class AbstractSkin {

	protected String skin;
	protected ISkinDecorator decorator;
	protected String name;

	/**
	 * Método constructor.
	 * @param skin El path completo del archivo de skin.
	 * @param decorator Una instancia de ISkinDecorator.
	 * @param name El nombre del skin.
	 */
	public AbstractSkin(String skin, ISkinDecorator decorator, String name) {
		this.skin = skin;
		this.decorator = decorator;
		this.name = name;
	}

	/**
	 * @return Devuelve el path completo del archivo de skin.
	 */
	public String getSkin() {
		return skin;
	}

	/**
	 * Setea el path completo del archivo de skin.
	 * @param skin
	 */
	public void setSkin(String skin) {
		this.skin = skin;
	}

	/**
	 * @return El decorador asociado (una instancia de ISkinDecorator).
	 */
	public ISkinDecorator getDecorator() {
		return decorator;
	}

	/**
	 * Setea el decorador para el skin.
	 * @param decorator
	 */
	public void setDecorator(ISkinDecorator decorator) {
		this.decorator = decorator;
	}

	/**
	 * @return El nombre del skin.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setea el nombre del skin.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Inicializa el skin. Si el decorador no es nulo también lo inicializa.
	 * Este método deberá ser llamado antes de la construcción del frame para que
	 * el skin se aplique en su totalidad.
	 * @throws FWException
	 */
	public void init() throws FWException {
		try {
			if(isSkinLF()) {
				GuiUtil.setSkinLookAndFeelThemepack(skin);
			} else {
				GuiUtil.setLookAndFeel(skin);
			}
			if(decorator != null) {
				decorator.init();
			}
		} catch(Exception e) {
			throw new FWException("Error al inicializar el skin '" + skin + "'", e);
		}
	}

	/**
	 * @return El nombre canónico de la clase.
	 */
	public String getCanonicalName() {
		return getClass().getCanonicalName();
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object obj) {
		if(obj instanceof AbstractSkin && ((AbstractSkin)obj).getSkin().equals(getSkin())) {
			return true;
		}
		return false;
	}

	/**
	 * @return Devuelve <b>true</b> si el skin es de SkinLF.
	 */
	public abstract boolean isSkinLF();

	/**
	 * @return Devuelve una imágen de previsualización del skin aplicado.
	 */
	public abstract Icon getPreview();

}