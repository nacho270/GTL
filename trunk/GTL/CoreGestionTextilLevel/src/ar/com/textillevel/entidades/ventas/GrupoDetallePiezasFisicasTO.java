package ar.com.textillevel.entidades.ventas;

import java.io.Serializable;
import java.util.List;

public class GrupoDetallePiezasFisicasTO implements Serializable {

	private static final long serialVersionUID = -8206330887301653438L;

	private String odt;
	private String proveedor;
	private List<DetallePiezaFisicaTO> piezasTotales;
	private List<DetallePiezaFisicaTO> piezasSeleccionadas;

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

	public List<DetallePiezaFisicaTO> getPiezasTotales() {
		return piezasTotales;
	}

	public void setPiezasTotales(List<DetallePiezaFisicaTO> piezasTotales) {
		this.piezasTotales = piezasTotales;
	}

	public List<DetallePiezaFisicaTO> getPiezasSeleccionadas() {
		return piezasSeleccionadas;
	}

	public void setPiezasSeleccionadas(List<DetallePiezaFisicaTO> piezasSeleccionadas) {
		this.piezasSeleccionadas = piezasSeleccionadas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		GrupoDetallePiezasFisicasTO other = (GrupoDetallePiezasFisicasTO) obj;
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
