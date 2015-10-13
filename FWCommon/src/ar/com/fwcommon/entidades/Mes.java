package ar.com.fwcommon.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.fwcommon.auditoria.AuditoriaException;
import ar.com.fwcommon.auditoria.CloneableAuditoria;

@Entity
@Table(name="T_Mes")
public class Mes implements CloneableAuditoria, Serializable {

	private static final long serialVersionUID = 6913887151834889165L;

	private int idMes;
	private int nroMes;
	private String nombre;

	public Mes() {
	}

	public Mes(int nroMes, String nombre) {
		this.nroMes = nroMes;
		this.nombre = nombre;
	}

	/**
	 * Devuelve el <b>id</b> del mes.
	 * @return idMes
	 */
	@Id
	@GeneratedValue
	@Column(name="P_IdMes")
	public int getIdMes() {
        return idMes;
    }

	/**
	 * Setear el <b>id</b> del mes.
	 * @param idMes
	 */
	public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

	/**
	 * Devuelve el <b>número de mes</b> asociado al mes.
	 * @return nroMes
	 */
	@Column(name="A_NumeroMes")
	public int getNroMes() {
        return nroMes;
    }

	/**
	 * Setea el <b>número de mes</b> asociado al mes.
	 * @param nroMes
	 */
	public void setNroMes(int nroMes) {
        this.nroMes = nroMes;
    }

	/**
	 * Devuelve el <b>nombre</b> del mes.
	 * @return nombre
	 */
	@Column(name="A_Nombre")
	public String getNombre() { 
    	return nombre; 
    }

	/**
	 * Setea el <b>nombre</b> del mes.
	 * @param nombre
	 */
	public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }

	/** Sobreescritura del método equals de Object */
	public boolean equals(Object mes) { 
		boolean resultado = false;
		if(!(mes instanceof Mes))
			return resultado;
		if(mes == null)
			return resultado;
		else {
			resultado = (((Mes)mes).getIdMes() == idMes) 
						&& (((Mes)mes).getNroMes() == nroMes) 
						&& (((Mes)mes).getNombre().equals(nombre));  
		}
		return resultado;
	}

	/**
	 * Devuelve una copia del mes en cuestión.
	 * @return mes
	 */
	public Mes clonar() {
		Mes mes = new Mes();
		mes.setIdMes(idMes);
		mes.setNombre(nombre);
		mes.setNroMes(nroMes);
		return mes;
	}

	/** Sobreescritura del método toString de Object */
	public String toString() {
		return nombre;
	}

	public Object deepClone() throws AuditoriaException {
    	Mes copia;
		try {
			copia = (Mes) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(
					"This should not occur since we implement Cloneable");
		}
		return copia;
	}
}