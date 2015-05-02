package ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.ETipoAntiFichada;

@Entity
@DiscriminatorValue(value = "AF_P")
public class AntiFichadaParcial extends AntiFichada implements Serializable {

	private static final long serialVersionUID = -6010898363339080230L;

	private Timestamp fechaHora;
	private Boolean entrada;

	@Column(name = "A_FECHA_HORA", nullable = true)
	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	@Column(name = "A_ENTRADA", nullable = true, columnDefinition = "INTEGER UNSIGNED DEFAULT 0")
	public Boolean getEntrada() {
		return entrada;
	}

	public void setEntrada(Boolean entrada) {
		this.entrada = entrada;
	}

	@Override
	@Transient
	public ETipoAntiFichada getTipoAntiFichada() {
		return ETipoAntiFichada.PARCIAL;
	}

}
