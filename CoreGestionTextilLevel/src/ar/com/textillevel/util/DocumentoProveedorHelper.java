package ar.com.textillevel.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;

public class DocumentoProveedorHelper {

	private static DocumentoProveedorHelper instance = new DocumentoProveedorHelper();

	private DocumentoProveedorHelper() {
	}
	
	public static DocumentoProveedorHelper getInstance() {
		return instance;
	}

	public Float getFactorMoneda(List<ItemFacturaProveedor> itemFacturaList) {
		Set<Float> factorSet  = new HashSet<Float>();
		boolean estaElUno = false;
		for(ItemFacturaProveedor ifp : itemFacturaList) {
			if(ifp.getFactorConversionMoneda().floatValue() != 1f) {
				factorSet.add(ifp.getFactorConversionMoneda().floatValue());
			} else {
				estaElUno = true;
			}
		}
		if(factorSet.isEmpty() && estaElUno) {
			return 1f;
		}
		if(factorSet.isEmpty() || factorSet.size() > 1) {
			return null;
		}
		return factorSet.iterator().next();
	}

	public Float getFactorMonedaCorreccFact(List<ItemCorreccionFacturaProveedor> itemCorreccionFacturaList) {
		Set<Float> factorSet  = new HashSet<Float>();
		boolean estaElUno = false;
		for(ItemCorreccionFacturaProveedor ifp : itemCorreccionFacturaList) {
			if(ifp.getFactorConversionMoneda().floatValue() != 1f) {
				factorSet.add(ifp.getFactorConversionMoneda().floatValue());
			} else {
				estaElUno = true;
			}
		}
		if(factorSet.isEmpty() && estaElUno) {
			return 1f;
		}
		if(factorSet.isEmpty() || factorSet.size() > 1) {
			return null;
		}
		return factorSet.iterator().next();
	}
	
	public Map<ImpuestoItemProveedor, Double> calcularMapImpuestos(BigDecimal porcDescGlobal, List<ItemFacturaProveedor> itemFacturaList) {
		//armo un map con los impuestos y los totales por impuesto
		Map<ImpuestoItemProveedor, Double> mapImpuestos = new HashMap<ImpuestoItemProveedor, Double>();
		for(ItemFacturaProveedor ifp : itemFacturaList) {
			List<ImpuestoItemProveedor> impuestos = ifp.getImpuestos();
			//el % de descuento global se lo aplico a todos los items de factura para calcular el impuesto 
			BigDecimal importeOrig = ifp.recalcularImporteTotal();
			importeOrig = importeOrig.subtract(importeOrig.multiply(porcDescGlobal.divide(new BigDecimal(100))).multiply(ifp.getFactorConversionMoneda()));
			//aplico la lista de impuestos a cada item
			for(ImpuestoItemProveedor impuesto : impuestos) {
				if(impuesto != null) {
					Double impValue = mapImpuestos.get(impuesto);
					if(impValue == null) {
						impValue = 0D;
					}
					impValue += importeOrig.doubleValue() * (impuesto.getPorcDescuento()/100);
					mapImpuestos.put(impuesto, impValue);
				}
			}
		}
		return mapImpuestos;
	}

	public Map<ImpuestoItemProveedor, Double> calcularMapImpuestosCorreccFact(List<ItemCorreccionFacturaProveedor> itemCorreccionFacturaList) {
		Map<ImpuestoItemProveedor, Double> mapImpuestos = new HashMap<ImpuestoItemProveedor, Double>();
		for(ItemCorreccionFacturaProveedor icfp : itemCorreccionFacturaList) {
			List<ImpuestoItemProveedor> impuestos = icfp.getImpuestos();
			//el % de descuento global se lo aplico a todos los items de factura para calcular el impuesto 
			BigDecimal importeOrig = icfp.recalcularImporteTotal();
			//aplico la lista de impuestos a cada item
			for(ImpuestoItemProveedor impuesto : impuestos) {
				if(impuesto != null) {
					Double impValue = mapImpuestos.get(impuesto);
					if(impValue == null) {
						impValue = 0D;
					}
					impValue += importeOrig.doubleValue() * (impuesto.getPorcDescuento()/100);
					mapImpuestos.put(impuesto, impValue);
				}
			}
		}
		return mapImpuestos;
	}

}