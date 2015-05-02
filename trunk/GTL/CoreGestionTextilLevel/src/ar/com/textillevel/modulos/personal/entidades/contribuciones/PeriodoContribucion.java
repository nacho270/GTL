package ar.com.textillevel.modulos.personal.entidades.contribuciones;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_PERS_PERIODO_CONTRIBUCION")
public class PeriodoContribucion implements Serializable {

	private static final long serialVersionUID = 3701658005437534608L;

	private Integer id;
	private Date fechaDesde;
	private Date fechaHasta;
	private BigDecimal importeFijo;
	private BigDecimal porcentaje;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_FECHA_DESDE",nullable=false)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Column(name="A_FECHA_HASTA",nullable=false)
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	@Column(name="A_IMPORTE_FIJO", nullable=true)
	public BigDecimal getImporteFijo() {
		return importeFijo;
	}

	public void setImporteFijo(BigDecimal importeFijo) {
		this.importeFijo = importeFijo;
	}

	@Column(name="A_PORCENTAJE", nullable=true)
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	@Transient
	public boolean esVigenteEnFecha(Date fecha) {
		return !fecha.before(getFechaDesde()) && !fecha.after(getFechaHasta());
	}

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
		PeriodoContribucion other = (PeriodoContribucion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}