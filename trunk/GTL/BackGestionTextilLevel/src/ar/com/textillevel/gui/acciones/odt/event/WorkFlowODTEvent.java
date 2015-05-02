package ar.com.textillevel.gui.acciones.odt.event;

import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class WorkFlowODTEvent {

	private ODTTO odtTO;
	private Byte ordenTipoMaquina;
	private EAvanceODT avance;
	private boolean oficina;

	public WorkFlowODTEvent(ODTTO odt, Byte ordenTipoMaquina) {
		this.odtTO = odt;
		this.ordenTipoMaquina = ordenTipoMaquina;
	}

	public WorkFlowODTEvent(ODTTO odtTO, EAvanceODT avance, boolean ultima) {
		super();
		this.oficina = ultima;
		this.odtTO = odtTO;
		this.avance = avance;
	}

	public ODTTO getOdtTO() {
		return odtTO;
	}

	public void setOdtTO(ODTTO odtTO) {
		this.odtTO = odtTO;
	}

	public Byte getOrdenMaquina() {
		return ordenTipoMaquina;
	}

	public void setOrdenMaquina(Byte ordenMaquina) {
		this.ordenTipoMaquina = ordenMaquina;
	}

	public EAvanceODT getAvance() {
		return avance;
	}

	public void setAvance(EAvanceODT avance) {
		this.avance = avance;
	}

	public boolean isOficina() {
		return oficina;
	}

	public void setOficina(boolean oficina) {
		this.oficina = oficina;
	}

}
