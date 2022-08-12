package ar.com.textillevel.entidades.documentos.factura;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;

@Entity
@DiscriminatorValue(value = "NC")
public class NotaCredito extends CorreccionFactura {

	private static final long serialVersionUID = 2602232678527908608L;

	private BigDecimal montoSobrante;

	public NotaCredito() {
		super();
	}

	@Column(name="A_MONTO_SOBRANTE",nullable=true)
	public BigDecimal getMontoSobrante() {
		return montoSobrante;
	}

	public void setMontoSobrante(BigDecimal montoSobrante) {
		this.montoSobrante = montoSobrante;
	}

	
	@Override
	@Transient
	public ETipoCorreccionFactura getTipo() {
		return ETipoCorreccionFactura.NOTA_CREDITO;
	}
	
	@Override
	@Transient
	public ETipoDocumento getTipoDocumento() {
		return ETipoDocumento.NOTA_CREDITO;
	}

	@Override
	@Transient
	public double getTotalIVA() {
		double ivaInsc = 0;
		if (getPorcentajeIVAInscripto() != null) {
			double valIVAInsc = getPorcentajeIVAInscripto().doubleValue() / 100;
			ivaInsc = getMontoSubtotal().doubleValue() * valIVAInsc;
		}
		return ivaInsc;
	}

}
