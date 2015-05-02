package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;
import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.BusquedaReciboSueldoParams;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;

@Remote
public interface ReciboSueldoFacadeRemote {

	public List<InfoReciboSueltoTO> getInfoReciboSueldoTOList(BusquedaReciboSueldoParams params);
	
	public ReciboSueldo guardarRecibo(ReciboSueldo reciboSueldo);

	public ReciboSueldo getByIdEager(Integer idReciboSueldo);

	public void eliminarReciboSueldo(ReciboSueldo reciboSueldo);

	public ReciboSueldo getReciboSueldoAnterior(ReciboSueldo reciboSueldo);
	
	public ReciboSueldo getReciboSueldoSiguiente(ReciboSueldo reciboSueldo);

}
