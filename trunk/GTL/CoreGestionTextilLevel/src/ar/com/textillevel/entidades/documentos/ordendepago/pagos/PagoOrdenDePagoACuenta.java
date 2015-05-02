package ar.com.textillevel.entidades.documentos.ordendepago.pagos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.IPagoOrdenPagoVisitor;

@Entity
@DiscriminatorValue(value="PODPAC")
public class PagoOrdenDePagoACuenta extends PagoOrdenDePago{

	private static final long serialVersionUID = -4911997598474831042L;

	@Override
	@Transient
	public void accept(IPagoOrdenPagoVisitor visitor) {
		visitor.visit(this);
	}
}
