package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("TENIDO")
public class ProductoTenido extends Producto implements Serializable {

	private static final long serialVersionUID = 2902009434349939354L;

	private GamaColor gamaColor;
	private Color color;

	@ManyToOne
	@JoinColumn(name="F_GAMA_P_ID")
	@Fetch(FetchMode.JOIN)
	public GamaColor getGamaColor() {
		return gamaColor;
	}
	
	public void setGamaColor(GamaColor gamaColor) {
		this.gamaColor = gamaColor;
	}

	@ManyToOne
	@JoinColumn(name="F_COLOR_P_ID")
	@Fetch(FetchMode.JOIN)
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.TENIDO;
	}
	
	@Transient
	public String getDescripcionParaFactura(){
		return this.getDescripcion().substring(0, this.getDescripcion().lastIndexOf('-')-1).trim().toUpperCase();
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

}
