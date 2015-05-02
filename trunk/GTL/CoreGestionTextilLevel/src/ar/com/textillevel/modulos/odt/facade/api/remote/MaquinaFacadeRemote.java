package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TerminacionFraccionado;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;

@Remote
public interface MaquinaFacadeRemote {

	public List<Maquina> getAllByTipo(TipoMaquina tipoMaquina);

	public List<Maquina> getAllSorted();

	public void remove(Maquina maquina);

	public Maquina save(Maquina maquina) throws ValidacionException;

	public Maquina getByIdEager(Integer id);

	public List<TerminacionFraccionado> getAllTerminaciones();

	public TerminacionFraccionado save(TerminacionFraccionado terminacion) throws ValidacionException;

}
