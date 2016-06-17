package ar.com.textillevel.gui.acciones.odt.event;

import ar.com.textillevel.modulos.odt.to.ODTTO;

public class BotonSubirBajarEvent {
	public static final byte SUBIR = 0;
	public static final byte BAJAR = 1;
	
	private byte accion;
	private ODTTO odt;
	
	public BotonSubirBajarEvent(byte accion, ODTTO odt) {
		this.accion = accion;
		this.odt = odt;
	}

	public byte getAccion() {
		return accion;
	}
	
	public void setAccion(byte accion) {
		this.accion = accion;
	}
	
	public ODTTO getOdt() {
		return odt;
	}
	
	public void setOdt(ODTTO odt) {
		this.odt = odt;
	}
}
