package ar.com.textillevel.modulos.personal.entidades.calenlaboral;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "T_PERS_RANGO_DIAS_FERIADO")
public class RangoDiasFeriado implements Serializable, Comparable<RangoDiasFeriado> {

	private static final long serialVersionUID = 7587199626801179394L;

	public Integer id;
	private String descripcion;
	private Date desde;
	private Date hasta;
	private List<ConfigFormaPagoSindicato> configsFormasPagoSindicatos;

	public RangoDiasFeriado() {
		this.configsFormasPagoSindicatos = new ArrayList<ConfigFormaPagoSindicato>();
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

	@Column(name = "A_DESCRIPCION", nullable = false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "A_DESDE", nullable = false)
	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	@Column(name = "A_HASTA", nullable = false)
	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_RANGO_DIAS_FERIADO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ConfigFormaPagoSindicato> getConfigsFormasPagoSindicatos() {
		return configsFormasPagoSindicatos;
	}

	public void setConfigsFormasPagoSindicatos(List<ConfigFormaPagoSindicato> configsFormasPagoSindicatos) {
		this.configsFormasPagoSindicatos = configsFormasPagoSindicatos;
	}

	public boolean seSolapaCon(RangoDiasFeriado f) {
		return (!f.getDesde().before(getDesde()) && !f.getDesde().after(getDesde()))  ||
			   (!f.getDesde().before(getHasta()) && !f.getDesde().after(getHasta()));
	}

	@Override
	public String toString() {
		return getDescripcion();
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
		RangoDiasFeriado other = (RangoDiasFeriado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public boolean contiene(Date fecha) {
		return !getDesde().before(fecha) && !fecha.after(getHasta());
	}

	public int compareTo(RangoDiasFeriado o) {
		return getDesde().compareTo(o.getDesde());
	}

}