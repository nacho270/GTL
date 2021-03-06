package ar.com.textillevel.gui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;

public final class EmailSender {

	private EmailSender() {}

	public static void enviarCotizacionPorEmail(JasperPrint jasperPrintCotizacion, List<String> to, List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws JRException, FileNotFoundException, AddressException, MessagingException {
		File file = new File(System.getProperty("java.io.tmpdir") + "cotizacion.pdf");
		JasperHelper.exportarAPDF(jasperPrintCotizacion, file);
		GenericUtils.enviarEmailAndPersistContacts("Cotizaci�n",
				"<html><b>Estimado cliente:<br><br>" + 
				"En esta oportunidad, nos dirigimos a Ud. a fin de comunicarle los nuevos precios sobre nuestros servicios.<br><br>"+
				firma(), Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

	public static void enviarDocumentoContablePorEmail(ETipoDocumento tipoDocContable, Integer nroDoc, Integer nroRemito, JasperPrint jasperDocumento, List<String> to, List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws JRException, FileNotFoundException, AddressException, MessagingException {
		String nroRemitoStr = nroRemito == null ? "" : (nroRemito+"");
		boolean facturaB = GenericUtils.isSistemaTest() && tipoDocContable == ETipoDocumento.FACTURA;
		String strTipoDoc = facturaB ? "Importe RTO" : tipoDocContable.toString();
		String strDocumento = strTipoDoc.toLowerCase()
				.replaceAll("_", " de ")
				.replaceAll("debito", "d�bito")
				.replaceAll("credito", "cr�dito");
		File file = new File(System.getProperty("java.io.tmpdir") + strTipoDoc.toLowerCase() + "_" + (facturaB ? nroRemitoStr : nroDoc) + ".pdf");
		JasperHelper.exportarAPDF(jasperDocumento, file);
		String asunto = StringUtil.ponerMayuscula(strDocumento) + " N� " + (facturaB ? nroRemitoStr : nroDoc);
		GenericUtils.enviarEmailAndPersistContacts(asunto,
				"<html><b>Estimado cliente:<br><br>Por medio de la presente, adjuntamos " +
						(facturaB ? "importe correspondiente al remito  N� " + nroRemitoStr : "la " + asunto) + ".<br><br>" +
				firma(), Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

	public static void enviarRemitoPorEmail(Integer nroFactura, List<Integer> nrosRemito, List<JasperPrint> jaspersRemitos,
			List<String> to, List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws JRException, FileNotFoundException, AddressException, MessagingException {
		List<File> files = new ArrayList<File>();
		for(int i = 0; i < jaspersRemitos.size(); i++) {
			File file = new File(System.getProperty("java.io.tmpdir") + "remito_" + nrosRemito.get(i) + ".pdf");
			JasperHelper.exportarAPDF(jaspersRemitos.get(i), file);
			files.add(file);
		}
		String asunto = GenericUtils.isSistemaTest() ? "Remito N� " + nrosRemito.get(0) :
			"Remito/s N� " + StringUtil.getCadena(nrosRemito, ",");
		GenericUtils.enviarEmailAndPersistContacts(asunto,
				"<html><b>Estimado cliente:<br><br>" +
				"Por medio de la presente, " +
					(GenericUtils.isSistemaTest() ? "adjuntamos el remito N� " + nrosRemito.get(0) :
						"adjuntamos lo/s " + asunto + " correspondientes a la Factura N� " + nroFactura)
					+ ".<br><br>" +
				firma(), files, to, cc, idEntidad, tipoEntidad);
		for (File file : files) {
			file.delete();
		}
	}

	public static void enviarResumenCuentaPorEmail(JasperPrint jasperPrintCotizacion, List<String> to,
			List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws JRException, FileNotFoundException, AddressException, MessagingException {
		File file = new File(System.getProperty("java.io.tmpdir") + "resumen.pdf");
		JasperHelper.exportarAPDF(jasperPrintCotizacion, file);
		GenericUtils.enviarEmailAndPersistContacts("Resumen de cuenta al " + DateUtil.dateToString(DateUtil.getHoy(), DateUtil.SHORT_DATE),
				"<html><b>Estimado cliente:<br><br>" + 
				"Por medio de la presente, adjuntamos resumen de cuenta.<br><br>" +
				firma(), Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

	public static void enviarReciboPorEmail(Integer nroRecibo, JasperPrint jasper, List<String> to,
			List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws AddressException, MessagingException, FileNotFoundException, JRException {
		File file = new File(System.getProperty("java.io.tmpdir") + "recibo.pdf");
		JasperHelper.exportarAPDF(jasper, file);
		String asunto = "Recibo N� " + nroRecibo;
		GenericUtils.enviarEmailAndPersistContacts(asunto,
				"<html><b>Estimado cliente:<br><br>" + 
				"Por medio de la presente, adjuntamos el " + asunto + ".<br><br>" +
				firma(), Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

	private static String firma() {
		String firma = "Sin otro particular, lo saludamos muy atentamente.</b><br><br>";
		if (!GenericUtils.isSistemaTest()) {
			return firma +
			   "<img src=\"cid:AbcXyz123\"><br>" +
			   "<font style=\"font-size:12.8px;text-align:center\" color=\"#999999\">" + 
			   		"<b>Administraci&oacute;n Textil Level S.A<br>"
			   		+ "Tel: (+54) 11 4454-2395 / 2279<br>"
			   		+ "Av. San Martin 4215 Lomas Del Mirador</b>" +
			   "</font></html>";
		}
		return firma;
	}
}
