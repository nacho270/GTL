package ar.com.fwcommon.auditoria.ejb;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_TipoEvento")
public class TipoEvento implements Comparable, Serializable {

	private int idTipoEvento;
	private String nombre;
	private int tipo;

	/**Método Constructor*/
	public TipoEvento() {
	}

	/**
	 * Devuelve el id.
	 * @return idTipoEvento
	 */
	@Id
	@GeneratedValue
	@Column(name = "P_IdTipoEvento")
	public int getIdTipoEvento() {
		return idTipoEvento;
	}

	/**
	 * Setea el id.
	 * @param idTipoEvento
	 */
	public void setIdTipoEvento(int idTipoEvento) {
		this.idTipoEvento = idTipoEvento;
	}

	/**
	 * Devuelve el nombre.
	 * @return nombre
	 */
	@Column(name = "A_Nombre")
	public String getNombre() {
		return nombre;
	}

	/**
	 * Setea el nombre.
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el tipo de evento.
	 * @return tipo
	 */
	@Column(name = "A_Tipo")
	public int getTipo() {
		return tipo;
	}

	/**
	 * Setea el tipo de evento.
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/* (non-Javadoc)
	 * java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getNombre();
	}

	/* (non-Javadoc)
	 * java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if(o instanceof TipoEvento)
			return this.getTipo() - ((TipoEvento)o).getTipo();
		return -1;
	}

	/* (non-Javadoc)
	 * java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		TipoEvento tipoEvento = null;
		if(obj instanceof TipoEvento)
			tipoEvento = (TipoEvento)obj;
		if(tipoEvento != null && tipoEvento.getIdTipoEvento() == getIdTipoEvento() && tipoEvento.getTipo() == getTipo())
			return true;
		else
			return false;
	}
}