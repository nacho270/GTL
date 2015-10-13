package ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.enums.ETipoAsignacion;

@Entity
@DiscriminatorValue(value = "NOREMPORCBRUTO")
public class AsignacionNoRemPorcSueldoBruto extends Asignacion implements Serializable {

	private static final long serialVersionUID = 469874961942160782L;

	private BigDecimal porcentaje;
	private String textoVisualizacionRS;

	@Column(name = "A_PORC_SUELDO_BRUTO", nullable=true)
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	@Column(name = "A_TEXTO_VIS_RS", nullable=true)
	public String getTextoVisualizacionRS() {
		return textoVisualizacionRS;
	}

	public void setTextoVisualizacionRS(String textoVisualizacionRS) {
		this.textoVisualizacionRS = textoVisualizacionRS;
	}

	@Override
	public String toString() {
		return DateUtil.dateToString(getFechaDesde()) + " - " + DateUtil.dateToString(getFechaHasta()) + " " + getTextoVisualizacionRS() + " (" + getPorcentaje() + "%)";
	}

	@Override
	@Transient
	public ETipoAsignacion getTipo() {
		return ETipoAsignacion.PORCE_SUELDO_BRUTO;
	}

}