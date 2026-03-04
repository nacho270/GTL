package ar.com.textillevel.gui.util;

import java.io.File;
import java.io.FileNotFoundException;
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

	public static void enviarCotizacionPorEmail(String asunto, String cuerpoMail, List<File> files, List<String> to, List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws AddressException, MessagingException {
		File file = files.get(0);
		GenericUtils.enviarEmailAndPersistContacts(asunto,cuerpoMail, Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

	public static void enviarDocumentoContablePorEmail(String asunto, String cuerpoMail, File file, List<String> to, List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws AddressException, MessagingException {
		GenericUtils.enviarEmailAndPersistContacts(asunto,cuerpoMail, Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

	public static void enviarRemitoPorEmail(String asunto, String cuerpoMail, List<File> files, List<String> to, List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws AddressException, MessagingException {
		GenericUtils.enviarEmailAndPersistContacts(asunto, cuerpoMail, files, to, cc, idEntidad, tipoEntidad);
		for (File file : files) {
			file.delete();
		}
	}

	public static void enviarResumenCuentaPorEmail(File file, String subject, String cuerpoMail, List<String> to, List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws AddressException, MessagingException {
		GenericUtils.enviarEmailAndPersistContacts(subject, cuerpoMail, Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

	public static void enviarReciboPorEmail(String asunto, String cuerpoMail, File file, List<String> to,
			List<String> cc, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) throws AddressException, MessagingException {
		GenericUtils.enviarEmailAndPersistContacts(asunto, cuerpoMail, Collections.singletonList(file), to, cc, idEntidad, tipoEntidad);
		file.delete();
	}

}
