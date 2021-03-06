package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.remito.Tarima;

@Remote
public interface TarimaFacadeRemote {

	public List<Tarima> getAllSorted();
	public Tarima save(Tarima tarima) throws ValidacionException;
	public Tarima getById(Integer idTarima);

}
