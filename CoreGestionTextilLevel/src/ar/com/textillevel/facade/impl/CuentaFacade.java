package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.BancoDAOLocal;
import ar.com.textillevel.dao.api.local.ClienteDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionFacturaPersonaDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionFacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.CuentaDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.MovimientoCuentaDAOLocal;
import ar.com.textillevel.dao.api.local.OrdenDePagoDAOLocal;
import ar.com.textillevel.dao.api.local.PersonaDAOLocal;
import ar.com.textillevel.dao.api.local.ProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.ReciboDAOLocal;
import ar.com.textillevel.entidades.cuenta.Cuenta;
import ar.com.textillevel.entidades.cuenta.CuentaBanco;
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.CuentaPersona;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebe;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebePersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaber;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberPersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoInternoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.cuenta.to.CuentaTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.cuenta.to.MovimientoTO;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.NotaDebitoPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoCorreccion;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.remote.CuentaFacadeRemote;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

@Stateless
public class CuentaFacade implements CuentaFacadeLocal, CuentaFacadeRemote {

	@EJB
	private CuentaDAOLocal cuentaDao;

	@EJB
	private ClienteDAOLocal clienteDao;

	@EJB
	private MovimientoCuentaDAOLocal movimientoDao;
	
	@EJB
	private ReciboDAOLocal reciboDao;

	@EJB
	private FacturaDAOLocal facturaDao;
	
	@EJB
	private FacturaProveedorDAOLocal facturaProveedorDao;
	
	@EJB
	private CorreccionDAOLocal correccionDao;

	@EJB
	private CorreccionFacturaProveedorDAOLocal correccionProveedorDao;
	
	@EJB
	private CorreccionFacturaPersonaDAOLocal correccionPersonaDao;

	@EJB
	private ProveedorDAOLocal proveedorDAO;
	
	@EJB
	private BancoDAOLocal bancoDao;
	
	@EJB
	private PersonaDAOLocal personaDao;
	
	@EJB
	private OrdenDePagoDAOLocal ordenDePagoDAO;

	public CuentaCliente getCuentaClienteByNroCliente(Integer nroCliente){
		Cliente clienteByNumero = clienteDao.getClienteByNumero(nroCliente);
		if(clienteByNumero == null){
			return null;
		}
		return getCuentaClienteByIdCliente(clienteByNumero.getId());
	}
	
	public CuentaCliente getCuentaClienteByIdCliente(Integer idCliente) {
		CuentaCliente cc = cuentaDao.getCuentaClienteByIdCliente(idCliente);
		if (cc == null) {
			cc = new CuentaCliente();
			cc.setCliente(clienteDao.getReferenceById(idCliente));
			cc.setFechaCreacion(DateUtil.getAhora());
			cc.setSaldo(new BigDecimal(0d));
			cc = (CuentaCliente) cuentaDao.save(cc);
		}
		return cc;
	}

	public CuentaProveedor getCuentaProveedorByIdProveedor(Integer idProveedor) {
		CuentaProveedor cp = cuentaDao.getCuentaProveedorByIdProveedor(idProveedor);
		if (cp == null) {
			cp = new CuentaProveedor();
			cp.setProveedor(proveedorDAO.getReferenceById(idProveedor));
			cp.setFechaCreacion(DateUtil.getAhora());
			cp.setSaldo(new BigDecimal(0d));
			cp = (CuentaProveedor) cuentaDao.save(cp);
		}
		return cp;
	}
	
	public CuentaBanco getCuentaBancoByIdBanco(Integer idBanco) {
		CuentaBanco cb = cuentaDao.getCuentaBancoByIdBanco(idBanco);
		if (cb == null) {
			cb = new CuentaBanco();
			cb.setBanco(bancoDao.getReferenceById(idBanco));
			cb.setFechaCreacion(DateUtil.getAhora());
			cb.setSaldo(new BigDecimal(0d));
			cb = (CuentaBanco) cuentaDao.save(cb);
		}
		return cb;
	}
	
	public CuentaPersona getCuentaPersonaByIdPersona(Integer idPersona) {
		CuentaPersona cp = cuentaDao.getCuentaPersonaByIdPersona(idPersona);
		if (cp == null) {
			cp = new CuentaPersona();
			cp.setPersona(personaDao.getReferenceById(idPersona));
			cp.setFechaCreacion(DateUtil.getAhora());
			cp.setSaldo(new BigDecimal(0d));
			cp = (CuentaPersona) cuentaDao.save(cp);
		}
		return cp;
	}

	public void crearMovimientoDebe(Factura f) {
		Cliente cl = f.getCliente();
		CuentaCliente cuenta = getCuentaClienteByIdCliente(cl.getId());
		double saldo = cuenta.getSaldo().doubleValue();
		if(saldo > 0){
			double montoFaltante = f.getMontoFaltantePorPagar().doubleValue();
			if(saldo >= montoFaltante){
				f.setMontoFaltantePorPagar(new BigDecimal(0f));
				f.setEstadoFactura(EEstadoFactura.PAGADA);
			}else{
				f.setMontoFaltantePorPagar(new BigDecimal(montoFaltante-saldo));
			}
		}
		f = facturaDao.save(f);
		cuenta.setSaldo(new BigDecimal(saldo - f.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoDebe md = new MovimientoDebe();
		md.setFactura(f);
		md.setMonto(f.getMontoTotal());
		md.setCuenta(cuenta);
		String descripcionResumen = DateUtil.dateToString(f.getFechaEmision(),DateUtil.SHORT_DATE) +(f.getRemitos()!=null?" - RTO " + f.getNrosRemito(): "") + " - FC " + f.getNroFactura();
		md.setDescripcionResumen(descripcionResumen);
		md.setFechaHora(f.getFechaEmision());
		movimientoDao.save(md);
	}
	
	public NotaDebito crearMovimientoDebe(NotaDebito nd) {
		CuentaCliente cuenta = getCuentaClienteByIdCliente(nd.getCliente().getId());
		double saldo = cuenta.getSaldo().doubleValue();
		if(saldo > 0){
			double montoFaltante = nd.getMontoFaltantePorPagar().doubleValue();
			if(saldo >= montoFaltante){
				nd.setMontoFaltantePorPagar(new BigDecimal(0f));
				nd.setEstadoCorreccion(EEstadoCorreccion.PAGADA);
			}else{
				nd.setMontoFaltantePorPagar(new BigDecimal(montoFaltante-saldo));
			}
		}
		nd = (NotaDebito) correccionDao.save(nd);
		cuenta.setSaldo(new BigDecimal(saldo - nd.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoDebe md = new MovimientoDebe();
		md.setNotaDebito(nd);
		md.setMonto(nd.getMontoTotal());
		md.setCuenta(cuenta);
		md.setDescripcionResumen(DateUtil.dateToString(nd.getFechaEmision(),DateUtil.SHORT_DATE) + " - ND " + nd.getNroFactura() + ". " + nd.getDescripcion());
		md.setFechaHora(nd.getFechaEmision());
		movimientoDao.save(md);
		return nd;
	}

	public void crearMovimientoDebeRemitoSalidaDevolucion(RemitoSalida remitoSalida, ETipoProducto tipoProducto) {
		crearMovimientoDebeRemitoSalida(remitoSalida, DateUtil.dateToString(DateUtil.getAhora(),DateUtil.SHORT_DATE) + " - "  + tipoProducto.getDescripcion().toUpperCase() + " - RTO " + remitoSalida.getNroRemito());
	}

	public void crearMovimientoDebeRemitoSalidaDibujo(RemitoSalida remitoSalida) {
		final String dibujos = FluentIterable.from(remitoSalida.getDibujoEstampados()).transform(new Function<DibujoEstampado, String>() {
			@Override
			public String apply(DibujoEstampado dibujo) {
				return dibujo.toString();
			}
		}).join(Joiner.on(','));
		crearMovimientoDebeRemitoSalida(remitoSalida, DateUtil.dateToString(DateUtil.getAhora(),DateUtil.SHORT_DATE) + " - RTO " + remitoSalida.getNroRemito() + " - DIBUJO(s): " + dibujos);
	}
	
	private void crearMovimientoDebeRemitoSalida(RemitoSalida remitoSalida, String descripcion) {
		CuentaCliente cuenta = getCuentaClienteByIdCliente(remitoSalida.getCliente().getId());
		MovimientoDebe md = new MovimientoDebe();
		md.setMonto(BigDecimal.ZERO);
		md.setCuenta(cuenta);
		md.setRemitoSalida(remitoSalida);
		String descripcionResumen = descripcion;
		md.setDescripcionResumen(descripcionResumen);
		md.setFechaHora(DateUtil.getAhora());
		movimientoDao.save(md);
	}
	
	public void crearMovimientoHaber(NotaCredito nc) {
		CuentaCliente cuenta = getCuentaClienteByIdCliente(nc.getCliente().getId());
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() + nc.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoHaber mh = new MovimientoHaber();
		mh.setNotaCredito(nc);
		mh.setMonto(nc.getMontoTotal().negate());
		mh.setCuenta(cuenta);
		mh.setDescripcionResumen(DateUtil.dateToString(nc.getFechaEmision(),DateUtil.SHORT_DATE)+ " - NC " + nc.getNroFactura() + ". " + nc.getDescripcion());
		mh.setFechaHora(nc.getFechaEmision());
		movimientoDao.save(mh);
	}

	public void crearMovimientoHaber(Recibo r) {
		CuentaCliente cuenta = getCuentaClienteByIdCliente(r.getCliente().getId());
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() + r.getMonto().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoHaber mr = new MovimientoHaber();
		mr.setRecibo(r);
		mr.setMonto(r.getMonto().negate());
		mr.setCuenta(cuenta);
		mr.setDescripcionResumen(DateUtil.dateToString(r.getFecha(),DateUtil.SHORT_DATE) +" - RC " + r.getNroRecibo());
		mr.setFechaHora(r.getFechaEmision());
		movimientoDao.save(mr);
	}

	public List<MovimientoCuenta> getMovimientosByIdClienteYFecha(Integer nroCliente, Date fechaDesde, Date fechaHasta, 
			boolean incluirFacturasPagadas/*, boolean masAntiguoPrimero*/, ETipoDocumento filtroTipoDocumento) {
		Cliente clienteByNumero = clienteDao.getClienteByNumero(nroCliente);
		if(clienteByNumero==null){
			return null;
		}
		CuentaCliente cuentaCliente = getCuentaClienteByIdCliente(clienteByNumero.getId());
		if(cuentaCliente == null){
			return null;
		}
		return movimientoDao.getMovimientosByIdClienteYFecha(cuentaCliente.getId(), fechaDesde, fechaHasta, incluirFacturasPagadas/*, masAntiguoPrimero*/, filtroTipoDocumento);
	}

	public Map<Integer, List<Integer>> getMapaRecibosYPagosRecibos() {
		return reciboDao.getMapaRecibosYPagosRecibos();
	}

	public InfoCuentaTO getInfoReciboYPagosRecibidos(Integer nroCliente) {
		return reciboDao.getInfoReciboYPagosRecibidos(nroCliente);
	}

	public InfoCuentaTO getInfoOrdenDePagoYPagosRecibidos(Integer idProveedor) {
		return ordenDePagoDAO.getInfoOrdenDePagoYPagosRecibidos(idProveedor);
	}

	public void borrarMovimientoFactura(Factura factura) {
		movimientoDao.borrarMovimientoFactura(factura.getId());
		CuentaCliente cuenta = getCuentaClienteByIdCliente(factura.getCliente().getId());
		double saldo = cuenta.getSaldo().doubleValue() + factura.getMontoTotal().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);
	}

	public void borrarMovimientoRecibo(Recibo recibo) {
		MovimientoHaber movimientoHaberByRecibo = movimientoDao.getMovimientoHaberByRecibo(recibo.getId());
		CuentaCliente cuenta = getCuentaClienteByIdCliente(recibo.getCliente().getId());
		double saldo = cuenta.getSaldo().doubleValue() - movimientoHaberByRecibo.getMonto().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		movimientoDao.removeById(movimientoHaberByRecibo.getId());
		cuentaDao.save(cuenta);
	}

	public void borrarMovimientoFacturaProveedor(FacturaProveedor factura) {
		int cantsMovsBorrados = movimientoDao.borrarMovimientoFacturaProveedor(factura.getId());
		if(cantsMovsBorrados > 0) {//actualizo la cuenta sólo si hubo movimientos que borrar
			CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(factura.getProveedor().getId());
			double saldo = cuenta.getSaldo().doubleValue() + factura.getMontoTotal().doubleValue();
			cuenta.setSaldo(new BigDecimal(saldo));
			cuentaDao.save(cuenta);
		}
	}

	public void borrarMovimientoNotaDebitoProveedor(NotaDebitoProveedor ndp) {
		int cantsMovsBorrados = movimientoDao.borrarMovimientoNotaDebitoProveedor(ndp.getId());
		if(cantsMovsBorrados > 0) {//actualizo la cuenta sólo si hubo movimientos que borrar
			CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(ndp.getProveedor().getId());
			double saldo = cuenta.getSaldo().doubleValue() + ndp.getMontoTotal().doubleValue();
			cuenta.setSaldo(new BigDecimal(saldo));
			cuentaDao.save(cuenta);
		}
	}

	public void borrarMovimientoNotaCreditoProveedor(NotaCreditoProveedor ncp) {
		int cantsMovsBorrados = movimientoDao.borrarMovimientoNotaCreditoProveedor(ncp.getId());
		if(cantsMovsBorrados > 0) {//actualizo la cuenta sólo si hubo movimientos que borrar
			CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(ncp.getProveedor().getId());
			double saldo = cuenta.getSaldo().doubleValue() - ncp.getMontoTotal().doubleValue();
			cuenta.setSaldo(new BigDecimal(saldo));
			cuentaDao.save(cuenta);
		}
	}

	public void borrarMovimientoNotaDebitoPersona(NotaDebitoPersona ndp) {
		int cantsMovsBorrados = movimientoDao.borrarMovimientoNotaDebitoPersona(ndp.getId());
		if(cantsMovsBorrados > 0) {//actualizo la cuenta sólo si hubo movimientos que borrar
			CuentaPersona cuenta = getCuentaPersonaByIdPersona(ndp.getPersona().getId());
			double saldo = cuenta.getSaldo().doubleValue() + ndp.getMontoTotal().doubleValue();
			cuenta.setSaldo(new BigDecimal(saldo));
			cuentaDao.save(cuenta);
		}
	}

	public void borrarMovimientoNotaCreditoCliente(NotaCredito nc){
		movimientoDao.borrarMovimientoNotaCreditoCliente(nc.getId());
		CuentaCliente cuenta = getCuentaClienteByIdCliente(nc.getCliente().getId());
		double saldo = cuenta.getSaldo().doubleValue() - nc.getMontoTotal().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);
	}
	
	public void borrarMovimientoNotaDebitoCliente(NotaDebito nd){
		movimientoDao.borrarMovimientoNotaDebitoCliente(nd.getId());
		CuentaCliente cuenta = getCuentaClienteByIdCliente(nd.getCliente().getId());
		double saldo = cuenta.getSaldo().doubleValue() + nd.getMontoTotal().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);
	}
	
	public void crearMovimientoHaberProveedor(OrdenDePago orden) {
		CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(orden.getProveedor().getId());
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() + orden.getMonto().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoHaberProveedor mhp = new MovimientoHaberProveedor();
		mhp.setMonto(orden.getMonto());
		mhp.setCuenta(cuenta);
		mhp.setOrdenDePago(orden);
		mhp.setDescripcionResumen(DateUtil.dateToString(orden.getFechaEmision(),DateUtil.SHORT_DATE) +" - ODP - " + orden.getNroOrden());
		mhp.setFechaHora(orden.getFechaEmision());
		movimientoDao.save(mhp);
	}

	public void crearMovimientoDebeProveedor(FacturaProveedor f) {
		CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(f.getProveedor().getId());
		double saldo = cuenta.getSaldo().doubleValue();
		if(saldo > 0) { //La factura se paga con el saldo a favor que tiene el proveedor
			double montoFaltante = f.getMontoFaltantePorPagar().doubleValue();
			if(saldo >= montoFaltante) {
				f.setMontoFaltantePorPagar(new BigDecimal(0f));
			}else{
				f.setMontoFaltantePorPagar(new BigDecimal(montoFaltante - saldo));
			}
		}
		f = facturaProveedorDao.save(f);
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() - f.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);

		MovimientoDebeProveedor mdp = new MovimientoDebeProveedor();
		mdp.setMonto(f.getMontoTotal().negate());
		mdp.setCuenta(cuenta);
		mdp.setFacturaProveedor(f);
		mdp.setDescripcionResumen(DateUtil.dateToString(f.getFechaIngreso(),DateUtil.SHORT_DATE) +" - FC " + f.getNroFactura());
		mdp.setFechaHora(new Timestamp(f.getFechaIngreso().getTime()));
		movimientoDao.save(mdp);
	}

	public void crearMovimientoDebeProveedor(NotaDebitoProveedor notaDebito) {
		CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(notaDebito.getProveedor().getId());
		double saldo = cuenta.getSaldo().doubleValue();
		if(saldo > 0) { //La nota de débito se paga con el saldo a favor que tiene el proveedor
			double montoFaltante = notaDebito.getMontoFaltantePorPagar().doubleValue();
			if(saldo >= montoFaltante) {
				notaDebito.setMontoFaltantePorPagar(new BigDecimal(0f));
			}else{
				notaDebito.setMontoFaltantePorPagar(new BigDecimal(montoFaltante - saldo));
			}
		}
		notaDebito = (NotaDebitoProveedor)correccionProveedorDao.save(notaDebito); 
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() - notaDebito.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);

		MovimientoDebeProveedor mdp = new MovimientoDebeProveedor();
		mdp.setMonto(notaDebito.getMontoTotal().negate());
		mdp.setCuenta(cuenta);
		mdp.setNotaDebitoProveedor(notaDebito);
		mdp.setDescripcionResumen(DateUtil.dateToString(notaDebito.getFechaIngreso(),DateUtil.SHORT_DATE) +" - ND " + notaDebito.getNroCorreccion());
		mdp.setFechaHora(new Timestamp(notaDebito.getFechaIngreso().getTime()));
		movimientoDao.save(mdp);
	}

	public void crearMovimientoDebePersona(NotaDebitoPersona notaDebito, String obsMovimiento) {
		CuentaPersona cuenta = getCuentaPersonaByIdPersona(notaDebito.getPersona().getId());
		double saldo = cuenta.getSaldo().doubleValue();
		if(saldo > 0) { //La nota de débito se paga con el saldo a favor que tiene el proveedor
			double montoFaltante = notaDebito.getMontoFaltantePorPagar().doubleValue();
			if(saldo >= montoFaltante) {
				notaDebito.setMontoFaltantePorPagar(new BigDecimal(0f));
			}else{
				notaDebito.setMontoFaltantePorPagar(new BigDecimal(montoFaltante - saldo));
			}
		}
		notaDebito = (NotaDebitoPersona)correccionPersonaDao.save(notaDebito); 
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() - notaDebito.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);

		MovimientoDebePersona mdp = new MovimientoDebePersona();
		mdp.setMonto(notaDebito.getMontoTotal());
		mdp.setCuenta(cuenta);
		mdp.setNotaDebitoPersona(notaDebito);
		mdp.setDescripcionResumen(DateUtil.dateToString(notaDebito.getFechaIngreso(),DateUtil.SHORT_DATE) +" - ND " + notaDebito.getNroCorreccion());
		mdp.setFechaHora(new Timestamp(notaDebito.getFechaIngreso().getTime()));
		mdp.setObservaciones(obsMovimiento);
		movimientoDao.save(mdp);
	}
	
	
//	ESTO ES LO QUE HICIMOS EL SABADO EN LA FABRICA Y DIEGO DESPUES DIJO QUE NO FUNCIONO (NO SE USA)
//	VUELVO A PONER LO ANTERIOR
	public BigDecimal getTransporteCuenta(Integer idCliente, Date fechaTope,boolean menorEstricto) throws ValidacionException {
		CuentaCliente cc = getCuentaClienteByNroCliente(idCliente);
		if(cc==null){
			throw new ValidacionException(EValidacionException.CLIENTE_INEXISTENTE.getInfoValidacion());
		}
		return movimientoDao.getSaldoCuentaHastaFecha2(cc, fechaTope,menorEstricto);
	}

	public BigDecimal getTransporteCuenta(Integer idCliente, Date fechaTope, Integer idMovimiento) throws ValidacionException {
		CuentaCliente cc = getCuentaClienteByNroCliente(idCliente);
		if(cc==null){
			throw new ValidacionException(EValidacionException.CLIENTE_INEXISTENTE.getInfoValidacion());
		}
		return movimientoDao.getSaldoCuentaHastaFechaByIdMovimiento(cc, fechaTope, idMovimiento);
	}
	
	public List<MovimientoCuenta> getMovimientosTransporteCuentaCliente(Integer idCliente, Date fechaTope) throws ValidacionException {
		CuentaCliente cc = getCuentaClienteByNroCliente(idCliente);
		if(cc==null){
			throw new ValidacionException(EValidacionException.CLIENTE_INEXISTENTE.getInfoValidacion());
		}
		return movimientoDao.getMovimientosDeTransporte(cc, fechaTope);
	}
	
	public List<MovimientoCuenta> getMovimientosTransporteCuentaProveedor(Integer idProveedor, Date fechaTope) throws ValidacionException {
		CuentaProveedor cp = getCuentaProveedorByIdProveedor(idProveedor);
		return movimientoDao.getMovimientosDeTransporte(cp, fechaTope);
	}
	
	public List<MovimientoCuenta> getMovimientosTransporteCuentaPersona(Integer idPersona, Date fechaTope) throws ValidacionException {
		CuentaPersona cp = getCuentaPersonaByIdPersona(idPersona);
		return movimientoDao.getMovimientosDeTransporte(cp, fechaTope);
	}
	
	public List<MovimientoCuenta> getMovimientosTransporteCuentaBanco(Integer idBanco, Date fechaTope) throws ValidacionException {
		CuentaBanco cb = getCuentaBancoByIdBanco(idBanco);
		return movimientoDao.getMovimientosDeTransporte(cb, fechaTope);
	}
	
	public List<MovimientoCuenta> getMovimientosByIdProveedorYFecha(Integer idProveedor, Date fechaDesde, Date fechaHasta, boolean ultimosMovimientos, ETipoDocumento tipoDocumento) {
		CuentaProveedor cp = getCuentaProveedorByIdProveedor(idProveedor);
		return movimientoDao.getMovimientosProveedorByIdCuentaYFecha(cp.getId(), fechaDesde, fechaHasta, ultimosMovimientos, tipoDocumento);
	}

	public BigDecimal getTransporteCuentaProveedor(Integer idProveedor, Date fechaTope) {
		CuentaProveedor cp = getCuentaProveedorByIdProveedor(idProveedor);
		return movimientoDao.getSaldoCuentaHastaFechaSinNegate(cp, fechaTope);
	}

	public void crearMovimientoHaberProveedor(NotaCreditoProveedor notaCredito, String obsMovimiento) {
		CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(notaCredito.getProveedor().getId());
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() + Math.abs(notaCredito.getMontoTotal().doubleValue())));
		cuentaDao.save(cuenta);
		MovimientoHaberProveedor mhp = new MovimientoHaberProveedor();
		mhp.setObservaciones(obsMovimiento);
		mhp.setMonto(new BigDecimal(Math.abs(notaCredito.getMontoTotal().negate().doubleValue()))); //los movimientos se guardan con saldo positivo. El saldo de la nota de crédito viene negativo
//		mhp.setMonto(notaCredito.getMontoTotal());//27/7/13 Segun Diego, estamos contando mal las NC
		mhp.setCuenta(cuenta);
		mhp.setNotaCredito(notaCredito);
		mhp.setDescripcionResumen(DateUtil.dateToString(notaCredito.getFechaIngreso(),DateUtil.SHORT_DATE) +" - NC - " + notaCredito.getNroCorreccion());
		mhp.setFechaHora(new Timestamp(notaCredito.getFechaIngreso().getTime()));
		movimientoDao.save(mhp);
	}

	public void crearMovimientoHaberBanco(OrdenDeDeposito orden) {
		CuentaBanco cuenta = getCuentaBancoByIdBanco(orden.getBanco().getId());
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() + orden.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoHaberBanco mhb = new MovimientoHaberBanco();
		mhb.setMonto(orden.getMontoTotal());
		mhb.setCuenta(cuenta);
		mhb.setOrdenDeDeposito(orden);
		mhb.setDescripcionResumen(DateUtil.dateToString(orden.getFecha(),DateUtil.SHORT_DATE) +" - ODD - " + orden.getNroOrden());
		mhb.setFechaHora(new Timestamp(orden.getFecha().getTime()));
		movimientoDao.save(mhb);
	}
	
	public void asignarFechaMovimientoHaberSegunNCP(NotaCreditoProveedor ncp) {
		MovimientoHaberProveedor movimientoHPByNC = movimientoDao.getMovimientoHPByNC(ncp.getId());
		movimientoHPByNC.setFechaHora(DateUtil.getAhora());
		movimientoHPByNC.setDescripcionResumen(DateUtil.dateToString(ncp.getFechaIngreso(),DateUtil.SHORT_DATE) +" - NC - " + ncp.getNroCorreccion());
		movimientoDao.save(movimientoHPByNC);
	}

	public void asignarFechaMovimientoDebeSegunNDP(NotaDebitoProveedor ndp) {
		MovimientoDebeProveedor movimientoDPByND = movimientoDao.getMovimientoDPByND(ndp.getId());
		movimientoDPByND.setFechaHora(DateUtil.getAhora());
		movimientoDPByND.setDescripcionResumen(DateUtil.dateToString(ndp.getFechaIngreso(),DateUtil.SHORT_DATE) +" - ND - " + ndp.getNroCorreccion());
		movimientoDao.save(movimientoDPByND);
	}

	public BigDecimal getTransporteCuentaBanco(Integer idBanco, Date fechaTope) {
		CuentaBanco cb = getCuentaBancoByIdBanco(idBanco);
		return movimientoDao.getSaldoCuentaHastaFecha(cb, fechaTope);
	}

	public List<MovimientoCuenta> getMovimientosByIdBancoYFecha(Integer idBanco, Date fechaDesde, Date fechaHasta) {
		CuentaBanco cb = getCuentaBancoByIdBanco(idBanco);
		return movimientoDao.getMovimientosBancoByIdCuentaYFecha(cb.getId(), fechaDesde, fechaHasta);
	}

	public void borrarMovimientoOrdenDePago(OrdenDePago orden) {
		movimientoDao.borrarMovimientoOrdenDePago(orden.getId());
		CuentaProveedor cuenta = getCuentaProveedorByIdProveedor(orden.getProveedor().getId());
		double saldo = cuenta.getSaldo().doubleValue() - orden.getMonto().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);
	}

	public MovimientoCuenta actualizarMovimiento(MovimientoCuenta mov) {
		return movimientoDao.save(mov);
	}
	
	public List<MovimientoCuenta> getMovimientosByIdPersonaYFecha(Integer idPersona, Date fechaDesde, Date fechaHasta) {
		CuentaPersona cb = getCuentaPersonaByIdPersona(idPersona);
		return movimientoDao.getMovimientosPersonaByIdCuentaYFecha(cb.getId(), fechaDesde, fechaHasta);
	}

	public BigDecimal getTransporteCuentaPersona(Integer idPersona, Date fechaTope) {
		CuentaPersona cb = getCuentaPersonaByIdPersona(idPersona);
		return movimientoDao.getSaldoCuentaHastaFecha(cb, fechaTope);
	}

	public void borrarMovimientoOrdenDePagoPersona(OrdenDePagoAPersona orden) {
		movimientoDao.borrarMovimientoOrdenDePagoPersona(orden.getId());
		CuentaPersona cuenta = getCuentaPersonaByIdPersona(orden.getPersona().getId());
		double saldo = cuenta.getSaldo().doubleValue() - orden.getMontoTotal().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);
	}

	public void borrarMovimientoFacturaPersona(FacturaPersona factura) {
		movimientoDao.borrarMovimientoFacturaPersona(factura.getId());
		CuentaPersona cuenta = getCuentaPersonaByIdPersona(factura.getPersona().getId());
		double saldo = cuenta.getSaldo().doubleValue() + factura.getMonto().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);		
	}

	public void crearMovimientoHaberPersona(OrdenDePagoAPersona orden) {
		CuentaPersona cuenta = getCuentaPersonaByIdPersona(orden.getPersona().getId());
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() + orden.getMontoTotal().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoHaberPersona mhp = new MovimientoHaberPersona();
		mhp.setMonto(orden.getMontoTotal());
		mhp.setCuenta(cuenta);
		mhp.setOrdenDePago(orden);
		mhp.setDescripcionResumen(DateUtil.dateToString(orden.getFecha(),DateUtil.SHORT_DATE) +" - ODP - " + orden.getNroOrden());
		mhp.setFechaHora(new Timestamp(orden.getFecha().getTime()));
		movimientoDao.save(mhp);
	}
	
	public void crearMovimientoDebePersona(FacturaPersona factura) {
		CuentaPersona cuenta = getCuentaPersonaByIdPersona(factura.getPersona().getId());
		cuenta.setSaldo(new BigDecimal(cuenta.getSaldo().doubleValue() - factura.getMonto().doubleValue()));
		cuentaDao.save(cuenta);
		MovimientoDebePersona mdp = new MovimientoDebePersona();
		mdp.setMonto(factura.getMonto());
		mdp.setCuenta(cuenta);
		mdp.setFacturaPersona(factura);
		mdp.setDescripcionResumen(DateUtil.dateToString(factura.getFecha(),DateUtil.SHORT_DATE) +" - FC - " + factura.getNroFactura());
		mdp.setFechaHora(new Timestamp(factura.getFecha().getTime()));
		movimientoDao.save(mdp);
	}

	public void actualizarMovimientoFacturaPersona(FacturaPersona factura, BigDecimal montoAnterior) {
		MovimientoCuenta mov = movimientoDao.getMovimientoDebePersonaByFactura(factura);
		mov.setMonto(factura.getMonto());
		mov.setFechaHora(new Timestamp(factura.getFecha().getTime()));
		mov.setDescripcionResumen(DateUtil.dateToString(factura.getFecha(),DateUtil.SHORT_DATE) +" - FC - " + factura.getNroFactura());
		movimientoDao.save(mov);
		CuentaPersona cuenta = getCuentaPersonaByIdPersona(factura.getPersona().getId());
		double saldo = cuenta.getSaldo().doubleValue() + montoAnterior.doubleValue() - factura.getMonto().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);		
	}

	public void actualizarMovimientoOrdenDePagoPersona(OrdenDePagoAPersona orden, BigDecimal montoAnterior) {
		MovimientoCuenta mov = movimientoDao.getMovimientoHaberPersonaByOrdenDePago(orden);
		mov.setMonto(orden.getMontoTotal());
		mov.setFechaHora(new Timestamp(orden.getFecha().getTime()));
		mov.setDescripcionResumen(DateUtil.dateToString(orden.getFecha(),DateUtil.SHORT_DATE) +" - ODP - " + orden.getNroOrden());
		movimientoDao.save(mov);
		CuentaPersona cuenta = getCuentaPersonaByIdPersona(orden.getPersona().getId());
		double saldo = cuenta.getSaldo().doubleValue() - montoAnterior.doubleValue() + orden.getMontoTotal().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);	
	}

	public void actualizarMovimientoFacturaCliente(Factura factura, BigDecimal montoAnterior) {
		MovimientoCuenta mov = movimientoDao.getMovimientoDebeByFactura(factura);
		mov.setMonto(factura.getMontoTotal());
		mov.setFechaHora(new Timestamp(factura.getFechaEmision().getTime()));
		mov.setDescripcionResumen(DateUtil.dateToString(factura.getFechaEmision(),DateUtil.SHORT_DATE) +(factura.getRemitos()!=null?" - RTO " + factura.getNrosRemito(): "") + " - FC " + factura.getNroFactura());
		movimientoDao.save(mov);
		CuentaCliente cuenta = getCuentaClienteByIdCliente(factura.getCliente().getId());
		double saldo = cuenta.getSaldo().doubleValue() + montoAnterior.doubleValue() - factura.getMontoTotal().doubleValue();
		cuenta.setSaldo(new BigDecimal(saldo));
		cuentaDao.save(cuenta);		
	}

	public CuentaTO getCuentaTO(Cliente cliente, int cantMovimientos) {
		return getCuentaTO(cliente, cantMovimientos, getCuentaClienteByIdCliente(cliente.getId()), true);
	}
	
	public CuentaTO getCuentaTO(Proveedor proveedor, int cantidadMovimientos) {
		return getCuentaTO(proveedor, cantidadMovimientos, getCuentaProveedorByIdProveedor(proveedor.getId()), false);
	}
	
	private CuentaTO getCuentaTO(IAgendable entidad, int cantidadMovimientos, Cuenta cuenta, boolean isNegate) {
		CuentaTO cuentaTO = new CuentaTO();
		CuentaOwnerTO cuentaOwner = new CuentaOwnerTO(entidad);
		cuentaTO.setOwner(cuentaOwner);
		float saldo=0f;
		List<MovimientoTO> movimientoTOs = new ArrayList<MovimientoTO>();
		MovimientoTO transporte = new MovimientoTO();
		transporte.setDescripcion("TRANSPORTE CUENTA");
		transporte.setTransporte(true);
		List<MovimientoCuenta> allMovimientos = movimientoDao.getAllMovimientosByIdCliente(cuenta.getId());
		movimientoTOs.add(transporte); //agrego el transporte primero!
		int i = 0;
		DatosMovimientoTOVisitor v = new DatosMovimientoTOVisitor();
		for(MovimientoCuenta mc : allMovimientos) {
			saldo += mc.getMonto().floatValue();
			if(i > allMovimientos.size() - cantidadMovimientos - 1) {
				MovimientoTO movTO = new MovimientoTO();
				movimientoTOs.add(movTO);
				movTO.setMonto(!isNegate?mc.getMonto().negate().floatValue():mc.getMonto().floatValue());
				movTO.setSaldoParcial(NumUtil.redondearDecimales( (isNegate?-1:1)*saldo, 2, RoundingMode.CEILING.ordinal()));
				movTO.setIdMovimiento(mc.getId());
				movTO.setDescripcion(mc.getDescripcionResumen());
				movTO.setObservaciones(mc.getObservaciones());
				mc.aceptarVisitor(v);
				movTO.setIdDocuemento(v.getIdDocumento());
				movTO.setIdTipoDocumento(v.getIdTipoDocumento());
			} else {//sumo al transporte si no pertenece a los ultimos movimientos 
				transporte.setMonto(transporte.getMonto().floatValue() + mc.getMonto().floatValue());
			}
			i++;
		}
		transporte.setMonto(NumUtil.redondearDecimales(transporte.getMonto(), 2, RoundingMode.CEILING.ordinal()));
		transporte.setSaldoParcial( (isNegate?(-1):1)*transporte.getMonto());
		cuentaTO.setMovimientos(movimientoTOs);
		cuentaTO.setSaldo((isNegate?-1:1)*saldo);
		return cuentaTO;
	}
	
	private class DatosMovimientoTOVisitor implements IFilaMovimientoVisitor{

		private Integer idDocumento;
		private Integer idTipoDocumento;
		
		public void visit(MovimientoHaber mh) {
			setIdDocumento(mh.getRecibo() == null ? mh.getNotaCredito().getId() : mh.getRecibo().getId());
			setIdTipoDocumento(mh.getRecibo() == null ? ETipoDocumento.NOTA_CREDITO.getId() : ETipoDocumento.RECIBO.getId());
		}

		public void visit(MovimientoDebe md) {
			setIdDocumento(md.getFactura() == null ? md.getNotaDebito().getId() : md.getFactura().getId());
			setIdTipoDocumento(md.getFactura() == null ? ETipoDocumento.NOTA_DEBITO.getId() : ETipoDocumento.FACTURA.getId());

		}

		public void visit(MovimientoInternoCuenta movimiento) {
			
		}

		public void visit(MovimientoHaberProveedor mh) {
			setIdDocumento(mh.getOrdenDePago() == null ? mh.getNotaCredito().getId() : mh.getOrdenDePago().getId());
			setIdTipoDocumento(mh.getOrdenDePago() == null ? ETipoDocumento.NOTA_CREDITO_PROV.getId() : ETipoDocumento.ORDEN_PAGO.getId());
		}

		public void visit(MovimientoDebeProveedor md) {
			setIdDocumento(md.getFacturaProveedor() == null ? md.getNotaDebitoProveedor().getId() : md.getFacturaProveedor().getId());
			setIdTipoDocumento(md.getFacturaProveedor() == null ? ETipoDocumento.NOTA_DEBITO_PROV.getId() : ETipoDocumento.FACTURA_PROV.getId());
		}

		public void visit(MovimientoHaberBanco movimiento) {
			
		}

		public void visit(MovimientoDebeBanco movimiento) {
			
		}

		public void visit(MovimientoDebePersona movimientoDebePersona) {
			
		}

		public void visit(MovimientoHaberPersona movimientoHaberPersona) {
			
		}

		public Integer getIdDocumento() {
			return idDocumento;
		}

		public void setIdDocumento(Integer idDocumento) {
			this.idDocumento = idDocumento;
		}

		public Integer getIdTipoDocumento() {
			return idTipoDocumento;
		}

		public void setIdTipoDocumento(Integer idTipoDocumento) {
			this.idTipoDocumento = idTipoDocumento;
		}
	}
}