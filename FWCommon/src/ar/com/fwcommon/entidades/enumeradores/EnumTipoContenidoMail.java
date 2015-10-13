package ar.com.fwcommon.entidades.enumeradores;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;

import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.entidades.Mail;




public enum EnumTipoContenidoMail {
	TEXT_PLAIN (1,"text/plain"){
		public void armarCuerpo (Message message,Mail mailObject) throws MessagingException{
			message.setText(mailObject.getCuerpo());
		}
	}
	, TEXT_HTML (2,"text/html")	{
		public void armarCuerpo (Message message,Mail mailObject)throws MessagingException{
			message.setContent(mailObject.getCuerpo(), this.getMime());
		}
		
	}, MULTIPART_ALTERNATIVE(3,"multipart/alternative")	{
		public void armarCuerpo (Message message,Mail mailObject)throws MessagingException{
			//faltarian las dos partes en el objeto mailObject. Todavía no se si este tipo de mails sigue siendo util en la actualidad.
			//TODO: ver si es necesario
			throw new UnsupportedOperationException("Sin implementar");
		}
	};
	
	private int id;
	private String mime;

	/**
	 * arma el cuerpo del mail, para un mensaje javamail.
	 * 
	 * @param message
	 * @param mailObject
	 * @throws MessagingException
	 */
	public abstract void armarCuerpo (Message message,Mail mailObject)throws MessagingException;
	
	private EnumTipoContenidoMail(int id, String mime){
		this.id = id;
		this.mime = mime;
	}

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	
	public String getMime() {
		return mime;
	}

	
	public void setMime(String mime) {
		this.mime = mime;
	}
	
	private static Map<Integer, EnumTipoContenidoMail> keyMap;
	
	public static Map<Integer, EnumTipoContenidoMail> getKeyMap (){
		if (keyMap == null){
			Map<Integer, EnumTipoContenidoMail> keyMapTemp = new HashMap<Integer, EnumTipoContenidoMail>();
			for (EnumTipoContenidoMail tipo : values()){
				keyMapTemp.put(tipo.id, tipo);
			}
			keyMap = keyMapTemp;//ojo, el metodo no es synchronized
		}
		return keyMap;
	}
	
	public static EnumTipoContenidoMail getById (int id){
		EnumTipoContenidoMail enumTipoMensaje = getKeyMap().get(id);
		if (enumTipoMensaje==null){
			throw new FWRuntimeException ("No existe el tipo de contenido con id:" + id);
		}
		return enumTipoMensaje;
	}
	
}
