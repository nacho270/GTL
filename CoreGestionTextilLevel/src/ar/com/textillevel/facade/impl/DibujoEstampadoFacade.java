package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.DibujoEstampadoDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;

@Stateless
public class DibujoEstampadoFacade implements DibujoEstampadoFacadeRemote {

	@EJB
	private DibujoEstampadoDAOLocal dibujoEstampadoDAOLocal;

	public List<DibujoEstampado> getAllOrderByNombre() {
		return dibujoEstampadoDAOLocal.getAllOrderBy("nroDibujo");
	}

	public boolean existsNroDibujo(Integer idDibujo, Integer nro) {
		return dibujoEstampadoDAOLocal.existsNroDibujo(idDibujo, nro); 
	}
	
	public DibujoEstampado save(DibujoEstampado dibujoEstampado) {
		return dibujoEstampadoDAOLocal.save(dibujoEstampado);
	}

	public void remove(DibujoEstampado dibujoEstampado) {
		dibujoEstampadoDAOLocal.removeById(dibujoEstampado.getId());
	}

	public DibujoEstampado getByIdEager(Integer idDibujoEstampado) {
		return dibujoEstampadoDAOLocal.getByIdEager(idDibujoEstampado);
	}

	@Override
	public List<DibujoEstampado> getByNroCliente(Integer nroCliente) {
		return dibujoEstampadoDAOLocal.getByNroCliente(nroCliente);
	}
}
