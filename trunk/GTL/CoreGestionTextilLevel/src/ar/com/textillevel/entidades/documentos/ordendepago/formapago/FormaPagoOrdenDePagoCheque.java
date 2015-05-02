package ar.com.textillevel.entidades.documentos.ordendepago.formapago;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.cheque.Cheque;

@Entity
@DiscriminatorValue(value="FPODP_CHEQUE")
public class FormaPagoOrdenDePagoCheque extends FormaPagoOrdenDePago {

	private static final long serialVersionUID = -2849920098578776149L;

	private Cheque cheque;

	@Override
	@Transient
	public BigDecimal getImporte() {
		return cheque.getImporte();
	}

	@Override
	@Transient
	public String getDescripcion() {
		// TODO Auto-generated method stub
		return null;
	}

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="F_CHEQUE_P_ID")
	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

	@Override
	@Transient
	public void accept(IFormaPagoVisitor visitor) {
		visitor.visit(this);
	}

}
