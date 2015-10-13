package ar.com.fwcommon.entidades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Transient;

import ar.com.fwcommon.entidades.enumeradores.EnumEstadoMail;
import ar.com.fwcommon.entidades.enumeradores.EnumTipoContenidoMail;

public class Mail implements Serializable {

	private static final long serialVersionUID = 1L;
	private int idMail;
	private String aplicacion;
	private String usuario;
	private String origen;
	private String asunto;
	private String cuerpo;
	// private EnumTipoMensaje tipoMensaje;
	private Integer idTipoContenido;
	private Integer reintentos;
	private Timestamp ultimoIntento;
	private Integer idAutomata;

	private String direccionRespuesta;
	private Timestamp fechaAlta;
	private Timestamp fechaEnvio;
	private int estado;// (ar.com.fwcommon.entidades.enumeradores.EnumEstadoMail)
	private List<Destinatario> destinatarios;
	private Integer idEnumMailDecoder;

	private static long unDiaMs = 24 * 60 * 60 * 1000;

	/** Método constructor */
	public Mail() {
		this.reintentos = 0;
		this.idTipoContenido = EnumTipoContenidoMail.TEXT_PLAIN.getId();
		this.ultimoIntento = new Timestamp(System.currentTimeMillis() - unDiaMs);
	}

	/**
	 * Devuelve el <b>id</b> del Mail.
	 * 
	 * @return idMail
	 */
	public int getIdMail() {
		return idMail;
	}

	/**
	 * Setea el <b>id</b> del Mail.
	 * 
	 * @param idMail
	 */
	public void setIdMail(int idMail) {
		this.idMail = idMail;
	}

	/**
	 * Devuelve la <b>aplicación</b> asociada.
	 * 
	 * @return aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}

	/**
	 * Setea la <b>aplicación</b> asociada.
	 * 
	 * @param aplicacion
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	/**
	 * Devuelve el <b>usuario</b> asociado.
	 * 
	 * @return usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Setea el <b>usuario</b> asociado.
	 * 
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Devuelve la dirección <b>origen</b> del Mail. (ie, el header from)
	 * 
	 * @return origen
	 */
	public String getOrigen() {
		return origen;
	}

	/**
	 * Devuelve la dirección <b>origen</b> del Mail.
	 * 
	 * @return rv
	 * @throws Exception
	 * @throws AddressException
	 */
	public InternetAddress getOrigenAsInternetAddress() throws Exception {
		if (origen == null) {
			throw new Exception("Dirección origen es NULL");
		}
		try {
			return new InternetAddress(origen);
		} catch (AddressException e) {
			throw new Exception("Dirección incorrecta (" + origen + ")", e);
		}
	}

	/**
	 * Setea la dirección de <b>origen</b> del Mail.
	 * 
	 * @param origen
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * Devuelve el <b>asunto</b> del Mail.
	 * 
	 * @return asunto
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * Setea el <b>asunto</b> del Mail.
	 * 
	 * @param asunto
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * Devuelve el <b>cuerpo</b> del Mail.
	 * 
	 * @return cuerpo
	 */
	public String getCuerpo() {
		return cuerpo;
	}

	/**
	 * Setea el <b>cuerpo</b> del Mail.
	 * 
	 * @param cuerpo
	 */
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	/**
	 * Devuelve la <b>dirección de respuesta</b> del Mail.
	 * 
	 * @return direccionRespuesta
	 */
	public String getDireccionRespuesta() {
		return direccionRespuesta;
	}

	/**
	 * Devuelve la <b>dirección de respuesta</b> del Mail.
	 * 
	 * @return rv TODO: Modificar para aceptar varias direcciones de respuesta
	 */
	public InternetAddress[] getDireccionesRespuestaAsInternetAddress() {
		InternetAddress[] rv = new InternetAddress[1];
		try {
			rv[0] = new InternetAddress(direccionRespuesta);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return rv;
	}

	/**
	 * Setea la <b>dirección de respuesta</b> del Mail.
	 * 
	 * @param direccionRespuesta
	 */
	public void setDireccionRespuesta(String direccionRespuesta) {
		this.direccionRespuesta = direccionRespuesta;
	}

	/**
	 * Devuelve la <b>fecha de alta</b> del Mail en la BD.
	 * 
	 * @return fechaAlta
	 */
	public Timestamp getFechaAlta() {
		return fechaAlta;
	}

	/**
	 * Setea la <b>fecha de alta</b> del Mail en la BD.
	 * 
	 * @param fechaAlta
	 */
	public void setFechaAlta(Timestamp fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	/**
	 * Devuelve la <b>fecha de envío</b> del Mail.
	 * 
	 * @return fechaEnvio
	 */
	public Timestamp getFechaEnvio() {
		return fechaEnvio;
	}

	/**
	 * Setea la <b>fecha de envío</b> del Mail.
	 * 
	 * @param fechaEnvio
	 */
	public void setFechaEnvio(Timestamp fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	/**
	 * Devuelve el <b>estado</b> del Mail.
	 * (ar.com.fwcommon.entidades.enumeradores.EnumEstadoMail)
	 * 
	 * @return estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * Setea el <b>estado</b> del Mail.
	 * (ar.com.fwcommon.entidades.enumeradores.EnumEstadoMail)
	 * 
	 * @param estado
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}

	/**
	 * Devuelve la lista de <b>destinatarios</b> del Mail.
	 * 
	 * @return destinatarios
	 */
	public List<Destinatario> getDestinatarios() {
		return destinatarios;
	}

	/**
	 * Setea la lista de <b>destinatarios</b> del Mail.
	 * 
	 * @param destinatarios
	 */
	public void setDestinatarios(List<Destinatario> destinatarios) {
		this.destinatarios = destinatarios;
	}

	/**
	 * Devuelve un array de objetos InternetAddress con las direcciones de todos
	 * los destinatarios.
	 * 
	 * @param tipoDestinatario
	 * @return rv
	 * @throws AddressException
	 */
	public InternetAddress[] getDirecciones() throws Exception {
		return convertDestinatarios2Address(this.getDestinatarios());
	}

	/**
	 * Devuelve un array de objetos InternetAddress con las direcciones de todos
	 * los destinatarios.
	 * 
	 * @param tipoDestinatario
	 * @return rv
	 * @throws AddressException
	 */
	public InternetAddress[] getDireccionesValidas() throws Exception {
		return convertDestinatariosValidos2Address(this.getDestinatarios());
	}

	/**
	 * Devuelve una lista de <b>destinatarios</b> por tipo.
	 * 
	 * @param tipoDestinatario
	 * @return rv
	 */
	public List<Destinatario> getDestinatariosPorTipo(int tipoDestinatario) {
		List<Destinatario> rv = new ArrayList<Destinatario>();
		if (destinatarios != null) {
			for (Destinatario destinatario : destinatarios) {
				if (destinatario.getTipoDestinatario() == tipoDestinatario) {
					rv.add(destinatario);
				}
			}
		}
		return rv;
	}

	/**
	 * Devuelve un array de objetos InternetAddress con las direcciones de los
	 * destinatarios de un tipo.
	 * 
	 * @param tipoDestinatario
	 * @return rv
	 * @throws Exception
	 * @throws AddressException
	 */
	public InternetAddress[] getDireccionesPorTipo(int tipoDestinatario) throws Exception {
		List<Destinatario> destinatarios = getDestinatariosPorTipo(tipoDestinatario);
		InternetAddress[] rv = convertDestinatariosValidos2Address(destinatarios);
		return rv;
	}

	public InternetAddress[] getDireccionesValidasPorTipo(int tipoDestinatario) {
		List<Destinatario> destinatarios = getDestinatariosPorTipo(tipoDestinatario);
		InternetAddress[] rv = convertDestinatariosValidos2Address(destinatarios);
		return rv;
	}

	/*
	 * private InternetAddress[] convertDestinatarios2Address(List<Destinatario>
	 * destinatarios) { InternetAddress[] rv = null; if(destinatarios != null) {
	 * rv = new InternetAddress[destinatarios.size()]; int i = 0;
	 * for(Destinatario destinatario : destinatarios) { try { rv[i] = new
	 * InternetAddress(destinatario.getDireccionMail()); i++; } catch(Throwable
	 * e) { e.printStackTrace(); } } } return rv; }
	 */

	private InternetAddress[] convertDestinatarios2Address(List<Destinatario> destinatarios) throws Exception {
		List<InternetAddress> dirList = new ArrayList<InternetAddress>(destinatarios.size());
		if (destinatarios != null) {
			for (Destinatario destinatario : destinatarios) {
				try {
					dirList.add(new InternetAddress(destinatario.getDireccionMail()));
				} catch (AddressException e) {
					throw new Exception("Dirección incorrecta (" + destinatario.getDireccionMail() + ")", e);
				}
			}
		}
		InternetAddress[] rv = new InternetAddress[dirList.size()];
		return dirList.toArray(rv);
	}

	/**
	 * Lo mismo que convertDestinatarios2Address, pero cuando una direccion no
	 * es valida no tira una excepcion
	 * 
	 * @param destinatarios
	 * @return
	 */
	private InternetAddress[] convertDestinatariosValidos2Address(List<Destinatario> destinatarios) {
		List<InternetAddress> dirList = new ArrayList<InternetAddress>(destinatarios.size());
		if (destinatarios != null) {
			for (Destinatario destinatario : destinatarios) {
				try {
					dirList.add(new InternetAddress(destinatario.getDireccionMail()));
				} catch (AddressException e) {
					// throw new Exception ("Dirección incorrecta (" +
					// destinatario.getDireccionMail() + ")", e) ;
				}
			}
		}
		InternetAddress[] rv = new InternetAddress[dirList.size()];
		return dirList.toArray(rv);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("idMail: " + idMail);
		buffer.append("\n");
		buffer.append("aplicacion: " + aplicacion);
		buffer.append("\n");
		buffer.append("usuario: " + usuario);
		buffer.append("\n");
		buffer.append("origen: " + origen);
		buffer.append("\n");
		buffer.append("asunto: " + asunto);
		buffer.append("\n");
		buffer.append("cuerpo:\n\n" + cuerpo);
		buffer.append("\n\n");
		buffer.append("direccionRespuesta: " + direccionRespuesta);
		buffer.append("\n");
		buffer.append("fechaAlta: " + fechaAlta);
		buffer.append("\n");
		buffer.append("fechaEnvio: " + fechaEnvio);
		buffer.append("\n");
		buffer.append("estado: " + EnumEstadoMail.getDescripcion(estado));
		buffer.append("\n");
		buffer.append("destinatarios: " + destinatarios);
		return buffer.toString();
	}

	/**
	 * <p>
	 * Arma el cuerpo del mensaje de acuerdo a varias configuraciones internas.
	 * </p>
	 * <p>
	 * Por defecto solo haría:
	 * </p>
	 * <code>message.setText(this.getCuerpo())</code>
	 * <p>
	 * Pero haría otras cosas en mensajes con MIME "text/html",
	 * "mime/alternative", eventualmente imgs embebidas (sin implementar)
	 * </p>
	 * 
	 * @param newMail
	 *            : javax.mail.Message
	 * @throws MessagingException
	 */
	public void armarCuerpoMessage(Message message) throws MessagingException {
		this.getTipoContenido().armarCuerpo(message, this);
	}

	public EnumTipoContenidoMail getTipoContenido() {
		return EnumTipoContenidoMail.getById(getIdTipoContenido());
	}

	public void setTipoContenido(EnumTipoContenidoMail contentType) {
		this.idTipoContenido = contentType.getId();
	}

	public void setTipoMensaje(EnumTipoContenidoMail tipoMensaje) {
		// this.tipoMensaje = tipoMensaje;
		this.idTipoContenido = tipoMensaje.getId();
	}

	private Integer getIdTipoContenido() {
		if (idTipoContenido == null) {
			idTipoContenido = 1;// txt
		}
		return idTipoContenido;
	}

	@SuppressWarnings("unused")
	private void setIdTipoContenido(Integer idTipoMensaje) {
		this.idTipoContenido = idTipoMensaje;
	}

	public Integer getReintentos() {
		return reintentos;
	}

	public void setReintentos(Integer reintentos) {
		this.reintentos = reintentos;
	}

	public Timestamp getUltimoIntento() {
		return ultimoIntento;
	}

	public void setUltimoIntento(Timestamp ultimoIntento) {
		this.ultimoIntento = ultimoIntento;
	}

	public Integer getIdAutomata() {
		return idAutomata;
	}

	public void setIdAutomata(Integer idAplicacion) {
		this.idAutomata = idAplicacion;
	}

	@Transient
	public Integer getIdEnumMailDecoder() {
		return idEnumMailDecoder;
	}

	public void setIdEnumMailDecoder(Integer idEnumMailDecoder) {
		this.idEnumMailDecoder = idEnumMailDecoder;
	}

}