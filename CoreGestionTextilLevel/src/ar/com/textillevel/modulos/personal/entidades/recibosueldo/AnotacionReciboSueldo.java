package ar.com.textillevel.modulos.personal.entidades.recibosueldo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.ETipoAnotacionReciboSueldo;

@Entity
@Table(name = "T_PERS_ANOTACION_RS")
public class AnotacionReciboSueldo implements Serializable, Comparable<AnotacionReciboSueldo> {

	private static final long serialVersionUID = 7831478923210978977L;

	private Integer id;
	private Integer idTipoAnotacion;
	private Date fecha;
	private Date fechaHasta;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_ID_TIPO_ANOTACION", nullable=false)
	private Integer getIdTipoAnotacion() {
		return idTipoAnotacion;
	}

	private void setIdTipoAnotacion(Integer idTipoAnotacion) {
		this.idTipoAnotacion = idTipoAnotacion;
	}

	@Transient
	public ETipoAnotacionReciboSueldo getTipoAnotacion() {
		if (getIdTipoAnotacion() == null) {
			return null;
		}
		return ETipoAnotacionReciboSueldo.getById(getIdTipoAnotacion());
	}

	public void setTipoAnotacion(ETipoAnotacionReciboSueldo tipoAnotacion) {
		if (tipoAnotacion == null) {
			setIdTipoAnotacion(null);
			return;
		}
		setIdTipoAnotacion(tipoAnotacion.getId());
	}

	@Column(name = "A_FECHA", nullable=false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "A_FECHA_HASTA", nullable=true)
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	@Override
	public String toString() {
		if(getFecha().compareTo(getFechaHasta()) == 0) {
			return DateUtil.dateToString(getFecha(), "dd");
		} else {
			return DateUtil.dateToString(getFecha(), "dd") + "-" + DateUtil.dateToString(getFechaHasta(), "dd");
		}
	}

	public int compareTo(AnotacionReciboSueldo o) {
		return getFecha().compareTo(o.getFecha());
	}

}