package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;

@Entity
@DiscriminatorValue(value = "MIC")
public class MovimientoInternoCuenta extends MovimientoCuenta {

	private static final long serialVersionUID = 3231801318229776711L;

	private NotaDebito notaDebito;

	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}

	@ManyToOne
	@JoinColumn(name="F_NOTA_DEBITO_P_ID")
	public NotaDebito getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebito notaDebito) {
		this.notaDebito = notaDebito;
	}
}
