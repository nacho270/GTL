package ar.com.textillevel.entidades.to.ivacompras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IVAComprasTO implements Serializable {

	private static final long serialVersionUID = -4447048125813563525L;

	private List<DescripcionFacturaIVAComprasTO> items; 

	public IVAComprasTO() {
		this.items = new ArrayList<DescripcionFacturaIVAComprasTO>();
	}

	public List<DescripcionFacturaIVAComprasTO> getItems() {
		return items;
	}

	public void setItems(List<DescripcionFacturaIVAComprasTO> items) {
		this.items = items;
	}

}
