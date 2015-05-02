package ar.com.textillevel.modulos.odt.entidades.secuencia.fw;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.util.Clonable;

@MappedSuperclass
public abstract class PasoSecuenciaAbstract<P extends ProcedimientoAbstract> implements Serializable, Clonable<PasoSecuenciaAbstract<P>> {

	private static final long serialVersionUID = -3097229631804017608L;

	private Integer id;
	private TipoMaquina sector;
	private ProcesoTipoMaquina proceso;
	private String observaciones;
	private P subProceso;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_SECTOR_P_ID", nullable = false)
	public TipoMaquina getSector() {
		return sector;
	}

	public void setSector(TipoMaquina sector) {
		this.sector = sector;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_PROCESO_P_ID", nullable = false)
	public ProcesoTipoMaquina getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoTipoMaquina proceso) {
		this.proceso = proceso;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name = "F_PROCEDIMIENTO_P_ID", nullable = false)
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public P getSubProceso() {
		return subProceso;
	}

	public void setSubProceso(P subProceso) {
		this.subProceso = subProceso;
	}

	@Column(name = "A_OBSERVACIONES", nullable = true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((proceso == null) ? 0 : proceso.hashCode());
		result = prime * result + ((sector == null) ? 0 : sector.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasoSecuenciaAbstract<P> other = (PasoSecuenciaAbstract<P>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (proceso == null) {
			if (other.proceso != null)
				return false;
		} else if (!proceso.equals(other.proceso))
			return false;
		if (sector == null) {
			if (other.sector != null)
				return false;
		} else if (!sector.equals(other.sector))
			return false;
		return true;
	}

}
