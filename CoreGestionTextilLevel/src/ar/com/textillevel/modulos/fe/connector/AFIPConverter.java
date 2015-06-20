package ar.com.textillevel.modulos.fe.connector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.modulos.fe.cliente.dto.AlicIva;
import ar.com.textillevel.modulos.fe.cliente.requests.FECAECabRequest;
import ar.com.textillevel.modulos.fe.cliente.requests.FECAEDetRequest;
import ar.com.textillevel.modulos.fe.cliente.requests.FECAERequest;

public class AFIPConverter {

	private static DateFormat df;
	static {
		df = new SimpleDateFormat(DateUtil.DEFAULT_DATE_WITHOUT_SEPARATOR); 
	}
	
	public static FECAERequest crearRequest(List<DocumentoContableCliente> documentos, int nroSucursal, int idTipoComprobanteAFIP) {
		FECAERequest req = new FECAERequest();
		req.setFeCabReq(crearCabeceraRequest(documentos.size(), nroSucursal, idTipoComprobanteAFIP));
		FECAEDetRequest[] docsRequests = new FECAEDetRequest[documentos.size()];
		for(int i = 0; i < documentos.size(); i++) {
			docsRequests[i] = documentoACAERequest(documentos.get(i));
		}
		req.setFeDetReq(docsRequests);
		return req;
	}

	private static FECAECabRequest crearCabeceraRequest(int cantidad, int nroSucursal, int idTipoComprobanteAFIP) {
		FECAECabRequest cabecera = new FECAECabRequest();
		cabecera.setCantReg(cantidad);
		cabecera.setPtoVta(nroSucursal);
		cabecera.setCbteTipo(idTipoComprobanteAFIP); //ver el metodo AFIPConnector.getTiposComprobante
		return cabecera;
	}
	
	private static FECAEDetRequest documentoACAERequest(DocumentoContableCliente documento) {
		FECAEDetRequest req = new FECAEDetRequest();
		req.setConcepto(EConceptoFacturaElectronica.SERVICIOS.getId());
		req.setCbteDesde(documento.getNroFactura()); //no entiendo bien porque hay desde y hasta
		req.setCbteHasta(documento.getNroFactura());
		
		req.setDocNro(Long.valueOf(documento.getCliente().getCuit().replace("-", ""))); // Imagino que siempre es con CUIT.
		req.setDocTipo(80);  // HARDCODE id para CUIT. Hay que ver el tipo de documento a usar basandose en el metodo  AFIPConnector.getTiposDoc
		
		req.setCbteFch(df.format(documento.getFechaEmision()));
		req.setImpTotal(documento.getMontoTotal().doubleValue());
		double montoIVA = documento.getMontoSubtotal().multiply(documento.getPorcentajeIVAInscripto()).doubleValue();
		req.setImpIVA(montoIVA);
		req.setMonCotiz(1d);
		req.setMonId("PES"); //hardcode pesos. Podria ser dinamico pero el WS retorna solo esta y es lo unico que se usa.
		req.setImpTotConc(0d); //no se usa. Ver FacturaFacade:253
		req.setImpNeto(documento.getMontoSubtotal().doubleValue()); //Ver FacturaFacade:258. Ver que pasa con los nulls. No creo que haga falta multiplicar por -1 si es NC
		req.setImpOpEx(0d); //no se usa. Ver FacturaFacade:252
		req.setImpTrib(0d); //no se que es
		AlicIva ali = new AlicIva();
		ali.setId(5);
		ali.setBaseImp(documento.getMontoSubtotal().doubleValue());
		ali.setImporte(montoIVA);
		req.setIva(new AlicIva[]{ali});
		return req;
	}
}
