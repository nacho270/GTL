package ar.com.textillevel.modulos.personal.entidades.legajos;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;

@Entity
@Table(name = "T_PERS_LEGAJO_EMPLEADO")
public class LegajoEmpleado implements Serializable {

	private static final long serialVersionUID = -4062673196752046144L;

	private Integer id;
	private Integer nroLegajo;
	private Date fechaAlta;
	private Empleado empleado;
	private Puesto puesto;
	private Categoria categoria;
	private BigDecimal valorHora;
	private String observaciones;
	private String otrosComentarios;
	private Integer nroTarjeta;
	private List<HorarioDia> horario;
	private BigDecimal sueldoEstimadoQuincenal;
	private BigDecimal sueldoEstimadoMensual;
	private Boolean afiliadoASindicato;
	private Integer ultimoNumeroVale;
	// private Integer version;
	private List<VigenciaEmpleado> historialVigencias;
	private String nroCuentaBanco;
	private Banco bancoDePago;
	private Boolean dadoDeBaja;

	private List<RegistroVacacionesLegajo> historialVacaciones;
	
	public LegajoEmpleado() {
		super();
		horario = new ArrayList<HorarioDia>();
		historialVacaciones = new ArrayList<RegistroVacacionesLegajo>();
		historialVigencias = new ArrayList<VigenciaEmpleado>();
		ultimoNumeroVale = 0;
		dadoDeBaja = false;
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

	@Column(name = "A_NRO_LEGAJO", nullable = false)
	public Integer getNroLegajo() {
		return nroLegajo;
	}

	public void setNroLegajo(Integer nroLegajo) {
		this.nroLegajo = nroLegajo;
	}

	@Column(name = "A_FECHA_ALTA", nullable = false)
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@OneToOne(mappedBy = "legajo", fetch = FetchType.LAZY)
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "F_PUESTO_P_ID")
	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "F_CATEGORIA_P_ID")
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Column(name = "A_VALOR_HORA", nullable = false)
	public BigDecimal getValorHora() {
		return valorHora;
	}

	public void setValorHora(BigDecimal valorHora) {
		this.valorHora = valorHora;
	}

	@Column(name = "A_OBSERVACIONES", nullable = true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Column(name = "A_OTROS_COMENTARIOS", nullable = true)
	public String getOtrosComentarios() {
		return otrosComentarios;
	}

	public void setOtrosComentarios(String otrosComentarios) {
		this.otrosComentarios = otrosComentarios;
	}

	@Column(name = "A_NRO_TARJETA", nullable = true)
	public Integer getNroTarjeta() {
		return nroTarjeta;
	}

	public void setNroTarjeta(Integer nroTarjeta) {
		this.nroTarjeta = nroTarjeta;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "F_LEGAJO_P_ID")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<HorarioDia> getHorario() {
		return horario;
	}

	public void setHorario(List<HorarioDia> horario) {
		this.horario = horario;
	}

	@Column(name = "A_SUELDO_ESTIM_QUINCEN", nullable = false)
	public BigDecimal getSueldoEstimadoQuincenal() {
		return sueldoEstimadoQuincenal;
	}

	public void setSueldoEstimadoQuincenal(BigDecimal sueldoEstimadoQuincenal) {
		this.sueldoEstimadoQuincenal = sueldoEstimadoQuincenal;
	}

	@Column(name = "A_SUELDO_ESTIM_MENS", nullable = false)
	public BigDecimal getSueldoEstimadoMensual() {
		return sueldoEstimadoMensual;
	}

	public void setSueldoEstimadoMensual(BigDecimal sueldoEstimadoMensual) {
		this.sueldoEstimadoMensual = sueldoEstimadoMensual;
	}

	@Column(name = "A_AFIL_SINDICATO", nullable = false, columnDefinition = "INTEGER UNSIGNED DEFAULT 0")
	public Boolean getAfiliadoASindicato() {
		return afiliadoASindicato;
	}

	public void setAfiliadoASindicato(Boolean afiliadoASindicato) {
		this.afiliadoASindicato = afiliadoASindicato;
	}

	@Column(name = "A_ULTIMO_NRO_VALE", nullable = false, columnDefinition = "INTEGER UNSIGNED DEFAULT 1")
	public Integer getUltimoNumeroVale() {
		return ultimoNumeroVale;
	}

	public void setUltimoNumeroVale(Integer ultimoNumeroVale) {
		this.ultimoNumeroVale = ultimoNumeroVale;
	}

	@Transient
	public RangoHorario getHorario(Date fecha) {
		for (HorarioDia hd : getHorario()) {
			if (hd.contieneFecha(fecha)) {
				return hd.getRangoHorario();
			}
		}
		return null;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "F_LEGAJO_P_ID", nullable = false)
	public List<VigenciaEmpleado> getHistorialVigencias() {
		return historialVigencias;
	}

	public void setHistorialVigencias(List<VigenciaEmpleado> historialVigencias) {
		this.historialVigencias = historialVigencias;
	}

	@Column(name = "A_NRO_CTA_BANCO", nullable = true)
	public String getNroCuentaBanco() {
		return nroCuentaBanco;
	}

	public void setNroCuentaBanco(String nroCuentaBanco) {
		this.nroCuentaBanco = nroCuentaBanco;
	}

	@ManyToOne
	@JoinColumn(name = "F_BANCO_P_ID", nullable = true)
	public Banco getBancoDePago() {
		return bancoDePago;
	}

	public void setBancoDePago(Banco bancoDePago) {
		this.bancoDePago = bancoDePago;
	}

	@Column(name = "A_DADO_DE_BAJA", nullable = false, columnDefinition = "INTEGER UNSIGNED DEFAULT 0")
	public Boolean getDadoDeBaja() {
		return dadoDeBaja;
	}

	public void setDadoDeBaja(Boolean dadoDeBaja) {
		this.dadoDeBaja = dadoDeBaja;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "F_LEGAJO_P_ID", nullable = false)	
	public List<RegistroVacacionesLegajo> getHistorialVacaciones() {
		return historialVacaciones;
	}
	
	public void setHistorialVacaciones(List<RegistroVacacionesLegajo> historialVacaciones) {
		this.historialVacaciones = historialVacaciones;
	}

	// @Version
	// @Column(name = "A_VERSION")
	// public Integer getVersion() {
	// return version;
	// }
	//
	// public void setVersion(Integer version) {
	// this.version = version;
	// }
}
