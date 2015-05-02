package ar.com.textillevel.entidades.documentos.recibo.pagos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.IPagoReciboVisitor;

@Entity
@DiscriminatorValue(value="PRAC")
public class PagoReciboACuenta extends PagoRecibo{

	private static final long serialVersionUID = 3592496792221267767L;

	@Override
	@Transient
	public void accept(IPagoReciboVisitor visitor) {
		visitor.visit(this);
	}

}
