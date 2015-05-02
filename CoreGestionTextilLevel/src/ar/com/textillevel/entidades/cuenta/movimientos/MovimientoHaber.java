package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;

@Entity
@DiscriminatorValue(value="MH")
public class MovimientoHaber extends MovimientoCuenta {

	private static final long serialVersionUID = -5960250066286360712L;

	private Recibo recibo;
	private NotaCredito notaCredito;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_RECIBO_P_ID")
	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	@ManyToOne
	@JoinColumn(name="F_NOTA_CREDITO_P_ID")
	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}
}
