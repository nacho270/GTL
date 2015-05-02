package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.clarin.fwjava.util.DateUtil;

@Entity
@Table(name = "T_PERS_REG_VAC_LEG")
public class RegistroVacacionesLegajo implements Serializable {

	private static final long serialVersionUID = -2603469151538720543L;

	private Integer id;
	private Date fechaDesde;
	private Date fechaHasta;
	private Integer diasHabiles;
	private Integer totalDiasTomados;
	private Integer diasConfiguracion;
	private Integer diasRemanentes;
	private Boolean notificadoSalida;
	private Boolean notificadoVuelta;
	private Integer anioCorrespondiente;
	private LegajoEmpleado legajo;

	public RegistroVacacionesLegajo() {
		notificadoSalida = false;
		notificadoVuelta = false;
	}

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

	@Column(name = "A_DIAS_HABILES", nullable = false)
	public Integer getDiasHabiles() {
		return diasHabiles;
	}

	public void setDiasHabiles(Integer diasHabiles) {
		this.diasHabiles = diasHabiles;
	}

	@Column(name = "A_DIAS_TOTAL_TOMADOS", nullable = false)
	public Integer getTotalDiasTomados() {
		return totalDiasTomados;
	}

	public void setTotalDiasTomados(Integer totalDiasTomados) {
		this.totalDiasTomados = totalDiasTomados;
	}

	@Column(name = "A_DIAS_CONFIGURACION", nullable = false)
	public Integer getDiasConfiguracion() {
		return diasConfiguracion;
	}

	public void setDiasConfiguracion(Integer diasConfiguracion) {
		this.diasConfiguracion = diasConfiguracion;
	}

	@Column(name = "A_DIAS_REMANENTES", nullable = false)
	public Integer getDiasRemanentes() {
		return diasRemanentes;
	}

	public void setDiasRemanentes(Integer diasRemanentes) {
		this.diasRemanentes = diasRemanentes;
	}

	@Column(name = "A_NOTIF_SALIDA", nullable = true, columnDefinition = "INTEGER UNSIGNED DEFAULT 0")
	public Boolean getNotificadoSalida() {
		return notificadoSalida;
	}

	public void setNotificadoSalida(Boolean notificadoSalida) {
		this.notificadoSalida = notificadoSalida;
	}

	@Column(name = "A_NOTIF_VUELTA", nullable = true, columnDefinition = "INTEGER UNSIGNED DEFAULT 0")
	public Boolean getNotificadoVuelta() {
		return notificadoVuelta;
	}

	public void setNotificadoVuelta(Boolean notificadoVuelta) {
		this.notificadoVuelta = notificadoVuelta;
	}

	@Column(name = "A_ANIO_CORRESPONDIENTE", nullable = false)
	public Integer getAnioCorrespondiente() {
		return anioCorrespondiente;
	}

	public void setAnioCorrespondiente(Integer anioCorrespondiente) {
		this.anioCorrespondiente = anioCorrespondiente;
	}
	
	@Transient
	public boolean seSolapa(Date fechaDesde, Date fechaHasta){
		return DateUtil.isBetween(getFechaDesde(), getFechaHasta(), fechaDesde) || DateUtil.isBetween(getFechaDesde(), getFechaHasta(), fechaHasta);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anioCorrespondiente == null) ? 0 : anioCorrespondiente.hashCode());
		result = prime * result + ((fechaDesde == null) ? 0 : fechaDesde.hashCode());
		result = prime * result + ((fechaHasta == null) ? 0 : fechaHasta.hashCode());
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
		RegistroVacacionesLegajo other = (RegistroVacacionesLegajo) obj;
		if (anioCorrespondiente == null) {
			if (other.anioCorrespondiente != null)
				return false;
		} else if (!anioCorrespondiente.equals(other.anioCorrespondiente))
			return false;
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
		return true;
	}

	@ManyToOne
	@JoinColumn(name="F_LEGAJO_P_ID",insertable=false,updatable=false,nullable=false)
	public LegajoEmpleado getLegajo() {
		return legajo;
	}
	
	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}
}
