package ar.com.textillevel.modulos.odt.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.modulos.odt.dao.api.local.TipoMaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.TipoMaquinaFacadeRemote;

@Stateless
public class TipoMaquinaFacade implements TipoMaquinaFacadeRemote {

	@EJB
	private TipoMaquinaDAOLocal tipoMaquinaDao;

	public List<TipoMaquina> getAllOrderByName() {
		return tipoMaquinaDao.getAllOrderBy("nombre");
	}
	
	public List<TipoMaquina> getAllOrderByNameEager(int mask) {
		List<TipoMaquina> tipos = tipoMaquinaDao.getAllOrderBy("nombre");
		for(TipoMaquina t : tipos){
			doEager(mask, t);
		}
		return tipos;
	}

	public List<TipoMaquina> getAllOrderByOrden(int mask) {
		List<TipoMaquina> tipos = tipoMaquinaDao.getAllOrderBy("orden");
		for(TipoMaquina t : tipos){
			doEager(mask, t);
		}
		return tipos;
	}
	
	public List<TipoMaquina> getAllOrderByOrden() {
		return tipoMaquinaDao.getAllOrderBy("orden");
	}

	public void remove(TipoMaquina tipoMaquina) {
		tipoMaquinaDao.removeById(tipoMaquina.getId());
	}

	public TipoMaquina save(TipoMaquina tipoMaquina) throws ValidacionException {
		if(tipoMaquinaDao.existsByNombre(tipoMaquina)) {
			List<String> strList = new ArrayList<String>();
			strList.add(tipoMaquina.getNombre());
			throw new ValidacionException(EValidacionException.ODT_TIPO_MAQUINA_MISMO_NOMBRE.getInfoValidacion(), strList);
		}
		if(tipoMaquinaDao.existsByOrden(tipoMaquina)) {
			List<String> strList = new ArrayList<String>();
			strList.add(tipoMaquina.getOrden().toString());
			throw new ValidacionException(EValidacionException.ODT_TIPO_MAQUINA_MISMO_ORDEN.getInfoValidacion(), strList);
		}
		return tipoMaquinaDao.save(tipoMaquina);
	}

	public List<TipoMaquina> getAllByIdTipo(Integer idTipoMaquina) {
		return tipoMaquinaDao.getAllByIdTipo(idTipoMaquina);
	}

	public TipoMaquina getByIdEager(Integer idTipoMaquina, int mask) {
		TipoMaquina tipoMaquina = tipoMaquinaDao.getById(idTipoMaquina);
		doEager(mask, tipoMaquina);
		return tipoMaquina;
	}

	private void doEager(int mask, TipoMaquina tipoMaquina) {
		if( (mask & MASK_PROCESOS) == MASK_PROCESOS){
			tipoMaquina.getProcesos().size();
			if( (mask & MASK_SUBPROCESOS) == MASK_SUBPROCESOS){
				for(ProcesoTipoMaquina p : tipoMaquina.getProcesos()){
					for(InstruccionProcedimiento instruccion : p.getInstrucciones()) {
						if(instruccion instanceof InstruccionProcedimientoPasadas && ((InstruccionProcedimientoPasadas)instruccion).getQuimicos()!=null){
							((InstruccionProcedimientoPasadas)instruccion).getQuimicos().size();
						}
					}
					if(p.getProcedimientos() !=null){
						p.getProcedimientos().size();
						if( (mask & MASK_INSTRUCCIONES) == MASK_INSTRUCCIONES){
							for(ProcedimientoTipoArticulo procedimiento: p.getProcedimientos()){
								procedimiento.getTipoArticulo().getNombre();
								procedimiento.getTipoArticulo().getTiposArticuloComponentes().size();
								if(procedimiento.getPasos() != null){
									procedimiento.getPasos().size();
									for(InstruccionProcedimiento instruccion : procedimiento.getPasos()){
										if(instruccion instanceof InstruccionProcedimientoPasadas && ((InstruccionProcedimientoPasadas)instruccion).getQuimicos()!=null){
											((InstruccionProcedimientoPasadas)instruccion).getQuimicos().size();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
//	public TipoMaquina getByIdSuperEager(Integer idTipoMaquina) {
//		return tipoMaquinaDao.getByIdSuperEager(idTipoMaquina);
//	}
}
