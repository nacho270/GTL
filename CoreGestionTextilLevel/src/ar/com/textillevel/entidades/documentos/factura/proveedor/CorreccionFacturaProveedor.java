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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.visitor.ICorreccionFacturaProveedorVisitor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Entity
@Table(name = "T_CORRECCION_FACTURA_PROV")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CorreccionFacturaProveedor implements Serializable {

	private static final long serialVersionUID = -350290215745242715L;

	private Integer id;
	private List<FacturaProveedor> facturas;
	private Date fechaIngreso;
	private BigDecimal montoTotal;
	private Proveedor proveedor;
	private String nroCorreccion;
	private List<ItemCorreccionFacturaProveedor> itemsCorreccion;
	private String usuarioConfirmacion;
	private Boolean verificada;
	private BigDecimal impVarios;
	private BigDecimal percepIVA;

	public CorreccionFacturaProveedor() {
		this.facturas = new ArrayList<FacturaProveedor>();
		this.itemsCorreccion = new ArrayList<ItemCorreccionFacturaProveedor>();
		this.impVarios = new BigDecimal(0);
		this.percepIVA = new BigDecimal(0);
		this.montoTotal = new BigDecimal(0);
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

	@ManyToMany
	@JoinTable(name = "T_CORR_FACT_PROV_ASOC", 
			joinColumns = { @JoinColumn(name = "F_CORR_FACT_PROV_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_FACT_PROV_P_ID") })
	public List<FacturaProveedor> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<FacturaProveedor> facturas) {
		this.facturas = facturas;
	}

	@Column(name = "A_FECHA_INGRESO", nullable = false)
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	@Column(name = "A_MONTO_TOTAL", nullable = false)
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_PROV_P_ID", nullable=false)
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "F_FACTURA_PROV_P_ID")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ItemCorreccionFacturaProveedor> getItemsCorreccion() {
		return itemsCorreccion;
	}

	public void setItemsCorreccion(List<ItemCorreccionFacturaProveedor> itemsCorreccion) {
		this.itemsCorreccion = itemsCorreccion;
	}

	@Column(name = "A_NRO_CORRECCION", nullable = false)
	public String getNroCorreccion() {
		return nroCorreccion;
	}

	public void setNroCorreccion(String nroCorreccion) {
		this.nroCorreccion = nroCorreccion;
	}
	
	@Column(name="A_USR_CONFIRMACION")	
	public String getUsuarioConfirmacion() {
		return usuarioConfirmacion;
	}
	
	public void setUsuarioConfirmacion(String usuarioConfirmacion) {
		this.usuarioConfirmacion = usuarioConfirmacion;
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

	@Transient
	@Override
	public String toString() {
		return "Fecha: " + DateUtil.dateToString(this.getFechaIngreso(), DateUtil.SHORT_DATE) + " - Nro: " + this.getNroCorreccion() + 
		   " - Importe (Pesos): " + getDecimalFormat().format(this.getMontoTotal());
	}

	@Transient
	private NumberFormat getDecimalFormat() {
		NumberFormat df = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}
	
	@Transient
	public abstract void accept(ICorreccionFacturaProveedorVisitor visitor);

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
		CorreccionFacturaProveedor other = (CorreccionFacturaProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
