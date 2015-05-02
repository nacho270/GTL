package ar.com.textillevel.modulos.alertas.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@DiscriminatorValue(value = "AFS")
public class AlertaFaltaStock extends Alerta {

	private static final long serialVersionUID = -1493468473824237996L;
	
	private PrecioMateriaPrima precioMateriaPrima;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_PRECIO_MP_P_ID")
	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	@Override
	public void accept(IVisitorAlerta visitor) {
		visitor.visit(this);
	}
}
