package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;

@Remote
public interface DibujoEstampadoFacadeRemote {
	
	public List<DibujoEstampado> getAllOrderByNombre();
	
	public DibujoEstampado save(DibujoEstampado dibujoEstampado, Integer nroDibujoOriginal) throws ValidacionException;

	public void remove(DibujoEstampado dibujoEstampado, boolean force) throws ValidacionException ;

	public DibujoEstampado getByIdEager(Integer idDibujoEstampado);

	public boolean existsNroDibujo(Integer idDibujo, Integer nro);

	public List<DibujoEstampado> getByNroClienteYEstado(Integer nroCliente, EEstadoDibujo estadoDibujo);

	public void modificarCliente(DibujoEstampado dibujoEstampado, Cliente cliente);

	public List<DibujoEstampado> getAllByClienteAndClienteDefault(Cliente cliente);

	public Integer getProximoNroDibujo(Integer nroComienzo);

	public List<DibujoEstampado> getAllByEstadoYCliente(EEstadoDibujo salida, Cliente cliente);


}
