package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.to.EstadoActualMaquinaTO;
import ar.com.textillevel.modulos.odt.to.EstadoGeneralODTsTO;
import ar.com.textillevel.modulos.odt.to.InfoAsignacionMaquinaTO;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.modulos.odt.to.stock.InfoBajaStock;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;

@Remote
public interface OrdenDeTrabajoFacadeRemote {

	public List<OrdenDeTrabajo> getOdtNoAsociadasByClient(Integer idCliente);

	public String getUltimoCodigoODT();

	public List<OrdenDeTrabajo> getOdtEagerByRemitoList(Integer idRemito);

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente); 

	public OrdenDeTrabajo getByIdEager(Integer idODT);

	public List<OrdenDeTrabajo> getByIdsEager(List<Integer> ids);
	
	public OrdenDeTrabajo getByCodigoEager(String codigo);
	
	public List<OrdenDeTrabajo> getOrdenesDeTrabajo(EEstadoODT estado, Date fechaDesde, Date fechaHasta);
	
	public List<EstadoActualMaquinaTO> getEstadoMaquinas(Integer idTipoMaquina, Date fechaDesde, Date fechaHasta, Cliente cliente);
	
	public EstadoGeneralODTsTO getEstadoDeProduccionActual(Date fechaDesde, Date fechaHasta, Cliente cliente);

	public void detenerODT(Integer idOdt, UsuarioSistema usuarioSistema);

	public List<TransicionODT> getHistoricoTransiciones(Integer idODT);

	public CambioAvance actualizarObservacionesCambioAvance(Integer idCambioAvance, String observaciones);

	public void bajarODT(ODTTO odt, ESectorMaquina sectorFrom) throws ValidacionException;

	public void subirODT(ODTTO odt, ESectorMaquina sectorFrom) throws ValidacionException;

	public OrdenDeTrabajo grabarODT(OrdenDeTrabajo odt, UsuarioSistema usuario);

	public void borrarSecuencia(OrdenDeTrabajo ordenDeTrabajo, UsuarioSistema usuarioSistema) throws ValidacionException;

	public OrdenDeTrabajo grabarODTYDescontarStock(OrdenDeTrabajo odt, Set<InfoBajaStock> infoStock, UsuarioSistema usuarioSistema);
	public OrdenDeTrabajo actualizarODTYStock(OrdenDeTrabajo odt, Set<InfoBajaStock> infoStock, UsuarioSistema usuarioSistema);
	
	public OrdenDeTrabajo getODTEagerByCodigo(String codigo);
	public OrdenDeTrabajo grabarAndRegistrarCambioEstadoAndAvance(OrdenDeTrabajo odt, EEstadoODT estado, EAvanceODT avance, Terminal terminal, UsuarioSistema usuarioSistema);

	public PiezaODT getPiezaODTByCodigo(String codPiezaODT);
	
	public void grabarAndRegistrarAvanceEnEstadoEnProceso(Integer idODT, Maquina maquina, ESectorMaquina sector, Terminal terminal, UsuarioSistema usuarioSistema);

	public InfoAsignacionMaquinaTO getMaquinaAndProximoOrdenBySector(ESectorMaquina sector);
	
	public List<OrdenDeTrabajo> getAllNoFinalizadasBySector(ESectorMaquina sector);

	public List<ODTTO> getAllODTTOByParams(Date fechaDesde, Date fechaHasta, Cliente cliente, Integer idTipoMaquina, Integer idProducto, EEstadoODT... estado);

	public OrdenDeTrabajo borrarPiezasSinSalida(OrdenDeTrabajo odt, UsuarioSistema usuarioSistema);

	public OrdenDeTrabajo asignarProductoArticuloODT(OrdenDeTrabajo odt, ProductoArticulo productoArticulo, UsuarioSistema usuarioSistema);

}