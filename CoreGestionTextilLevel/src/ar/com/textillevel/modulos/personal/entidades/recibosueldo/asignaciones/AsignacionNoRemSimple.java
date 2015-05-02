package ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.enums.ETipoAsignacion;

@Entity
@DiscriminatorValue(value = "NOREMSIMP")
public class AsignacionNoRemSimple extends Asignacion implements Serializable {

	private static final long serialVersionUID = -5131977195416433551L;

	private BigDecimal importe;

	public AsignacionNoRemSimple() {
	}

	@Column(name = "A_IMPORTE", nullable=true)
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	@Override
	public String toString() {
		return DateUtil.dateToString(getFechaDesde()) + " - " + DateUtil.dateToString(getFechaHasta()) + " $" + Double.valueOf(getImporte().doubleValue()).toString(); 
	}

	@Override
	@Transient
	public ETipoAsignacion getTipo() {
		return ETipoAsignacion.SIMPLE;
	}

}