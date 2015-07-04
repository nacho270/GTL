package ar.com.textillevel.gui.acciones.impresionfactura;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
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
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.util.GTLBeanFactory;

public class ImpresionFacturaHandler {

	private Factura factura;
	private CorreccionFactura correccionFactura;
	private List<RemitoSalida> remitos;
	private String cantidadCopias;
	private Cliente cliente;
	private FacturaFacadeRemote facturaFacade;
	private CorreccionFacadeRemote correccionFacade;
	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;
	private DocumentoContableFacadeRemote documentoContableFacade;
	private ClienteFacadeRemote clienteFacade;
	private String strFacturasRelacionadas;

	public ImpresionFacturaHandler(DocumentoContableCliente documento, String cantidadCopias) {
		this.cantidadCopias = cantidadCopias;
		facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
		correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		parametrosGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void imprimir() throws JRException, CLException, ValidacionException {
		documentoContableFacade.checkImpresionDocumentoContable(getFactura() != null ? getFactura() : getCorreccionFactura());
		FacturaTO factura = armarFacturaTO();
		Map parameters = getParametros(factura);
		JasperReport reporte;
		if (GenericUtils.isSistemaTest()) {
			reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/facturab_v2.jasper");
		} else {
			reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/factura_electronica.jasper");
		}
		JasperPrint jasperPrint = JasperHelper.fillReport(reporte, parameters, factura.getItems());
		Integer cantidadAImprimir = Integer.valueOf(cantidadCopias);
		Integer cantidadImpresa = JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
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
	private Map getParametros(FacturaTO factura) {
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
		parameters.put("FECHA_FACT", DateUtil.dateToString(DateUtil.stringToDate(factura.getFecha()), DateUtil.SHORT_DATE));
		parameters.put("TIPO_DOC", factura.getTipoDocumento());
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
		factura.setNroFactura(getNroFactura());
		factura.setNroRemito(getStrFacturasRelacionadas() != null && getStrFacturasRelacionadas().trim().length() > 0 ? getStrFacturasRelacionadas().replaceAll("/", " / ") : getNrosRemitos());
		factura.setPorcIvaInsc(GenericUtils.getDecimalFormatFactura().format(getFactura()!=null?getFactura().getPorcentajeIVAInscripto():getCorreccionFactura().getPorcentajeIVAInscripto()));
		factura.setSubTotal((getCorreccionFactura() != null && getCorreccionFactura() instanceof NotaCredito ? "-" : "") + (getCorreccionFactura() != null ? GenericUtils.getDecimalFormatFactura().format(getCorreccionFactura().getMontoSubtotal().doubleValue()) : GenericUtils.getDecimalFormatFactura().format(getFactura().getMontoSubtotal().doubleValue())));
		
		if (getCorreccionFactura() != null) {
			factura.setTipoDocumento(getCorreccionFactura().getTipo().getDescripcion());
		} else if (getFactura() != null || getRemitos() != null) {
			factura.setTipoDocumento("FACTURA");
		}
		
		factura.setTipoFactura(getCorreccionFactura() != null ? "" : getFactura().getTipoFactura().getDescripcion());
		factura.setTotalFactura(getCorreccionFactura() != null ? (getCorreccionFactura() != null && getCorreccionFactura() instanceof NotaCredito ? "-" : "") + GenericUtils.getDecimalFormatFactura().format(getCorreccionFactura().getMontoTotal().doubleValue()).replace(',', '.') : GenericUtils.getDecimalFormatFactura().format(getFactura().getMontoTotal()));
		factura.setCaeAFIP(getCorreccionFactura() != null ? getCorreccionFactura().getCaeAFIP() : getFactura().getCaeAFIP());
		if (getFactura() != null) {
			factura.setTotalIvaInscr(getFactura().getPorcentajeIVAInscripto() != null ? GenericUtils.getDecimalFormatFactura().format(getFactura().getPorcentajeIVAInscripto().doubleValue() * getFactura().getMontoSubtotal().doubleValue() / 100) : null);
		} else {
			factura.setTotalIvaInscr(getCorreccionFactura().getPorcentajeIVAInscripto() != null ? (getCorreccionFactura() != null && getCorreccionFactura() instanceof NotaCredito ? "-" : "") + getIvaInsc() : null);
		}

		return factura;
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
		String nro = StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal()), 4) + "-";
		if (getFactura() != null) {
			nro += StringUtil.fillLeftWithZeros(String.valueOf(getFactura().getNroFactura()), 8);
		} else {
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
			ItemFacturaTO ito = new ItemFacturaTO();
			ito.setCantidad("1");
			ito.setDescripcion(getCorreccionFactura().getDescripcion());
			BigDecimal montoSubtotal = getCorreccionFactura().getMontoSubtotal();
			if(montoSubtotal!=null){
				montoSubtotal = montoSubtotal.multiply(new BigDecimal(getCorreccionFactura() instanceof NotaCredito?-1:1));
				ito.setPrecioUnitario(GenericUtils.getDecimalFormatFactura().format(montoSubtotal));
				ito.setImporte(montoSubtotal != null ? (GenericUtils.getDecimalFormatFactura().format(montoSubtotal.doubleValue())) : null);
			} else {
				BigDecimal monto = getCorreccionFactura().getMontoTotal();
				if (monto!=null){
					monto = monto.multiply(new BigDecimal(getCorreccionFactura() instanceof NotaCredito?-1:1));
					ito.setPrecioUnitario(GenericUtils.getDecimalFormatFactura().format(monto));
					ito.setImporte(GenericUtils.getDecimalFormatFactura().format(monto));
				}
			}
			lista.add(ito);
		}
		return lista;
	}
		
	private String getArticulo(ItemFactura ifactura) {
		if (ifactura instanceof ItemFacturaProducto) {
			if( ((ItemFacturaProducto)ifactura).getProducto().getArticulo()!=null){
				return ((ItemFacturaProducto)ifactura).getProducto().getArticulo().getNombre();
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
			return "BONIFICACION";
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
		sb.append(StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal()), 4) + "-");
		boolean primera = true;
		for(Factura f : facturasRelacionadas){
			if(primera){
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

	private ParametrosGeneralesFacadeRemote getParametrosGeneralesFacade() {
		return parametrosGeneralesFacade;
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
