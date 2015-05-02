package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.dao.api.AccionSancionDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.AntiFichadaDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.SancionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaVigencia;
import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionApercibimiento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionCartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Apercibimiento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.CartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.ETipoCartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.ISancionVisitor;
import ar.com.textillevel.modulos.personal.facade.api.remote.SancionFacadeRemote;

@Stateless
@SuppressWarnings("rawtypes")
public class SancionFacade implements SancionFacadeRemote {

	@EJB
	private SancionDAOLocal sancionDAO;

	@EJB
	private AccionSancionDAOLocal accionSancionDAO;

	@EJB
	private AntiFichadaDAOLocal antifichadaDAO;

	public Apercibimiento ingresarApercibimiento(Apercibimiento apercibimiento, String usuario) throws ValidacionException {
		apercibimiento = (Apercibimiento)sancionDAO.save(apercibimiento);
		AccionApercibimiento a = new AccionApercibimiento();
		a.setFechaHora(DateUtil.getAhora());
		a.setSancion(apercibimiento);
		a.setUsuario(usuario);
		accionSancionDAO.save(a);
		return apercibimiento;
	}

	public CartaDocumento ingresarCartaDocumento(CartaDocumento cartaDocumento, String usuario) throws ValidacionException {
		CartaDocumento cd = (CartaDocumento)sancionDAO.save(cartaDocumento);
		AccionCartaDocumento acd = new AccionCartaDocumento();
		acd.setFechaHora(DateUtil.getAhora());
		acd.setSancion(cd);
		acd.setUsuario(usuario);
		acd.setEstadoCD(cd.getEstadoCD());
		accionSancionDAO.save(acd);

		//Si es una CD de sancion genero la antifichada correspondiente
		if(cartaDocumento.getTipoCD() == ETipoCartaDocumento.SANCION_POR_NO_JUSTIF) {
			AntiFichadaVigencia antifichadaVigencia = new AntiFichadaVigencia();
			antifichadaVigencia.setJustificada(false);
			antifichadaVigencia.setLegajo(cartaDocumento.getLegajo());
			antifichadaVigencia.setFechaDesde(cartaDocumento.getFechaSancion());
			antifichadaVigencia.setFechaHasta(cartaDocumento.getFechaIncorporacion());
			antifichadaVigencia.setSancion(cd);
			antifichadaDAO.save(antifichadaVigencia);
		}

		return cd;
	}

	public List<Sancion> getSanciones(LegajoEmpleado legajo) {
		return sancionDAO.getSanciones(legajo);
	}

	public List<Sancion> getSancionesNoAsociadas(LegajoEmpleado legajo, ETipoCartaDocumento tipoCD) {
		List<Sancion> allSancionesNoAsociadas = sancionDAO.getSancionesNoAsociadas(legajo);
		FilterSancionCDVisitor visitor = new FilterSancionCDVisitor(tipoCD); 
		for(Sancion s : allSancionesNoAsociadas) {
			s.accept(visitor);
		}
		return visitor.getSanciones();
	}

	public List<AccionSancion> getHistoria(Sancion sancion) {
		return accionSancionDAO.getHistoria(sancion);
	}

	public void eliminarSancion(Sancion sancion) throws ValidacionException {
		accionSancionDAO.borrarAccionesSancion(sancion);
		sancionDAO.removeById(sancion.getId());
		antifichadaDAO.borrarAntifichadasSancion(sancion);
		//TODO: Eliminar las sanciones asociadas. Por ejemplo si es carta documento eliminar los apercibimientos!!!
	}

	public AccionSancion updateAccion(AccionHistorica ah) {
		return accionSancionDAO.save((AccionSancion)ah);
	}

	public Sancion updateSancionAndSaveAccionHistorica(Sancion sancion, AccionSancion as) {
		as.setFechaHora(DateUtil.getAhora());
		accionSancionDAO.save(as);
		return sancionDAO.save(sancion);
	}

	public static class FilterSancionCDVisitor implements ISancionVisitor {

		private ETipoCartaDocumento tipoCD;
		private List<Sancion> sanciones;

		public FilterSancionCDVisitor(ETipoCartaDocumento tipoCD) {
			this.tipoCD = tipoCD;
			this.sanciones = new ArrayList<Sancion>();
		}

		public void visit(Apercibimiento apercibimiento) {
			if(tipoCD==ETipoCartaDocumento.AVISO_JUSTIF_FALTA || tipoCD==ETipoCartaDocumento.SANCION_POR_NO_JUSTIF) {
				getSanciones().add(apercibimiento);
			}
		}

		public void visit(CartaDocumento cd) {
			if(tipoCD==ETipoCartaDocumento.SANCION_POR_NO_JUSTIF && cd.getTipoCD() == ETipoCartaDocumento.AVISO_JUSTIF_FALTA) {
				getSanciones().add(cd);
			}
		}

		public List<Sancion> getSanciones() {
			return sanciones;
		}

	}
	
}