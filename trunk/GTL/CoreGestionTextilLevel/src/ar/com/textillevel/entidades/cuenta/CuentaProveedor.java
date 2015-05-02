package ar.com.textillevel.entidades.cuenta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.gente.Proveedor;

@Entity
@DiscriminatorValue(value = "CP")
public class CuentaProveedor extends Cuenta {

	private static final long serialVersionUID = -8339263198419855409L;

	private Proveedor proveedor;

	@ManyToOne
	@JoinColumn(name="F_PROVEEDOR_P_ID", nullable=true)
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

}
