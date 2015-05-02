package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;

@Entity
@Table(name = "T_PERS_VALE_ATENCION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class ValeAtencion implements Serializable {

	private static final long serialVersionUID = -3540883928764569731L;

	private Integer id;
	private Date fechaVale;
	private LegajoEmpleado legajo;
	private Integer idEstadoValeEnfermedad;
	private Horario horarioSalida;	
	private Date fechaAlta;
	private Date fechaControl;
	private Boolean asistioAlTrabajo;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_FECHA", nullable=false)
	public Date getFechaVale() {
		return fechaVale;
	}

	public void setFechaVale(Date fechaVale) {
		this.fechaVale = fechaVale;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_LEGAJO_P_ID", nullable=false)
	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	@Column(name = "A_ID_ESTADO", nullable=true)
	private Integer getIdEstadoValeEnfermedad() {
		return idEstadoValeEnfermedad;
	}

	private void setIdEstadoValeEnfermedad(Integer idEstadoValeEnfermedad) {
		this.idEstadoValeEnfermedad = idEstadoValeEnfermedad;
	}

	@Transient
	public EEStadoValeEnfermedad getEstadoValeAtencion() {
		if (getIdEstadoValeEnfermedad() == null) {
			return null;
		}
		return EEStadoValeEnfermedad.getById(getIdEstadoValeEnfermedad());
	}

	public void setEstadoValeAtencion(EEStadoValeEnfermedad estadoValeEnfermedad) {
		if (estadoValeEnfermedad == null) {
			this.setIdEstadoValeEnfermedad(null);
		}
		setIdEstadoValeEnfermedad(estadoValeEnfermedad.getId());
	}

	@Column(name = "A_FECHA_CONTROL")
	public Date getFechaControl() {
		return fechaControl;
	}

	public void setFechaControl(Date fechaControl) {
		this.fechaControl = fechaControl;
	}

	@Column(name = "A_FECHA_ALTA")
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "A_ASISTIO_AL_TRABAJO")
	public Boolean getAsistioAlTrabajo() {
		return asistioAlTrabajo;
	}

	public void setAsistioAlTrabajo(Boolean asistioAlTrabajo) {
		this.asistioAlTrabajo = asistioAlTrabajo;
	}

	 @Embedded
	 @AttributeOverrides( {
	            @AttributeOverride(name="horas", column = @Column(name="A_HORAS_SALIDA", nullable=true) ),
	            @AttributeOverride(name="minutos", column = @Column(name="A_MINUTOS_SALIDA", nullable=true) )
	    } )
	 public Horario getHorarioSalida() {
		return horarioSalida;
	}

	public void setHorarioSalida(Horario horarioSalida) {
		this.horarioSalida = horarioSalida;
	}

	@Transient
	public abstract String getResumen();

	@Transient
	public abstract ETipoVale getTipoVale();

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
		ValeAtencion other = (ValeAtencion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}