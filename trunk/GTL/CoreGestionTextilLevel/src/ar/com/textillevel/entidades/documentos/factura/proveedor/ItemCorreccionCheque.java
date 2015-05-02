package ar.com.textillevel.entidades.documentos.factura.proveedor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import ar.com.textillevel.entidades.cheque.Cheque;

@Entity
@DiscriminatorValue(value = "ICCH")
public class ItemCorreccionCheque extends ItemCorreccionFacturaProveedor {

	private static final long serialVersionUID = 7052743066300013960L;
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
