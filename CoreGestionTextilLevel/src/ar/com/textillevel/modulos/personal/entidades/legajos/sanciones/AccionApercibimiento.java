package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.clarin.fwjava.util.StringUtil;

@Entity
@DiscriminatorValue(value="ACCAP")
public class AccionApercibimiento extends AccionSancion<Apercibimiento> {

	private static final long serialVersionUID = -3216481280062415666L;

	@Override
	@Transient
	public String getDescrResumen() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append("Ingreso de Apercibimiento.").append("\n");
		sb.append("Motivo: " + getSancion().getMotivo());
		if(!StringUtil.isNullOrEmpty(getSancion().getObservaciones())) {
			sb = sb.append("\n").append("Observaciones: " + getSancion().getObservaciones());
		}
		return sb.toString();
	}

	@Override
	public String calculateNombreDocumento() {
		return "APERC-" + getId();
	}


}