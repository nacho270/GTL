package ar.com.textillevel.entidades.cuenta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.gente.Cliente;

@Entity
@DiscriminatorValue(value="CC")
public class CuentaCliente extends Cuenta {

	private static final long serialVersionUID = -5831693779190588630L;

	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name="F_CLIENTE_P_ID", nullable=true)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}