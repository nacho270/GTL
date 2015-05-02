package ar.com.textillevel.entidades.documentos.recibo.pagos;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.IPagoReciboVisitor;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;

@Entity
@DiscriminatorValue(value="PRF")
public class PagoReciboFactura extends PagoRecibo{

	private static final long serialVersionUID = 6880387758438665003L;
	
	private Factura factura;
	private Integer idDescrPagoFactura;

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
	@JoinColumn(name = "F_FACTURA_P_ID")
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@Column(name="A_ID_DESCR_PAGO_FACTURA")
	private Integer getIdDescrPagoFactura() {
		return idDescrPagoFactura;
	}

	private void setIdDescrPagoFactura(Integer idDescrPagoFactura) {
		this.idDescrPagoFactura = idDescrPagoFactura;
	}
	
	public void setDescrPagoFactura(EDescripcionPagoFactura descrPagoFactura) {
		if (descrPagoFactura == null) {
			this.setIdDescrPagoFactura(null);
		}
		setIdDescrPagoFactura(descrPagoFactura.getId());
	}

	@Transient
	public EDescripcionPagoFactura getDescrPagoFactura() {
		if (getIdDescrPagoFactura() == null) {
			return null;
		}
		return EDescripcionPagoFactura.getById(getIdDescrPagoFactura());
	}

	@Override
	@Transient
	public void accept(IPagoReciboVisitor visitor) {
		visitor.visit(this);
	}

}
