package ar.com.textillevel.modulos.chat.mensajes;

public class MensajeMessageChat extends MensajeChat {

	private static final long serialVersionUID = 3935231469707464756L;

	private String nickDestino;
	private String msg;

	public MensajeMessageChat(ETipoMensajeChat tipoMensaje, String nick, String nickDestino, String msg) {
		super(tipoMensaje, nick);
		this.nickDestino = nickDestino;
		this.msg = msg;
	}

	public String getNickDestino() {
		return nickDestino;
	}

	public void setNickDestino(String nickDestino) {
		this.nickDestino = nickDestino;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
