package ar.com.textillevel.entidades.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;

public class ResultadoBusquedaAgenda implements Serializable {

	private static final long serialVersionUID = 2627653189673256165L;

	private List<Cliente> clientes;
	private List<Proveedor> proveedores;

	public ResultadoBusquedaAgenda() {
		proveedores = new ArrayList<Proveedor>();
		clientes = new ArrayList<Cliente>();
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
}
