package ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.Quincena;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.enums.ETipoAsignacion;

@Entity
@Table(name = "T_PERS_ASIGNACION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class Asignacion implements Serializable {

	private static final long serialVersionUID = -8131025654585487956L;

	private Integer id;
	private Date fechaDesde;
	private Date fechaHasta;
	private Sindicato sindicato;
	private List<Asignacion> adicionales;
	private Quincena quincena; //especifica la quincena a aplicar la asignaciòn. Si es == null, aplica en ambas. Solo tiene sentido si sindicato.getTipoCobro()==ETipoCobro.QUINCENAL 

	public Asignacion() {
		this.adicionales = new ArrayList<Asignacion>();
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

	@ManyToOne
	@JoinColumn(name="F_QUINCENA_P_ID",nullable=true)
	public Quincena getQuincena() {
		return quincena;
	}

	public void setQuincena(Quincena quincena) {
		this.quincena = quincena;
	}

	@ManyToMany
	@JoinTable(name = "T_PERS_ASIG_ADIC_ASOC", 
			joinColumns = { @JoinColumn(name = "F_ASIG_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_ASIG_ADICIONAL_P_ID") })
	public List<Asignacion> getAdicionales() {
		return adicionales;
	}

	public void setAdicionales(List<Asignacion> adicionales) {
		this.adicionales = adicionales;
	}

	@Transient
	public abstract ETipoAsignacion getTipo();

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
		Asignacion other = (Asignacion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}