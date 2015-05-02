package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name="T_PROCESO_TIPO_MAQUINA")
public class ProcesoTipoMaquina implements Serializable {

	private static final long serialVersionUID = 3839208269795625161L;

	private Integer id;
	private String nombre;
	private Integer orden;
	private Boolean requiereMuestra;
	private List<InstruccionProcedimiento> instrucciones;
	private List<ProcedimientoTipoArticulo> procedimientos;

	public ProcesoTipoMaquina() {
		this.instrucciones = new ArrayList<InstruccionProcedimiento>();
		this.procedimientos = new ArrayList<ProcedimientoTipoArticulo>();
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

	@Column(name="A_ORDEN", insertable=false, updatable=false)
	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_PROCESO_P_ID",nullable=false)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ProcedimientoTipoArticulo> getProcedimientos() {
		return procedimientos;
	}

	public void setProcedimientos(List<ProcedimientoTipoArticulo> procedimientos) {
		this.procedimientos = procedimientos;
	}

	@Column(name="A_REQUIERE_MUESTRA", nullable=true)
	public Boolean getRequiereMuestra() {
		return requiereMuestra;
	}

	public void setRequiereMuestra(Boolean requiereMuestra) {
		this.requiereMuestra = requiereMuestra;
	}

	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="F_PROCESO_P_ID",nullable=true)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<InstruccionProcedimiento> getInstrucciones() {
		return instrucciones;
	}

	public void setInstrucciones(List<InstruccionProcedimiento> instrucciones) {
		this.instrucciones = instrucciones;
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
		ProcesoTipoMaquina other = (ProcesoTipoMaquina) obj;
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

}