package ar.com.fwcommon.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@MappedSuperclass
public class Aplicacion implements Serializable {

	private int idAplicacion;
	private String descripcion;
	private List<Modulo> modulos;

	@Id
	@Column(name="P_IdAplicacion")
	public int getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(int idAplicacion) {
		this.idAplicacion = idAplicacion;
	}
	@Transient
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Transient
	public List<Modulo> getModulos() {
		return modulos;
	}
	@Transient
	public List<Modulo> getModulosOrdenados() {
		List<Modulo> modulosOrdenados = new ArrayList<Modulo>(modulos);
		Collections.sort(modulosOrdenados);
		return modulosOrdenados;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public String toString() {
		return getDescripcion();
	}

}