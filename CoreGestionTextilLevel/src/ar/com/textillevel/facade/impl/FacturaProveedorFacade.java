package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.CorreccionFacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.PagoOrdenDePagoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.to.ivacompras.DescripcionFacturaIVAComprasTO;
import ar.com.textillevel.entidades.to.ivacompras.DetalleImpuestoFacturaIVAComprasTO;
import ar.com.textillevel.entidades.to.ivacompras.ETipoDocumentoProveedor;
import ar.com.textillevel.entidades.to.ivacompras.IVAComprasTO;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;

@Stateless
public class FacturaProveedorFacade implements FacturaProveedorFacadeRemote, FacturaProveedorFacadeLocal {

	@EJB
	private FacturaProveedorDAOLocal facturaProveedorDAO;

	@EJB
	private CuentaFacadeLocal cuentaFacade;

	@EJB
	private PagoOrdenDePagoDAOLocal pagoOrdenDePagoDAO;

	@EJB
	private CorreccionFacturaProveedorDAOLocal correccionFacturaDAO;
	
	@EJB
	private AuditoriaFacadeLocal<FacturaProveedor> auditoriaFacade;

	public FacturaProveedor getByIdEager(Integer idFactura) {
		return facturaProveedorDAO.getByIdEager(idFactura);
	}

	public FacturaProveedor ingresarFactura(FacturaProveedor factura, String usuario) {
		boolean alta = factura.getId() == null;
		factura = facturaProveedorDAO.save(factura);
		if(alta){
			cuentaFacade.crearMovimientoDebeProveedor(factura);
			auditoriaFacade.auditar(usuario, "Alta de factura proveedor Nº: " + factura.getNroFactura() + ", Proveedor: " + factura.getProveedor().getNombreCorto() , EnumTipoEvento.ALTA, factura);
		}else{
			cuentaFacade.borrarMovimientoFacturaProveedor(factura);
			cuentaFacade.crearMovimientoDebeProveedor(factura);
			auditoriaFacade.auditar(usuario, "Edición de factura proveedor Nº: " + factura.getNroFactura() + ", Proveedor: " + factura.getProveedor().getNombreCorto() , EnumTipoEvento.MODIFICACION, factura);
		}
		return factura;
	}

	public List<FacturaProveedor> getFacturaListByParams(Integer idProveedor, List<Integer> idPrecioMatPrimaList, Date fechaDesde, Date fechaHasta) {
		return facturaProveedorDAO.getFacturaListByParams(idProveedor, idPrecioMatPrimaList, fechaDesde, fechaHasta);
	}

	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor) {
		return facturaProveedorDAO.getFacturasImpagas(idProveedor);
	}

	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor, List<Integer> idsExcluidos, BigDecimal montoHasta) {
		List<FacturaProveedor> lista = facturaProveedorDAO.getFacturasImpagas(idProveedor, idsExcluidos);
		List<FacturaProveedor> ret = new ArrayList<FacturaProveedor>();
		BigDecimal suma = new BigDecimal(0d);
		if(lista!=null && !lista.isEmpty()){
			for(FacturaProveedor fp : lista){
				if(suma.add(fp.getMontoFaltantePorPagar()).compareTo(montoHasta)==-1){
					ret.add(fp);
					suma = suma.add(fp.getMontoFaltantePorPagar());
				}else{
					ret.add(fp);
					break;
				}
			}
		}
		return ret;
	}
	
	public FacturaProveedor actualizarFactura(FacturaProveedor factura) {
		return facturaProveedorDAO.save(factura);
	}

	public List<FacturaProveedor> getFacturasParaNotasCredito(Integer idProveedor) {
		return facturaProveedorDAO.getFacturasParaNotasCredito(idProveedor);
	}

	public void confirmarFactura(FacturaProveedor factura, String usrName) {
		factura.setVerificada(true);
		factura.setUsuarioConfirmacion(usrName);
		facturaProveedorDAO.save(factura);
		auditoriaFacade.auditar(usrName, "Verficación de Factura de proveedor Nº: " + factura.getNroFactura(), EnumTipoEvento.MODIFICACION, factura);
	}

	public void borrarFactura(FacturaProveedor factura, String usuario) throws ValidacionException {
		factura = facturaProveedorDAO.getById(factura.getId());
		checkEliminacionOrEdicionFacturaProveedor(factura);
		cuentaFacade.borrarMovimientoFacturaProveedor(factura);
		facturaProveedorDAO.removeById(factura.getId());
		auditoriaFacade.auditar(usuario, "Baja de factura Nº: " + factura.getNroFactura() + ", Proveedor: " + factura.getProveedor().getNombreCorto() , EnumTipoEvento.BAJA, factura); 
	}

	public void checkEliminacionOrEdicionFacturaProveedor(FacturaProveedor factura) throws ValidacionException {
		if(pagoOrdenDePagoDAO.existsFacturaEnPagoOrdenDePago(factura)) {
			throw new ValidacionException(EValidacionException.FACTURA_PROV_EXISTE_EN_ORDEN_DE_PAGO.getInfoValidacion());
		}
		List<CorreccionFacturaProveedor> correccionList = correccionFacturaDAO.getCorreccionListByFactura(factura);
		if(!correccionList.isEmpty()) {
			throw new ValidacionException(EValidacionException.FACTURA_PROV_EXISTE_EN_CORRECCION.getInfoValidacion());
		}
	}

	public IVAComprasTO calcularIVACompras(Date fechaDesde, Date fechaHasta, Proveedor proveedor) {
		IVAComprasTO ivaComprasTO = new IVAComprasTO();
		List<Object[]> infoFacturasIvaCompras = facturaProveedorDAO.calcularInfoFacturasIvaCompras(fechaDesde, fechaHasta, proveedor);
		ivaComprasTO.getItems().addAll(obtenerItems(infoFacturasIvaCompras));
		List<Object[]> infoCorreccionesIvaCompras = correccionFacturaDAO.calcularInfoFacturasIvaCompras(fechaDesde, fechaHasta, proveedor);
		ivaComprasTO.getItems().addAll(calcularSubtotal(obtenerItems(infoCorreccionesIvaCompras)));
		Collections.sort(ivaComprasTO.getItems(), new Comparator<DescripcionFacturaIVAComprasTO>() {

			public int compare(DescripcionFacturaIVAComprasTO o1,DescripcionFacturaIVAComprasTO o2) {
				return o1.getFecha().compareTo(o2.getFecha());
			}

		});
		
		
		
		return ivaComprasTO;
	}

	private Collection<? extends DescripcionFacturaIVAComprasTO> calcularSubtotal(Collection<DescripcionFacturaIVAComprasTO> obtenerItems) {
		Collection<DescripcionFacturaIVAComprasTO> result = new ArrayList<DescripcionFacturaIVAComprasTO>();
		for(DescripcionFacturaIVAComprasTO  d : obtenerItems) {
			double totalImpuestos = 0d;
			for(DetalleImpuestoFacturaIVAComprasTO item : d.getDetalleImpuestoList()) {
				totalImpuestos += item.getImporte();
			}
			d.setNetoGravado(String.valueOf(Double.valueOf(d.getTotalComp())-totalImpuestos));
			result.add(d);
		}
		
		return result;
	}

	private Collection<DescripcionFacturaIVAComprasTO> obtenerItems(List<Object[]> infoFacturasIvaCompras) {
		Map<Integer, DescripcionFacturaIVAComprasTO> mapDescrFactura = new HashMap<Integer, DescripcionFacturaIVAComprasTO>();
		for(Object[] row : infoFacturasIvaCompras) {
			Integer idFactura = (Integer)row[2];
			DescripcionFacturaIVAComprasTO descripcionFacturaIVAComprasTO = mapDescrFactura.get(idFactura);
			if(descripcionFacturaIVAComprasTO == null) {
				mapDescrFactura.put(idFactura, constructDescrFacturaIVACompras(row));
			} else {
				
				
				
				descripcionFacturaIVAComprasTO.getDetalleImpuestoList().add(constructDetalleImpuestoIVACompras(row, descripcionFacturaIVAComprasTO.getTipoComprobante()));
			}
		}
		return mapDescrFactura.values();
	}

	private DetalleImpuestoFacturaIVAComprasTO constructDetalleImpuestoIVACompras(Object[] row, String tipoComprobante) {
		DetalleImpuestoFacturaIVAComprasTO detalleImpuestoFacturaIVAComprasTO = new DetalleImpuestoFacturaIVAComprasTO(row[0] == null ? 0 : Integer.valueOf(row[0].toString()), row[1] == null ? 0d : Double.valueOf(row[1].toString()));
		if(tipoComprobante.compareToIgnoreCase(ETipoDocumentoProveedor.NOTA_DE_CREDITO.toString().toUpperCase()) == 0) {
			detalleImpuestoFacturaIVAComprasTO.setImporte(detalleImpuestoFacturaIVAComprasTO.getImporte()*(-1));
		}
		return detalleImpuestoFacturaIVAComprasTO;
	}

	private DescripcionFacturaIVAComprasTO constructDescrFacturaIVACompras(Object[] row) {
		DescripcionFacturaIVAComprasTO descripcionFacturaIVAComprasTO = new DescripcionFacturaIVAComprasTO();
		descripcionFacturaIVAComprasTO.setTotalComp(row[3].toString());
		descripcionFacturaIVAComprasTO.setFecha((Date)row[4]);
		descripcionFacturaIVAComprasTO.setNetoGravado(row[5].toString());
		descripcionFacturaIVAComprasTO.setNroComprobante(row[6].toString());
		descripcionFacturaIVAComprasTO.setTipoComprobante(ETipoDocumentoProveedor.getByIdStr(row[7].toString()));
		descripcionFacturaIVAComprasTO.setNetoNoGravado(row[8] == null ? "0.00" : row[8].toString());
		descripcionFacturaIVAComprasTO.setPercIVA(row[9] == null ? "0.00" : row[9].toString());
		descripcionFacturaIVAComprasTO.setRazonSocial(row[10].toString());
		descripcionFacturaIVAComprasTO.setCuit(row[11].toString());
		descripcionFacturaIVAComprasTO.setPosIVA(EPosicionIVA.getById((Integer)row[12]).getDescripcionResumida());
		descripcionFacturaIVAComprasTO.getDetalleImpuestoList().add(constructDetalleImpuestoIVACompras(row, descripcionFacturaIVAComprasTO.getTipoComprobante()));
		return descripcionFacturaIVAComprasTO;
	}

	public boolean existeNroFacturaByProveedor(Integer idFactura, String nroFactura, Integer idProveedor) {
		return facturaProveedorDAO.existeNroFacturaByProveedor(idFactura, nroFactura, idProveedor);
	}

	public List<FacturaProveedor> getFacturasConMateriaPrimaIBC(Integer idProveedor) {
		return facturaProveedorDAO.getFacturasConMateriaPrimaIBC(idProveedor);
	}

}