package ar.com.textillevel.entidades.documentos.recibo.formapago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="FPRGAN")
public class FormaPagoRetencionGanancias extends FormaPagoSimple {

	private static final long serialVersionUID = 7371807764982214222L;

	@Override
	@Transient
	public String getDescripcion() {
		return "";
	}

	@Override
	@Transient
	public void accept(IFormaPagoVisitor visitor) {
		visitor.visit(this);
	}

}