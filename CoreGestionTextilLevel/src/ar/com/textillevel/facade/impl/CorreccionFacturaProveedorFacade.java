package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.IgnoreDependency;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.ChequeDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionFacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.FormaPagoOrdenDePagoDAOLocal;
import ar.com.textillevel.dao.api.local.PagoOrdenDePagoDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoSalidaDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionCheque;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.visitor.ICorreccionFacturaProveedorVisitor;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CorreccionFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;

@Stateless
public class CorreccionFacturaProveedorFacade implements CorreccionFacturaProveedorFacadeRemote, CorreccionFacturaProveedorFacadeLocal {

	@EJB
	private CorreccionFacturaProveedorDAOLocal correccionDAO;

	@EJB
	private CuentaFacadeLocal cuentaFacade;

	@EJB
	private PagoOrdenDePagoDAOLocal pagoOrdenDePagoDAO;

	@EJB
	private FormaPagoOrdenDePagoDAOLocal formaPagoOrdenDePagoDAO;
	
	@EJB
	private RemitoSalidaDAOLocal remitoSalidaDAO;

	@EJB
	private AuditoriaFacadeLocal<CorreccionFacturaProveedor> auditoriaFacade;
	
	@EJB
	@IgnoreDependency
	private CorreccionFacadeLocal correccionClienteFacade;
	
	@EJB
	@IgnoreDependency
	private ChequeDAOLocal chequeDao;

	@EJB
	@IgnoreDependency
	private CorreccionDAOLocal correccionDao;
	
	public CorreccionFacturaProveedor guardarCorreccionYGenerarMovimiento(CorreccionFacturaProveedor correccion, String usuario, String obsMovimiento) {
		CorreccionFacturaProveedorPersisterVisitor cfppv = new CorreccionFacturaProveedorPersisterVisitor(usuario, obsMovimiento);
		correccion.accept(cfppv);
		return cfppv.getCorreccionFacturaProveedor();
	}

	public CorreccionFacturaProveedor getCorreccionFacturaByIdEager(Integer id) {
		return correccionDAO.getByIdEager(id);
	}

	private class CorreccionFacturaProveedorPersisterVisitor implements ICorreccionFacturaProveedorVisitor {

		private final String usuario;
		private final String obsMovimiento;
		private CorreccionFacturaProveedor correccionFacturaProveedor;

		public CorreccionFacturaProveedorPersisterVisitor(String usuario, String obsMovimiento) {
			this.usuario = usuario;
			this.obsMovimiento = obsMovimiento;
		}

		public CorreccionFacturaProveedor getCorreccionFacturaProveedor() {
			return correccionFacturaProveedor;
		}

		public void visit(NotaCreditoProveedor ncp) {
			BigDecimal montoTotalAbsoluto = new BigDecimal(Math.abs(ncp.getMontoTotal().doubleValue()));
			ncp.setMontoSobrantePorUtilizar(montoTotalAbsoluto);
			ncp.setMontoTotal(montoTotalAbsoluto);
			ncp = (NotaCreditoProveedor)correccionDAO.save(ncp);

			ncp.setMontoTotal(ncp.getMontoTotal());
			List<ItemCorreccionFacturaProveedor> itemsCorreccion = ncp.getItemsCorreccion();
			for(ItemCorreccionFacturaProveedor it : itemsCorreccion) {
				it.setImporte(it.getImporte());
			}
			ncp.setUsuarioConfirmacion(usuario);
			correccionFacturaProveedor = ncp;
			cuentaFacade.crearMovimientoHaberProveedor(ncp, obsMovimiento);
			auditoriaFacade.auditar(usuario, "Creación de nota de crédito de proveedor "+" Nº: " + ncp.getNroCorreccion(), EnumTipoEvento.ALTA, ncp);
		}

		public void visit(NotaDebitoProveedor ndp) {
			ndp.setMontoFaltantePorPagar(ndp.getMontoTotal());
			ndp.setUsuarioConfirmacion(usuario);
			ndp = (NotaDebitoProveedor)correccionDAO.save(ndp);
			correccionFacturaProveedor = ndp;
			cuentaFacade.crearMovimientoDebeProveedor(ndp);
			auditoriaFacade.auditar(usuario, "Creación de nota de débito de proveedor "+" Nº: " + ndp.getNroCorreccion(), EnumTipoEvento.ALTA, ndp);
		}

	}
	
	public List<NotaDebitoProveedor> getNotasDeDebitoImpagas(Integer idProveedor){
		return correccionDAO.getNotasDeDebitoImpagas(idProveedor);
	}

	public List<NotaCreditoProveedor> getNotasCreditoNoUsadas(Integer idProveedor) {
		return correccionDAO.getNotasCreditoNoUsadas(idProveedor);
	}

	public CorreccionFacturaProveedor actualizarCorreccion(CorreccionFacturaProveedor correccion) {
		return correccionDAO.save(correccion);
	}

	public void confirmarCorreccion(CorreccionFacturaProveedor correccion, String usrName) {
		correccion.setVerificada(true);
		correccion.setUsuarioConfirmacion(usrName);
		correccionDAO.save(correccion);
		auditoriaFacade.auditar(usrName, "Verificacion de " + (correccion instanceof NotaDebitoProveedor?"Nota de débito":"Nota de crédito" )+ " de proveedor Nº: " + correccion.getNroCorreccion(), EnumTipoEvento.BAJA, correccion);
	}

	public CorreccionFacturaProveedor completarDatosCorreccion(CorreccionFacturaProveedor correccion, String usrName) {
		CompletarCorreccionProveedorPersisterVisitor cfppv = new CompletarCorreccionProveedorPersisterVisitor(usrName);
		correccion.accept(cfppv);
		return cfppv.getCorreccionFacturaProveedor();
	}

	private class CompletarCorreccionProveedorPersisterVisitor implements ICorreccionFacturaProveedorVisitor {

		private final String usuario;
		private CorreccionFacturaProveedor correccionFacturaProveedor;

		public CompletarCorreccionProveedorPersisterVisitor(String usuario) {
			this.usuario = usuario;
		}

		public CorreccionFacturaProveedor getCorreccionFacturaProveedor() {
			return correccionFacturaProveedor;
		}

		public void visit(NotaCreditoProveedor ncp) {
			ncp = (NotaCreditoProveedor)correccionDAO.save(ncp);
			correccionFacturaProveedor = ncp;
			cuentaFacade.asignarFechaMovimientoHaberSegunNCP(ncp);
			auditoriaFacade.auditar(usuario, "Asignación de fecha y número de nota de crédito de proveedor "+" Nº: " + ncp.getNroCorreccion(), EnumTipoEvento.ALTA, ncp);
		}

		public void visit(NotaDebitoProveedor ndp) {
			ndp = (NotaDebitoProveedor)correccionDAO.save(ndp);
			correccionFacturaProveedor = ndp;
			cuentaFacade.asignarFechaMovimientoDebeSegunNDP(ndp);
			auditoriaFacade.auditar(usuario, "Asignación de fecha y número de nota de débito de proveedor "+" Nº: " + ndp.getNroCorreccion(), EnumTipoEvento.ALTA, ndp);
		}
	}

	public void borrarCorreccionFacturaProveedor(CorreccionFacturaProveedor correFacturaProveedor, String usuario) throws ValidacionException {
		CorreccionFacturaProveedor cfp = correccionDAO.getById(correFacturaProveedor.getId());
		if(cfp instanceof NotaDebitoProveedor) {
			NotaDebitoProveedor ndp = (NotaDebitoProveedor)cfp;
			if(pagoOrdenDePagoDAO.existsNotaDebitoProvEnPagoOrdenDePago(ndp)) {
				throw new ValidacionException(EValidacionException.NOTA_DEBITO_PROV_EXISTE_EN_ORDEN_DE_PAGO.getInfoValidacion());
			}
			cuentaFacade.borrarMovimientoNotaDebitoProveedor(ndp);
			ItemCorreccionCheque itemCheque = obtenerItemCorreccionChequeSiLoTiene(ndp);
			if(itemCheque!=null) {
				Cheque c = itemCheque.getCheque();
				List<NotaDebito> notasDebitoByCheque = correccionClienteFacade.getNotasDebitoByCheque(c);
				for(NotaDebito nd : notasDebitoByCheque) {
					c.setEstadoCheque(nd.getEstadoAnteriorCheque());
					chequeDao.save(c);
					if(correccionDao.notaDebitoSeUsaEnRecibo(nd)){
						throw new ValidacionException(EValidacionException.NOTA_DEBITO_SE_USA_EN_RECIBO.getInfoValidacion());
					}
					cuentaFacade.borrarMovimientoNotaDebitoCliente(nd);
					correccionDao.removeById(nd.getId());
				}
			}
			correccionDAO.removeById(ndp.getId());
			auditoriaFacade.auditar(usuario, "borrado de nota de débito de proveedor Nº: " + ndp.getNroCorreccion(), EnumTipoEvento.BAJA, ndp);
			return;
		}
		if(cfp instanceof NotaCreditoProveedor) {
			NotaCreditoProveedor ncp = (NotaCreditoProveedor)cfp;
			if(formaPagoOrdenDePagoDAO.existsNotaCreditoProvEnFormaPagoOrdenDePago(ncp)) {
				throw new ValidacionException(EValidacionException.NOTA_CREDITO_PROV_EXISTE_EN_ORDEN_DE_PAGO.getInfoValidacion());
			}
			remitoSalidaDAO.borrarAsociacionNotaCredito(ncp);
			cuentaFacade.borrarMovimientoNotaCreditoProveedor(ncp);
			correccionDAO.removeById(ncp.getId());
			auditoriaFacade.auditar(usuario, "borrado de nota de crédito de proveedor Nº: " + ncp.getNroCorreccion(), EnumTipoEvento.BAJA, ncp);
		}
	}

	private ItemCorreccionCheque obtenerItemCorreccionChequeSiLoTiene(NotaDebitoProveedor notaDebitoProveedor) {
		for(ItemCorreccionFacturaProveedor i : notaDebitoProveedor.getItemsCorreccion()){
			if(i instanceof ItemCorreccionCheque){
				return (ItemCorreccionCheque)i;
			}
		}
		return null;
	}
	
	public boolean existeNroCorreccionByProveedor(Integer idCorreccion, String nroCorreccion, Integer idProveedor) {
		return correccionDAO.existeNroCorreccionByProveedor(idCorreccion, nroCorreccion, idProveedor);
	}

	public CorreccionFacturaProveedor obtenerNotaDeDebitoByCheque(Cheque c) {
		return correccionDAO.obtenerNotaDeDebitoByCheque(c);
	}

}