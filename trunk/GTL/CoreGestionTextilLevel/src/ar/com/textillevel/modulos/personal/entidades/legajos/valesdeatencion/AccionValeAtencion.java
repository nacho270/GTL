package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.IAccionHistoricaVisitor;

@Entity
@Table(name = "T_PERS_ACCION_VALE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class AccionValeAtencion<T extends ValeAtencion> extends AccionHistorica implements Serializable {

	private static final long serialVersionUID = -8962019077460100991L;

	private T valeAtencion;
	private Integer idEstadoValeEnfermedad;
	private Date fechaControl;
	

	@ManyToOne(fetch = FetchType.LAZY, targetEntity=ValeAtencion.class)
	@JoinColumn(name = "F_VALE_ATENCION_P_ID", nullable=false)
	public T getValeAtencion() {
		return valeAtencion;
	}

	public void setValeAtencion(T valeAtencion) {
		this.valeAtencion = valeAtencion;
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

	@Override
	public void accept(IAccionHistoricaVisitor visitor) {
		visitor.visit(this);
	}

}