package ar.com.textillevel.modulos.personal.entidades.calenlaboral;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "T_PERS_CALEN_ANUAL_FERIADO")
public class CalendarioAnualFeriado implements Serializable  {

	private static final long serialVersionUID = -6695933970502886502L;

	private Integer anio;
	private List<RangoDiasFeriado> feriados;
	private List<ConfigFormaPagoSindicato> configsFormasPagoSindicatos;

	public CalendarioAnualFeriado() {
		this.feriados = new ArrayList<RangoDiasFeriado>();
		this.configsFormasPagoSindicatos = new ArrayList<ConfigFormaPagoSindicato>();
	}

	@Id
	@Column(name = "P_ID")
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_CALEN_ANUAL_FERIADO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<RangoDiasFeriado> getFeriados() {
		return feriados;
	}

	public void setFeriados(List<RangoDiasFeriado> feriados) {
		this.feriados = feriados;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_CALEN_ANUAL_FERIADO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ConfigFormaPagoSindicato> getConfigsFormasPagoSindicatos() {
		return configsFormasPagoSindicatos;
	}

	public void setConfigsFormasPagoSindicatos(List<ConfigFormaPagoSindicato> configsFormasPagoSindicatos) {
		this.configsFormasPagoSindicatos = configsFormasPagoSindicatos;
	}

	@Transient
	public RangoDiasFeriado getRangoEnFecha(Date fecha) {
		for(RangoDiasFeriado r : getFeriados()) {
			if(r.contiene(fecha)) {
				return r;
			}
		}
		return null;
	}

	@Transient
	public boolean isFeriado(Date fecha) {
		return getRangoEnFecha(fecha) != null;
	}

	@Override
	public String toString() {
		return String.valueOf(anio);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anio == null) ? 0 : anio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalendarioAnualFeriado other = (CalendarioAnualFeriado) obj;
		if (anio == null) {
			if (other.anio != null)
				return false;
		} else if (!anio.equals(other.anio))
			return false;
		return true;
	}

}