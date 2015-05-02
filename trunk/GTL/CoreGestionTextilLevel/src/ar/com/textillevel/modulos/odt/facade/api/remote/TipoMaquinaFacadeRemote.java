package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;

@Remote
public interface TipoMaquinaFacadeRemote {
	
	public static final int MASK_PROCESOS = 2;
	public static final int MASK_SUBPROCESOS = 4;
	public static final int MASK_INSTRUCCIONES = 8;
	
	public List<TipoMaquina> getAllOrderByName();
	public List<TipoMaquina> getAllOrderByOrden();
	public void remove(TipoMaquina tipoMaquina);
	public TipoMaquina save(TipoMaquina tipoMaquina) throws ValidacionException;
	public List<TipoMaquina> getAllByIdTipo(Integer idTipoMaquina);
	public TipoMaquina getByIdEager(Integer idTipoMaquina, int mask);
	public List<TipoMaquina> getAllOrderByNameEager(int mask);
	public List<TipoMaquina> getAllOrderByOrden(int mask);
//	public TipoMaquina getByIdSuperEager(Integer idTipoMaquina);

}
