package ar.com.textillevel.mobile.modules.factura.proveedor.to;

import java.io.Serializable;
import java.util.List;

public class ImpuestoFacturaProvMobTO implements Serializable {

	private static final long serialVersionUID = 3364562557833920148L;

	private String total;
	private String totalConFactor;
	private List<ItemImpuestoFacturaProvMobTO> itemsImpuesto;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotalConFactor() {
		return totalConFactor;
	}

	public void setTotalConFactor(String totalConFactor) {
		this.totalConFactor = totalConFactor;
	}

	public List<ItemImpuestoFacturaProvMobTO> getItemsImpuesto() {
		return itemsImpuesto;
	}

	public void setItemsImpuesto(List<ItemImpuestoFacturaProvMobTO> itemsImpuesto) {
		this.itemsImpuesto = itemsImpuesto;
	}

}