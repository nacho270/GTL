package ar.com.fwcommon.componentes.error.notificadores;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.com.fwcommon.boss.BossAplicacion;
import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.BossIdiomas;
import ar.com.fwcommon.boss.BossUsuarioVerificado;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.xml.Cuerpo;
import ar.com.fwcommon.componentes.error.xml.Propiedad;
import ar.com.fwcommon.componentes.error.xml.Tip;
import ar.com.fwcommon.entidades.Usuario;
import ar.com.fwcommon.mail.BossMail;
import ar.com.fwcommon.mail.DestinatarioRequest;
import ar.com.fwcommon.mail.MailRequest;
import ar.com.fwcommon.util.Configuracion;
import ar.com.fwcommon.util.NetworkUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.fwcommon.util.xstream.XStreamWrapper;

public class NotificadorErroresEmailWS implements INotificadorErrores {

	private static NotificadorErroresEmailWS instance;
	private String direccionNotificacion;
	private static final int LONG_SUBJECT = 200;

	private NotificadorErroresEmailWS() {
		String direccionNotificacion = Configuracion.getMailNotiError();
		if(direccionNotificacion == null) {
			FWException e = new FWException(
				BossError.ERR_APLICACION, 
				BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("mensaje_contexto_error_subsistema_gestion_errores"), 
				BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("mensaje_error_error_subsistema_gestion_errores"), 
				null, 
				new String[] { BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("tip_error_subsistema_gestion_errores") }
			);
			BossError.gestionarError(e);
			System.exit(-1);
		}
		this.direccionNotificacion = direccionNotificacion;
	}

	public static NotificadorErroresEmailWS getInstance() {
		if(instance == null) {
			instance = new NotificadorErroresEmailWS();
		}
		return instance;
	}

	public void notificarError(FWException e) {
		Collection<String> direcciones = new ArrayList<String>();
		direcciones.add(direccionNotificacion);
		List<DestinatarioRequest> destinatarios = BossMail.crearDestinatarios(null, null, direcciones);
		String subject = BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("subject_default_mensaje_error");
		if(e.getMensajeContexto() != null) {
			subject = e.getMensajeContexto();
		}
		if(subject.length() > LONG_SUBJECT) {
			subject = StringUtil.truncate(subject, LONG_SUBJECT);
		}
		String nombreUsuario = null;
		if(BossAplicacion.getIdAplicacion() != null) {
			Usuario usuario = BossUsuarioVerificado.getUsuarioVerificado();
			if(usuario != null && usuario.getNombre() != null) {
				nombreUsuario = usuario.getNombre();
			}
		}
		Cuerpo cuerpo = new Cuerpo();
		cuerpo.setUsuario(nombreUsuario);
		List<Propiedad> propiedades = new ArrayList<Propiedad>();
		Propiedad java_runtime_version = new Propiedad();
		java_runtime_version.setLlave("java.runtime.version");
		java_runtime_version.setValor(System.getProperty("java.runtime.version"));
		propiedades.add(java_runtime_version);
		Propiedad user_name = new Propiedad();
		user_name.setLlave("user.name");
		user_name.setValor(System.getProperty("user.name"));
		propiedades.add(user_name);
		Propiedad user_language = new Propiedad();
		user_language.setLlave("user.language");
		user_language.setValor(System.getProperty("user.language"));
		propiedades.add(user_language);
		Propiedad java_version = new Propiedad();
		java_version.setLlave("java.version");
		java_version.setValor(System.getProperty("java.version"));
		propiedades.add(java_version);
		Propiedad os_name = new Propiedad();
		os_name.setLlave("os.name");
		os_name.setValor(System.getProperty("os.name"));
		propiedades.add(os_name);
		Propiedad ip = new Propiedad();
		ip.setLlave("ip");
		try {
			ip.setValor(NetworkUtil.getIpLocalHost());
		} catch(FWException ex) {
			ex.printStackTrace();
		}
		propiedades.add(ip);
		cuerpo.setPropiedades(propiedades);
		int tipoError = e.getTipoError();
		String descripcionTipoError;
		if(tipoError == BossError.ERR_APLICACION) {
			descripcionTipoError = BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("descripcion_tipo_error_aplicacion") ; 
		} else if(tipoError == BossError.ERR_CONEXION) {
			descripcionTipoError = BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("descripcion_tipo_error_conexion") ;
		} else if(tipoError == BossError.ERR_INDETERMINADO) {
			descripcionTipoError = BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("descripcion_tipo_error_indeterminado") ;
		} else {
			descripcionTipoError = BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("descripcion_tipo_error_no_disponible") ;
		}
		cuerpo.setTipoError(descripcionTipoError);
		if(e.getMensajeError() != null) {
			cuerpo.setMensajeError(e.getMensajeError());
		}
		if(e.getLlamadaParametrizada() != null) {
			cuerpo.setLlamadaParametrizada(e.getLlamadaParametrizada());
		}
		if(e.getCause() != null) {
			StringWriter sw = new StringWriter();
			e.getCause().printStackTrace(new PrintWriter(sw));
			cuerpo.setCausa(sw.toString());
		}
		if(e.getTips() != null) {
			List<Tip> tips = new ArrayList<Tip>();
			for(int i = 0; i < e.getTips().length; i++) {
				Tip tip = new Tip();
				tip.setTip(e.getTips()[i]);
				tips.add(tip);
			}
			cuerpo.setTips(tips);
		}
		MailRequest mail = new MailRequest();
		String aplicacion = null;
		if(BossAplicacion.getAplicacion() == null) {
			aplicacion = BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("iniciales_no_disponible") ;
		} else {
			aplicacion = BossAplicacion.getAplicacion().getDescripcion();
		}
		mail.setAplicacion(aplicacion);
		mail.setAsunto(subject);
		XStreamWrapper xsw = new XStreamWrapper();
		mail.setCuerpo(xsw.serialize(cuerpo));
		mail.setDestinatarios(destinatarios);
		mail.setDireccionRespuesta(direccionNotificacion);
		mail.setOrigen(direccionNotificacion);
		mail.setUsuario(nombreUsuario == null ? "" : nombreUsuario);
		if(BossMail.enviarMail(mail) != BossMail.OK) {
			System.err.println(BossIdiomas.getInstance(BossIdiomas.FWCOMMON).getString("error_notificacion_error_via_mail"));
		}
	}

}