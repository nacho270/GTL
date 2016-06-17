package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminadoSanford;

public class PanDatosMaquinaSectorTerminadoSandford extends PanDatosMaquinaCommon {

	private static final long serialVersionUID = 1L;
	private MaquinaSectorTerminadoSanford maquina;

	public PanDatosMaquinaSectorTerminadoSandford() {
		super();
		construct();
	}

	private void construct() {
	}

	public void setMaquina(MaquinaSectorTerminadoSanford msts) {
		this.maquina = msts;
	}

	public MaquinaSectorTerminadoSanford getMaquinaConDatosSeteados() {
		return maquina;
	}

	@Override
	public ValidadorCamposMaquinaHandler configureValidadorHandler() {
		return new ValidadorCamposMaquinaHandler();
	}

}