package ar.com.textillevel.entidades.portal.to;

import java.io.Serializable;

public class ParamsSistemaTO implements Serializable {

	private static final long serialVersionUID = -4853143145670893150L;

	private Integer nroSucursal;

	public ParamsSistemaTO(Integer nroSucursal) {
		this.nroSucursal = nroSucursal;
	}

	public Integer getNroSucursal() {
		return nroSucursal;
	}

	public void setNroSucursal(Integer nroSucursal) {
		this.nroSucursal = nroSucursal;
	}

}
