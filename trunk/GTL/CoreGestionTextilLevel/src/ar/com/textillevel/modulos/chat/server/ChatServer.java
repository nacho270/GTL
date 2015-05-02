package ar.com.textillevel.modulos.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.com.textillevel.modulos.chat.mensajes.ETipoMensajeChat;
import ar.com.textillevel.modulos.chat.mensajes.MensajeChat;
import ar.com.textillevel.modulos.chat.mensajes.MensajeMessageChat;
import ar.com.textillevel.modulos.chat.mensajes.MensajeUsuarios;

public class ChatServer{

	private static final int PORT = Integer.valueOf(System.getProperty("textillevel.chat.server.port","7777"));
	private static final int COLA = 100;
	
	private static final Logger logger = Logger.getLogger(ChatServer.class);

	private static ChatServer instance;

	private final Map<String, ObjectOutputStream> sesiones = new HashMap<String, ObjectOutputStream>();

	private final ServerSocket server;

	public static ChatServer getInstance() throws IOException {
		if (instance == null) {
			instance = new ChatServer();
		}
		return instance;
	}

	private ChatServer() throws IOException {
		server = new ServerSocket(PORT, COLA);
	}
	
	public void arrancar(){
		new InnerServer().start();
	}

	private class InnerServer extends Thread{
		@Override
		public void run() {
			Socket s1;
			while (true) {
				try {
					logger.debug("Esperando conexiones....");
					s1 = server.accept();
					logger.debug("Conexion recibida: " + s1.getInetAddress().getHostAddress());
					new MessageProcessor(s1).start();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	
	
	private class MessageProcessor extends Thread {

		private final Socket socketRecibido;

		public MessageProcessor(Socket socket) {
			socketRecibido = socket;
		}

		@Override
		public void run() {
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(socketRecibido.getInputStream());
				boolean fin = false;
				while(!fin){
					MensajeChat msg = (MensajeChat) ois.readObject();
					logger.debug("Mensaje recibido de " + socketRecibido.getRemoteSocketAddress().toString());
					switch (msg.getTipoMensaje()) {
						case LOGIN: {
							ObjectOutputStream oop = new ObjectOutputStream(socketRecibido.getOutputStream());
							synchronized (this) {
								if(sesiones.get(msg.getNick())!=null){
									sesiones.get(msg.getNick()).close();
									sesiones.remove(msg.getNick());
								}
								sesiones.put(msg.getNick(), oop);
							}
							MensajeChat newMsg = new MensajeChat(ETipoMensajeChat.LOGIN, msg.getNick());
							broadCast(newMsg, msg.getNick());
							MensajeUsuarios mu = new MensajeUsuarios(ETipoMensajeChat.USUARIOS, msg.getNick(), new ArrayList<String>(sesiones.keySet()));
							oop.writeObject(mu);
							break;
						}
						case LOGOUT: {
							sesiones.get(msg.getNick()).close();
							synchronized (this) {
								sesiones.get(msg.getNick()).close();
								sesiones.remove(msg.getNick());
							}
							MensajeChat newMsg = new MensajeChat(ETipoMensajeChat.LOGOUT, msg.getNick());
							broadCast(newMsg, msg.getNick());
							socketRecibido.close();
							fin = true;
							break;
						}
						case MESSAGE: {
							MensajeMessageChat mensRec = (MensajeMessageChat)msg;
							MensajeMessageChat mens = new MensajeMessageChat(ETipoMensajeChat.MESSAGE, mensRec.getNick(), mensRec.getNickDestino(), mensRec.getMsg());
							sesiones.get(mensRec.getNickDestino()).writeObject(mens);
							break;
						}
						case USUARIOS: {
							MensajeUsuarios msgu = new MensajeUsuarios(ETipoMensajeChat.USUARIOS, "", new ArrayList<String>(sesiones.keySet()));
							sesiones.get(msg.getNick()).writeObject(msgu);
							break;
						}
					}
				}
			} catch (IOException e) {
//				e.printStackTrace();
			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
			} finally{
				try {
					socketRecibido.close();
				} catch (IOException e) {
//					e.printStackTrace();
					logger.error("Error al cerrar el socket", e);
				}
			}
		}
		
		private synchronized void broadCast(MensajeChat mensaje, String excludeNick) throws IOException{
			for(String nick : sesiones.keySet()){
				if(excludeNick == null || (nick!=null && !nick.equals(excludeNick))){
					try{
						sesiones.get(nick).writeObject(mensaje);
					}catch(SocketException se){
						se.printStackTrace();
						sesiones.get(nick).close();
						socketRecibido.close();
						sesiones.remove(nick);
					}
				}
			}
		}
	}
}
