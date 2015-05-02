package ar.com.textillevel.entidades.documentos.factura.proveedor;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.com.textillevel.entidades.documentos.factura.proveedor.visitor.ICorreccionFacturaProveedorVisitor;

@Entity
@DiscriminatorValue(value="NC")
public class NotaCreditoProveedor  extends CorreccionFacturaProveedor {

	private static final long serialVersionUID = -1503937274153027616L;

	private BigDecimal montoSobrantePorUtilizar;

	public NotaCreditoProveedor () {
		super();
		this.montoSobrantePorUtilizar = BigDecimal.ZERO;
	}

	@Column(name = "A_MONTO_SOBRANTE_X_UTILIZAR", nullable = true)
	public BigDecimal getMontoSobrantePorUtilizar() {
		return montoSobrantePorUtilizar;
	}

	public void setMontoSobrantePorUtilizar(BigDecimal montoSobrantePorUtilizar) {
		this.montoSobrantePorUtilizar = montoSobrantePorUtilizar;
	}

	@Override
	public void accept(ICorreccionFacturaProveedorVisitor visitor) {
		visitor.visit(this);
	}

}