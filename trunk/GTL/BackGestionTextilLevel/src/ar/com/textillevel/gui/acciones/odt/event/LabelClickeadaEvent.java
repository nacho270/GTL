package ar.com.textillevel.gui.acciones.odt.event;

import java.awt.Frame;

import ar.com.textillevel.gui.acciones.odt.JFrameVisionGeneralProduccion.ModeloFiltro;
import ar.com.textillevel.modulos.odt.to.TipoMaquinaTO;

public class LabelClickeadaEvent {

	private Frame padre;
	private TipoMaquinaTO tipoMaquina;
	private boolean ultima;
	private ModeloFiltro datosFiltro;
	
	public LabelClickeadaEvent(Frame frame, TipoMaquinaTO tipoMaquina, boolean ultima, ModeloFiltro datosFiltro) {
		this.padre = frame;
		this.tipoMaquina = tipoMaquina;
		this.ultima = ultima;
		this.datosFiltro = datosFiltro;
	}

	public Frame getPadre() {
		return padre;
	}

	public void setPadre(Frame padre) {
		this.padre = padre;
	}

	public TipoMaquinaTO getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(TipoMaquinaTO tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}

	public boolean isUltima() {
		return ultima;
	}

	public void setUltima(boolean ultima) {
		this.ultima = ultima;
	}

	public ModeloFiltro getDatosFiltro() {
		return datosFiltro;
	}

	public void setDatosFiltro(ModeloFiltro datosFiltro) {
		this.datosFiltro = datosFiltro;
	}
}
