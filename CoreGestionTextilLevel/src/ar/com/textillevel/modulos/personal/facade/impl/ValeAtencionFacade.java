package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.dao.api.AccionValeAtencionDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.AntiFichadaDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.ValeAtencionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAccidente;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValePreocupacional;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAccidente;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValePreocupacional;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAtencionFacadeRemote;

@Stateless
@SuppressWarnings("rawtypes")
public class ValeAtencionFacade implements ValeAtencionFacadeRemote {

	@EJB
	private ValeAtencionDAOLocal valeAtencionDAO;

	@EJB
	private AccionValeAtencionDAOLocal accionValeAtencionDAO;

	@EJB
	private AntiFichadaDAOLocal antifichadaDAO;

	public List<ValeAtencion> getValesAtencion(LegajoEmpleado legajo) {
		return valeAtencionDAO.getValesAtencion(legajo);
	}

	public List<AccionValeAtencion> getHistoria(ValeAtencion valeAtencion) {
		return accionValeAtencionDAO.getHistoria(valeAtencion);
	}

	public ValeEnfermedad ingresarValeEnfermedad(ValeEnfermedad valeEnfermedad, String usuario) throws ValidacionException {
		valeEnfermedad = (ValeEnfermedad)valeAtencionDAO.save(valeEnfermedad);
		AccionValeEnfermedad a = new AccionValeEnfermedad();
		a.setEstadoValeAtencion(EEStadoValeEnfermedad.ABIERTO);
		a.setFechaHora(DateUtil.getAhora());
		a.setValeAtencion(valeEnfermedad);
		a.setUsuario(usuario);
		accionValeAtencionDAO.save(a);
		return valeEnfermedad;
	}

	public ValeAccidente ingresarValeAccidente(ValeAccidente valeAccidente, String usuario) throws ValidacionException {
		valeAccidente = (ValeAccidente)valeAtencionDAO.save(valeAccidente);
		AccionValeAccidente a = new AccionValeAccidente();
		a.setEstadoValeAtencion(EEStadoValeEnfermedad.ABIERTO);
		a.setFechaHora(DateUtil.getAhora());
		a.setValeAtencion(valeAccidente);
		a.setUsuario(usuario);
		accionValeAtencionDAO.save(a);
		return valeAccidente;
	}

	public ValePreocupacional ingresarValePreocupacional(ValePreocupacional valePreocupacional, String usuario) throws ValidacionException {
		valePreocupacional = (ValePreocupacional)valeAtencionDAO.save(valePreocupacional);
		AccionValePreocupacional a = new AccionValePreocupacional();
		a.setObservaciones(valePreocupacional.getObservaciones());
		a.setEstadoValeAtencion(EEStadoValeEnfermedad.JUSTIFICADO_Y_ALTA);
		a.setFechaHora(DateUtil.getAhora());
		a.setValeAtencion(valePreocupacional);
		a.setUsuario(usuario);
		accionValeAtencionDAO.save(a);
		return valePreocupacional;
	}

	public void eliminarValeAtencion(ValeAtencion valeAtencion) throws ValidacionException {
		antifichadaDAO.borrarAntifichadasValeAtencion(valeAtencion);
		accionValeAtencionDAO.borrarAccionesValeAtencion(valeAtencion);
		valeAtencionDAO.removeById(valeAtencion.getId());
	}

	public AccionValeAtencion updateAccion(AccionHistorica ah) {
		return accionValeAtencionDAO.save((AccionValeAtencion)ah);
	}

	public ValeAtencion updateValeAtencionAndSaveAccionHistorica(ValeAtencion valeAtencion, AccionValeAtencion ava, List<? extends AntiFichada> antifichadaList) {
		for(AntiFichada antifichada : antifichadaList) {
			antifichadaDAO.save(antifichada);
		}
		ava.setFechaHora(DateUtil.getAhora());
		accionValeAtencionDAO.save(ava);
		return valeAtencionDAO.save(valeAtencion);
	}

}