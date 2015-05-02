package ar.com.textillevel.facade.api.remote;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.enums.ETipoInformeProduccion;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.remitosalida.RemitoSalidaConBajaStockTO;

@Remote
public interface RemitoSalidaFacadeRemote {

	public RemitoSalida save(RemitoSalida remitoSalida, String usuario);
	
	public Integer getLastNroRemito();
	
	public RemitoSalida getById(Integer id);
	
	public RemitoSalida getByIdConPiezasYProductos(Integer id);
	
	public RemitoSalida getByNroRemitoConPiezasYProductos(Integer nroRemito);

	public RemitoSalida getByNroRemitoConPiezasYProductosAnulado(Integer nroRemito);
	
	public List<RemitoSalida> getRemitosByNroRemitoConPiezasYProductos(Integer nroRemito);

	public Integer getUltimoNumeroFactura(EPosicionIVA posIva);

	public void eliminarRemitoSalida(Integer idRemitoSalida, String usrName) throws ValidacionException;
	
	public void eliminarRemitoSalida01OrVentaTela(Integer idRemitoSalida, String usrName) throws ValidacionException;
	
	public void checkEliminacionOrAnulacionRemitoSalida(Integer idRemitoSalida) throws ValidacionException;

	public RemitoSalida ingresarRemitoSalidaProveedor(RemitoSalida remitoSalida, List<FacturaProveedor> facturasParaGenerarNC, String user) throws ValidacionException;

	public Map<Date, List<Map<String, BigDecimal>>> getInformeProduccion(Date fechaDesde, Date fechaHasta, Cliente cliente, ETipoInformeProduccion tipoInforme);

	public List<RemitoSalida> getRemitoSalidaByFechasAndCliente(Date fechaDesde,Date fechaHasta, Integer idCliente);

	public List<RemitoSalida> getRemitoSalidaByFechasAndProveedor(Date fechaDesde,Date fechaHasta, Integer idProveedor);

	public RemitoSalida ingresarRemitoSalidaPorSalida01(RemitoSalidaConBajaStockTO remitoSalidaTO) throws ValidacionException;

	public RemitoSalida ingresarRemitoSalidaPorVentaDeTela(RemitoSalidaConBajaStockTO remitoSalidaTO) throws ValidacionException;

	public void anularRemitoSalida(RemitoSalida remitoSalida);

	public List<RemitoSalida> save(List<RemitoSalida> remitosSalida, String usrName);

	public List<RemitoSalida> getByIdsConPiezasYProductos(List<Integer> ids);

	public List<RemitoSalida> getRemitosSalidaSinFacturaPorCliente(Cliente cliente);

}