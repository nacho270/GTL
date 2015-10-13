package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.ISancionVisitor;

@Entity
@DiscriminatorValue(value="AP")
public class Apercibimiento extends Sancion {

	private static final long serialVersionUID = -7045015425906543864L;

	@Override
	@Transient
	public String getResumen() {
		StringBuilder sb = new StringBuilder();
		sb.append("Motivo: " + getMotivo());
		if(!StringUtil.isNullOrEmpty(getObservaciones())) {
			sb = sb.append("\n").append("Observaciones: " + getObservaciones());
		}
		return sb.toString();
	}

	@Override
	@Transient
	public ETipoSancion getTipoSancion() {
		return ETipoSancion.APERCIBIMIENTO;
	}

	@Override
	public void accept(ISancionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Apercibimiento " + DateUtil.dateToString(getFechaSancion()) + " - Motivo: " + getMotivo();
	}

}