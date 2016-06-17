package ar.com.textillevel.modulos.odt.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.modulos.odt.dao.api.local.MaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.TerminacionFraccionadoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TerminacionFraccionado;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.MaquinaFacadeRemote;

@Stateless
public class MaquinaFacade implements MaquinaFacadeRemote {

	@EJB
	private MaquinaDAOLocal maquinaDao;
	
	@EJB
	private TerminacionFraccionadoDAOLocal terminacionDAO;

	public List<Maquina> getAllByTipo(TipoMaquina tipoMaquina) {
		return maquinaDao.getAllByTipo(tipoMaquina);
	}

	public List<Maquina> getAllSorted() {
		return maquinaDao.getAllSorted();
	}

	public void remove(Maquina maquina) {
		maquinaDao.removeById(maquina.getId());
	}

	public Maquina save(Maquina maquina) throws ValidacionException {
		if(maquinaDao.existsNombreByTipoMaquina(maquina)) {
			List<String> strList = new ArrayList<String>();
			strList.add(maquina.getNombre());
			strList.add(maquina.getTipoMaquina().getNombre());
			throw new ValidacionException(EValidacionException.ODT_MAQUINA_MISMO_NOMBRE_Y_TIPO_MAQUINA.getInfoValidacion(), strList);
		}
		return maquinaDao.save(maquina);
	}

	public Maquina getByIdEager(Integer id) {
		return maquinaDao.getByIdEager(id);
	}

	public List<TerminacionFraccionado> getAllTerminaciones() {
		return terminacionDAO.getAllOrderBy("nombre");
	}

	public TerminacionFraccionado save(TerminacionFraccionado terminacion) throws ValidacionException {
		if(terminacionDAO.existsTerminacionByNombre(terminacion)) {
			List<String> strList = new ArrayList<String>();
			strList.add(terminacion.getNombre());
			throw new ValidacionException(EValidacionException.MAQUINA_TERMINACION_MISMO_NOMBRE.getInfoValidacion(), strList);
		}
		return terminacionDAO.save(terminacion);
	}

	public List<Maquina> getBySector(ESectorMaquina sector) {
		return maquinaDao.getAllBySector(sector);
	}

}