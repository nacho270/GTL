package ar.com.textillevel.entidades.to.informeproduccion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InformeProduccionTO implements Serializable {

	private static final long serialVersionUID = 6381777079066657058L;

	private List<ItemInformeProduccionTO> items;
	private String total;

	public InformeProduccionTO(){
		items = new ArrayList<ItemInformeProduccionTO>();
	}
	
	public List<ItemInformeProduccionTO> getItems() {
		return items;
	}

	public void setItems(List<ItemInformeProduccionTO> items) {
		this.items = items;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
