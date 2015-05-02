package ar.com.textillevel.entidades.documentos.ordendepago.formapago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="FPODPIB")
public class FormaPagoOrdenDePagoRetencionIngresosBrutos extends FormaPagoOrdenDePagoSimple{

	private static final long serialVersionUID = -7885340648102751980L;

	@Override
	@Transient
	public String getDescripcion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public void accept(IFormaPagoVisitor visitor) {
		visitor.visit(this);
	}

}
