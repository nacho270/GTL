package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Entity
@DiscriminatorValue(value="ITTC")
public class ItemFacturaTelaCruda extends ItemFactura {

	private static final long serialVersionUID = 1053668221212085088L;

	private Articulo articulo;

	@ManyToOne
	@JoinColumn(name="F_ARTICULO_P_ID")
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.TELA_CRUDA;
	}

}