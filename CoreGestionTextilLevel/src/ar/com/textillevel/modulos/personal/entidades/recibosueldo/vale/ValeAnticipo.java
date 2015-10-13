package ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;

@Entity
@Table(name = "T_PERS_VALE_ANTICIPO")
public class ValeAnticipo implements Serializable {

	private static final long serialVersionUID = -4278963331619956114L;

	private Integer id;
	private LegajoEmpleado legajo;
	private Integer nroVale;
	private Date fecha;
	private String usuarioLogueado;
	private String concepto;
	private BigDecimal monto;
	private String montoLetras;
	private Integer idEstadoVale;
	
	public ValeAnticipo() {
		setEstadoValeAnticipo(EEstadoValeAnticipo.A_DESCONTAR);
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "F_LEGAJO_P_ID")
	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	@Column(name = "A_NRO_VALE", nullable = false)
	public Integer getNroVale() {
		return nroVale;
	}

	public void setNroVale(Integer nroVale) {
		this.nroVale = nroVale;
	}

	@Column(name = "A_FECHA", nullable = false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "A_USUARIO_LOGUEADO", nullable = false)
	public String getUsuarioLogueado() {
		return usuarioLogueado;
	}

	public void setUsuarioLogueado(String usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}

	@Column(name = "A_CONCEPTO", nullable = false)
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	@Column(name = "A_MONTO", nullable = false)
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "A_MONTO_LETRAS", nullable = false)
	public String getMontoLetras() {
		return montoLetras;
	}

	public void setMontoLetras(String montoLetras) {
		this.montoLetras = montoLetras;
	}
	
	@Column(name="A_ID_ESTADO_VALE",nullable=false)
	private Integer getIdEstadoVale() {
		return idEstadoVale;
	}
	
	private void setIdEstadoVale(Integer idEstadoVale) {
		this.idEstadoVale = idEstadoVale;
	}
	
	@Transient
	public EEstadoValeAnticipo getEstadoVale(){
		return EEstadoValeAnticipo.getById(getIdEstadoVale());
	}
	
	public void setEstadoValeAnticipo(EEstadoValeAnticipo estado){
		if(estado == null){
			setIdEstadoVale(null);
			return;
		}
		setIdEstadoVale(estado.getId());
	}

	@Override
	public String toString() {
		return " $ " + monto + " - " + DateUtil.dateToString(fecha) +  " - concepto: " + getConcepto(); 
	}

}