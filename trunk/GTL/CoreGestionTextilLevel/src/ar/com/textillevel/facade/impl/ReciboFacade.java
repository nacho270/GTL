package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.ChequeDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.dao.api.local.ReciboDAOLocal;
import ar.com.textillevel.dao.api.local.UsuarioSistemaDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPago;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoCheque;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoEfectivo;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoNotaCredito;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoRetencionGanancias;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoRetencionIVA;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoRetencionIngresosBrutos;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoTransferencia;
import ar.com.textillevel.entidades.documentos.recibo.formapago.IFormaPagoVisitor;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoRecibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboACuenta;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboFactura;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboNotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.IPagoReciboVisitor;
import ar.com.textillevel.entidades.documentos.recibo.to.ResumenReciboTO;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EEstadoCorreccion;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.ReciboFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;

@Stateless
public class ReciboFacade implements ReciboFacadeRemote, ReciboFacadeLocal {

	@EJB
	private ReciboDAOLocal reciboDAO;

	@EJB
	private CuentaFacadeLocal cuentaFacade;

	@EJB
	private ChequeDAOLocal chequeDAO;

	@EJB
	private CorreccionDAOLocal correccionDAO;

	@EJB
	private UsuarioSistemaDAOLocal usuarioSistemaDAO;

	@EJB
	private FacturaDAOLocal facturaDAO;

	@EJB
	private AuditoriaFacadeLocal<Recibo> auditoriaFacade;
	
	public Recibo saveRecibo(Recibo recibo) {
		return reciboDAO.save(recibo); 
	}
	
	public Recibo ingresarRecibo(Recibo recibo, String usuario) throws ValidacionException {
		checkConsistenciaRecibo(recibo);
		if(recibo.getId() == null) { //Es un alta de recibo
			recibo = createRecibo(recibo, usuario);
			auditoriaFacade.auditar(usuario, "Creación de recibo Nº: " + recibo.getNroRecibo(), EnumTipoEvento.ALTA,recibo);
		} else {//update de recibo
			undoReciboStuff(recibo);
			recibo = createRecibo(recibo, usuario);
			auditoriaFacade.auditar(usuario, "Modificación de recibo Nº: " + recibo.getNroRecibo(), EnumTipoEvento.MODIFICACION,recibo);			
		}
		return recibo;
	}

	private Recibo createRecibo(Recibo recibo, String usrName) {
		//Si el usuario que ingresa el recibo es administrador hay que dejar el recibo en estado ACEPTADO
		UsuarioSistema usrSistema = usuarioSistemaDAO.getUsuarioSistemaByUsername(usrName);
		if(usrSistema == null) {
			throw new RuntimeException("Error: No existe un usuario con username: " + usrName);
		} else if(usrSistema.getPerfil().getIsAdmin() != null && usrSistema.getPerfil().getIsAdmin()) {
			recibo.setEstadoRecibo(EEstadoRecibo.ACEPTADO);
			recibo.setUsuarioConfirmacion(usrName);
		}
		recibo = reciboDAO.save(recibo); 
		cuentaFacade.crearMovimientoHaber(recibo);
		return recibo;
	}

	private void checkConsistenciaRecibo(Recibo recibo) throws ValidacionException {
		//Chequeo que todos los cheques sean del cliente al que le estoy haciendo el recibo
		for(FormaPago fp : recibo.getPagos()) {
			if(fp instanceof FormaPagoCheque) {
				FormaPagoCheque fpch = (FormaPagoCheque)fp;
				if(!fpch.getCheque().getCliente().equals(recibo.getCliente())) {
					throw new ValidacionException(EValidacionException.RECIBO_CHEQUES_INCONSISTENTES.getInfoValidacion());
				}
			}
		}
		//Chequeo que el número de recibo sea único
		if(reciboDAO.existsNroRecibo(recibo.getId(), recibo.getNroRecibo())) {
			throw new ValidacionException(EValidacionException.RECIBO_NRO_RECIBO_EXISTENTE.getInfoValidacion(), new String[] { recibo.getNroRecibo().toString() });
		}
		//Chequeo que todas las FC/ND sean del cliente al que le estoy haciendo el recibo
		for(PagoRecibo pr : recibo.getPagoReciboList()) {
			if(pr instanceof PagoReciboFactura) {
				PagoReciboFactura prf = (PagoReciboFactura)pr;
				if(!prf.getFactura().getCliente().getId().equals(recibo.getCliente().getId())) {
					throw new ValidacionException(EValidacionException.RECIBO_PAGOS_INCONSISTENTES.getInfoValidacion());
				}
			}
			if(pr instanceof PagoReciboNotaDebito) {
				PagoReciboNotaDebito prnd = (PagoReciboNotaDebito)pr;
				if(!prnd.getNotaDebito().getCliente().getId().equals(recibo.getCliente().getId())) {
					throw new ValidacionException(EValidacionException.RECIBO_PAGOS_INCONSISTENTES.getInfoValidacion());
				}
			}
		}
	}

	public Integer getLastNroRecibo() {
		return reciboDAO.getLastNroRecibo();
	}

	public Recibo getByNroReciboEager(Integer nroRecibo) {
		return reciboDAO.getByNroReciboEager(nroRecibo);
	}
	
	public Recibo getByIdEager(Integer id) {
		return reciboDAO.getByIdEager(id);
	}

	public void anularRecibo(Recibo recibo, String usuario) throws CLException {
		/**
		 * Pasos:
		 * set estado recibo = rechazado
		 * borrar movimiento que tenga el recibo
		 * restar saldo de la cuenta por el monto del recibo
		 * poner todas las facturas referencidas en los PagoRecibos como Impagas y restarle el monto pagado en el recibo
		 * poner todas las notas de debito referencidas en los PagoRecibos como Impagas y restarle el monto pagado en el recibo
		 */
		undoReciboStuff(recibo);
		recibo = reciboDAO.getById(recibo.getId());
		recibo.setEstadoRecibo(EEstadoRecibo.RECHAZADO);
		recibo = reciboDAO.save(recibo); 
		auditoriaFacade.auditar(usuario, "Rechazo de recibo Nº: " + recibo.getNroRecibo(), EnumTipoEvento.ANULACION, recibo);
	}

	public void cambiarEstadoRecibo(Recibo r, EEstadoRecibo estadoNuevo, String usrName) {
		r.setEstadoRecibo(estadoNuevo);
		if(estadoNuevo == EEstadoRecibo.ACEPTADO){
			r.setUsuarioConfirmacion(usrName);
		}
		reciboDAO.save(r);
		auditoriaFacade.auditar(usrName, "Cambio de estado de recibo Nº: " + r.getNroRecibo() + ". Estado nuevo: " + estadoNuevo.getDescripcion(),EnumTipoEvento.MODIFICACION,r);
	}

	public Date getUltimaFechaReciboGrabado() {
		return reciboDAO.getUltimaFechaReciboGrabado();
	}

	public void borrarRecibo(Recibo recibo, String usuario) throws ValidacionException {
		checkEliminacionRecibo(recibo.getId());
		undoReciboStuff(recibo);
		reciboDAO.removeById(recibo.getId());
		auditoriaFacade.auditar(usuario, "Eliminación de recibo Nº: " + recibo.getNroRecibo(), EnumTipoEvento.BAJA, recibo);
	}

	private void undoReciboStuff(Recibo recibo) {
		recibo = reciboDAO.getById(recibo.getId());
		UndoFormaPagoVisitor undoFormaPagoVisitor = new UndoFormaPagoVisitor();
		for(FormaPago fp : recibo.getPagos()) {
			fp.accept(undoFormaPagoVisitor);
		}
		UndoPagoReciboVisitor undoPagoReciboVisitor = new UndoPagoReciboVisitor();
		for(PagoRecibo pr : recibo.getPagoReciboList()) {
			pr.accept(undoPagoReciboVisitor);
		}
		cuentaFacade.borrarMovimientoRecibo(recibo);
	}

	public void checkEliminacionRecibo(Integer idRecibo) throws ValidacionException {
		Recibo recibo = reciboDAO.getById(idRecibo);
		Integer lastNroRecibo = reciboDAO.getLastNroRecibo();
		if(!recibo.getNroRecibo().equals(lastNroRecibo)) {
			throw new ValidacionException(EValidacionException.RECIBO_IMPOSIBLE_ELIMINAR_NO_ES_EL_ULTIMO.getInfoValidacion());
		}
	}

	public void checkEdicionRecibo(Integer idRecibo) throws ValidacionException {
		Recibo recibo = reciboDAO.getById(idRecibo);
		Integer lastNroReciboMismoCliente = reciboDAO.getLastNroReciboByCliente(recibo.getCliente().getId());
		if(lastNroReciboMismoCliente != null && !lastNroReciboMismoCliente.equals(recibo.getNroRecibo())) {
			throw new ValidacionException(EValidacionException.RECIBO_IMPOSIBLE_EDITAR_NO_ES_EL_ULTIMO_BY_CLIENTE.getInfoValidacion());
		}
	}

	private class UndoPagoReciboVisitor implements IPagoReciboVisitor {

		public void visit(PagoReciboACuenta prac) {
		}

		public void visit(PagoReciboFactura prf) {
			Factura factura = prf.getFactura();
			BigDecimal montoFaltantePorPagar = factura.getMontoFaltantePorPagar();
			montoFaltantePorPagar = montoFaltantePorPagar.add(prf.getMontoPagado());
			factura.setMontoFaltantePorPagar(montoFaltantePorPagar);
			if(montoFaltantePorPagar.doubleValue() > 0) {
				factura.setEstadoFactura(EEstadoFactura.IMPAGA);
			}
			facturaDAO.save(factura);
		}

		public void visit(PagoReciboNotaDebito prnd) {
			NotaDebito notaDebito = prnd.getNotaDebito();
			BigDecimal montoFaltantePorPagar = notaDebito.getMontoFaltantePorPagar();
			notaDebito.setMontoFaltantePorPagar(montoFaltantePorPagar.add(prnd.getMontoPagado()));
			if(notaDebito.getMontoFaltantePorPagar().doubleValue() > 0) {
				notaDebito.setEstadoCorreccion(EEstadoCorreccion.IMPAGA);
			}
			correccionDAO.save(notaDebito);
		}

	}

	private class UndoFormaPagoVisitor implements IFormaPagoVisitor {

		public void visit(FormaPagoCheque formaPago) {
			Cheque cheque = formaPago.getCheque();
			cheque.setEstadoCheque(EEstadoCheque.PENDIENTE_COBRAR);
			chequeDAO.save(cheque);
		}

		public void visit(FormaPagoEfectivo formaPagoEfectivo) {
		}

		public void visit(FormaPagoRetencionIngresosBrutos formaPagoRetencionIngresosBrutos) {
		}

		public void visit(FormaPagoRetencionGanancias formaPagoRetGanancias) {
		}

		public void visit(FormaPagoRetencionIVA formaPagoRetencionIVA) {
		}

		public void visit(FormaPagoTransferencia formaPagoTransferencia) {
		}

		public void visit(FormaPagoNotaCredito formaPagoNotaCredito) {
			NotaCredito notaCredito = formaPagoNotaCredito.getNotaCredito();
			notaCredito.setMontoSobrante(formaPagoNotaCredito.getImporteNC());
			correccionDAO.save(notaCredito);
		}

	}

	public List<ResumenReciboTO> getResumenReciboList(Integer idCliente, Date fechaDesde, Date fechaHasta) {
		return reciboDAO.getResumenReciboList(idCliente, fechaDesde, fechaHasta);
	}

}