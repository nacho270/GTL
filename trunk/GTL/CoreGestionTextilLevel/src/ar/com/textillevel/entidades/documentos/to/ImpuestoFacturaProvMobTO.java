package ar.com.textillevel.entidades.documentos.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.util.Utils;

public class ImpuestoFacturaProvMobTO implements Serializable {

	private static final long serialVersionUID = 3364562557833920148L;

	private String total;
	private String totalConFactor;
	private List<ItemImpuestoFacturaProvMobTO> itemsImpuesto;

	public ImpuestoFacturaProvMobTO(Float factorMoneda, Map<ImpuestoItemProveedor, Double> mapImpuesto) {
		this.itemsImpuesto = new ArrayList<ItemImpuestoFacturaProvMobTO>();
		double totalD = 0d;
		for(ImpuestoItemProveedor item : mapImpuesto.keySet()) {
			Double valor = mapImpuesto.get(item);
			this.itemsImpuesto.add(new ItemImpuestoFacturaProvMobTO(item.toString(), Utils.getDecimalFormat().format(valor.doubleValue()).toString()));
			totalD += valor;
		}
		this.total = Utils.getDecimalFormat().format(totalD);
		this.totalConFactor = Utils.getDecimalFormat().format(totalD/factorMoneda);
	}

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