package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Apercibimiento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.CartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.ETipoCartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;

@SuppressWarnings("rawtypes")
@Remote
public interface SancionFacadeRemote {

	public List<Sancion> getSanciones(LegajoEmpleado legajo);
	
	public List<Sancion> getSancionesNoAsociadas(LegajoEmpleado legajo, ETipoCartaDocumento tipoCD);

	public List<AccionSancion> getHistoria(Sancion sancion);

	public Apercibimiento ingresarApercibimiento(Apercibimiento apercibimiento, String usuario) throws ValidacionException;

	public CartaDocumento ingresarCartaDocumento(CartaDocumento cartaDocumento, String usuario) throws ValidacionException;

	public void eliminarSancion(Sancion sancion) throws ValidacionException;

	public AccionSancion updateAccion(AccionHistorica ah);

	public Sancion updateSancionAndSaveAccionHistorica(Sancion sancion, AccionSancion as);

}
