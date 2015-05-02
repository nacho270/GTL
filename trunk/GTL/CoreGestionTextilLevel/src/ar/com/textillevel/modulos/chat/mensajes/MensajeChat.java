package ar.com.textillevel.modulos.chat.mensajes;

import java.io.Serializable;

public class MensajeChat implements Serializable {

	private static final long serialVersionUID = -2964964258003677736L;

	private ETipoMensajeChat tipoMensaje;
	private String nick;
	
	public MensajeChat(ETipoMensajeChat tipoMensaje, String nick) {
		super();
		this.tipoMensaje = tipoMensaje;
		this.nick = nick;
	}

	public ETipoMensajeChat getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(ETipoMensajeChat tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
}
