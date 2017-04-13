package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
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
	
	public void registrarAvanceODT(Integer idOdt, EAvanceODT avance, boolean oficina, UsuarioSistema usuarioSistema);
	
	public void registrarTransicionODT(Integer idOdt, Maquina maquina, UsuarioSistema usuarioSistema);

	public void detenerODT(Integer idOdt, UsuarioSistema usuarioSistema);

	public List<TransicionODT> getHistoricoTransiciones(Integer idODT);

	public CambioAvance actualizarObservacionesCambioAvance(Integer idCambioAvance, String observaciones);

	public void bajarODT(ODTTO odt);

	public void subirODT(ODTTO odt);

	public OrdenDeTrabajo grabarODT(OrdenDeTrabajo odt, UsuarioSistema usuario);

	public void borrarSecuencia(OrdenDeTrabajo ordenDeTrabajo, UsuarioSistema usuarioSistema) throws ValidacionException;

	public OrdenDeTrabajo grabarODTYDescontarStock(OrdenDeTrabajo odt, Set<InfoBajaStock> infoStock, UsuarioSistema usuarioSistema);
	public OrdenDeTrabajo actualizarODTYStock(OrdenDeTrabajo odt, Set<InfoBajaStock> infoStock, UsuarioSistema usuarioSistema);
	
	public OrdenDeTrabajo getODTEagerByCodigo(String codigo);
	public OrdenDeTrabajo grabarAndRegistrarCambioEstadoAndAvance(OrdenDeTrabajo odt, EEstadoODT estado, EAvanceODT avance, UsuarioSistema usuarioSistema);

	public PiezaODT getPiezaODTByCodigo(String codPiezaODT);
	
	public void grabarAndRegistrarAvanceEnEstadoEnProceso(Integer idODT, ESectorMaquina sector, Terminal terminal);
	
}
