package ar.com.textillevel.modulos.odt.to.stock;

import java.io.Serializable;

import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

public class InfoBajaStock implements Serializable{

	private static final long serialVersionUID = 3322371389858092031L;

	private ETipoInfoBajaStock tipo;
	private PrecioMateriaPrima precioMateriaPrima;
	private Float cantidadADescontar;

	public InfoBajaStock(ETipoInfoBajaStock tipo, PrecioMateriaPrima precioMateriaPrima, Float cantidadADescontar) {
		this.tipo = tipo;
		this.precioMateriaPrima = precioMateriaPrima;
		this.cantidadADescontar = cantidadADescontar;
	}

	public ETipoInfoBajaStock getTipo() {
		return tipo;
	}

	public void setTipo(ETipoInfoBajaStock tipo) {
		this.tipo = tipo;
	}

	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	public Float getCantidadADescontar() {
		return cantidadADescontar;
	}

	public void setCantidadADescontar(Float cantidadADescontar) {
		this.cantidadADescontar = cantidadADescontar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantidadADescontar == null) ? 0 : cantidadADescontar.hashCode());
		result = prime * result + ((precioMateriaPrima == null) ? 0 : precioMateriaPrima.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoBajaStock other = (InfoBajaStock) obj;
		if (cantidadADescontar == null) {
			if (other.cantidadADescontar != null)
				return false;
		} else if (!cantidadADescontar.equals(other.cantidadADescontar))
			return false;
		if (precioMateriaPrima == null) {
			if (other.precioMateriaPrima != null)
				return false;
		} else if (!precioMateriaPrima.equals(other.precioMateriaPrima))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}
}
