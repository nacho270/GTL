package ar.com.textillevel.entidades.documentos.ordendepago.formapago;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;

@Entity
@DiscriminatorValue(value="FPODP_NC")
public class FormaPagoOrdenDePagoNotaCredito extends FormaPagoOrdenDePago {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal importeNC;
	private NotaCreditoProveedor notaCredito;

	@Column(name = "A_IMPORTE_NC")
	public BigDecimal getImporteNC() {
		return importeNC;
	}

	public void setImporteNC(BigDecimal importeNC) {
		this.importeNC = importeNC;
	}

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="F_NOTA_CREDITO_P_ID")
	public NotaCreditoProveedor getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCreditoProveedor notaCredito) {
		this.notaCredito = notaCredito;
	}

	@Override
	@Transient
	public void accept(IFormaPagoVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public String getDescripcion() {
		return null;
	}

	@Override
	@Transient
	public BigDecimal getImporte() {
		return getImporteNC();
	}
}
