package ar.com.textillevel.modulos.fe.connector;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoIVAAFIP;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.modulos.fe.cliente.dto.AlicIva;
import ar.com.textillevel.modulos.fe.cliente.dto.CbteAsoc;
import ar.com.textillevel.modulos.fe.cliente.requests.FECAECabRequest;
import ar.com.textillevel.modulos.fe.cliente.requests.FECAEDetRequest;
import ar.com.textillevel.modulos.fe.cliente.requests.FECAERequest;

public class AFIPConverter {

	private static DateFormat df;
	private static NumberFormat nf;
	
	static {
		df = new SimpleDateFormat(DateUtil.DEFAULT_DATE_WITHOUT_SEPARATOR);
		nf = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setMinimumIntegerDigits(1);
		nf.setGroupingUsed(true);
	}
	
	public static FECAERequest crearRequest(DocumentoContableCliente documento, int nroSucursal, int idTipoComprobanteAFIP) {
		FECAERequest req = new FECAERequest();
		req.setFeCabReq(crearCabeceraRequest(1, nroSucursal, idTipoComprobanteAFIP));
		req.setFeDetReq(new FECAEDetRequest[]{documentoACAERequest(documento, nroSucursal)});
		return req;
	}

	private static FECAECabRequest crearCabeceraRequest(int cantidad, int nroSucursal, int idTipoComprobanteAFIP) {
		FECAECabRequest cabecera = new FECAECabRequest();
		cabecera.setCantReg(cantidad);
		cabecera.setPtoVta(nroSucursal);
		cabecera.setCbteTipo(idTipoComprobanteAFIP); //ver el metodo AFIPConnector.getTiposComprobante
		return cabecera;
	}

	private static FECAEDetRequest documentoACAERequest(DocumentoContableCliente documento, int nroSucursal) {
		FECAEDetRequest req = new FECAEDetRequest();
		req.setConcepto(EConceptoFacturaElectronica.SERVICIOS.getId());
		req.setCbteDesde(documento.getNroFactura()); //no entiendo bien porque hay desde y hasta
		req.setCbteHasta(documento.getNroFactura());

		req.setDocNro(Long.valueOf(documento.getCliente().getCuit().replace("-", ""))); // Imagino que siempre es con CUIT.
		req.setDocTipo(80);  // HARDCODE id para CUIT. Hay que ver el tipo de documento a usar basandose en el metodo  AFIPConnector.getTiposDoc

		req.setCbteFch(df.format(documento.getFechaEmision()));

		req.setFchServDesde(req.getCbteFch());
		req.setFchServHasta(req.getCbteFch()); //Hay que ver bien que significa esto!!!
		req.setFchVtoPago(req.getCbteFch());

		req.setImpTotal(getFormatedDouble(documento.getMontoTotal().doubleValue()));
		double montoIVA = getFormatedDouble(documento.getTotalIVA());
		req.setImpIVA(montoIVA);
		req.setMonCotiz(1d);
		req.setMonId("PES"); //hardcode pesos. Podria ser dinamico pero el WS retorna solo esta y es lo unico que se usa.
		req.setImpTotConc(0d); //no se usa. Ver FacturaFacade:253
		req.setImpNeto(getFormatedDouble(documento.getMontoSubtotal().floatValue())); //Ver FacturaFacade:258. Ver que pasa con los nulls. No creo que haga falta multiplicar por -1 si es NC
		req.setImpOpEx(0d); //no se usa. Ver FacturaFacade:252
		req.setImpTrib(0d); //no se que es
		
		ETipoIVAAFIP tipoIva;
		if (montoIVA == 0d) {
			tipoIva = ETipoIVAAFIP.IVA_0;
		}else{
			tipoIva = ETipoIVAAFIP.IVA_21;
		}
		
		AlicIva ali = new AlicIva();
		ali.setId(tipoIva.getIdAFIP());
		ali.setBaseImp(getFormatedDouble(documento.getMontoSubtotal().floatValue()));
		ali.setImporte(montoIVA);
		req.setIva(new AlicIva[]{ali});

		//Comprobantes asociados
		if(!documento.getDocsContableRelacionados().isEmpty()) {
			CbteAsoc[] cbtesAsoc = new CbteAsoc[documento.getDocsContableRelacionados().size()];
			for(int i=0; i < documento.getDocsContableRelacionados().size(); i++) {
				DocumentoContableCliente docRel = documento.getDocsContableRelacionados().get(i);
				CbteAsoc comprobanteAsociado = new CbteAsoc(docRel.getTipoDocumento().getIdTipoDocAFIP(documento.getTipoFactura()), nroSucursal, docRel.getNroFactura());
				cbtesAsoc[i] = comprobanteAsociado;
			}
			req.setCbtesAsoc(cbtesAsoc);
		}
		return req;
	}

	private static double getFormatedDouble(double value) {
		return new Double(nf.format(value).replaceAll(",", ""));
	}

}