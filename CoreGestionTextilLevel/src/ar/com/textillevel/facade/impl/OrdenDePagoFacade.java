package ar.com.textillevel.facade.impl;

import java.sql.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.dao.api.local.OrdenDePagoDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoCheque;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoNotaCredito;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoACuenta;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoFactura;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoNotaDebito;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.IPagoOrdenPagoVisitor;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EEstadoOrdenDePago;
import ar.com.textillevel.facade.api.local.ChequeFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.OrdenDePagoFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.OrdenDePagoFacadeRemote;

@Stateless
public class OrdenDePagoFacade implements OrdenDePagoFacadeRemote, OrdenDePagoFacadeLocal{
	
	@EJB
	private OrdenDePagoDAOLocal ordenDePagoDao;

	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;
	
	@EJB
	private FacturaProveedorFacadeLocal facturaProvFacade;
	
	@EJB
	private ChequeFacadeLocal chequeFacade;
	
	@EJB
	private CuentaFacadeLocal cuentaFacade;
	
	@EJB
	private AuditoriaFacadeLocal<OrdenDePago> auditoriaFacade;
	
	@EJB
	private CorreccionFacturaProveedorFacadeLocal correccionFacade;
	
	public Integer getNewNroOrdenDePago() {
		Integer numero = ordenDePagoDao.getNewNumeroOrdenDePago();
		if(numero !=null){
			return numero;
		}else{
			ParametrosGenerales parametrosGenerales = parametrosGeneralesFacade.getParametrosGenerales();
			if(parametrosGenerales == null){
				throw new RuntimeException("Falta configurar el número de comienzo de las órdenes de pago en 'parametros generales'");
			}
			return parametrosGenerales.getNroComienzoOrdenDePago();
		}
	}

	public OrdenDePago guardarOrdenDePago(OrdenDePago orden, String usuario) throws FWException{
		orden = guardarOrdenDePagoInterno(orden, usuario);
		auditoriaFacade.auditar(usuario, "Creacion de Orden de Pago Nº: " + orden.getNroOrden(), EnumTipoEvento.ALTA, orden);
		return orden;
	}

	private OrdenDePago guardarOrdenDePagoInterno(OrdenDePago orden, String usuario) throws FWException {
		ActualizadorPagoOrdenDePagoVisitor actualizador = new ActualizadorPagoOrdenDePagoVisitor();
		for(PagoOrdenDePago p : orden.getPagos()){
			p.accept(actualizador);
		}
		
		for(FormaPagoOrdenDePago fp : orden.getFormasDePago()){
			if(fp instanceof FormaPagoOrdenDePagoCheque){
				Cheque c = ((FormaPagoOrdenDePagoCheque)fp).getCheque();
				c.setEstadoCheque(EEstadoCheque.SALIDA_PROVEEDOR);
				c.setProveedorSalida(orden.getProveedor());
				c.setFechaSalida(new Date(orden.getFechaEmision().getTime()));
				chequeFacade.grabarCheque(c, usuario);
			}else if(fp instanceof FormaPagoOrdenDePagoNotaCredito){
				NotaCreditoProveedor nc = ((FormaPagoOrdenDePagoNotaCredito)fp).getNotaCredito();
				nc.setMontoSobrantePorUtilizar(nc.getMontoSobrantePorUtilizar().subtract(((FormaPagoOrdenDePagoNotaCredito) fp).getImporteNC()));
				((FormaPagoOrdenDePagoNotaCredito) fp).setNotaCredito((NotaCreditoProveedor) correccionFacade.actualizarCorreccion(nc));
			}
		}
		orden.setUsuarioCreador(usuario);
		orden.setEstadoOrden(EEstadoOrdenDePago.PREPARADO);
		orden = ordenDePagoDao.save(orden);
		cuentaFacade.crearMovimientoHaberProveedor(orden);
		return orden;
	}
	
	private class ActualizadorPagoOrdenDePagoVisitor implements IPagoOrdenPagoVisitor{

		public void visit(PagoOrdenDePagoACuenta popac) {
			
		}

		public void visit(PagoOrdenDePagoFactura popf) {
			FacturaProveedor fp = popf.getFactura();
			fp.setMontoFaltantePorPagar(fp.getMontoFaltantePorPagar().subtract(popf.getMontoPagado()));
			popf.setFactura(facturaProvFacade.actualizarFactura(fp));
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {
			NotaDebitoProveedor fp = popnd.getNotaDebito();
			fp.setMontoFaltantePorPagar(fp.getMontoFaltantePorPagar().subtract(popnd.getMontoPagado()));
			popnd.setNotaDebito((NotaDebitoProveedor)correccionFacade.actualizarCorreccion(fp));
		}
	}

	public OrdenDePago getOrdenDePagoByNroOrdenEager(Integer nroOrden) {
		return ordenDePagoDao.getOrdenDePagoByNroOrdenEager(nroOrden);
	}
	
	public OrdenDePago actualizarOrden(OrdenDePago orden, String usrName){
		orden = ordenDePagoDao.save(orden);
		auditoriaFacade.auditar(usrName, "Verificación de Orden de pago Nº: " + orden.getNroOrden(), EnumTipoEvento.MODIFICACION, orden);
		return orden;
	}
	
	public OrdenDePago editarOrdenDePago(OrdenDePago orden, String usrName) throws FWException{
		borrarOrdenDePagoInterno(orden, usrName);
		orden = guardarOrdenDePagoInterno(orden, usrName);
		auditoriaFacade.auditar(usrName, "Edición de Orden de pago Nº: " + orden.getNroOrden(), EnumTipoEvento.MODIFICACION, orden);
		return orden;
	}

	public void confirmarOrden(OrdenDePago orden, String usrName) {
		orden.setEstadoOrden(EEstadoOrdenDePago.VERIFICADO);
		orden.setUsuarioConfirmacion(usrName);
		actualizarOrden(orden, usrName);
	}
	
	public void borrarOrdenDePago(OrdenDePago orden, String usuario) throws FWException{
		orden = borrarOrdenDePagoInterno(orden, usuario);
		auditoriaFacade.auditar(usuario, "Eliminación de órden de pago  Nº: " + orden.getNroOrden(),EnumTipoEvento.BAJA,orden);
	}

	private OrdenDePago borrarOrdenDePagoInterno(OrdenDePago orden, String usuario) throws FWException {
		orden = getOrdenDePagoByNroOrdenEager(orden.getNroOrden());
		for(FormaPagoOrdenDePago fp : orden.getFormasDePago()){
			if(fp instanceof FormaPagoOrdenDePagoCheque){//pongo cheques en cartera
				Cheque c = ((FormaPagoOrdenDePagoCheque)fp).getCheque();
				c.setFechaSalida(null);
				c.setProveedorSalida(null);
				c.setEstadoCheque(EEstadoCheque.EN_CARTERA);
				chequeFacade.grabarCheque(c, usuario);
			}else if(fp instanceof FormaPagoOrdenDePagoNotaCredito){//sumo a la nota de credito lo que use para pagar
				NotaCreditoProveedor nc = ((FormaPagoOrdenDePagoNotaCredito)fp).getNotaCredito();
				nc.setMontoSobrantePorUtilizar(fp.getImporte());
				correccionFacade.actualizarCorreccion(nc);
			}
		}
		for(PagoOrdenDePago pop : orden.getPagos()){ //sumo lo que se pago al monto faltante por pagar de factura y nota de debito
			if(pop instanceof PagoOrdenDePagoFactura){
				FacturaProveedor fp = ((PagoOrdenDePagoFactura)pop).getFactura();
				fp.setMontoFaltantePorPagar(fp.getMontoFaltantePorPagar().add(pop.getMontoPagado()));
				facturaProvFacade.actualizarFactura(fp);
			}else if(pop instanceof PagoOrdenDePagoNotaDebito){
				NotaDebitoProveedor nd = ((PagoOrdenDePagoNotaDebito)pop).getNotaDebito();
				nd.setMontoFaltantePorPagar(nd.getMontoFaltantePorPagar().add(pop.getMontoPagado()));
				correccionFacade.actualizarCorreccion(nd);
			}
		}
		
		cuentaFacade.borrarMovimientoOrdenDePago(orden);
		ordenDePagoDao.removeById(orden.getId());
		return orden;
	}

	public OrdenDePago getByIdEager(Integer idODP) {
		return ordenDePagoDao.getByIdEager(idODP);
	}

	public void marcarEntregada(String numero, String nombreTerminal) {
		OrdenDePago odp = ordenDePagoDao.getByNumero(numero);
		if (odp == null) {
			throw new RuntimeException("Orden de pago no encontrada");
		}
		if (odp.getEntregado() == null || odp.getEntregado().equals(Boolean.FALSE)) {
			odp.setEntregado(true);
			odp.setFechaHoraEntregada(DateUtil.getAhora());
			odp.setTerminalEntrega(nombreTerminal);
			auditoriaFacade.auditar(nombreTerminal, "Marcar orden de pago numero: " + numero + " como entregada",
					EnumTipoEvento.MODIFICACION, odp);
		}
	}

	public void reingresar(String numero, String nombreTerminal) {
		OrdenDePago odp = ordenDePagoDao.getByNumero(numero);
		if (odp == null) {
			throw new RuntimeException("Orden de pago no encontrada");
		}
		if (odp.getEntregado() != null && odp.getEntregado().equals(Boolean.TRUE)) {
			odp.setEntregado(false);
			odp.setFechaHoraEntregada(DateUtil.getAhora());
			odp.setTerminalEntrega(nombreTerminal);
			auditoriaFacade.auditar(nombreTerminal, "Reingreso orden de pago numero: " + numero, EnumTipoEvento.MODIFICACION, odp);
		}
	}
}
