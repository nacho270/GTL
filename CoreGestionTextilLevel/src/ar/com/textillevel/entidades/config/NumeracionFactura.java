package ar.com.textillevel.entidades.config;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;

@Entity
@Table(name = "T_NUMERACION_FACTURA")
public class NumeracionFactura implements Serializable {

	private static final long serialVersionUID = -6170242249316977159L;

	private Integer id;
	private Date fechaDesde;
	private Date fechaHasta;
	private Integer nroDesde;
	private Integer nroHasta;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_FECHA_DESDE", nullable = false)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Column(name = "A_FECHA_HASTA", nullable = false)
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	@Column(name = "A_NRO_DESDE", nullable = false)
	public Integer getNroDesde() {
		return nroDesde;
	}

	public void setNroDesde(Integer nroDesde) {
		this.nroDesde = nroDesde;
	}

	@Column(name = "A_NRO_HASTA", nullable = false)
	public Integer getNroHasta() {
		return nroHasta;
	}

	public void setNroHasta(Integer nroHasta) {
		this.nroHasta = nroHasta;
	}

	@Transient
	public boolean seSolapaEnNumero(NumeracionFactura numeracion){
		return seSolapaEnNumero(numeracion.getNroDesde(),numeracion.getNroHasta());
	}
	
	@Transient
	private boolean seSolapaEnNumero(Integer nroDesde, Integer nroHasta) {
		return (getNroDesde()>=nroDesde && getNroDesde()<=nroHasta) || (getNroHasta()>=nroDesde && getNroHasta()<=nroHasta);
	}

	@Transient
	public boolean seSolapaEnFecha(NumeracionFactura numeracion){
		return seSolapaEnFecha(numeracion.getFechaDesde(),numeracion.getFechaHasta());
	}
	
	@Transient
	private boolean seSolapaEnFecha(Date fechaDesde, Date fechaHasta) {
		return DateUtil.isBetweenConExtremos(fechaDesde, fechaHasta, getFechaDesde()) || DateUtil.isBetweenConExtremos(fechaDesde, fechaHasta, getFechaHasta()) ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaDesde == null) ? 0 : fechaDesde.hashCode());
		result = prime * result + ((fechaHasta == null) ? 0 : fechaHasta.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nroDesde == null) ? 0 : nroDesde.hashCode());
		result = prime * result + ((nroHasta == null) ? 0 : nroHasta.hashCode());
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
		NumeracionFactura other = (NumeracionFactura) obj;
		if (fechaDesde == null) {
			if (other.fechaDesde != null)
				return false;
		} else if (!fechaDesde.equals(other.fechaDesde))
			return false;
		if (fechaHasta == null) {
			if (other.fechaHasta != null)
				return false;
		} else if (!fechaHasta.equals(other.fechaHasta))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nroDesde == null) {
			if (other.nroDesde != null)
				return false;
		} else if (!nroDesde.equals(other.nroDesde))
			return false;
		if (nroHasta == null) {
			if (other.nroHasta != null)
				return false;
		} else if (!nroHasta.equals(other.nroHasta))
			return false;
		return true;
	}

}
