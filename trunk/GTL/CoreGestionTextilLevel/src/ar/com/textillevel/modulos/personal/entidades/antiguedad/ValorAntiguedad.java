package ar.com.textillevel.modulos.personal.entidades.antiguedad;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_PERS_VALOR_ANT")
public class ValorAntiguedad implements Serializable {

	private static final long serialVersionUID = -188883745460435967L;

	private Integer id;
	private BigDecimal valorDefault;
	private Integer antiguedad;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_VALOR_DEFAULT", nullable = false)
	public BigDecimal getValorDefault() {
		return valorDefault;
	}

	public void setValorDefault(BigDecimal valorDefault) {
		this.valorDefault = valorDefault;
	}

	@Column(name = "A_ANTIGUEDAD", nullable = false)
	public Integer getAntiguedad() {
		return antiguedad;
	}

	public void setAntiguedad(Integer antiguedad) {
		this.antiguedad = antiguedad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((antiguedad == null) ? 0 : antiguedad.hashCode());
		result = prime * result + ((valorDefault == null) ? 0 : valorDefault.hashCode());
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
		ValorAntiguedad other = (ValorAntiguedad) obj;
		if (antiguedad == null) {
			if (other.antiguedad != null)
				return false;
		} else if (!antiguedad.equals(other.antiguedad))
			return false;
		if (valorDefault == null) {
			if (other.valorDefault != null)
				return false;
		} else if (!valorDefault.equals(other.valorDefault))
			return false;
		return true;
	}

}
