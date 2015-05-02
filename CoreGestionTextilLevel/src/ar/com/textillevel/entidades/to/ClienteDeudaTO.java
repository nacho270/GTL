package ar.com.textillevel.entidades.to;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClienteDeudaTO implements Serializable {

	private static final long serialVersionUID = 3544092888648381624L;

	private String razonSocial;
	private BigDecimal deuda;

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public BigDecimal getDeuda() {
		return deuda;
	}

	public void setDeuda(BigDecimal deuda) {
		this.deuda = deuda;
	}
}
