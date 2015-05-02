package ar.com.textillevel.entidades.documentos.recibo.formapago;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="FPTX")
public class FormaPagoTransferencia extends FormaPagoSimple {

	private static final long serialVersionUID = -6343026956070742688L;

	private String observaciones;
	private Double nroTx;

	@Column(name = "A_NRO_TX")
	public Double getNroTx() {
		return nroTx;
	}

	public void setNroTx(Double nroTx) {
		this.nroTx = nroTx;
	}

	@Column(name = "A_OBSERVACIONES")
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	@Transient
	public void accept(IFormaPagoVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public String getDescripcion() {
		return null;
	}

}
