package ar.com.textillevel.gui.acciones;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class ImprimirListadoChequesHandler {

	private static final String ARCHIVO_JASPER_CHEQUES_VENCIDOS = "/ar/com/textillevel/reportes/listado_cheques_vencidos.jasper";
	private static final String ARCHIVO_JASPER_CHEQUES_POR_VENCER = "/ar/com/textillevel/reportes/listado_cheques_por_vencer.jasper";

	public static void imprimirListadoChequesVencidos(final List<Cheque> cheques, final Integer diasVencimiento) throws JRException {
		internalImprimir(ARCHIVO_JASPER_CHEQUES_VENCIDOS, cheques, diasVencimiento);
	}
	
	public static void imprimirChequesPorVencer(final List<Cheque> cheques, final Integer diasVencimiento) throws JRException {
		internalImprimir(ARCHIVO_JASPER_CHEQUES_POR_VENCER, cheques, diasVencimiento);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void internalImprimir(final String archivoReporte, final List<Cheque> cheques, final Integer diasVencimiento) throws JRException {
		List<ChequeImpresionTO> chequesTO = FluentIterable.from(cheques).transform(new Function<Cheque, ChequeImpresionTO>() {
			@Override
			public ChequeImpresionTO apply(Cheque cheque) {
				ChequeImpresionTO cito = new ChequeImpresionTO();
				cito.setBanco(cheque.getBanco().getNombre());
				cito.setNumero(cheque.getNumeracion().toString());
				cito.setCliente(cheque.getCliente().getRazonSocial());
				cito.setImporte(GenericUtils.getDecimalFormat().format(cheque.getImporte().floatValue()));
				cito.setFechaDeposito(DateUtil.dateToString(cheque.getFechaDeposito(), DateUtil.SHORT_DATE));
				cito.setFechaVencimiento(DateUtil.dateToString(DateUtil.sumarDias(cheque.getFechaDeposito(), diasVencimiento), DateUtil.SHORT_DATE));
				return cito;
			}
		}).toList();
		Map params = new HashMap();
		params.put("FECHA", DateUtil.dateToString(DateUtil.getHoy(), DateUtil.SHORT_DATE));
		JasperReport reporte = JasperHelper.loadReporte(archivoReporte);
		JasperPrint jasperPrint = JasperHelper.fillReport(reporte, params, chequesTO);
		JasperHelper.imprimirReporte(jasperPrint, true, true, 1);
	}

	public static class ChequeImpresionTO implements Serializable {

		private static final long serialVersionUID = -1831123745882924119L;

		private String numero;
		private String cliente;
		private String importe;
		private String banco;
		private String fechaDeposito;
		private String fechaVencimiento;
		private String diasFaltantes;

		public String getNumero() {
			return numero;
		}

		public void setNumero(String numero) {
			this.numero = numero;
		}

		public String getCliente() {
			return cliente;
		}

		public void setCliente(String cliente) {
			this.cliente = cliente;
		}

		public String getImporte() {
			return importe;
		}

		public void setImporte(String importe) {
			this.importe = importe;
		}

		public String getBanco() {
			return banco;
		}

		public void setBanco(String banco) {
			this.banco = banco;
		}

		public String getFechaDeposito() {
			return fechaDeposito;
		}

		public void setFechaDeposito(String fechaDeposito) {
			this.fechaDeposito = fechaDeposito;
		}

		public String getFechaVencimiento() {
			return fechaVencimiento;
		}

		public void setFechaVencimiento(String fechaVencimiento) {
			this.fechaVencimiento = fechaVencimiento;
		}

		public String getDiasFaltantes() {
			return diasFaltantes;
		}

		public void setDiasFaltantes(String diasFaltantes) {
			this.diasFaltantes = diasFaltantes;
		}
	}
}
