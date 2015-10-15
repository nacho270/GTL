package ar.com.fwcommon.entidades;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.fwcommon.auditoria.AuditoriaException;
import ar.com.fwcommon.auditoria.CloneableAuditoria;
import ar.com.fwcommon.util.DateUtil;

@Entity
@Table(name="T_Vigencia")
public class Vigencia implements Serializable, CloneableAuditoria, Comparable<Vigencia> {

	private static final long serialVersionUID = 1L;
	private int idVigencia;
	private Date fechaDesde;
	private Date fechaHasta;
	private String descripcion;

	/** Método constructor */
	public Vigencia() {
	}

	/**
	 * Método constructor.
	 * 
	 * @param fechaDesde
	 *            La fecha inicial de la vigencia.
	 * @param fechaHasta
	 *            La fecha final de la vigencia.
	 */
	public Vigencia(Date fechaDesde, Date fechaHasta) {
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
	}

	/**
	 * Método constructor.
	 * 
	 * @param fechaDesde
	 *            La fecha inicial de la vigencia.
	 * @param fechaHasta
	 *            La fecha final de la vigencia.
	 * @param descripcion
	 *            La descripción de la vigencia.
	 */
	public Vigencia(Date fechaDesde, Date fechaHasta, String descripcion) {
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve el <b>id</b> de la vigencia.
	 * 
	 * @return idVigencia El nro. de id de la vigencia.
	 */
	@Id
	@Column(name="P_IdVigencia")
	@GeneratedValue
	public int getIdVigencia() {
		return idVigencia;
	}

	/**
	 * Setea el <b>id</b> de la vigencia.
	 * 
	 * @param idVigencia
	 *            El nro. de id de la vigencia.
	 */
	public void setIdVigencia(int idVigencia) {
		this.idVigencia = idVigencia;
	}

	/**
	 * Devuelve la fecha <b>desde</b> de la vigencia.
	 * 
	 * @return fechaDesde La fecha desde o fecha inicial de la vigencia.
	 */
	@Column(name="A_FechaDesde")
	public Date getFechaDesde() {
		return fechaDesde;
	}

	/**
	 * Setea la fecha <b>desde</b> de la vigencia.
	 * 
	 * @param fechaDesde
	 *            La fecha desde o fecha inicial de la vigencia.
	 */
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	/**
	 * Devuelve la fecha <b>hasta</b> de la vigencia.
	 * 
	 * @return fechaHasta La fecha hasta o fecha final de la vigencia.
	 */
	@Column(name="A_FechaHasta")
	public Date getFechaHasta() {
		return fechaHasta;
	}

	/**
	 * Setea la fecha <b>hasta</b> de la vigencia.
	 * 
	 * @param fechaHasta
	 *            La fecha hasta o fecha final de la vigencia.
	 */
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	/**
	 * Devuelve la <b>descripción</b> de la vigencia.
	 * 
	 * @return descripcion La descripción de la vigencia.
	 */
	@Column(name="A_Descripcion")
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Setea la <b>descripción</b> de la vigencia.
	 * 
	 * @param descripcion
	 *            La descripción de la vigencia.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve <b>true</b> si la fecha pasada como parámetro se encuentra
	 * entre las fechas <b>fechaDesde</b> y <b>fechaHasta</b> inclusive.
	 * 
	 * @param fecha
	 *            La fecha a saber si es vigente.
	 * @return
	 */
	public boolean esVigente(Date fecha) {
		if(!fecha.before(fechaDesde))
			if(fechaHasta == null || !fecha.after(fechaHasta))
				return true;
		return false;
	}

	/**
	 * Clona una <b>vigencia</b> y devuelve una copia de la misma.
	 * 
	 * @return vigencia
	 */
	public Vigencia clonar() {
		Vigencia vigencia = new Vigencia();
		vigencia.setDescripcion(descripcion);
		vigencia.setFechaDesde(fechaDesde);
		vigencia.setFechaHasta(fechaHasta);
		return vigencia;
	}

	/** Sobreescritura del método equals de object */
	// TODO: Tener en cuenta fechas nulas
	public boolean equals(Object obj) {
		if(!(obj instanceof Vigencia))
			return false;
		else if(fechaDesde.equals(((Vigencia)obj).getFechaDesde())) {
			if(fechaHasta == null && ((Vigencia)obj).getFechaHasta() == null)
				return true;
			else if(fechaHasta == null || ((Vigencia)obj).getFechaHasta() == null)
				return false;
			else if(fechaDesde.equals(((Vigencia)obj).getFechaDesde()) && fechaHasta.equals(((Vigencia)obj).getFechaHasta()))
				return true;
		}
		return false;
	}

	public Object deepClone() throws AuditoriaException {
		Vigencia copia;
		try {
			copia = (Vigencia)super.clone();
		} catch(CloneNotSupportedException e) {
			throw new Error("This should not occur since we implement Cloneable");
		}
		return copia;
	}

	public int compareTo(Vigencia v) {
		// return fechaDesde.compareTo(((Vigencia)v).getFechaDesde());
		return idVigencia - v.getIdVigencia();
	}

	/**
	 * Sólo tiene sentido si las vigencias no están superpuestas.
	 * 
	 * @param vigencia
	 *            La vigencia a comparar
	 * @return <code>true</code> si es menor a <code>vigencia</code>
	 */
	public boolean esMenor(Vigencia vigencia) {
		if(getFechaHasta() == null) {
			return false;
		} else if(vigencia.getFechaHasta() == null) {
			return true;
		} else {
			return getFechaHasta().before(vigencia.getFechaDesde());
		}
	}

	/**
	 * Retorna la descripción de la instancia. Se utiliza para el mecanismo de
	 * auditoría.
	 * 
	 * @return
	 */
	@Transient
	public String getDescripcionInstancia() {
		return toString();
	}

	public String toString() {
		return "["
				+ (getFechaDesde() != null ? DateUtil.dateToString(getFechaDesde()) : "Fecha Desde Abierta")
				+ " - "
				+ (getFechaHasta() != null ? DateUtil.dateToString(getFechaHasta()) : "Fecha Hasta Abierta")
				+ "]";
	}

	public boolean deepEquals(Vigencia vigencia) {
		return equalsOrBothNull(getFechaDesde(), vigencia.getFechaDesde())
				&& equalsOrBothNull(getFechaHasta(), vigencia.getFechaHasta());
	}

	private boolean equalsOrBothNull(Date fecha1, Date fecha2) {
		if(fecha1 == null && fecha2 == null) {
			return true;
		} else if(fecha1 != null && fecha2 != null) {
			return fecha1.equals(fecha2);
		} else {
			return false;
		}
	}

	/**
	 * @param vigencia
	 *            La vigencia a analizar. Debe ser distinta de <code>null</code>
	 * @return <code>true</code> Si contiene totalmente a
	 *         <code>vigencia</code>
	 */
	public boolean contiene(Vigencia vigencia) {
		return !getFechaDesde().after(vigencia.getFechaDesde())
				&& (getFechaHasta() == null || !getFechaHasta().before(
						vigencia.getFechaHasta()));
	}

	/**
	 * @param fecha
	 * @return <code>true</code> si contiene a <code>fecha</code>
	 */
	public boolean contiene(Date fecha) {
		if(fecha == null) {
			return false;
		} else {
			if(getFechaDesde() == null && getFechaHasta() == null) {
				return true;
			} else if(getFechaDesde() == null) {
				return getFechaHasta().after(fecha);
			} else if(getFechaHasta() == null) {
				return getFechaDesde().before(fecha);
			}
			return getFechaDesde().before(fecha) && getFechaHasta().after(fecha);
		}
	}

	public boolean contieneInclusive(Date fecha) {
		if(fecha == null) {
			return false;
		} else {
			if(getFechaDesde() == null && getFechaHasta() == null) {
				return true;
			} else if(getFechaDesde() == null) {
				return getFechaHasta().after(fecha);
			} else if(getFechaHasta() == null) {
				return getFechaDesde().before(fecha);
			}
			return getFechaDesde().equals(fecha) || getFechaHasta().equals(fecha) || (getFechaDesde().before(fecha) && getFechaHasta().after(fecha));
		}
	}

	/**
	 * Devuelve <b>true</b> si las vigencias se superponen parcial o
	 * totalmente.
	 * 
	 * @param v
	 *            La vigencia a evaluar si se solapa.
	 * @return
	 */
	public boolean seSolapa(Vigencia vigencia) {
		if((getFechaHasta() != null && vigencia.getFechaDesde() != null && getFechaHasta().before(vigencia.getFechaDesde()))
				|| (vigencia.getFechaHasta() != null && getFechaDesde() != null && vigencia.getFechaHasta().before(getFechaDesde()))) {
			return false;
		}
		return true;
	}

}