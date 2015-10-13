package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.textillevel.dao.api.local.FacturaPersonaDAOLocal;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.FacturaPersonaFacadeRemote;

@Stateless
public class FacturaPersonaFacade implements FacturaPersonaFacadeRemote{
	
	@EJB
	private FacturaPersonaDAOLocal facturaDao;
	
	@EJB
	private AuditoriaFacadeLocal<FacturaPersona> auditoriaFacade;
	
	@EJB
	private CuentaFacadeLocal cuentaFacade;
	
	public boolean existeNroFacturaParaPersona(Integer nroFactura, Persona persona){
		return facturaDao.existeNroFacturaParaPersona(nroFactura,persona);
	}

	public FacturaPersona guardarFactura(FacturaPersona factura, String usuario) {
		factura = guardarInterno(factura);
		auditoriaFacade.auditar(usuario, "Creación de factura de persona Nº " + factura.getNroFactura(), EnumTipoEvento.ALTA, factura);
		return factura;
	}
	
	public void eliminarFactura(FacturaPersona factura, String usuario){
		factura = eliminarInterno(factura);
		auditoriaFacade.auditar(usuario, "Eliminación de factura de persona Nº " + factura.getNroFactura(), EnumTipoEvento.BAJA, factura);
	}

	private FacturaPersona eliminarInterno(FacturaPersona factura) {
		cuentaFacade.borrarMovimientoFacturaPersona(factura);
		facturaDao.removeById(factura.getId());
		return factura;
	}
	
	public FacturaPersona editarFactura(FacturaPersona factura, String usuario){
		FacturaPersona facturaAnterior = facturaDao.getById(factura.getId());
		cuentaFacade.actualizarMovimientoFacturaPersona(factura,facturaAnterior.getMonto());
		factura = facturaDao.save(factura);
		auditoriaFacade.auditar(usuario, "Edición de factura de persona Nº " + factura.getNroFactura(), EnumTipoEvento.MODIFICACION, factura);
		return factura;
	}

	private FacturaPersona guardarInterno(FacturaPersona factura) {
		factura = facturaDao.save(factura);
		cuentaFacade.crearMovimientoDebePersona(factura);
		return factura;
	}

	public FacturaPersona getById(Integer idFactura) {
		return facturaDao.getById(idFactura);
	}

	public void confirmarFactura(FacturaPersona factura, String usuario) {
		factura.setUsuarioVerificador(usuario);
		facturaDao.save(factura);
		auditoriaFacade.auditar(usuario, "Verificación de Factura de persona Nº: " + factura.getNroFactura(), EnumTipoEvento.MODIFICACION, factura);

	}
}
