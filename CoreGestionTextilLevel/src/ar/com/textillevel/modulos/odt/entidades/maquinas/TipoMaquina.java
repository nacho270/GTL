package ar.com.textillevel.modulos.odt.entidades.maquinas;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.IndexColumn;

import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

@Entity
@Table(name="T_TIPO_MAQUINA")
public class TipoMaquina implements Serializable {

	private static final long serialVersionUID = -3370772817218825129L;

	private Integer id;
	private String nombre;
	private Byte orden;
	private Byte idTipoSector;
	private List<ProcesoTipoMaquina> procesos;
	
	public TipoMaquina() {
		this.procesos = new ArrayList<ProcesoTipoMaquina>();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NOMBRE",nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name="A_ORDEN",nullable=false)
	public Byte getOrden() {
		return orden;
	}

	public void setOrden(Byte orden) {
		this.orden = orden;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_TIPO_MAQUINA_P_ID", nullable=false)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@IndexColumn(name="A_ORDEN", base=1)
	public List<ProcesoTipoMaquina> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<ProcesoTipoMaquina> procesos) {
		this.procesos = procesos;
	}

	@Column(name="A_ID_SECTOR",nullable=false)
	private Byte getIdTipoSector() {
		return idTipoSector;
	}

	private void setIdTipoSector(Byte idTipoSector) {
		this.idTipoSector = idTipoSector;
	}
	
	@Transient
	public ESectorMaquina getSectorMaquina(){
		return ESectorMaquina.getById(getIdTipoSector());
	}
	
	public void setSectorMaquina(ESectorMaquina sector){
		setIdTipoSector(sector.getId());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((orden == null) ? 0 : orden.hashCode());
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
		TipoMaquina other = (TipoMaquina) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (orden == null) {
			if (other.orden != null)
				return false;
		} else if (!orden.equals(other.orden))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return nombre.toUpperCase();
	}

	@Transient
	public ProcesoTipoMaquina getProcesoById(Integer idProceso) {
		for(ProcesoTipoMaquina p : getProcesos()) {
			if(p.getId().equals(idProceso)) {
				return p;
			}
		}
		return null;
	}
}
