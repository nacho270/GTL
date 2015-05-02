package ar.com.textillevel.entidades.cuenta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cheque.Banco;

@Entity
@DiscriminatorValue(value = "CB")
public class CuentaBanco extends Cuenta{

	private static final long serialVersionUID = -5806359000568865918L;

	private Banco banco;

	@ManyToOne
	@JoinColumn(name="F_BANCO_P_ID", nullable=true)
	public Banco getBanco() {
		return banco;
	}
	
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
}
