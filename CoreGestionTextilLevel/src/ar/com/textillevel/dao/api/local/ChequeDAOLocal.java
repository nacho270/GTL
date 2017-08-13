package ar.com.textillevel.dao.api.local;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EnumTipoFecha;

@Local
public interface ChequeDAOLocal extends DAOLocal<Cheque, Integer>{

	public List<Cheque> getChequesPorFechaYPaginado(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha);

	public List<Cheque> getChequesPorFechaYPaginado(String numeracionCheque, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRow, EnumTipoFecha tipoFechas);

	public Integer getCantidadDeCheques(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha);

	public Cheque getChequeByNumero(String nroCheque);

	public Cheque getChequeByNumeroCuitYBanco(String nroCheque, String cuit, Banco banco);

	public Integer getUltimoNumeroInternoCheque(Character letra);
	
	public List<Cheque> getChequesByCliente(Integer idCliente, EEstadoCheque estadoCheque);

	public List<Cheque> obtenerChequesVencidos(Date sumarDias);

	public List<Cheque> getListaDeChequesProximosAVencer(Date tolerancia, Date vencimiento);

	public List<Cheque> getChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha);
	
	public Integer getCantidadDeChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha);
	
	public Integer getCantidadDeCheques(String numeracionCheque,  EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha);

	public Integer getCantidadDechequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha);

	public List<Cheque> getChequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer paginaActual, Integer maxRows);

	/* PARA BUSCAR CHEQUES EN LA ORDEN DE PAGO */
	public List<Cheque> getChequesPorNumeracionNumeroFechaEImporte(List<NumeracionCheque> numerosInternos, String nroCheque, Date fechaDesde, Date fechaHasta, BigDecimal importeDesde, BigDecimal importeHasta, List<Cheque> excluidos);

	/* PARA SUGERIS CHEQUES EN ORDEN DE PAGO */
	public List<Cheque> getChequeSugeridos(BigDecimal montoHasta, Integer diasDesde, Integer diasHasta, Date fechaPromedio, List<Cheque> chequesDescartados) ;

	/* BUSQUEDA EN LA ORDEN DE DEPOSITO */
	public List<Cheque> getChequesByNrosInternos(List<NumeracionCheque> numerosInternos, List<Integer> idsUtilizados);

	/* Metodos de validacion para la eliminacion */
	public boolean chequeSeUsaEnRecibo(Cheque c);
	public boolean chequeSeUsaEnOrdenDePago(Cheque c);
	public boolean chequeSeUsaEnOrdenDePagoPersona(Cheque c);
	/* Fin metodos de validacio para la eliminacion */

	/* PARA MOBILE */
	public Cheque getChequeByNumeracion(String letraCheque, Integer nroCheque);
}
