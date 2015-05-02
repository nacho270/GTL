package ar.com.textillevel.modulos.personal.entidades.legajos.tareas;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.clarin.fwjava.util.DateUtil;

@Entity
@Table(name = "T_PERS_CONFIG_VAL_HOR_CAT")
public class ConfiguracionValorHoraCategoria implements Serializable {

	private static final long serialVersionUID = -1490447500355716537L;

	private Integer id;
	private Date fechaDesde;
	private Date fechaHasta;
	private Sindicato sindicato;
	private List<CategoriaValorPuesto> categoriasValoresPuesto;

	public ConfiguracionValorHoraCategoria() {
		this.categoriasValoresPuesto = new ArrayList<CategoriaValorPuesto>();
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

	@Column(name = "A_FECHA_DESDE", nullable=false)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Column(name = "A_FECHA_HASTA", nullable=false)
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
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
	@JoinColumn(name="F_CONFIG_VAL_HOR_CAT_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<CategoriaValorPuesto> getCategoriasValoresPuesto() {
		return categoriasValoresPuesto;
	}

	public void setCategoriasValoresPuesto(List<CategoriaValorPuesto> categoriasValoresPuesto) {
		this.categoriasValoresPuesto = categoriasValoresPuesto;
	}

	@Override
	public String toString() {
		return DateUtil.dateToString(fechaDesde) + " - " + DateUtil.dateToString(fechaHasta); 
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
		ConfiguracionValorHoraCategoria other = (ConfiguracionValorHoraCategoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getValorHoraParaPuestoAndCategoria(Categoria cat, Puesto puesto) {
		for(CategoriaValorPuesto cvp : getCategoriasValoresPuesto()) {
			if(cvp.getCategoria().equals(cat)) {
				return cvp.getValorHoraParaPuesto(puesto);
			}
		}
		return null;
	}

}