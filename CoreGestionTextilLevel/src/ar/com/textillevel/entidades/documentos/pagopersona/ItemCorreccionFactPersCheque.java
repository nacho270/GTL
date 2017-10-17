package ar.com.textillevel.entidades.documentos.pagopersona;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import ar.com.textillevel.entidades.cheque.Cheque;

@Entity
@DiscriminatorValue(value = "ICFPCH")
public class ItemCorreccionFactPersCheque extends ItemCorreccionFacturaPersona {

	private static final long serialVersionUID = -8920721116002979982L;

	private Cheque cheque;

	@ManyToOne
	@JoinColumn(name="F_CHEQUE_P_ID")
	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

}
