package ar.com.textillevel.modulos.odt.facade.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;

@Local
public interface OrdenDeTrabajoFacadeLocal {
	public void cambiarODTAFacturada(Integer idOdt, UsuarioSistema usuarioSistema);
	public void cambiarODTAOficina(Integer idOdt, UsuarioSistema usuarioSistema);
	public List<OrdenDeTrabajo> getOdtEagerByRemitoList(Integer idRE);

	//METODOS WEB SERVICE
	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaCompletoSinSalidaByClient(Integer idCliente);
	public List<OrdenDeTrabajo> getByIdsEager(List<Integer> ids);
	public List<OrdenDeTrabajo> getOrdenesDeTrabajo(EEstadoODT estado, Date fechaDesde, Date fechaHasta);

}
