package ar.com.textillevel.gui.acciones.impresionfactura;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaBonificacion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaOtro;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPercepcion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaProducto;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaRecargo;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaSeguro;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaTelaCruda;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaTubo;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoImpresionDocumento;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ClienteTO;
import ar.com.textillevel.entidades.to.FacturaTO;
import ar.com.textillevel.entidades.to.ItemFacturaTO;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.GTLBeanFactory;

public class ImpresionFacturaHandler {

	private Factura factura;
	private CorreccionFactura correccionFactura;
	private List<RemitoSalida> remitos;
	private String cantidadCopias;
	private Cliente cliente;
	private FacturaFacadeRemote facturaFacade;
	private CorreccionFacadeRemote correccionFacade;
	private DocumentoContableFacadeRemote documentoContableFacade;
	private ClienteFacadeRemote clienteFacade;
	private String strFacturasRelacionadas;

	public ImpresionFacturaHandler(DocumentoContableCliente documento, String cantidadCopias) {
		this.cantidadCopias = cantidadCopias;
		facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
		correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		documentoContableFacade = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class);
		clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		this.cliente = clienteFacade.getById(documento.getCliente().getId());
		if (documento.getTipoDocumento() == ETipoDocumento.FACTURA){
			setFactura((Factura)documento);
			if(getFactura().getRemitos() != null && !getFactura().getRemitos().isEmpty()) {
				setRemitos(GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class).getByIdsConPiezasYProductos(extractIds(getFactura().getRemitos())));
			}
		}else{
			setCorreccionFactura((CorreccionFactura)documento);
			if(getCorreccionFactura() instanceof NotaCredito){
				setStrFacturasRelacionadas(getNrosFacturasRelacionadas( ((NotaCredito) getCorreccionFactura()).getFacturasRelacionadas()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public JasperPrint getJasperPrint() throws IOException, ValidacionException {
		documentoContableFacade.checkImpresionDocumentoContable(getFactura() != null ? getFactura() : getCorreccionFactura());
		FacturaTO factura = armarFacturaTO();
		JasperReport reporte;
		if (GenericUtils.isSistemaTest()) {
			reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/facturab_v2.jasper");
		} else {
			reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/factura_electronica.jasper");
		}
		return JasperHelper.fillReport(reporte, getParametros(factura, 1), factura.getItems());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void imprimir() throws JRException, FWException, ValidacionException, IOException {
		documentoContableFacade.checkImpresionDocumentoContable(getFactura() != null ? getFactura() : getCorreccionFactura());
		FacturaTO factura = armarFacturaTO();
		JasperReport reporte;
		if (GenericUtils.isSistemaTest()) {
			reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/facturab_v2.jasper");
		} else {
			reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/factura_electronica.jasper");
		}
		Integer cantidadAImprimir = Integer.valueOf(cantidadCopias);
		Integer cantidadImpresa = 0;
		for (int i = 1; i <= cantidadAImprimir; i++) {
			Map parameters = getParametros(factura, i);
			JasperPrint jasperPrint = JasperHelper.fillReport(reporte, parameters, factura.getItems());
			Integer copiasImpresas = JasperHelper.imprimirReporte(jasperPrint, true, true, 1);
			if (copiasImpresas != null) {
				cantidadImpresa = copiasImpresas.intValue() + 1;
			}
		}
		if (getCorreccionFactura() == null) {
			if (cantidadImpresa.equals(cantidadAImprimir)) {
				getFactura().setEstadoImpresion(EEstadoImpresionDocumento.IMPRESO);
			} else {
				getFactura().setEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE);
			}
			setFactura(getFacturaFacade().actualizarFactura(getFactura()));
		} else {
			if (cantidadImpresa.equals(cantidadAImprimir)) {
				getCorreccionFactura().setEstadoImpresion(EEstadoImpresionDocumento.IMPRESO);
			} else {
				getCorreccionFactura().setEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE);
			}
			setCorreccionFactura(getCorreccionFacade().actualizarCorreccion(getCorreccionFactura()));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getParametros(FacturaTO factura, int numeroCopia) throws IOException {
		Map parameters = new HashMap();
		parameters.put("NRO_FACTURA", factura.getNroFactura());
		parameters.put("TIPO_FACTURA", factura.getTipoFactura());
		parameters.put("RAZON_SOCIAL", factura.getCliente().getRazonSocial());
		parameters.put("DIRECCION", factura.getCliente().getDireccion());
		parameters.put("LOCALIDAD", factura.getCliente().getLocalidad());
		parameters.put("IVA", factura.getCliente().getCondicionIVA());
		parameters.put("CUIT", factura.getCliente().getCuit());
		parameters.put("COND_VENTA", factura.getCondicionVenta());
		parameters.put("NRO_REMITO", factura.getNroRemito());
		parameters.put("SUBTOTAL", factura.getSubTotal());
		parameters.put("PORC_IVA", factura.getPorcIvaInsc());
		parameters.put("TOTAL_IVA", factura.getTotalIvaInscr());
		parameters.put("TOTAL", factura.getTotalFactura());
		parameters.put("CAE", factura.getCaeAFIP());
		if (!GenericUtils.isSistemaTest()) {
			String barCode = getFactura()!=null?
					getFactura().crearCodigoDeBarrasAFIP(String.valueOf(documentoContableFacade.getCuitEmpresa())):
						getCorreccionFactura().crearCodigoDeBarrasAFIP(String.valueOf(documentoContableFacade.getCuitEmpresa()));
			parameters.put("BAR_CODE", barCode); //DEJO ESTO POR LAS DUDAS... COMO PARA TENER EL NUMERO POR SEPARADO, PERO NO HACE FALTA
			parameters.put("FECHA_VENCIMIENTO", getFactura()!=null?getFactura().convertirFechaVencimientoAFIP():getCorreccionFactura().convertirFechaVencimientoAFIP());
			parameters.put("IMAGEN", GenericUtils.createBarCode(barCode));
		}
		parameters.put("FECHA_FACT", DateUtil.dateToString(DateUtil.stringToDate(factura.getFecha()), DateUtil.SHORT_DATE));
		parameters.put("TIPO_DOC", factura.getTipoDocumento());
		parameters.put("COD_DOC", "COD. " + factura.getCodigoDocumento());
		if(numeroCopia == 1) {
			parameters.put("TIPO_COPIA", "ORIGINAL");
		} else if (numeroCopia == 2) {
			parameters.put("TIPO_COPIA", "DUPLICADO");
		} else if (numeroCopia == 3) {
			parameters.put("TIPO_COPIA", "TRIPLICADO");
		}
		return parameters;
	}

	private FacturaTO armarFacturaTO() {
		FacturaTO factura = new FacturaTO();
		factura.setCliente(getClienteTO());
		factura.setItems(getItemsTO());
		if(getFactura()!=null){
			factura.setCondicionVenta(getFactura().getCondicionDeVenta().getNombre());
		} else {
			factura.setCondicionVenta("");
		}
		factura.setFecha(DateUtil.dateToString(getFactura()!=null?getFactura().getFechaEmision():getCorreccionFactura().getFechaEmision(), DateUtil.SHORT_DATE));
		if (GenericUtils.isSistemaTest() && getFactura()!=null) {
			setFactura(getFacturaFacade().getByIdEagerRemitosEntrada(getFactura().getId()));
			factura.setNroFactura(getStrFacturasRelacionadas() != null && getStrFacturasRelacionadas().trim().length() > 0 ? getStrFacturasRelacionadas().replaceAll("/", " / ") : getNrosRemitos());
			factura.setNroRemito(extractNrosRemitoEntradaFactura());
		}else{
			factura.setNroFactura(getNroFactura());
			factura.setNroRemito(getStrFacturasRelacionadas() != null && getStrFacturasRelacionadas().trim().length() > 0 ? getStrFacturasRelacionadas().replaceAll("/", " / ") : getNrosRemitos());
		}
		BigDecimal porcIvaInscripto = getFactura()!=null?getFactura().getPorcentajeIVAInscripto():getCorreccionFactura().getPorcentajeIVAInscripto();
		if(porcIvaInscripto != null) {
			factura.setPorcIvaInsc(GenericUtils.getDecimalFormatFactura().format(porcIvaInscripto));
		}
		factura.setSubTotal((getCorreccionFactura() != null && getCorreccionFactura() instanceof NotaCredito ? "-" : "") + (getCorreccionFactura() != null ? GenericUtils.getDecimalFormatFactura().format(getCorreccionFactura().getMontoSubtotal().doubleValue()) : GenericUtils.getDecimalFormatFactura().format(getFactura().getMontoSubtotal().doubleValue())));
		
		if (getCorreccionFactura() != null) {
			factura.setTipoDocumento(getCorreccionFactura().getTipo().getDescripcion());
			factura.setCodigoDocumento(StringUtil.fillLeftWithZeros("" + getCorreccionFactura().getTipoDocumento().getIdTipoDocAFIP(null), 2));
		} else if (getFactura() != null || getRemitos() != null) {
			factura.setTipoDocumento("FACTURA");
			factura.setCodigoDocumento(StringUtil.fillLeftWithZeros("" + getFactura().getTipoDocumento().getIdTipoDocAFIP(null), 2));
		}
		
		factura.setTipoFactura(getCorreccionFactura() != null ? getCorreccionFactura().getTipoFactura().getDescripcion() : getFactura().getTipoFactura().getDescripcion());
		factura.setTotalFactura(getCorreccionFactura() != null ? (getCorreccionFactura() != null && getCorreccionFactura() instanceof NotaCredito ? "-" : "") + GenericUtils.getDecimalFormatFactura().format(getCorreccionFactura().getMontoTotal().doubleValue()).replace(',', '.') : GenericUtils.getDecimalFormatFactura().format(getFactura().getMontoTotal()));
		factura.setCaeAFIP(getCorreccionFactura() != null ? getCorreccionFactura().getCaeAFIP() : getFactura().getCaeAFIP());
		factura.setFechaVencimientoCaeAFIP(getCorreccionFactura() != null ? getCorreccionFactura().getFechaVencimientoCaeAFIP() : getFactura().getFechaVencimientoCaeAFIP());
		if (getFactura() != null) {
			factura.setTotalIvaInscr(getFactura().getPorcentajeIVAInscripto() != null ? GenericUtils.getDecimalFormatFactura().format(getFactura().getPorcentajeIVAInscripto().doubleValue() * getFactura().getMontoSubtotal().doubleValue() / 100) : null);
		} else {
			factura.setTotalIvaInscr(getCorreccionFactura().getPorcentajeIVAInscripto() != null ? getIvaInsc() : null);
		}
		return factura;
	}
	
	private String extractNrosRemitoEntradaFactura() {
		List<String> listaNros = new ArrayList<String>();
		if(getFactura().getRemitos()!=null && !getFactura().getRemitos().isEmpty()){
			for(RemitoSalida rs : getFactura().getRemitos()) {
				if (rs.getOdts()!=null && !rs.getOdts().isEmpty()){
					for(OrdenDeTrabajo odt : rs.getOdts()){
						listaNros.add(""+odt.getRemito().getNroRemito());
					}
				}
			}
		}
		return StringUtil.getCadena(listaNros, ", ");
	}

	private String getIvaInsc(){
		if(getCorreccionFactura()!=null){
			double subTotal =0;
			if(getCorreccionFactura().getMontoSubtotal()!=null){
				subTotal += getCorreccionFactura().getMontoSubtotal().doubleValue();
			}
			if (getCorreccionFactura().getPorcentajeIVAInscripto() != null) {
				double valIVAInsc = getCorreccionFactura().getPorcentajeIVAInscripto().doubleValue() / 100;
				double ivaInsc = 0;
				if(getCorreccionFactura() instanceof NotaDebito && ( ((NotaDebito)getCorreccionFactura()).getChequeRechazado()!=null || (( ((NotaDebito)getCorreccionFactura()).getIsParaRechazarCheque()!=null) && ( ((NotaDebito)getCorreccionFactura()).getIsParaRechazarCheque()==true)) )&& ((NotaDebito)getCorreccionFactura()).getGastos()!=null){
					ivaInsc = valIVAInsc * ((NotaDebito)getCorreccionFactura()).getGastos().doubleValue();
				}else{
					ivaInsc =  subTotal * valIVAInsc;
				}
				return (getCorreccionFactura() instanceof NotaCredito && ivaInsc >0?"-":"") +  GenericUtils.getDecimalFormatFactura().format(ivaInsc);
			}
			return "";
		}else{
			double subTotal = getFactura().getMontoSubtotal().doubleValue();
			if (getFactura().getPorcentajeIVAInscripto() != null) {
				double valIVAInsc = getFactura().getPorcentajeIVAInscripto().doubleValue() / 100;
				double ivaInsc = subTotal * valIVAInsc;
				return GenericUtils.getDecimalFormatFactura().format(ivaInsc);
			}
			return "";
		}
	}
	
	private String getNroFactura() {
		String nro;
		if (getFactura() != null) {
			nro = StringUtil.fillLeftWithZeros(String.valueOf(getFactura().getNroSucursal()), 4) + "-";
			nro += StringUtil.fillLeftWithZeros(String.valueOf(getFactura().getNroFactura()), 8);
		} else {
			nro = StringUtil.fillLeftWithZeros(String.valueOf(getCorreccionFactura().getNroSucursal()), 4) + "-";
			nro += StringUtil.fillLeftWithZeros(String.valueOf(getCorreccionFactura().getNroFactura()), 8);
		}
		return nro;
	}
	
	private ClienteTO getClienteTO() {
		ClienteTO cliente = new ClienteTO();
		Cliente clientePosta = getCliente();
		cliente.setCondicionIVA(clientePosta.getPosicionIva().getDescripcion());
		cliente.setCuit(clientePosta.getCuit());
		cliente.setDireccion(clientePosta.getDireccionReal().getDireccion());
		cliente.setLocalidad(clientePosta.getDireccionReal().getLocalidad().getNombreLocalidad());
		cliente.setRazonSocial(clientePosta.getRazonSocial());
		return cliente;
	}


	private List<ItemFacturaTO> getItemsTO() {
		List<ItemFacturaTO> lista = new ArrayList<ItemFacturaTO>();
		if(getFactura()!=null){
			for (ItemFactura item : getFactura().getItems()) {
				ItemFacturaTO ito = new ItemFacturaTO();
				ito.setArticulo(getArticulo(item));
				ito.setCantidad(GenericUtils.getDecimalFormatFactura().format(item.getCantidad()));
				ito.setDescripcion(item.getDescripcion());
				ito.setImporte(GenericUtils.getDecimalFormatFactura().format(item.getImporte()));
				ito.setPrecioUnitario(item.getPrecioUnitario()==null?"":GenericUtils.getDecimalFormatFactura().format(item.getPrecioUnitario()));
				ito.setUnidad(item.getUnidad().getDescripcion());
				lista.add(ito);
			}
		}else{
			for (ItemFactura item : getCorreccionFactura().getItems()) {
				ItemFacturaTO ito = new ItemFacturaTO();
				ito.setCantidad("1");
				ito.setDescripcion(item.getDescripcion());
				ito.setUnidad(item.getUnidad().getDescripcion());
				BigDecimal precioUnitario = item.getPrecioUnitario();
				if (precioUnitario != null) {
					precioUnitario = precioUnitario.multiply(new BigDecimal(getCorreccionFactura() instanceof NotaCredito ? -1 : 1));
					ito.setPrecioUnitario(GenericUtils.getDecimalFormatFactura().format(precioUnitario));
					ito.setImporte(precioUnitario != null ? (GenericUtils.getDecimalFormatFactura().format(precioUnitario.doubleValue())) : null);
				}
				lista.add(ito);
			}
		}
		return lista;
	}
		
	private String getArticulo(ItemFactura ifactura) {
		if (ifactura instanceof ItemFacturaProducto) {
			if( ((ItemFacturaProducto)ifactura).getProductoArticulo().getArticulo()!=null){
				return ((ItemFacturaProducto)ifactura).getProductoArticulo().getArticulo().getNombre();
			}else{
				return " - ";
			}
		}
		if (ifactura instanceof ItemFacturaSeguro) {
			return "SEGURO";
		}
		if (ifactura instanceof ItemFacturaTubo) {
			return "TUBOS";
		}
		if (ifactura instanceof ItemFacturaRecargo) {
			return "RECARGO";
		}
		if (ifactura instanceof ItemFacturaBonificacion) {
			return "BONIF.";
		}
		if (ifactura instanceof ItemFacturaPercepcion) {
			return "PERCEP.";
		}
		if(ifactura instanceof ItemFacturaPrecioMateriaPrima){
			return ((ItemFacturaPrecioMateriaPrima)ifactura).getPrecioMateriaPrima().getMateriaPrima().getTipo().getDescripcion().toUpperCase();
		}
		if(ifactura instanceof ItemFacturaTelaCruda){
			return ((ItemFacturaTelaCruda)ifactura).getArticulo().toString().toUpperCase();
		}
		if(ifactura instanceof ItemFacturaOtro){
			return "OTRO";
		}
		return "";
	}

	
	private Cliente getCliente() {
		return cliente;
	}
	
	private List<Integer> extractIds(List<RemitoSalida> rsList) {
		List<Integer> remitosIds = new ArrayList<Integer>(rsList.size());
		for(RemitoSalida rs : rsList) {
			remitosIds.add(rs.getId());
		}
		return remitosIds;
	}

	private String getNrosFacturasRelacionadas(List<Factura> facturasRelacionadas) {
		StringBuilder sb = new StringBuilder();
		boolean primera = true;
		for(Factura f : facturasRelacionadas){
			if(primera){
				sb.append(StringUtil.fillLeftWithZeros(String.valueOf(f.getNroSucursal()), 4) + "-");
				sb.append(StringUtil.fillLeftWithZeros(String.valueOf(f.getNroFactura()), 8));
				primera = false;
				continue;
			}
			sb.append("/");
			sb.append(f.getNroFactura());
		}
		String facRel = sb.toString();
		setStrFacturasRelacionadas(facRel);
		return facRel;
	}

	private String getNrosRemitos(){
		if(getRemitos()!=null){
			List<String> lista = new ArrayList<String>();
			for(RemitoSalida r : remitos){
				lista.add(String.valueOf(r.getNroRemito()));
			}
			return StringUtil.getCadena(lista, " / ");
		}
		return "";
	}
	
	/************************************************ FIN METODOS COMUNES *******************************************************************************/

	public Factura getFactura() {
		return factura;
	}

	public CorreccionFactura getCorreccionFactura() {
		return correccionFactura;
	}

	public FacturaFacadeRemote getFacturaFacade() {
		return facturaFacade;
	}

	public CorreccionFacadeRemote getCorreccionFacade() {
		return correccionFacade;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setCorreccionFactura(CorreccionFactura correccionFactura) {
		this.correccionFactura = correccionFactura;
	}

	
	
	public List<RemitoSalida> getRemitos() {
		return remitos;
	}


	public void setRemitos(List<RemitoSalida> remitos) {
		this.remitos = remitos;
	}


	public String getStrFacturasRelacionadas() {
		return strFacturasRelacionadas;
	}


	public void setStrFacturasRelacionadas(String strFacturasRelacionadas) {
		this.strFacturasRelacionadas = strFacturasRelacionadas;
	}
}
