package ar.com.textillevel.entidades.documentos.pagopersona;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import ar.com.textillevel.entidades.documentos.pagopersona.visitor.ICorreccionFacturaPersonaVisitor;

@Entity
@DiscriminatorValue(value="NDPERS")
public class NotaDebitoPersona extends CorreccionFacturaPersona {

	private static final long serialVersionUID = -1375012807603704462L;

	private BigDecimal montoFaltantePorPagar;

	@Column(name = "A_MONTO_FALTANTE_X_PAGAR", nullable = true)
	public BigDecimal getMontoFaltantePorPagar() {
		return montoFaltantePorPagar;
	}

	public void setMontoFaltantePorPagar(BigDecimal montoFaltantePorPagar) {
		this.montoFaltantePorPagar = montoFaltantePorPagar;
	}

	@Override
	public void accept(ICorreccionFacturaPersonaVisitor visitor) {
		visitor.visit(this);
	}

}