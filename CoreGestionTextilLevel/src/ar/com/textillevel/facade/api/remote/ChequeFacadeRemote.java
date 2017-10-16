package ar.com.textillevel.facade.api.remote;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.cheque.to.OperacionSobreChequeTO;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EnumTipoFecha;

@Remote
public interface ChequeFacadeRemote {

	/* BUSQUEDAS DE CHEQUES */
	
	public List<Cheque> getChequesPorFechaYPaginado(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta);
	public Integer getCantidadDeCheques(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta);

	public List<Cheque> getChequesPorFechaYPaginado(String numeracionCheque, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta);
	public Integer getCantidadDeCheques(String numeracionCheque,  EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta);

	public List<Cheque> getChequesPorNumeroDeCheque(String numeroCheque,  EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta );
	public Integer getCantidadDechequesPorNumeroDeCheque(String numeroCheque,  EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta );
	
	public List<Cheque> getChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta);
	public Integer getCantidadDeChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta);
	
	/* FIN BUSQUEDAS */
	
	public Cheque grabarCheque(Cheque cheque, String usuario) throws FWException;
	public Cheque getChequeByNumeroCuitYBanco(String nroCheque, String cuit, Banco banco);
	public Integer getUltimoNumeroInternoCheque(Character letra);
	public List<Cheque> getChequesByCliente(Integer idCliente, EEstadoCheque estadoCheque);
	public CorreccionFactura rechazarCheque(Cheque cheque, Date fecha, String motivoRechazo, BigDecimal gastos, String usuario, boolean debeDiscriminarIVA) throws ValidacionException, ValidacionExceptionSinRollback;
	public void eliminarCheque(Integer id, String usuario) throws ValidacionException;
	public List<Cheque> obtenerChequesVencidos(Integer toleraciaDias);
	public List<Cheque> getListaDeChequesProximosAVencer(Integer diasAntes, Integer diasEnQueVencen);

	/* PARA BUSCAR CHEQUES EN LA ORDEN DE PAGO */
	public List<Cheque> getChequesPorNumeracionNumeroFechaEImporte(List<NumeracionCheque> numerosInternos, String nroCheque, Date fechaDesde, Date fechaHasta, BigDecimal importeDesde, BigDecimal importeHasta, List<Cheque> excluidos);
	
	/* PARA SUGERENCIA DE CHEQUES EN ORDEN DE PAGO */
	public List<Cheque> getChequeSugeridos(BigDecimal montoHasta, Integer diasDesde, Integer diasHasta, Date fechaPromedio, List<Cheque> chequesDescartados);
	
	/* PARA BUSCAR CHEQUES EN LA ORDEN DE DEPOSITO */
	public List<Cheque> getChequesByNrosInternos(List<NumeracionCheque> numerosInternos, List<Integer> idsUtilizados);
	
	/* BORRA LOS CHEQUES PASADO COMO PARAMETROS */
	public void eliminarCheques(List<Cheque> cheques, String usuario) throws ValidacionException;

	public List<OperacionSobreChequeTO> getOperacionSobreChequeTOList(Cheque ch);

}
