package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAccidente;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValePreocupacional;

@SuppressWarnings("rawtypes")
@Remote
public interface ValeAtencionFacadeRemote {

	public List<ValeAtencion> getValesAtencion(LegajoEmpleado legajo);

	public List<AccionValeAtencion> getHistoria(ValeAtencion valeAtencion);

	public ValeEnfermedad ingresarValeEnfermedad(ValeEnfermedad valeEnfermedad, String usuario) throws ValidacionException;

	public ValeAccidente ingresarValeAccidente(ValeAccidente valeAccidente, String usuario) throws ValidacionException;
	
	public ValePreocupacional ingresarValePreocupacional(ValePreocupacional valePreocupacional, String usuario) throws ValidacionException;

	public void eliminarValeAtencion(ValeAtencion valeAtencion) throws ValidacionException;

	public AccionValeAtencion updateAccion(AccionHistorica ah);

	public ValeAtencion updateValeAtencionAndSaveAccionHistorica(ValeAtencion valeAtencion, AccionValeAtencion ava, List<? extends AntiFichada> antifichadaList);

}
