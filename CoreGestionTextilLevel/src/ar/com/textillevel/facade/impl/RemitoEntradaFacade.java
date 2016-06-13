package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.dao.api.local.ArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.PiezaRemitoDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoSalidaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.PiezaRemitoEntradaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CuentaArticuloFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoEntradaFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoEntradaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.modulos.odt.dao.api.local.OrdenDeTrabajoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

@Stateless
public class RemitoEntradaFacade implements RemitoEntradaFacadeRemote, RemitoEntradaFacadeLocal {

	@EJB
	private RemitoEntradaDAOLocal remitoEntradaDAO;

	@EJB
	private RemitoEntradaProveedorDAOLocal remitoEntradaProveedorDAO;

	@EJB
	private OrdenDeTrabajoDAOLocal odtDAO;

	@EJB
	private RemitoSalidaDAOLocal remitoSalidaDAO;

	@EJB
	private ArticuloDAOLocal articuloDAO;

	@EJB
	private PiezaRemitoDAOLocal piezaRemitoDAO;
	
	@EJB
	private CuentaArticuloFacadeLocal cuentaArticuloFacade;

	@EJB
	private RemitoEntradaProveedorFacadeLocal remitoEntradaProveedorFacade;
	
	@EJB
	private AuditoriaFacadeLocal<RemitoEntrada> auditoriaFacade;

	public RemitoEntrada save(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, String usuario) {
		boolean isAlta = remitoEntrada.getId() == null;
		remitoEntrada = remitoEntradaDAO.save(remitoEntrada);

		//Borro las odts que estaban asociadas y ahora no están (solo en caso de modificación se borran)
		List<OrdenDeTrabajo> odtsYaAsociadas = odtDAO.getODTAsociadas(remitoEntrada.getId());
		for(OrdenDeTrabajo odt : odtsYaAsociadas) {
			if(!odtList.contains(odt)) {
				odtDAO.removeById(odt.getId());
			}
		}

		//Grabo las nuevas o las odts que existian y quedaron luego de la modificación
		for(OrdenDeTrabajo odt : odtList) {
			odt.setRemito(remitoEntrada);
			for(PiezaODT podt : odt.getPiezas()) {
				podt.setPiezaRemito(getPiezaRemito(remitoEntrada.getPiezas(), podt.getPiezaRemito().getOrdenPieza()));
			}
			odt.setFechaODT(new Timestamp(remitoEntrada.getFechaEmision().getTime()));
			odt = odtDAO.save(odt);
		}

		if(isAlta) {
			auditoriaFacade.auditar(usuario, "Creacion de remito de entrada Nº: " + remitoEntrada.getNroRemito(), EnumTipoEvento.ALTA,remitoEntrada);
		} else {
			auditoriaFacade.auditar(usuario, "Modificación de remito de entrada Nº: " + remitoEntrada.getNroRemito(), EnumTipoEvento.MODIFICACION,remitoEntrada);
		}
		return remitoEntrada;
	}

	private PiezaRemito getPiezaRemito(List<PiezaRemito> piezas, Integer ordenPieza) {
		for(PiezaRemito pr : piezas) {
			if(ordenPieza.equals(pr.getOrdenPieza())) {
				return pr;
			}
		}
		return null;
	}

	public boolean existsNroRemitoByCliente(Integer idCliente, Integer nroRemito) {
		return remitoEntradaDAO.existsNroRemitoByCliente(idCliente, nroRemito);
	}

	public RemitoEntrada getByIdEager(Integer idRemito) {
		return remitoEntradaDAO.getByIdEager(idRemito);
	}

	public List<RemitoEntrada> getRemitoEntradaByClienteList(Integer idCliente) {
		return remitoEntradaDAO.getRemitoEntradaByClienteList(idCliente);
	}

	public RemitoEntrada getByNroRemitoEager(Integer nroCliente, Integer nroRemito) {
		return remitoEntradaDAO.getByNroRemitoEager(nroCliente, nroRemito);
	}

	public void eliminarRemitoEntrada(Integer idRE) throws ValidacionException {
		RemitoEntrada re = remitoEntradaDAO.getByIdEager(idRE);
		List<OrdenDeTrabajo> odts = odtDAO.getODTAsociadas(re.getId());
		if(!odts.isEmpty()) {
			checkEliminacionRemitoEntrada(re.getId(), odts);
		}
		for(OrdenDeTrabajo odt : odts) {
			odtDAO.removeById(odt.getId());
		}
		remitoEntradaDAO.removeById(re.getId());
	}

	public void eliminarRemitoEntrada01OrCompraDeTela(Integer idRemitoEntrada, String usrName) throws ValidacionException {
		RemitoEntrada remitoEntrada = remitoEntradaDAO.getById(idRemitoEntrada);
		if(remitoEntrada.getArticuloStock() == null && remitoEntrada.getPrecioMatPrima() == null) {
			throw new ValidacionException(EValidacionException.REMITO_ENTRADA_NO_ES_01_NI_COMPRA_TELA.getInfoValidacion());
		}
		undoRemitoEntrada01OrCompraTela(remitoEntrada.getId());
		//TODO: Falta eliminar las ODTs
		remitoEntradaDAO.removeById(remitoEntrada.getId());
	}

	private void undoRemitoEntrada01OrCompraTela(Integer idRE) throws ValidacionException {
		RemitoEntradaProveedor reProveedorGeneradoByRECompraTela = remitoEntradaProveedorDAO.getREProveedorByIdRECliente(idRE);
		if(reProveedorGeneradoByRECompraTela != null) { //Se trata de un remito de compra de tela => se intenta borrar el remito de entrada de proveedor generado  
			remitoEntradaProveedorFacade.eliminarRemitoEntrada(reProveedorGeneradoByRECompraTela.getId());
		}
		RemitoEntrada re = remitoEntradaDAO.getById(idRE);
		for(PiezaRemito pr : re.getPiezas()) {
			if(pr.getEnSalida() != null && pr.getEnSalida()) {
				throw new ValidacionException(EValidacionException.REMITO_ENTRADA_IMP_BORRAR_PIEZAS_EN_SALIDA.getInfoValidacion());
			}
		}
		cuentaArticuloFacade.borrarMovimientosCuentaArticulo(re);
	}

	public void checkEliminacionRemitoEntrada(Integer idRECliente, List<OrdenDeTrabajo> odts) throws ValidacionException {
		List<RemitoSalida> remitoSalida = new ArrayList<RemitoSalida>();
		for(OrdenDeTrabajo odt : odts) {
			remitoSalida.addAll(remitoSalidaDAO.getRemitosByODT(odt));
		}
		if(!remitoSalida.isEmpty()) {
			throw new ValidacionException(EValidacionException.REMITO_ENTRADA_IMPOSIBLE_BORRAR_O_EDITAR.getInfoValidacion(), new String[] { extractInfoRemitoSalida(remitoSalida) });
		}
		RemitoEntradaProveedor reProveedor = remitoEntradaProveedorDAO.getREProveedorByIdRECliente(idRECliente);
		if(reProveedor != null) {
			throw new ValidacionException(EValidacionException.REMITO_ENTRADA_IMPOSIBLE_BORRAR_O_EDITAR_EXISTE_RE_PROV.getInfoValidacion(), new String[] { reProveedor.getNroRemito(), DateUtil.dateToString(reProveedor.getFechaEmision()) });
		}
	}

	private String extractInfoRemitoSalida(List<RemitoSalida> remitoSalida) {
		List<String> infoList = new ArrayList<String>();
		for(RemitoSalida rs : remitoSalida) {
			infoList.add(rs.getNroRemito().toString());
		}
		return StringUtil.getCadena(infoList, ", ");
	}

	public List<RemitoEntrada> getRemitoEntradaByFechasAndCliente(Date fechaDesde, Date fechaHasta, Integer idCliente) {
		return remitoEntradaDAO.getRemitoEntradaByFechasAndCliente(fechaDesde, fechaHasta, idCliente);
	}

	public List<RemitoEntrada> getRemitoEntradaConPiezasNoAsociadasList() {
		return remitoEntradaDAO.getRemitoEntradaConPiezasNoAsociadasList();
	}

	public RemitoEntrada ingresarRemitoEntrada01(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, String usuario) throws ValidacionException {
		if(remitoEntrada.getId() != null) {
			undoRemitoEntrada01OrCompraTela(remitoEntrada.getId());
		}
		
		remitoEntrada = remitoEntradaDAO.save(remitoEntrada);

		//Borro las odts que estaban asociadas y ahora no están (solo en caso de modificación se borran)
		List<OrdenDeTrabajo> odtsYaAsociadas = odtDAO.getODTAsociadas(remitoEntrada.getId());
		for(OrdenDeTrabajo odt : odtsYaAsociadas) {
			if(!odtList.contains(odt)) {
				odtDAO.removeById(odt.getId());
			}
		}

		//Grabo las nuevas o las odts que existian y quedaron luego de la modificación
		for(OrdenDeTrabajo odt : odtList) {
			odt.setRemito(remitoEntrada);
			for(PiezaODT podt : odt.getPiezas()) {
				podt.setPiezaRemito(getPiezaRemito(remitoEntrada.getPiezas(), podt.getPiezaRemito().getOrdenPieza()));
			}
			odt.setFechaODT(new Timestamp(remitoEntrada.getFechaEmision().getTime()));
			odt = odtDAO.save(odt);
		}

		//Modifico la cuenta del cliente/tela agregando tela
		cuentaArticuloFacade.crearMovimientoHaber(remitoEntrada);
		auditoriaFacade.auditar(usuario, "Creacion de remito de entrada 01 Nº: " + remitoEntrada.getNroRemito(), EnumTipoEvento.ALTA,remitoEntrada);
		
		return remitoEntrada;
	}

	public List<RemitoEntrada> getRemitoEntradaConPiezasSinODTByCliente(Integer idCliente) {
		return remitoEntradaDAO.getRemitoEntradaConPiezasSinODTByCliente(idCliente);
	}

	public RemitoEntrada completarPiezasRemitoEntrada(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtCapturedList, String usrName) {
		Set<ProductoArticulo> productoArtSet = new HashSet<ProductoArticulo>();
		RemitoEntrada rePersisted = remitoEntradaDAO.getById(remitoEntrada.getId());
		productoArtSet.addAll(rePersisted.getProductoArticuloList());
		//Grabo las piezas modificadas
		for(PiezaRemito pr : rePersisted.getPiezas()) {
			int indexOf = remitoEntrada.getPiezas().indexOf(pr);
			if(indexOf != -1) {
				PiezaRemito piezaRemito = remitoEntrada.getPiezas().get(indexOf);
				pr.setObservaciones(piezaRemito.getObservaciones());
				pr.setPiezaSinODT(false);
			}
		}
		rePersisted.setPesoTotal(rePersisted.getPesoTotal().add(remitoEntrada.getPesoTotal()));
		for(OrdenDeTrabajo odt : odtCapturedList) {
			odt.setRemito(rePersisted);
			odtDAO.save(odt);
			productoArtSet.add(odt.getProductoArticulo());
		}
		rePersisted.getProductoArticuloList().clear();
		rePersisted.getProductoArticuloList().addAll(productoArtSet);
		auditoriaFacade.auditar(usrName, "Se completaron piezas del remito de entrada Nº: " + remitoEntrada.getNroRemito(), EnumTipoEvento.MODIFICACION, remitoEntrada);
		return remitoEntradaDAO.save(rePersisted);
	}

	public List<RemitoEntrada> getByNroRemito(Integer nroRemito) {
		return remitoEntradaDAO.getByNroRemito(nroRemito);
	}

	public RemitoEntradaProveedor ingresarRemitoEntradaPorCompraTela(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, String usuario) throws ValidacionException {
		if(remitoEntrada.getId() != null) {
			undoRemitoEntrada01OrCompraTela(remitoEntrada.getId());
		}
		
		remitoEntrada = remitoEntradaDAO.save(remitoEntrada);

		//Borro las odts que estaban asociadas y ahora no están (solo en caso de modificación se borran)
		List<OrdenDeTrabajo> odtsYaAsociadas = odtDAO.getODTAsociadas(remitoEntrada.getId());
		for(OrdenDeTrabajo odt : odtsYaAsociadas) {
			if(!odtList.contains(odt)) {
				odtDAO.removeById(odt.getId());
			}
		}

		//Grabo las nuevas o las odts que existian y quedaron luego de la modificación
		for(OrdenDeTrabajo odt : odtList) {
			odt.setRemito(remitoEntrada);
			for(PiezaODT podt : odt.getPiezas()) {
				podt.setPiezaRemito(getPiezaRemito(remitoEntrada.getPiezas(), podt.getPiezaRemito().getOrdenPieza()));
			}
			odt.setFechaODT(new Timestamp(remitoEntrada.getFechaEmision().getTime()));
			odt = odtDAO.save(odt);
		}

		//Genero un remito de entrada de proveedor que finalmente provoca la entrada de stock
		RemitoEntradaProveedor rep = generarRemitoEntradaProveedor(remitoEntrada);
		auditoriaFacade.auditar(usuario, "Creacion de remito de entrada (compra de tela) Nº: " + remitoEntrada.getNroRemito(), EnumTipoEvento.ALTA,remitoEntrada);

		return rep;
	}

	private RemitoEntradaProveedor generarRemitoEntradaProveedor(RemitoEntrada remitoEntrada) {
		RemitoEntradaProveedor rep = new RemitoEntradaProveedor();
		rep.setFechaEmision(remitoEntrada.getFechaEmision());
		rep.setNroRemito(remitoEntrada.getNroRemito().toString());
		rep.setProveedor(remitoEntrada.getProveedor());
		
		PiezaRemitoEntradaProveedor unicaPieza = new PiezaRemitoEntradaProveedor();
		unicaPieza.setCantContenedor(new BigDecimal(0));
		unicaPieza.setPrecioMateriaPrima(remitoEntrada.getPrecioMatPrima());
		unicaPieza.setCantidad(remitoEntrada.getTotalMetros());
		rep.getPiezas().add(unicaPieza);

		rep.setRemitoEntrada(remitoEntrada);
		
		try {
			rep = remitoEntradaProveedorFacade.save(rep);
		} catch (FWException e) {
			e.printStackTrace();
		}

		return rep;
	}

	public List<RemitoEntrada> getRemitoEntradaConPiezasParaVender() {
		return remitoEntradaDAO.getRemitoEntradaConPiezasParaVender();
	}

	public Articulo getArticuloByPiezaSalidaCruda(Integer idPiezaRemitoSalidaCruda) {
		Integer idArticulo = remitoEntradaDAO.getArticuloByPiezaSalidaCruda(idPiezaRemitoSalidaCruda);
		if(idArticulo == null) {
			return null;
		} else {
			return articuloDAO.getById(idArticulo);
		}
		
	}

	public PiezaRemito getPiezaRemitoById(Integer idPiezaRemito) {
		return piezaRemitoDAO.getById(idPiezaRemito);
	}

	public RemitoEntrada getByIdPiezaRemitoEntradaEager(Integer idPiezaRemito) {
		return remitoEntradaDAO.getByIdPiezaRemitoEntradaEager(idPiezaRemito);
	}

	@Override
	public List<RemitoEntrada> getRemitosEntradaSinFactura() {
		return remitoEntradaDAO.getRemitosEntradaSinFactura();
	}

}