package ar.com.textillevel.modulos.odt.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

import org.hibernate.annotations.Cascade;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.util.ODTCodigoHelper;

@Entity
@Table(name="T_ORDEN_DE_TRABAJO")
public class OrdenDeTrabajo implements Serializable {

	private static final long serialVersionUID = 4254190684480472306L;

	private Integer id;
	private RemitoEntrada remito;
	private List<PiezaODT> piezas;
	private ProductoArticulo productoArticulo;
	private String codigo;
	private Timestamp fechaODT;
	private Integer idEstadoODT;
	private Byte idAvance;
	private Maquina maquinaActual;
	private Short ordenEnMaquina;
	private SecuenciaODT secuenciaDeTrabajo;
	private Maquina maquinaPrincipal;
	
	private List<TransicionODT> transiciones;
	
	private boolean noLocal=false;
	

	public OrdenDeTrabajo() {
		this.piezas = new ArrayList<PiezaODT>();
		this.setEstadoODT(EEstadoODT.PENDIENTE); 
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_REMITO_P_ID", nullable=true)
	public RemitoEntrada getRemito() {
		return remito;
	}

	public void setRemito(RemitoEntrada remito) {
		this.remito = remito;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_ODT_P_ID", nullable=false)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<PiezaODT> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<PiezaODT> piezas) {
		this.piezas = piezas;
	}

	@ManyToOne
	@JoinColumn(name="F_PRODUCTO_ARTICULO_P_ID", nullable=false)
	public ProductoArticulo getProductoArticulo() {
		return productoArticulo;
	}

	public void setProductoArticulo(ProductoArticulo productoArticulo) {
		this.productoArticulo = productoArticulo;
	}
	
	@Column(name="A_CODIGO", nullable=false)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@Column(name="A_FECHA_ODT",nullable=false)
	public Timestamp getFechaODT() {
		return fechaODT;
	}
	
	public void setFechaODT(Timestamp fechaODT) {
		this.fechaODT = fechaODT;
	}

	@Column(name="A_ID_ESTADO",nullable=false)
	private Integer getIdEstadoODT() {
		return idEstadoODT;
	}

	private void setIdEstadoODT(Integer idEstadoODT) {
		this.idEstadoODT = idEstadoODT;
	}
	
	@Transient
	public EEstadoODT getEstado(){
		return EEstadoODT.getById(getIdEstadoODT());
	}
	
	public void setEstadoODT(EEstadoODT estado){
		setIdEstadoODT(estado.getId());
	}
	
	@Column(name="A_ID_AVANCE",nullable=true)
	private Byte getIdAvance() {
		return idAvance;
	}

	private void setIdAvance(Byte idAvance) {
		this.idAvance = idAvance;
	}

	@Transient
	public EAvanceODT getAvance(){
		return EAvanceODT.getById(getIdAvance());
	}
	
	public void setAvance(EAvanceODT estado){
		if(estado!=null){
			setIdAvance(estado.getId());
		}else{
			setIdAvance(null);
		}
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_MAQUINA_ACTUAL_P_ID",nullable=true)
	public Maquina getMaquinaActual() {
		return maquinaActual;
	}

	public void setMaquinaActual(Maquina maquinaActual) {
		this.maquinaActual = maquinaActual;
	}

	@Column(name="A_ORDEN_EN_MAQUINA",nullable=true)
	public Short getOrdenEnMaquina() {
		return ordenEnMaquina;
	}
	
	public void setOrdenEnMaquina(Short ordenEnMaquina) {
		this.ordenEnMaquina = ordenEnMaquina;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		final OrdenDeTrabajo other = (OrdenDeTrabajo) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getTotalMetros() {
		BigDecimal totalMetros = new BigDecimal(0);
		for (PiezaODT pieza : getPiezas()) {
			totalMetros = totalMetros.add(pieza.getMetros() == null ? BigDecimal.ZERO : pieza.getMetros());
		}
		return totalMetros;
	}

	@Override
	@Transient
	public String toString() {
		return ODTCodigoHelper.getInstance().formatCodigo(getCodigo()) + "-" + getProductoArticulo() + (remito == null ? "" : (" - Remito : " + remito.getNroRemito()));
	}

	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_SECUENCIA_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public SecuenciaODT getSecuenciaDeTrabajo() {
		return secuenciaDeTrabajo;
	}

	public void setSecuenciaDeTrabajo(SecuenciaODT secuenciaDeTrabajo) {
		this.secuenciaDeTrabajo = secuenciaDeTrabajo;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_MAQUINA_ODT_P_ID",nullable=true)
	public Maquina getMaquinaPrincipal() {
		return maquinaPrincipal;
	}

	public void setMaquinaPrincipal(Maquina maquinaPrincipal) {
		this.maquinaPrincipal = maquinaPrincipal;
	}

	@Transient
	public PiezaODT getPiezaByPiezaPadreRE(PiezaRemito piezaEntrada) {
		for(PiezaODT pODT : getPiezas()) {
			if(pODT.getPiezaRemito().getOrdenPieza().equals(piezaEntrada.getOrdenPieza())) {
				return pODT;
			}
		}
		return null;
	}

	@Transient
	public List<TransicionODT> getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(List<TransicionODT> transiciones) {
		this.transiciones = transiciones;
	}

	@Transient
	public boolean isNoLocal() {
		return noLocal;
	}

	public void setNoLocal(boolean noLocal) {
		this.noLocal = noLocal;
	}

	@Transient
	public boolean puedeAsignarMetrosEnPiezas() {
		return getEstado().ordinal() <= EEstadoODT.EN_OFICINA.ordinal();
	}

	@Transient
	public boolean puedeDarSalida() {
		return getEstado() == EEstadoODT.EN_OFICINA;
	}

}