package ar.com.textillevel.facade.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.DibujoEstampadoDAOLocal;
import ar.com.textillevel.dao.api.local.GrupoArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.PrecioBaseEstampadoDAOLocal;
import ar.com.textillevel.dao.api.local.ProductoArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoSalidaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
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
	
	@EJB
	private RemitoSalidaDAOLocal remitoSalidaDAOLocal;

	public List<DibujoEstampado> getAllOrderByNombre() {
		return dibujoEstampadoDAOLocal.getAllOrderBy("nroDibujo");
	}

	public boolean existsNroDibujo(Integer idDibujo, Integer nro) {
		return dibujoEstampadoDAOLocal.existsNroDibujo(idDibujo, nro);
	}

	public DibujoEstampado save(DibujoEstampado dibujoEstampado, Integer nroDibujoOriginal) throws ValidacionException {
		if(dibujoEstampado.getId() == null) { //es nuevo, no se hacen validaciones 
			return dibujoEstampadoDAOLocal.save(dibujoEstampado);
		} else {
			checkEdicionDibujo(dibujoEstampado);
			DibujoEstampado dibResult = dibujoEstampadoDAOLocal.save(dibujoEstampado);
			if(!dibujoEstampado.getNroDibujo().equals(nroDibujoOriginal)) {
				dibujoEstampadoDAOLocal.fixHuecosNroDibujo(nroDibujoOriginal);
			}
			return dibResult;
		}
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
		checkEdicionDibujo(dibujo);
	}

	private void checkEdicionDibujo(DibujoEstampado dibujo) throws ValidacionException {
		RemitoSalida rs = remitoSalidaDAOLocal.getRemitoSalidaByDibujoEstampado(dibujo);
		if(rs != null) {
			throw new ValidacionException(EValidacionException.DIBUJO_IMPOSIBLE_ELIMINAR.getInfoValidacion());
		}
	}

	public Integer getProximoNroDibujo(Integer nroComienzo) {
		Integer ultNro = dibujoEstampadoDAOLocal.getUltNro(nroComienzo);
		if(ultNro == null) {
			return nroComienzo*1000;
		} else {
			return ultNro + 1;
		}
	}

	@Override
	public List<DibujoEstampado> getAllByEstadoYCliente(EEstadoDibujo estado, Cliente cliente) {
		return dibujoEstampadoDAOLocal.getAllByEstadoYCliente(estado, cliente);
	}

	@Override
	public void combinarDibujos(DibujoEstampado dibujoActual, List<DibujoEstampado> dibujosCombinados) throws ValidacionException {
		Cliente cl = null;
		Integer idFactura = null;
		for(DibujoEstampado de : dibujosCombinados) {
			cl = de.getCliente(); //deberían ser lo mismo así q puedo setearlo en cada vuelta del for
			idFactura = de.getIdFactura();
			remove(de, true);
		}
		dibujoActual.setCliente(cl);
		dibujoActual.setIdFactura(idFactura);
		save(dibujoActual, null);
	}

}