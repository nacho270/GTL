package ar.com.textillevel.entidades.documentos.factura.proveedor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.documentos.factura.proveedor.visitor.IItemFacturaProveedorVisitor;

@Entity
@DiscriminatorValue(value = "ITS")
public class ItemFacturaServicio extends ItemFacturaProveedor {

	private static final long serialVersionUID = -5873585295217854000L;

	private Servicio servicio;

	public ItemFacturaServicio() {
		super();
	}

	@ManyToOne
	@JoinColumn(name="F_SERVICIO_P_ID")
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	@Override
	public void accept(IItemFacturaProveedorVisitor visitor) {
		visitor.visit(this);
	}

}