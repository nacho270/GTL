package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

@Remote
public interface RemitoEntradaFacadeRemote {

	public RemitoEntrada save(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, String usuario);

	public boolean existsNroRemitoByCliente(Integer idCliente, Integer nroRemito);
	
	public List<RemitoEntrada> getRemitoEntradaByClienteList(Integer idCliente);
	
	public RemitoEntrada getByIdEager(Integer idRemito);

	public RemitoEntrada getByIdPiezaRemitoEntradaEager(Integer idPiezaRemito);

	public RemitoEntrada getByNroRemitoEager(Integer nroCliente, Integer nroRemito);

	public void eliminarRemitoEntrada(Integer idRE) throws ValidacionException;

	public List<RemitoEntrada> getRemitoEntradaByFechasAndCliente(Date fechaDesde, Date fechaHasta, Integer idCliente);

	public void checkEliminacionRemitoEntrada(Integer idRECliente, List<OrdenDeTrabajo> odts) throws ValidacionException;

	public List<RemitoEntrada> getRemitoEntradaConPiezasNoAsociadasList();

	public RemitoEntrada ingresarRemitoEntrada01(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, String usuario) throws ValidacionException;

	public RemitoEntradaProveedor ingresarRemitoEntradaPorCompraTela(RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, String usuario) throws ValidacionException;

	public void eliminarRemitoEntrada01OrCompraDeTela(Integer idRemitoEntrada, String usrName) throws ValidacionException;
	
	public List<RemitoEntrada> getRemitoEntradaConPiezasSinODTByCliente(Integer idCliente);

	public RemitoEntrada completarPiezasRemitoEntrada(RemitoEntrada remitoEntrada,List<OrdenDeTrabajo> odtCapturedList, String usrName);

	public List<RemitoEntrada> getByNroRemito(Integer nroRemito);

	public List<RemitoEntrada> getRemitoEntradaConPiezasParaVender();
	
	public Articulo getArticuloByPiezaSalidaCruda(Integer idPiezaRemitoSalidaCruda);

	public PiezaRemito getPiezaRemitoById(Integer idPiezaRemito);

	public List<RemitoEntrada> getRemitosEntradaSinFactura();

}
