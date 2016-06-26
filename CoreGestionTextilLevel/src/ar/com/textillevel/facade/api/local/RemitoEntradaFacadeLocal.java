package ar.com.textillevel.facade.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;

@Local
public interface RemitoEntradaFacadeLocal {

	public RemitoEntrada getByIdEager(Integer idRemito);

	public void eliminarRemitoEntradaForzado(Integer idRE, Boolean borrarRemitos);

	public RemitoEntrada save(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, String usuario);

	public RemitoEntrada saveWithTransiciones(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, List<TransicionODT> transiciones, String usuario);

}
