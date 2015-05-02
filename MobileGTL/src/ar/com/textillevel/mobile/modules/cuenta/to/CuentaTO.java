package ar.com.textillevel.mobile.modules.cuenta.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CuentaTO implements Serializable{

	private static final long serialVersionUID = 2364846464615362210L;
	
	public static final int TIPO_CUENTA_CLIENTE = 1;
	public static final int TIPO_CUENTA_PROVEEDOR = 2;
	
	private Float saldo;
	private CuentaOwnerTO owner;
	private List<MovimientoTO> movimientos;
	private List<ContactoTO> resultadoBusquedaContacto; //para cuando se busca por texto y hay varios. Retornamos esta lista llena y el resto vacio
	
	public CuentaTO() {
		resultadoBusquedaContacto = new ArrayList<ContactoTO>();
		movimientos = new ArrayList<MovimientoTO>();
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

	public List<MovimientoTO> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<MovimientoTO> movimientos) {
		this.movimientos = movimientos;
	}

	public CuentaOwnerTO getOwner() {
		return owner;
	}

	public void setOwner(CuentaOwnerTO owner) {
		this.owner = owner;
	}

	public List<ContactoTO> getResultadoBusquedaContacto() {
		return resultadoBusquedaContacto;
	}

	public void setResultadoBusquedaContacto(List<ContactoTO> resultadoBusquedaContacto) {
		this.resultadoBusquedaContacto = resultadoBusquedaContacto;
	}
}
