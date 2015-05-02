package ar.com.textillevel.entidades.documentos.factura.proveedor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_ITEM_CORRECC_FACT_PROV")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class ItemCorreccionFacturaProveedor implements Serializable {

	private static final long serialVersionUID = 6737211861035439773L;

	private Integer id;
	private BigDecimal cantidad;
	private BigDecimal importe;
	private BigDecimal precioUnitario;
	private BigDecimal porcDescuento;
	private BigDecimal factorConversionMoneda;
	private String descripcion;
	private List<ImpuestoItemProveedor> impuestos;

	public ItemCorreccionFacturaProveedor() {
		this.impuestos = new ArrayList<ImpuestoItemProveedor>();
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

	@Column(name = "A_CANTIDAD", nullable = true)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@Column(name = "A_IMPORTE", nullable = true)
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	@Column(name = "A_PRECIO_UNITARIO", nullable=false)
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	@Column(name = "A_PORC_DESC", nullable=false)
	public BigDecimal getPorcDescuento() {
		return porcDescuento;
	}

	public void setPorcDescuento(BigDecimal porcDescuento) {
		this.porcDescuento = porcDescuento;
	}

	@Column(name = "A_FACTOR_CONV_MONEDA", nullable=true)
	public BigDecimal getFactorConversionMoneda() {
		return factorConversionMoneda;
	}

	public void setFactorConversionMoneda(BigDecimal factorConversionMoneda) {
		this.factorConversionMoneda = factorConversionMoneda;
	}

	@Column(name = "A_DESCRIPCION", nullable=false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@ManyToMany
	@JoinTable(name = "T_ITEM_CORR_FACT_PROV_IMPUESTO", 
			joinColumns = { @JoinColumn(name = "F_ITEM_FACT_PROV_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_IMPUESTO_P_ID") })
	public List<ImpuestoItemProveedor> getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(List<ImpuestoItemProveedor> impuestos) {
		this.impuestos = impuestos;
	}

	@Transient
	public BigDecimal recalcularImporteTotal() {
		BigDecimal impOriginal = getPrecioUnitario().multiply(getCantidad());
		BigDecimal impActual = new BigDecimal(impOriginal.doubleValue());
		impActual = impActual.multiply(getFactorConversionMoneda());
		impActual = impActual.subtract(impOriginal.multiply(getPorcDescuento().divide(new BigDecimal(100))).multiply(getFactorConversionMoneda()));
		setImporte(impActual);
		return getImporte();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemCorreccionFacturaProveedor other = (ItemCorreccionFacturaProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}