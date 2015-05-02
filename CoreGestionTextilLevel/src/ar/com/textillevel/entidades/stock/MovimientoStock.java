package ar.com.textillevel.entidades.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.entidades.stock.visitor.IMovimientoStockVisitor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@Table(name = "T_MOVS_STOCK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "TIPO")
@DiscriminatorValue(value = "MS")
public abstract class MovimientoStock implements Serializable {

	private static final long serialVersionUID = -6695840827989443L;

	private Integer id;
	private BigDecimal cantidad;
	private PrecioMateriaPrima precioMateriaPrima;
	private RelacionContenedorPrecioMatPrima relContPrecioMatPrima;
	private Date fechaMovimiento;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_CANTIDAD", nullable = false)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@ManyToOne
	@JoinColumn(name="F_PRECIO_MP_P_ID", nullable=true)
	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}
	
	@Column(name="A_FECHA_MOV_STOCK",nullable=true)
	public Date getFechaMovimiento() {
		return fechaMovimiento;
	}

	public void setFechaMovimiento(Date fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	
	@ManyToOne
	@JoinColumn(name="F_REL_PRECIO_MP_P_ID", nullable=true)
	public RelacionContenedorPrecioMatPrima getRelContPrecioMatPrima() {
		return relContPrecioMatPrima;
	}

	public void setRelContPrecioMatPrima(RelacionContenedorPrecioMatPrima relContPrecioMatPrima) {
		this.relContPrecioMatPrima = relContPrecioMatPrima;
	}

	@Transient
	public abstract void aceptarVisitor(IMovimientoStockVisitor visitor);

}
