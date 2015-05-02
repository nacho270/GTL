package ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_PERS_VALOR_CONC_FECHA")
public class ValorConceptoFecha implements Serializable {

	private static final long serialVersionUID = -739790306870692713L;

	private Integer id;
	private BigDecimal valorNumerico;
	private BigDecimal valorPorcentual;
	private Date fechaDesde;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_VALOR_NUMERICO", nullable = true)
	public BigDecimal getValorNumerico() {
		return valorNumerico;
	}

	public void setValorNumerico(BigDecimal valorNumerico) {
		this.valorNumerico = valorNumerico;
	}

	@Column(name = "A_VALOR_PORCENTUAL", nullable = true)
	public BigDecimal getValorPorcentual() {
		return valorPorcentual;
	}

	public void setValorPorcentual(BigDecimal valorPorcentual) {
		this.valorPorcentual = valorPorcentual;
	}

	@Column(name = "A_FECHA_DESDE", nullable = false)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaDesde == null) ? 0 : fechaDesde.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valorNumerico == null) ? 0 : valorNumerico.hashCode());
		result = prime * result + ((valorPorcentual == null) ? 0 : valorPorcentual.hashCode());
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
		ValorConceptoFecha other = (ValorConceptoFecha) obj;
		if (fechaDesde == null) {
			if (other.fechaDesde != null)
				return false;
		} else if (!fechaDesde.equals(other.fechaDesde))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valorNumerico == null) {
			if (other.valorNumerico != null)
				return false;
		} else if (!valorNumerico.equals(other.valorNumerico))
			return false;
		if (valorPorcentual == null) {
			if (other.valorPorcentual != null)
				return false;
		} else if (!valorPorcentual.equals(other.valorPorcentual))
			return false;
		return true;
	}

}
