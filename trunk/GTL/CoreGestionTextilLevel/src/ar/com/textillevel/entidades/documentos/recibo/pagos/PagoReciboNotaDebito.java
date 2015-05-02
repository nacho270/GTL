package ar.com.textillevel.entidades.documentos.recibo.pagos;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.IPagoReciboVisitor;

@Entity
@DiscriminatorValue(value="PRND")
public class PagoReciboNotaDebito extends PagoRecibo {

	private static final long serialVersionUID = 5787959523918399151L;

	private NotaDebito notaDebito;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
	@JoinColumn(name = "F_NOTA_DEBITO_P_ID")
	public NotaDebito getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebito notaDebito) {
		this.notaDebito = notaDebito;
	}

	@Override
	@Transient
	public void accept(IPagoReciboVisitor visitor) {
		visitor.visit(this);
	}

}
