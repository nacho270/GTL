package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Remote
public interface DibujoEstampadoFacadeRemote {
	
	public List<DibujoEstampado> getAllOrderByNombre();
	
	public DibujoEstampado save(DibujoEstampado dibujoEstampado);

	public void remove(DibujoEstampado dibujoEstampado);

	public DibujoEstampado getByIdEager(Integer idDibujoEstampado);

	public boolean existsNroDibujo(Integer idDibujo, Integer nro);

	public List<DibujoEstampado> getByNroCliente(Integer nroCliente);

}
