package ar.com.textillevel.entidades.documentos.ordendepago.formapago;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="FPODPS")
public abstract class FormaPagoOrdenDePagoSimple extends FormaPagoOrdenDePago {

	private static final long serialVersionUID = -4225320118484502789L;

	protected BigDecimal importePagoSimple;

	public void setImportePagoSimple(BigDecimal importePagoSimple) {
		this.importePagoSimple = importePagoSimple;
	}

	@Column(name="A_IMPORTE_PAGO_SIMPLE")
	protected BigDecimal getImportePagoSimple() {
		return importePagoSimple;
	}
	
	@Override
	@Transient
	public BigDecimal getImporte(){
		return getImportePagoSimple();
	}
}
