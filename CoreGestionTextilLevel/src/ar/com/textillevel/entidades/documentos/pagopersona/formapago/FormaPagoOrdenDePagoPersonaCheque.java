package ar.com.textillevel.entidades.documentos.pagopersona.formapago;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.cheque.Cheque;

@Entity
@DiscriminatorValue(value="FPODPPERS_CHEQUE")
public class FormaPagoOrdenDePagoPersonaCheque extends FormaPagoOrdenDePagoPersona{

	private static final long serialVersionUID = -5283356624566285371L;

	private Cheque cheque;

	@Override
	@Transient
	public BigDecimal getImporte() {
		return cheque.getImporte();
	}

	@Override
	@Transient
	public String getDescripcion() {
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
}
