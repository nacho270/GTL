package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;

public class ABMSecuenciasModelTO implements Serializable {

	private static final long serialVersionUID = -6549497435017868735L;

	private List<SecuenciaTipoProducto> secuenciasAPersistir;
	private List<SecuenciaTipoProducto> secuenciasABorrar;

	public ABMSecuenciasModelTO() {
		this.secuenciasAPersistir = new ArrayList<SecuenciaTipoProducto>();
		this.secuenciasABorrar = new ArrayList<SecuenciaTipoProducto>();
	}

	public List<SecuenciaTipoProducto> getSecuenciasAPersistir() {
		return secuenciasAPersistir;
	}

	public void setSecuenciasAPersistir(List<SecuenciaTipoProducto> secuenciasAPersistir) {
		this.secuenciasAPersistir = secuenciasAPersistir;
	}

	public List<SecuenciaTipoProducto> getSecuenciasABorrar() {
		return secuenciasABorrar;
	}

	public void setSecuenciasABorrar(List<SecuenciaTipoProducto> secuenciasABorrar) {
		this.secuenciasABorrar = secuenciasABorrar;
	}
}
