package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.DibujoEstampadoDAOLocal;
import ar.com.textillevel.dao.api.local.GrupoArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.PrecioBaseEstampadoDAOLocal;
import ar.com.textillevel.dao.api.local.ProductoArticuloDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;

@Stateless
public class DibujoEstampadoFacade implements DibujoEstampadoFacadeRemote {

	@EJB
	private DibujoEstampadoDAOLocal dibujoEstampadoDAOLocal;

	@EJB
	private ProductoArticuloDAOLocal paDAOLocal;

	@EJB
	private PrecioBaseEstampadoDAOLocal pbeDAOLocal;

	@EJB
	private GrupoArticuloDAOLocal gaDAOLocal;

	public List<DibujoEstampado> getAllOrderByNombre() {
		return dibujoEstampadoDAOLocal.getAllOrderBy("nroDibujo");
	}

	public boolean existsNroDibujo(Integer idDibujo, Integer nro) {
		return dibujoEstampadoDAOLocal.existsNroDibujo(idDibujo, nro); 
	}
	
	public DibujoEstampado save(DibujoEstampado dibujoEstampado) {
		return dibujoEstampadoDAOLocal.save(dibujoEstampado);
	}

	public void remove(DibujoEstampado dibujoEstampado, boolean force) throws ValidacionException {
		List<PrecioBaseEstampado> allPrecioBaseByDibujo = pbeDAOLocal.getAllByDibujo(dibujoEstampado);
		if(!allPrecioBaseByDibujo.isEmpty()) {
			if(force) {//force
				deleteDibujo(dibujoEstampado);
			} else {//!force
				throw new ValidacionException(EValidacionException.DIBUJO_USADO_EN_LISTAS_DE_PRECIOS.getInfoValidacion());
			}
		}
		deleteDibujo(dibujoEstampado);
	}

	private void deleteDibujo(DibujoEstampado dibujoEstampado) throws ValidacionException {
		Integer nroDibujo = dibujoEstampado.getNroDibujo();
		checkEliminarCombinarDibujo(dibujoEstampado);
		gaDAOLocal.deleteGruposArtEstampadoByDibujo(dibujoEstampado);
		dibujoEstampadoDAOLocal.remove2(dibujoEstampado);
		dibujoEstampadoDAOLocal.fixHuecosNroDibujo(nroDibujo);
	}
	
	public DibujoEstampado getByIdEager(Integer idDibujoEstampado) {
		return dibujoEstampadoDAOLocal.getByIdEager(idDibujoEstampado);
	}

	@Override
	public List<DibujoEstampado> getByNroClienteYEstado(Integer nroCliente, EEstadoDibujo estadoDibujo) {
		return dibujoEstampadoDAOLocal.getByNroClienteYEstado(nroCliente, estadoDibujo);
	}

	@Override
	public void modificarCliente(DibujoEstampado dibujoEstampado, Cliente cliente) {
		dibujoEstampado = dibujoEstampadoDAOLocal.getById(dibujoEstampado.getId());
		gaDAOLocal.deleteGruposArtEstampadoByDibujo(dibujoEstampado); //en realidad intenta borrar para todos los clientes pero sólo tendría que estar asignado a una sola lista de precios		
		dibujoEstampado.setCliente(cliente);
		dibujoEstampadoDAOLocal.save(dibujoEstampado);
	}

	@Override
	public List<DibujoEstampado> getAllByClienteAndClienteDefault(Cliente cliente) {
		return dibujoEstampadoDAOLocal.getAllByClienteAndClienteDefault(cliente);
	}

	private void checkEliminarCombinarDibujo(DibujoEstampado dibujo) throws ValidacionException {
		List<ProductoArticulo> paList = paDAOLocal.getProductosArticuloByDibujo(dibujo);
		if(!paList.isEmpty()) {
			throw new ValidacionException(EValidacionException.DIBUJO_IMPOSIBLE_ELIMINAR.getInfoValidacion());
		}
	}

}
