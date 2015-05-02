package ar.com.textillevel.mobile.modules.cuenta.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;
import ar.com.textillevel.mobile.modules.common.handlers.PostExecuteHandler;
import ar.com.textillevel.mobile.modules.common.to.ETipoDocumento;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.ChequePostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.CorreccionFacturaClientePostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.FacturaPostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.FacturaProveedorPostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.OrdenDePagoPostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.ReciboPostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.RemitoEntradaPostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.RemitoEntradaProvPostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.handlers.RemitoSalidaPostExecuteHandler;
import ar.com.textillevel.mobile.util.AppConfig;
import ar.com.textillevel.mobile.util.CallAPI;

public class DocumentoClickResolver {

	public static DocumentoClickResolver instance = new DocumentoClickResolver();

	public static DocumentoClickResolver getInstance() {
		return instance;
	}

	@SuppressLint("DefaultLocale")
	public void buscarDocumentoPorNumeroYString(ETipoDocumento tipoDoc, String numero, String str, final Activity activity){
		if(tipoDoc == ETipoDocumento.CHEQUE){
			List<RequestParamContainer> lista = new ArrayList<RequestParamContainer>();
			lista.add(new RequestParamContainer("letraCheque", str.toUpperCase()));
			lista.add(new RequestParamContainer("nroCheque", numero));
			requestDocumentByParam(lista, AppConfig.API_URL_CHEQUE_BY_RO, tipoDoc.getId(), new ChequePostExecuteHandler(activity));
		}else{
			Toast.makeText(activity, "AÚN NO IMPLEMENTADO!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void buscarDocumentoPorNumero(ETipoDocumento tipoDoc, String nroDoc, final Activity activity) {
		if (tipoDoc == ETipoDocumento.FACTURA) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("nroFactura", nroDoc)), AppConfig.API_URL_FACTURA_BY_NRO, tipoDoc.getId(), new FacturaPostExecuteHandler(activity));
		} else if (tipoDoc == ETipoDocumento.RECIBO) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("nroRecibo", nroDoc)), AppConfig.API_URL_RECIBO_BY_NRO, tipoDoc.getId(), new ReciboPostExecuteHandler(activity));
		} else if (tipoDoc == ETipoDocumento.ORDEN_PAGO) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("nroODP", nroDoc)), AppConfig.API_URL_ODP_BY_NRO, tipoDoc.getId(), new OrdenDePagoPostExecuteHandler(activity));
		} else if (tipoDoc == ETipoDocumento.CHEQUE) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("nroCheque", nroDoc)), AppConfig.API_URL_CHEQUE, tipoDoc.getId(), new ChequePostExecuteHandler(activity));
		} else if (tipoDoc == ETipoDocumento.REMITO_ENTRADA) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("nroRE", nroDoc)), AppConfig.API_URL_RE, tipoDoc.getId(), new RemitoEntradaPostExecuteHandler(activity));
		} else if(tipoDoc == ETipoDocumento.NOTA_CREDITO || tipoDoc == ETipoDocumento.NOTA_DEBITO){
			//TODO:
		} else if(tipoDoc == ETipoDocumento.REMITO_SALIDA){
			//TODO:
		} else {
			Toast.makeText(activity, "AÚN NO IMPLEMENTADO!", Toast.LENGTH_LONG).show();
		}
	}

	public void clickDocumento(Integer idTipoDocumento, Integer idDocumento, final Activity activity) {
		if (idTipoDocumento == ETipoDocumento.FACTURA.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idFactura", String.valueOf(idDocumento))), AppConfig.API_URL_FACTURA, idTipoDocumento, new FacturaPostExecuteHandler(activity));
		} else if (idTipoDocumento == ETipoDocumento.RECIBO.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idRecibo", String.valueOf(idDocumento))), AppConfig.API_URL_RECIBO, idTipoDocumento, new ReciboPostExecuteHandler(activity));
		} else if (idTipoDocumento == ETipoDocumento.ORDEN_PAGO.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idODP", String.valueOf(idDocumento))), AppConfig.API_URL_ODP, idTipoDocumento, new OrdenDePagoPostExecuteHandler(activity));
		} else if (idTipoDocumento == ETipoDocumento.CHEQUE.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idCheque", String.valueOf(idDocumento))), AppConfig.API_URL_CHEQUE, idTipoDocumento, new ChequePostExecuteHandler(activity));
		} else if (idTipoDocumento == ETipoDocumento.REMITO_ENTRADA.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idRE", String.valueOf(idDocumento))), AppConfig.API_URL_RE, idTipoDocumento, new RemitoEntradaPostExecuteHandler(activity));
		} else if(idTipoDocumento == ETipoDocumento.NOTA_CREDITO.getId() || idTipoDocumento == ETipoDocumento.NOTA_DEBITO.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idCorreccion", String.valueOf(idDocumento))), AppConfig.API_URL_CORRECCION_CLIENTE, idTipoDocumento, new CorreccionFacturaClientePostExecuteHandler(activity));
		} else if(idTipoDocumento == ETipoDocumento.REMITO_SALIDA.getId()){
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idRS", String.valueOf(idDocumento))), AppConfig.API_URL_RS, idTipoDocumento, new RemitoSalidaPostExecuteHandler(activity));
		} else if(idTipoDocumento == ETipoDocumento.FACTURA_PROV.getId()){
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idFactura", String.valueOf(idDocumento))), AppConfig.API_URL_FACTURA_PROV, idTipoDocumento, new FacturaProveedorPostExecuteHandler(activity));
		} else if(idTipoDocumento == ETipoDocumento.NOTA_CREDITO_PROV.getId() || idTipoDocumento == ETipoDocumento.NOTA_DEBITO_PROV.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idCorreccion", String.valueOf(idDocumento))), AppConfig.API_URL_CORRECCION_FACTURA_PROV, idTipoDocumento, new FacturaProveedorPostExecuteHandler(activity));			
		} else if(idTipoDocumento == ETipoDocumento.REMITO_ENTRADA_PROV.getId()) {
			requestDocumentByParam(Collections.singletonList(new RequestParamContainer("idRE", String.valueOf(idDocumento))), AppConfig.API_URL_RE_PROV, idTipoDocumento, new RemitoEntradaProvPostExecuteHandler(activity));
		} else {
			Toast.makeText(activity, "AÚN NO IMPLEMENTADO!", Toast.LENGTH_LONG).show();
		}
	}

	private void requestDocumentByParam(List<RequestParamContainer> params, String apiURL, Integer idTipoDocumento, PostExecuteHandler handler) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(idTipoDocumento);
		for(RequestParamContainer rp : params){
			nameValuePairs.add(new BasicNameValuePair(rp.getParam(), rp.getValue()));
		}
		new CallAPI(AppConfig.getInstance().getServer() + apiURL, CallAPI.GET_METHOD, nameValuePairs, handler).execute();
	}

}