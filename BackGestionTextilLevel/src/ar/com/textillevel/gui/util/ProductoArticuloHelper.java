package ar.com.textillevel.gui.util;

import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.facade.api.remote.ProductoArticuloFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class ProductoArticuloHelper {

	private ProductoArticuloFacadeRemote prodArticuloFacade;

	public ProductoArticuloHelper() {
		this.prodArticuloFacade = GTLBeanFactory.getInstance().getBean2(ProductoArticuloFacadeRemote.class);
	}

	public List<ProductoArticulo> getPersistentInstances(List<ProductoArticulo> prodArticuloList) {
		return internalGetPersistentInstances(prodArticuloList);
	}

	public ProductoArticulo getPersistentInstance(ProductoArticulo pa) {
		List<ProductoArticulo> paList = new ArrayList<ProductoArticulo>();
		paList.add(pa);
		return internalGetPersistentInstances(paList).get(0);
	}

	private List<ProductoArticulo> internalGetPersistentInstances(List<ProductoArticulo> prodArticuloList) {
		List<ProductoArticulo> allTransientIntances = new ArrayList<ProductoArticulo>();
		List<ProductoArticulo> allPersistentIntances = new ArrayList<ProductoArticulo>();
		for(ProductoArticulo pa : prodArticuloList) {
			if(pa.getId() == null) {
				allTransientIntances.add(pa);
			} else {
				allPersistentIntances.add(pa);
			}
		}
		if(!allTransientIntances.isEmpty()) {
			allPersistentIntances.addAll(prodArticuloFacade.save(allTransientIntances));
		}
		return allPersistentIntances;
	}

}