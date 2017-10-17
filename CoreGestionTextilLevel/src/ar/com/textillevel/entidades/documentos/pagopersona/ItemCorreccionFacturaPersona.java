package ar.com.textillevel.entidades.documentos.pagopersona;

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

import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;

@Entity
@Table(name = "T_ITEM_CORRECC_FACT_PERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class ItemCorreccionFacturaPersona implements Serializable {

	private static final long serialVersionUID = 4140165646625820192L;

	private Integer id;
	private BigDecimal importe;
	private BigDecimal factorConversionMoneda;
	private String descripcion;
	private List<ImpuestoItemProveedor> impuestos;

	public ItemCorreccionFacturaPersona() {
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

	@Column(name = "A_IMPORTE", nullable = true)
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
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
	@JoinTable(name = "T_CORREC_FACT_PERS_IMPUESTO", 
			joinColumns = { @JoinColumn(name = "F_CORREC_FACT_PERS_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_IMPUESTO_P_ID") })
	public List<ImpuestoItemProveedor> getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(List<ImpuestoItemProveedor> impuestos) {
		this.impuestos = impuestos;
	}

	@Transient
	public BigDecimal getImporteTotalConImpuestos() {
		BigDecimal impOriginal = getImporte();
		BigDecimal impActual = new BigDecimal(impOriginal.doubleValue());
		impActual = impActual.multiply(getFactorConversionMoneda());
		for(ImpuestoItemProveedor imp :  getImpuestos()) {
			impActual = impActual.add(impActual.multiply(new BigDecimal(imp.getPorcDescuento()/100d)).multiply(getFactorConversionMoneda()));
		}
		return impActual;
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
		ItemCorreccionFacturaPersona other = (ItemCorreccionFacturaPersona) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getImporteImpuestos() {
		return getImporteTotalConImpuestos().subtract(getImporte());
	}

}