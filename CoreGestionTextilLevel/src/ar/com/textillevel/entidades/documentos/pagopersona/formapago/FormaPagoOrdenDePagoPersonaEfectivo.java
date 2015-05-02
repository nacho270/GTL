package ar.com.textillevel.entidades.documentos.pagopersona.formapago;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="FPODPPERS_EFEC")
public class FormaPagoOrdenDePagoPersonaEfectivo extends FormaPagoOrdenDePagoPersona{

	private static final long serialVersionUID = -1225750590387176472L;

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
	public String getDescripcion() {
		return null;
	}

	@Override
	@Transient
	public BigDecimal getImporte() {
		return getImportePagoSimple();
	}
}
