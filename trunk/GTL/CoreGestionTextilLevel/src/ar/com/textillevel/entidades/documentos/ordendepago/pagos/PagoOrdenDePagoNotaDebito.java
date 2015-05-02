package ar.com.textillevel.entidades.documentos.ordendepago.pagos;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.IPagoOrdenPagoVisitor;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;

@Entity
@DiscriminatorValue(value="PODPND")
public class PagoOrdenDePagoNotaDebito extends PagoOrdenDePago{

	private static final long serialVersionUID = -5245356122408070214L;

	private NotaDebitoProveedor notaDebito;
	private Integer idDescrPagoFactura;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
	@JoinColumn(name = "F_NOTA_DEBITO_P_ID")
	public NotaDebitoProveedor getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebitoProveedor notaDebito) {
		this.notaDebito = notaDebito;
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
	public void accept(IPagoOrdenPagoVisitor visitor) {
		visitor.visit(this);
	}
}
