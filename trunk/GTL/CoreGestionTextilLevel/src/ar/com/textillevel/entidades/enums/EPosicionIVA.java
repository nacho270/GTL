package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EPosicionIVA {

	RESPONSABLE_INSCRIPTO(1, "Responsable inscripto", ETipoFactura.A, "Resp. Insc"), 
	MONOTRIBUTISTA(2, "Monotributista", ETipoFactura.B, "Monotr."), 
	CONSUMIDOR_FINAL(3, "Consumidor final", ETipoFactura.B, "Cons. Final"), 
	RESPONSABLE_INSCRIPTO_AGENTE_RETENCION(4, "Responsable inscripto - Agente de rentención", ETipoFactura.A, "Resp. Insc"), 
	EXENTO(5, "Exento", ETipoFactura.B, "Ex."), 
	EXPORTACION(6, "Exportación", ETipoFactura.E, "Exp"), 
	EXPORTACION_ARG(7, "Exportación - Argentina", ETipoFactura.E, "Exp - Arg");

	private Integer id;
	private String descripcion;
	private ETipoFactura tipoFactura;
	private String descripcionResumida;

	private EPosicionIVA(Integer id, String descripcion, ETipoFactura tipoFactura, String descripcionResumida) {
		this.id = id;
		this.descripcion = descripcion;
		this.tipoFactura = tipoFactura;
		this.descripcionResumida = descripcionResumida;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String toString() {
		return getDescripcion().toUpperCase();
	}

	public static EPosicionIVA getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EPosicionIVA> keyMap;

	private static Map<Integer, EPosicionIVA> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EPosicionIVA>();
			EPosicionIVA values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	public ETipoFactura getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(ETipoFactura tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public String getDescripcionResumida() {
		return descripcionResumida;
	}

	public void setDescripcionResumida(String descripcionResumida) {
		this.descripcionResumida = descripcionResumida;
	}
}
