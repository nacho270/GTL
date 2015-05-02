package ar.com.textillevel.entidades.documentos.factura.proveedor;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.com.textillevel.entidades.documentos.factura.proveedor.visitor.ICorreccionFacturaProveedorVisitor;

@Entity
@DiscriminatorValue(value="ND")
public class NotaDebitoProveedor extends CorreccionFacturaProveedor {

	private static final long serialVersionUID = -3541091871146279107L;

	private BigDecimal montoFaltantePorPagar;

	@Column(name = "A_MONTO_FALTANTE_X_PAGAR", nullable = true)
	public BigDecimal getMontoFaltantePorPagar() {
		return montoFaltantePorPagar;
	}

	public void setMontoFaltantePorPagar(BigDecimal montoFaltantePorPagar) {
		this.montoFaltantePorPagar = montoFaltantePorPagar;
	}

	@Override
	public void accept(ICorreccionFacturaProveedorVisitor visitor) {
		visitor.visit(this);
	}

}
