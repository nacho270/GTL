package ar.com.textillevel.modulos.chat.mensajes;

import java.util.List;

public class MensajeUsuarios extends MensajeChat{

	private static final long serialVersionUID = -6922406655964541403L;

	private List<String> usuarios;
	
	public MensajeUsuarios(ETipoMensajeChat tipoMensaje, String nick, List<String> usuarios) {
		super(tipoMensaje, nick);
		this.usuarios = usuarios;
	}
	
	public List<String> getUsuarios() {
		return usuarios;
	}
	
	public void setUsuarios(List<String> usuarios) {
		this.usuarios = usuarios;
	}
}
