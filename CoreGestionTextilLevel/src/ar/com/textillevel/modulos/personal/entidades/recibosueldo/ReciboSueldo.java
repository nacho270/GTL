package ar.com.textillevel.modulos.personal.entidades.recibosueldo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

import ar.com.fwcommon.entidades.Mes;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoReciboSueldo;

@Entity
@Table(name = "T_PERS_RECIBO_SUELDO", uniqueConstraints=@UniqueConstraint(columnNames={"F_LEGAJO_P_ID","A_TEXTO_ORDEN"}))
public class ReciboSueldo implements Serializable {

	private static final long serialVersionUID = -2034642888002125579L;

	private Integer id;
	private LegajoEmpleado legajo;
	private Integer anio;
	private Mes mes;
	private Quincena quincena;
	private Integer diasTrabajados;
	private BigDecimal valorHora;
	private BigDecimal bruto;
	private BigDecimal neto;
	private BigDecimal totalRetenciones;
	private BigDecimal totalNoRemunerativo;
	private BigDecimal totalDeducciones;
	private Integer idEstadoReciboSueldo;
	private String textoOrden; //String con formato yyyy[NRO_MES][QUINCENA] que permite tener un orden cronológico entre los recibos de sueldo para un legajo
	private Date fechaUltDeposito;
	private Date fechaPago;
	private List<ItemReciboSueldo> items;
	private List<AnotacionReciboSueldo> anotaciones;

	public ReciboSueldo() {
		this.items = new ArrayList<ItemReciboSueldo>();
		this.anotaciones = new ArrayList<AnotacionReciboSueldo>();
		reset();
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_LEGAJO_P_ID", nullable=false)
	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	@Column(name = "A_ANIO", nullable=false)
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@ManyToOne
	@JoinColumn(name = "F_MES_P_ID", nullable=false)
	public Mes getMes() {
		return mes;
	}

	public void setMes(Mes mes) {
		this.mes = mes;
	}

	@ManyToOne
	@JoinColumn(name = "F_QUINCENA_P_ID", nullable=false)
	public Quincena getQuincena() {
		return quincena;
	}

	public void setQuincena(Quincena quincena) {
		this.quincena = quincena;
	}

	@Column(name = "A_ID_ESTADO", nullable=false)
	private Integer getIdEstadoReciboSueldo() {
		return idEstadoReciboSueldo;
	}

	private void setIdEstadoReciboSueldo(Integer idEstadoReciboSueldo) {
		this.idEstadoReciboSueldo = idEstadoReciboSueldo;
	}

	@Transient
	public EEstadoReciboSueldo getEstado() {
		if (getIdEstadoReciboSueldo() == null) {
			return null;
		}
		return EEstadoReciboSueldo.getById(getIdEstadoReciboSueldo());
	}

	public void setEstado(EEstadoReciboSueldo estado) {
		if (estado == null) {
			setIdEstadoReciboSueldo(null);
			return;
		}
		setIdEstadoReciboSueldo(estado.getId());
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_RECIBO_SUELDO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ItemReciboSueldo> getItems() {
		return items;
	}

	public void setItems(List<ItemReciboSueldo> items) {
		this.items = items;
	}

	@Column(name = "A_BRUTO", nullable=false)
	public BigDecimal getBruto() {
		return bruto;
	}

	public void setBruto(BigDecimal bruto) {
		this.bruto = bruto;
	}

	@Column(name = "A_NETO", nullable=false)
	public BigDecimal getNeto() {
		return neto;
	}

	public void setNeto(BigDecimal neto) {
		this.neto = neto;
	}

	@Column(name = "A_TOTAL_RETENCIONES", nullable=false)
	public BigDecimal getTotalRetenciones() {
		return totalRetenciones;
	}

	public void setTotalRetenciones(BigDecimal totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	@Column(name = "A_TOTAL_NO_REM", nullable=false)
	public BigDecimal getTotalNoRemunerativo() {
		return totalNoRemunerativo;
	}

	public void setTotalNoRemunerativo(BigDecimal totalNoRemunerativo) {
		this.totalNoRemunerativo = totalNoRemunerativo;
	}

	@Column(name = "A_TOTAL_DEDUCC", nullable=false)
	public BigDecimal getTotalDeducciones() {
		return totalDeducciones;
	}

	public void setTotalDeducciones(BigDecimal totalDeducciones) {
		this.totalDeducciones = totalDeducciones;
	}

	@Column(name = "A_TEXTO_ORDEN", nullable=false)
	public String getTextoOrden() {
		return textoOrden;
	}

	public void setTextoOrden(String textoOrden) {
		this.textoOrden = textoOrden;
	}

	@Column(name = "A_DIAS_TRABAJADOS", nullable=false)
	public Integer getDiasTrabajados() {
		return diasTrabajados;
	}

	public void setDiasTrabajados(Integer diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_RECIBO_SUELDO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<AnotacionReciboSueldo> getAnotaciones() {
		return anotaciones;
	}

	public void setAnotaciones(List<AnotacionReciboSueldo> anotaciones) {
		this.anotaciones = anotaciones;
	}

	@Column(name = "A_VALOR_HORA", nullable=false)
	public BigDecimal getValorHora() {
		return valorHora;
	}

	public void setValorHora(BigDecimal valorHora) {
		this.valorHora = valorHora;
	}

	@Column(name = "A_FECHA_ULT_DEPOSITO", nullable=true)
	public Date getFechaUltDeposito() {
		return fechaUltDeposito;
	}

	public void setFechaUltDeposito(Date fechaUltDeposito) {
		this.fechaUltDeposito = fechaUltDeposito;
	}

	@Column(name = "A_FECHA_PAGO", nullable=true)
	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	@Transient
	public void reset() {
		this.items.clear();
		this.anotaciones.clear();
		this.bruto = BigDecimal.ZERO;
		this.neto = BigDecimal.ZERO;
		this.totalRetenciones = BigDecimal.ZERO;
		this.totalNoRemunerativo = BigDecimal.ZERO;
		this.totalDeducciones = BigDecimal.ZERO;
		this.diasTrabajados = 0;
		this.quincena = null;
		setEstado(null);
	}

}