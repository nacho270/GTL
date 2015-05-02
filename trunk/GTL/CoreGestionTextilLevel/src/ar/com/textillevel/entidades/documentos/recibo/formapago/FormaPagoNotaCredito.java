package ar.com.textillevel.entidades.documentos.recibo.formapago;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.factura.NotaCredito;

@Entity
@DiscriminatorValue(value="FP_NC")
public class FormaPagoNotaCredito extends FormaPago {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal importeNC;
	private NotaCredito notaCredito;

	@Column(name = "A_IMPORTE_NC")
	public BigDecimal getImporteNC() {
		return importeNC;
	}

	public void setImporteNC(BigDecimal importeNC) {
		this.importeNC = importeNC;
	}

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="F_NOTA_CREDITO_P_ID")
	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
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
