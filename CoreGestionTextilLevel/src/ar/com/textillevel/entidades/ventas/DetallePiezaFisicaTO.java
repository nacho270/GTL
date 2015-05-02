package ar.com.textillevel.entidades.ventas;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetallePiezaFisicaTO implements Serializable {

	private static final long serialVersionUID = 8868255443961243084L;

	private Integer nroPieza;
	private BigDecimal metros;
	private String odt;
	private String proveedor;
	private Integer nroRemito;
	private Integer idRemito;
	private Integer idPiezaRemito;

	public Integer getNroPieza() {
		return nroPieza;
	}

	public void setNroPieza(Integer nroPieza) {
		this.nroPieza = nroPieza;
	}

	public BigDecimal getMetros() {
		return metros;
	}

	public void setMetros(BigDecimal metros) {
		this.metros = metros;
	}

	public String getOdt() {
		return odt;
	}

	public void setOdt(String odt) {
		this.odt = odt;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public Integer getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(Integer nroRemito) {
		this.nroRemito = nroRemito;
	}

	public Integer getIdRemito() {
		return idRemito;
	}

	public void setIdRemito(Integer idRemito) {
		this.idRemito = idRemito;
	}

	public Integer getIdPiezaRemito() {
		return idPiezaRemito;
	}

	public void setIdPiezaRemito(Integer idPiezaRemito) {
		this.idPiezaRemito = idPiezaRemito;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPiezaRemito == null) ? 0 : idPiezaRemito.hashCode());
		result = prime * result + ((idRemito == null) ? 0 : idRemito.hashCode());
		result = prime * result + ((metros == null) ? 0 : metros.hashCode());
		result = prime * result + ((nroPieza == null) ? 0 : nroPieza.hashCode());
		result = prime * result + ((nroRemito == null) ? 0 : nroRemito.hashCode());
		result = prime * result + ((odt == null) ? 0 : odt.hashCode());
		result = prime * result + ((proveedor == null) ? 0 : proveedor.hashCode());
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
		DetallePiezaFisicaTO other = (DetallePiezaFisicaTO) obj;
		if (idPiezaRemito == null) {
			if (other.idPiezaRemito != null)
				return false;
		} else if (!idPiezaRemito.equals(other.idPiezaRemito))
			return false;
		if (idRemito == null) {
			if (other.idRemito != null)
				return false;
		} else if (!idRemito.equals(other.idRemito))
			return false;
		if (metros == null) {
			if (other.metros != null)
				return false;
		} else if (!metros.equals(other.metros))
			return false;
		if (nroPieza == null) {
			if (other.nroPieza != null)
				return false;
		} else if (!nroPieza.equals(other.nroPieza))
			return false;
		if (nroRemito == null) {
			if (other.nroRemito != null)
				return false;
		} else if (!nroRemito.equals(other.nroRemito))
			return false;
		if (odt == null) {
			if (other.odt != null)
				return false;
		} else if (!odt.equals(other.odt))
			return false;
		if (proveedor == null) {
			if (other.proveedor != null)
				return false;
		} else if (!proveedor.equals(other.proveedor))
			return false;
		return true;
	}
}
