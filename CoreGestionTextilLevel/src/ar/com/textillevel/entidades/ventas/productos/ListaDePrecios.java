package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.gente.Cliente;

public class ListaDePrecios implements Serializable {

	private static final long serialVersionUID = -3823366476053289833L;

	private Integer id;
	private Cliente cliente;
	private List<PrecioProducto> precios;

	public ListaDePrecios() {
		this.precios = new ArrayList<PrecioProducto>();  
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<PrecioProducto> getPrecios() {
		return precios;
	}

	public void setPrecios(List<PrecioProducto> precios) {
		this.precios = precios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		final ListaDePrecios other = (ListaDePrecios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}