package ar.com.textillevel.modulos.personal.entidades.legajos.tareas;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_PERS_VALOR_PUESTO")
public class ValorPuesto implements Serializable {

	private static final long serialVersionUID = -6500452347255726355L;

	private Integer id;
	private BigDecimal valorHora;
	private Puesto puesto;

	public ValorPuesto() {
		valorHora = new BigDecimal(0);
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_VALOR_HORA", nullable=false)
	public BigDecimal getValorHora() {
		return valorHora;
	}

	public void setValorHora(BigDecimal valorHora) {
		this.valorHora = valorHora;
	}

	@ManyToOne
	@JoinColumn(name="F_PUESTO_P_ID",nullable=false)
	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ValorPuesto other = (ValorPuesto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}