package ar.com.textillevel.modulos.odt.entidades.maquinas;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value="MSTF")
public class MaquinaSectorTerminadoFraccionado extends MaquinaSectorTerminado {

	private static final long serialVersionUID = -8876868042016220879L;

	private TerminacionFraccionado terminacion;

	@ManyToOne
	@JoinColumn(name="F_TERM_FRACCIONADO_P_ID",nullable=true)
	public TerminacionFraccionado getTerminacion() {
		return terminacion;
	}

	public void setTerminacion(TerminacionFraccionado terminacion) {
		this.terminacion = terminacion;
	}

}
