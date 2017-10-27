package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.dao.api.local.DibujoEstampadoDAOLocal;
import ar.com.textillevel.dao.api.local.GrupoArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.PrecioBaseEstampadoDAOLocal;
import ar.com.textillevel.dao.api.local.ProductoArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaDibujoDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoSalidaDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaCorreccion;
import ar.com.textillevel.entidades.documentos.remito.ItemDibujoRemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoCorreccion;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ResultadoEliminacionDibujosTO;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CorreccionFacadeLocal;
import ar.com.textillevel.facade.api.local.DocumentoContableFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.util.CambioEstadoDibujoHelper;
import edu.emory.mathcs.backport.java.util.Collections;

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

	@EJB
	private RemitoEntradaDibujoDAOLocal reDibujoDAOLocal;

	@EJB
	private CorreccionFacadeLocal correccionFacade;
	
	@EJB
	private DocumentoContableFacadeLocal documentoContableFacade;
	
	@EJB
	private AuditoriaFacadeLocal<DibujoEstampado> auditoriaFacade;

	public List<DibujoEstampado> getAllOrderByNombre() {
		return dibujoEstampadoDAOLocal.getAllOrderBy("nroDibujo");
	}

	public boolean existsNroDibujo(Integer idDibujo, Integer nro) {
		return dibujoEstampadoDAOLocal.existsNroDibujo(idDibujo, nro);
	}

	public DibujoEstampado save(DibujoEstampado dibujoEstampado, Integer nroDibujoOriginal, String user) throws ValidacionException {
		if(dibujoEstampado.getId() == null) { //es nuevo, no se hacen validaciones 
			DibujoEstampado dibujoSaved = dibujoEstampadoDAOLocal.save(dibujoEstampado);
			auditoriaFacade.auditar(user, "ALTA DIB " +  dibujoEstampado.toString() , EnumTipoEvento.ALTA, dibujoEstampado);
			return dibujoSaved;
		} else {
			checkEdicionDibujo(dibujoEstampado);
			DibujoEstampado dibResult = dibujoEstampadoDAOLocal.save(dibujoEstampado);
			if(!dibujoEstampado.getNroDibujo().equals(nroDibujoOriginal)) {
				dibujoEstampadoDAOLocal.fixHuecosNroDibujo(nroDibujoOriginal);
			}
			auditoriaFacade.auditar(user, "MODIF DIB " +  dibujoEstampado.toString() , EnumTipoEvento.MODIFICACION, dibujoEstampado);
			return dibResult;
		}
	}

	@SuppressWarnings("unchecked")
	public void remove(DibujoEstampado dibujoEstampado, boolean force, String user) throws ValidacionException, ValidacionExceptionSinRollback {
		ResultadoEliminacionDibujosTO result = removeInterno(Collections.singletonList(dibujoEstampado), force, true, user);
		removeREDibujosVacios(result);
		auditoriaFacade.auditar(user, "DELETE DIB " +  dibujoEstampado.toString() , EnumTipoEvento.BAJA, dibujoEstampado);
	}

	private void removeREDibujosVacios(ResultadoEliminacionDibujosTO result) {
		for(RemitoEntradaDibujo reDibujoAsociado : result.getReDibujosInvolucrados()) {
			if(reDibujoAsociado != null && reDibujoAsociado.getItems().isEmpty()) {//borro el RE si es que se quedó sin dibujos!
				reDibujoDAOLocal.removeById(reDibujoAsociado.getId());
			}
		}
	}

	private ResultadoEliminacionDibujosTO removeInterno(List<DibujoEstampado> dibujos, boolean force, boolean intentarGenerarNC, String user) throws ValidacionException, ValidacionExceptionSinRollback {
		Set<RemitoEntradaDibujo> reDibujosInvolucrados = new HashSet<RemitoEntradaDibujo>();
		double importePorDibujos = 0;
		Cliente cl = null;
		for(DibujoEstampado dibujo : dibujos) {
			cl = dibujo.getCliente(); //deberían ser todos del mismo
			RemitoEntradaDibujo reByDibujo = reDibujoDAOLocal.getREByDibujo(dibujo);
			if(reByDibujo != null) {
				ItemDibujoRemitoEntrada item = reByDibujo.getItem(dibujo);
				importePorDibujos += item.getPrecio() == null ? 0 : item.getPrecio().doubleValue();
				reByDibujo.getItems().remove(item);
				reDibujosInvolucrados.add(reDibujoDAOLocal.save(reByDibujo));
			}
			
			List<PrecioBaseEstampado> allPrecioBaseByDibujo = pbeDAOLocal.getAllByDibujo(dibujo);
			if(!allPrecioBaseByDibujo.isEmpty()) {
				if(force) {//force
					deleteDibujo(dibujo);
				} else {//!force
					throw new ValidacionException(EValidacionException.DIBUJO_USADO_EN_LISTAS_DE_PRECIOS.getInfoValidacion());
				}
			}
			deleteDibujo(dibujo);
		}
		if(intentarGenerarNC && importePorDibujos>0) {//genero una NC para un cliente si corresponde

			NotaCredito nc = new NotaCredito();
			Set<Factura> fcSet = new HashSet<Factura>();
			for(RemitoEntradaDibujo re : reDibujosInvolucrados) {
				fcSet.add(re.getFactura());
			}

			nc.setDescripcion("Borrado Dibujos " + StringUtil.getCadena(dibujos, " | "));

			Factura unaFC = fcSet.iterator().next();
			nc.setCliente(cl);
			nc.setFechaEmision(DateUtil.getAhora());
			BigDecimal porcIVAInscripto = unaFC.getPorcentajeIVAInscripto();
			BigDecimal total = new BigDecimal(importePorDibujos + (porcIVAInscripto == null ? 0 : porcIVAInscripto.doubleValue()/100 * importePorDibujos));
			nc.setMontoTotal(total);
			nc.setPorcentajeIVAInscripto(porcIVAInscripto);
			nc.setMontoSubtotal(new BigDecimal(importePorDibujos));
			nc.setEstadoCorreccion(EEstadoCorreccion.IMPAGA);
			nc.setTipoFactura(unaFC.getTipoFactura());
			nc.setNroFactura(documentoContableFacade.getProximoNroDocumentoContable(cl.getPosicionIva(), nc.getTipoDocumento()));

			nc.getFacturasRelacionadas().addAll(fcSet);

			ItemFacturaCorreccion itCorrecc = new ItemFacturaCorreccion();
			itCorrecc.setCantidad(new BigDecimal(1));
			itCorrecc.setDescripcion(nc.getDescripcion());
			itCorrecc.setImporte(nc.getMontoTotal());
			itCorrecc.setUnidad(EUnidad.UNIDAD);
			itCorrecc.setPrecioUnitario(nc.getMontoSubtotal());
			nc.getItems().add(itCorrecc);

			correccionFacade.guardarCorreccionYGenerarMovimiento(nc, user);
		}
		//armar el TO y devolverlo
		return new ResultadoEliminacionDibujosTO(new ArrayList<RemitoEntradaDibujo>(reDibujosInvolucrados), importePorDibujos);
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
	public List<DibujoEstampado> getByNroClienteYEstado(Integer nroCliente, EEstadoDibujo estadoDibujo, Boolean incluir01) {
		return dibujoEstampadoDAOLocal.getByNroClienteYEstado(nroCliente, estadoDibujo, incluir01);
	}

	@Override
	public void modificarCliente(DibujoEstampado dibujoEstampado, Cliente cliente, String user) {
		dibujoEstampado = dibujoEstampadoDAOLocal.getById(dibujoEstampado.getId());
		gaDAOLocal.deleteGruposArtEstampadoByDibujo(dibujoEstampado); //en realidad intenta borrar para todos los clientes pero sólo tendría que estar asignado a una sola lista de precios		
		dibujoEstampado.setCliente(cliente);
		dibujoEstampadoDAOLocal.save(dibujoEstampado);
		auditoriaFacade.auditar(user, "ASIG CLI " + cliente.getNroCliente() + " a DIB " + dibujoEstampado.toString(), EnumTipoEvento.MODIFICACION, dibujoEstampado);
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
	public void combinarDibujos(DibujoEstampado dibujoActual, List<DibujoEstampado> dibujosCombinados, String user) throws ValidacionException {
		checkCombinarDibujos(dibujosCombinados);
		ResultadoEliminacionDibujosTO result = null;
		Cliente cl = dibujosCombinados.get(0).getCliente(); //deberían ser lo mismo así q puedo setearlo en cada vuelta del for
		dibujoActual.setCliente(cl);
		try {
			result = removeInterno(dibujosCombinados, true, false, user);
		} catch (ValidacionExceptionSinRollback e) {
			e.printStackTrace(); //no va a pasar porque fuerza el remove
		}
		//grabo el dibujo en el primero de los REs involucrados
		DibujoEstampado dibSaved = save(dibujoActual, null, user);
		RemitoEntradaDibujo unREDibujo = result.findAny();
		unREDibujo.addItem(result.getImportePorDibujos(), dibSaved);
		reDibujoDAOLocal.save(unREDibujo);
		
		auditoriaFacade.auditar(user, "COMB DIB " + dibSaved.toString(), EnumTipoEvento.MODIFICACION, dibujoActual);
	}

	private void checkCombinarDibujos(List<DibujoEstampado> dibujosCombinados) throws ValidacionException {
		Set<Cliente> clientes = new HashSet<Cliente>();
		for(DibujoEstampado de : dibujosCombinados) {
			if(de.getEstado() != EEstadoDibujo.EN_STOCK) {
				throw new ValidacionException(EValidacionException.DIBUJO_IMPOSIBLE_COMBINAR.getInfoValidacion());
			}
			if(de.getCliente() == null) {
				throw new ValidacionException(EValidacionException.DIBUJO_IMPOSIBLE_COMBINAR.getInfoValidacion());
			}
			clientes.add(de.getCliente());
		}
		if(clientes.size() != 1) {
			throw new ValidacionException(EValidacionException.DIBUJO_IMPOSIBLE_COMBINAR.getInfoValidacion());
		}
	}

	@Override
	public DibujoEstampado actualizarEstado(DibujoEstampado dibujo, EEstadoDibujo estadoUntil, String user) {
		EEstadoDibujo estadoOrig = dibujo.getEstado();
		if(!CambioEstadoDibujoHelper.getInstance().cambioEstadoValido(estadoOrig, estadoUntil)) {
			throw new IllegalArgumentException("No se puede cambiar el estado del dibujo de " + estadoOrig + " a " + estadoUntil);
		}
		dibujo.setEstado(estadoUntil);
		DibujoEstampado dibujoSaved = dibujoEstampadoDAOLocal.save(dibujo);
		auditoriaFacade.auditar(user, "CAMB ESTADO DIB " + dibujo.toString() + ", de " + estadoOrig + " a " + estadoUntil , EnumTipoEvento.MODIFICACION, dibujo);
		return dibujoSaved;
	}

}