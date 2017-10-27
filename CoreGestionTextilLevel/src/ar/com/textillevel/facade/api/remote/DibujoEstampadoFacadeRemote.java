package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;

@Remote
public interface DibujoEstampadoFacadeRemote {
	
	public List<DibujoEstampado> getAllOrderByNombre();
	
	public DibujoEstampado save(DibujoEstampado dibujoEstampado, Integer nroDibujoOriginal, String user) throws ValidacionException;

	public void remove(DibujoEstampado dibujoEstampado, boolean force, String user) throws ValidacionException, ValidacionExceptionSinRollback;

	public DibujoEstampado getByIdEager(Integer idDibujoEstampado);

	public boolean existsNroDibujo(Integer idDibujo, Integer nro);

	public List<DibujoEstampado> getByNroClienteYEstado(Integer nroCliente, EEstadoDibujo estadoDibujo, Boolean incluir01);

	public void modificarCliente(DibujoEstampado dibujoEstampado, Cliente cliente, String user);

	public List<DibujoEstampado> getAllByClienteAndClienteDefault(Cliente cliente);

	public Integer getProximoNroDibujo(Integer nroComienzo);

	public List<DibujoEstampado> getAllByEstadoYCliente(EEstadoDibujo salida, Cliente cliente);

	public void combinarDibujos(DibujoEstampado dibujoActual, List<DibujoEstampado> dibujosCombinados, String user) throws ValidacionException;
	
	public DibujoEstampado actualizarEstado(DibujoEstampado dibujo, EEstadoDibujo estadoUntil, String user);
	
}