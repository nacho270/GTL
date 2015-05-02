package ar.com.textillevel.gui.modulos.chat.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import main.GTLGlobalCache;
import ar.com.textillevel.gui.modulos.chat.gui.ChatWindow;
import ar.com.textillevel.gui.modulos.chat.notifications.NotificationHelper;
import ar.com.textillevel.gui.modulos.chat.notifications.notifier.EnumNotificaciones;
import ar.com.textillevel.modulos.chat.mensajes.ETipoMensajeChat;
import ar.com.textillevel.modulos.chat.mensajes.MensajeChat;
import ar.com.textillevel.modulos.chat.mensajes.MensajeMessageChat;
import ar.com.textillevel.modulos.chat.mensajes.MensajeUsuarios;

public class ChatClient {

	private static ChatClient instance = new ChatClient(System.getProperty("textillevel.chat.server.ip"),
														System.getProperty("textillevel.chat.server.port"));

	public static ChatClient getInstance() {
		return instance;
	}

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket client;
	
	private String ip;
	private String port;
	
	private boolean connected;

	public ChatClient(String ip, String port) {
		super();
		this.ip = ip;
		this.port = port;
		this.connected = false;
	}

	public void conectar() throws NumberFormatException, UnknownHostException, IOException  {
		client = new Socket(getIp(), Integer.valueOf(getPort()));
		oos = new ObjectOutputStream(client.getOutputStream());
		new MessageListener().start();
		MensajeChat msg = new MensajeChat(ETipoMensajeChat.LOGIN, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
		oos.writeObject(msg);
		connected = true;
		ChatWindow.getInstance().agregarUsuario(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
	}

	public void desconectar() {
		try {
			if(connected){
				MensajeChat msg = new MensajeChat(ETipoMensajeChat.LOGOUT, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				oos.writeObject(msg);
				connected = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(connected && oos!=null && ois!=null && client!=null){
					oos.close();
					ois.close();
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void enviarMensaje(String nickDestino, String mensaje) throws IOException{
		MensajeMessageChat mmc = new MensajeMessageChat(ETipoMensajeChat.MESSAGE, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName(), nickDestino, mensaje);
		oos.writeObject(mmc);
	}

	private class MessageListener extends Thread {

		@Override
		public void run() {
			try {
				ois = new ObjectInputStream(client.getInputStream());
				while (isConnected()) {
					MensajeChat msg = (MensajeChat) ois.readObject();
					switch(msg.getTipoMensaje()){
						case LOGIN:{
							ChatWindow.getInstance().agregarUsuario(msg.getNick());
							NotificationHelper.showNotification("Usuario conectado", msg.getNick(),EnumNotificaciones.CONNECTED);
							break;
						}
						case LOGOUT:{
							ChatWindow.getInstance().quitarUsuario(msg);
							NotificationHelper.showNotification("Usuario desconectado", msg.getNick(),EnumNotificaciones.DISCONNECTED);
							break;
						}
						case MESSAGE:{
							MensajeMessageChat mmc = (MensajeMessageChat)msg;
							ChatWindow.getInstance().agregarMensaje(mmc);
							if(!ChatWindow.getInstance().isVisible()){
								NotificationHelper.showNotification("Nuevo mensaje de: "+ mmc.getNickDestino(),mmc.getMsg(),EnumNotificaciones.HINT);
							}
							ChatWindow.getInstance().toFront();
							break;
						}
						case USUARIOS:{
							MensajeUsuarios mu = (MensajeUsuarios)msg;
							ChatWindow.getInstance().agregarListaUsuarios(mu.getUsuarios());
							break;
						}
					}
				}
			} catch (EOFException eof){
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	
	public boolean isConnected() {
		return connected;
	}

	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
