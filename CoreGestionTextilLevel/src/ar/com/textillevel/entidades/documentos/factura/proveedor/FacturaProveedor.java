package ar.com.textillevel.entidades.documentos.factura.proveedor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Entity
@Table(name = "T_FACTURA_PROVEEDOR")
public class FacturaProveedor implements Serializable {

	private static final long serialVersionUID = -7482581170374184248L;

	private Integer id;
	private BigDecimal montoSubtotal;
	private BigDecimal descuento;
	private BigDecimal montoTotal;
	private BigDecimal montoFaltantePorPagar;
	private Date fechaIngreso;
	private String nroFactura; 
	private List<RemitoEntradaProveedor> remitoList;
	private List<ItemFacturaProveedor> items;
	private Proveedor proveedor;
	private String usuarioConfirmacion;		//EL USUARIO QUE ACEPTA UN DOCUMENTO, LO PONGO ACA PARA QUE SEA FACIL TRAERLO EN LA TABLA DE MOVIMIENTOS
	private Boolean verificada;
	private String usuarioCreador;
	private BigDecimal impVarios;
	private BigDecimal percepIVA;
	private Integer idTipoFacturaProveedor;

	public FacturaProveedor() {
		this.items = new ArrayList<ItemFacturaProveedor>();
		this.remitoList = new ArrayList<RemitoEntradaProveedor>();
		this.descuento = new BigDecimal(0);
		this.montoTotal = new BigDecimal(0);
		this.montoSubtotal = new BigDecimal(0);
		this.impVarios = new BigDecimal(0);
		this.percepIVA = new BigDecimal(0);
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
	
	@Column(name = "A_MONTO_TOTAL", nullable = false)
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	@Column(name = "A_MONTO_FALTANTE_POR_PAGAR", nullable = false)
	public BigDecimal getMontoFaltantePorPagar() {
		return montoFaltantePorPagar;
	}

	public void setMontoFaltantePorPagar(BigDecimal montoFaltantePorPagar) {
		this.montoFaltantePorPagar = montoFaltantePorPagar;
	}

	@Column(name = "A_MONTO_SUBTOTAL", nullable = false)
	public BigDecimal getMontoSubtotal() {
		return montoSubtotal;
	}

	public void setMontoSubtotal(BigDecimal montoSubtotal) {
		this.montoSubtotal = montoSubtotal;
	}

	@Column(name = "A_DESCUENTO", nullable = false)
	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	@Column(name = "A_FECHA_INGRESO", nullable = false)
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	@Column(name = "A_NRO_FACTURA", nullable = false)
	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "F_FACTURA_PROV_P_ID")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ItemFacturaProveedor> getItems() {
		return items;
	}

	public void setItems(List<ItemFacturaProveedor> items) {
		this.items = items;
	}

	@OneToMany
	@JoinColumn(name = "F_FACTURA_PROV_P_ID")
	public List<RemitoEntradaProveedor> getRemitoList() {
		return remitoList;
	}

	public void setRemitoList(List<RemitoEntradaProveedor> remitoList) {
		this.remitoList = remitoList;
	}
	
	@Column(name="A_USR_CONFIRMACION")	
	public String getUsuarioConfirmacion() {
		return usuarioConfirmacion;
	}
	
	public void setUsuarioConfirmacion(String usuarioConfirmacion) {
		this.usuarioConfirmacion = usuarioConfirmacion;
	}

	@Column(name="A_USR_CREACION")
	public String getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}
	
	@Column(name="A_VERIFICADA", columnDefinition="INTEGER UNSIGNED DEFAULT 0")
	public Boolean getVerificada() {
		return verificada;
	}
	
	public void setVerificada(Boolean verificada) {
		this.verificada = verificada;
	}

	@Column(name="A_IMP_VARIOS", nullable=true)
	public BigDecimal getImpVarios() {
		if(impVarios == null) {
			return new BigDecimal(0); 
		} else {
			return impVarios;
		}
	}

	public void setImpVarios(BigDecimal impVarios) {
		this.impVarios = impVarios;
	}

	public void setPercepIVA(BigDecimal percepIVA) {
		this.percepIVA = percepIVA; 
	}

	@Column(name="A_PERCEP_IVA", nullable=true)
	public BigDecimal getPercepIVA() {
		if(percepIVA == null) {
			return new BigDecimal(0); 
		} else {
			return percepIVA;
		}
	}

	@ManyToOne
	@JoinColumn(name="F_PROV_P_ID", nullable=false)
	public Proveedor getProveedor() {
		return proveedor;
	}
	
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	@Column(name = "A_ID_TIPO_FACT_PROV", nullable = true)	
	public Integer getIdTipoFacturaProveedor() {
		return idTipoFacturaProveedor;
	}

	public void setIdTipoFacturaProveedor(Integer idTipoFacturaProveedor) {
		this.idTipoFacturaProveedor = idTipoFacturaProveedor;
	}

	public void setTipoFacturaProveedor(ETipoFacturaProveedor tipoFacturaProveedor) {
		if (tipoFacturaProveedor == null) {
			this.setIdTipoFacturaProveedor(null);
		}
		setIdTipoFacturaProveedor(tipoFacturaProveedor.getId());
	}

	@Transient
	public ETipoFacturaProveedor getTipoFacturaProveedor() {
		if (getIdTipoFacturaProveedor() == null) {
			return null;
		}
		return ETipoFacturaProveedor.getById(getIdTipoFacturaProveedor());
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
		final FacturaProveedor other = (FacturaProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	@Transient
	public String toString(){
		return "Fecha: " + DateUtil.dateToString(this.getFechaIngreso(), DateUtil.SHORT_DATE) + " - Nro: " + this.getNroFactura() + 
			   " - Importe (Pesos): " + getDecimalFormat().format(this.getMontoTotal()) + " - Importe faltante (Pesos): " + getDecimalFormat().format(this.getMontoFaltantePorPagar());
	}

	@Transient
	private NumberFormat getDecimalFormat() {
		NumberFormat df = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}

}