package ar.com.textillevel.modulos.personal.entidades.calenlaboral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Entity
@Table(name = "T_PERS_CONFIG_FORMA_PAGO_SIND")
public class ConfigFormaPagoSindicato implements Serializable {

	private static final long serialVersionUID = -3259047406161637920L;

	private Integer id;
	private Sindicato sindicato;
	private List<TotalHorasPagoDia> totalHorasPagoPorDias;

	public ConfigFormaPagoSindicato() {
		this.totalHorasPagoPorDias = new ArrayList<TotalHorasPagoDia>();
	}
	
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_SINDICATO_P_ID",nullable=false)
	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_CONFIG_PAGO_SINDICATO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<TotalHorasPagoDia> getTotalHorasPagoPorDias() {
		return totalHorasPagoPorDias;
	}

	public void setTotalHorasPagoPorDias(List<TotalHorasPagoDia> totalHorasPagoPorDias) {
		this.totalHorasPagoPorDias = totalHorasPagoPorDias;
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
		ConfigFormaPagoSindicato other = (ConfigFormaPagoSindicato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}