package ar.com.textillevel.gui.modulos.remitoentrada.to;

import java.sql.Date;

import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.documentos.remito.enums.ESituacionODTRE;
import ar.com.textillevel.entidades.gente.Cliente;

public class RemitoEntradaModuloTO {

	private Integer id;
	private Date fechaIngreso;
	private Cliente cliente;
	private Integer nroRemito;
	private String detalle;
	private ETipoREModulo tipoRE;
	private boolean esDe01;

	public RemitoEntradaModuloTO(RemitoEntradaDibujo red) {
		this.id = red.getId();
		this.fechaIngreso = new Date(red.getFechaIngreso().getTime());
		this.cliente = red.getCliente();
		this.detalle = (red.getFactura() == null ? "" : red.getFactura() + " - ") + "DIBUJOS: " + StringUtil.getCadena(red.getDibujos(), " | ");
		this.tipoRE = ETipoREModulo.RE_CON_DIBUJOS;
		this.esDe01 = false;
	}

	public RemitoEntradaModuloTO(RemitoEntrada re) {
		this.id = re.getId();
		this.fechaIngreso = re.getFechaEmision();
		this.cliente = re.getCliente();
		this.nroRemito = re.getNroRemito();
		if(re.getSituacionODT() == ESituacionODTRE.CON_ODT) {
			this.detalle = StringUtil.getCadena(re.getProductoArticuloList(), " | ");
		} else if(re.getSituacionODT() == ESituacionODTRE.SIN_ODT) {
			this.detalle = "[SIN ODT]";
		} else {
			this.detalle = "[ODT CON COLOR A DEFINIR]";
		}
		this.tipoRE = ETipoREModulo.RE_CON_PIEZAS;
		this.esDe01 = re.getArticuloStock() != null || re.getPrecioMatPrima() != null;
	}

	public Integer getId() {
		return id;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Integer getNroRemito() {
		return nroRemito;
	}

	public String getDetalle() {
		return detalle;
	}

	public ETipoREModulo getTipoRE() {
		return tipoRE;
	}

	public boolean isEsDe01() {
		return esDe01;
	}

	public String toString() {
		return getDetalle();
	}

	public enum ETipoREModulo {
		RE_CON_PIEZAS,
		RE_CON_DIBUJOS;
	}

}