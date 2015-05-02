package ar.com.textillevel.entidades.documentos.ordendepago.pagos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.IPagoOrdenPagoVisitor;

@Entity
@Table(name = "T_PAGO_ORDEN_DE_PAGO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TIPO",discriminatorType=DiscriminatorType.STRING)
public abstract class PagoOrdenDePago implements Serializable {

	private static final long serialVersionUID = 6739027343191719829L;

	private Integer id;
	private BigDecimal montoPagado;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_MONTO_TOTAL", nullable=false)
	public BigDecimal getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(BigDecimal montoPagado) {
		this.montoPagado = montoPagado;
	}

	@Transient
	public abstract void accept(IPagoOrdenPagoVisitor visitor);
}
