package ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.ETipoAntiFichada;

@Entity
@DiscriminatorValue(value = "AF_V")
public class AntiFichadaVigencia extends AntiFichada implements Serializable {

	private static final long serialVersionUID = 469168517641427379L;

	private Date fechaDesde;
	private Date fechaHasta;

	@Column(name = "A_FECHA_DESDE", nullable = true)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Column(name = "A_FECHA_HASTA", nullable = true)
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	@Override
	@Transient
	public ETipoAntiFichada getTipoAntiFichada() {
		return ETipoAntiFichada.VIGENCIA;
	}

}
