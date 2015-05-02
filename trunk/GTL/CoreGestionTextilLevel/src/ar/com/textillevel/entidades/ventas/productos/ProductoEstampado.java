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
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("ESTAMPADO")
public class ProductoEstampado extends Producto implements Serializable{
	
	private static final long serialVersionUID = 2536700659829607857L;

	private DibujoEstampado dibujo;
	private VarianteEstampado variante;
	
	public ProductoEstampado() {
		super();
	}

	@ManyToOne
	@JoinColumn(name="F_DIBUJO_P_ID")
	@Fetch(FetchMode.JOIN)
	public DibujoEstampado getDibujo() {
		return dibujo;
	}
	
	public void setDibujo(DibujoEstampado dibujo) {
		this.dibujo = dibujo;
	}
	
	@ManyToOne
	@JoinColumn(name="F_VARIANTE_P_ID")
	@Fetch(FetchMode.JOIN)
	public VarianteEstampado getVariante() {
		return variante;
	}
	
	public void setVariante(VarianteEstampado variante) {
		this.variante = variante;
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.ESTAMPADO;
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

}
