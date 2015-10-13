package ar.com.fwcommon.entidades;

import java.io.Serializable;

public class GrupoAccion implements Serializable {

	private static final long serialVersionUID = -6056530427887108093L;

	private Integer idGrupoAccion ;
	private String nombre ;
	private Integer orden ;

	public Integer getIdGrupoAccion() {
		return idGrupoAccion;
	}
	public void setIdGrupoAccion(Integer idGrupoAccion) {
		this.idGrupoAccion = idGrupoAccion;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	@Override
	public String toString() {
		return getNombre();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idGrupoAccion == null) ? 0 : idGrupoAccion.hashCode());
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
		final GrupoAccion other = (GrupoAccion) obj;
		if (idGrupoAccion == null) {
			if (other.idGrupoAccion != null)
				return false;
		}
		return idGrupoAccion.equals(other.idGrupoAccion);
	}

}
