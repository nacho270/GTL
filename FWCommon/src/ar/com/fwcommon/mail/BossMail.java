package ar.com.fwcommon.mail;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.util.Configuracion;
import ar.com.fwcommon.util.xstream.XStreamWrapper;

/**
 * Clase que permite comunicarse con el Web Service de Mail y enviar un E-Mail.
 */
public class BossMail {

	//private static CLLogger logger = new CLLogger(BossMail.class);
	private static Logger logger = Logger.getLogger(BossMail.class);
    private static String wsEndpoint;
    private static String wsNombre;
    public static final int OK = 0;
    public static final int ERROR = -1;
    private static MailSender mailSender;
    private static boolean mailSenderStarted = false;
    private static String mensajeError;
    private static Exception ultimaExcepcion;
    private static String[] tips;

    
    
    static {
        String wsEndpoint = Configuracion.getMailEndpoint();
        String wsNombre = Configuracion.getMailNombre();
        if(wsEndpoint == null || wsNombre == null) {
            FWException e = new FWException(BossError.ERR_APLICACION, "No se pudo inicializar el subsistema de envío de mails", "No se pudo cargar la configuracion de la BDD", null, new String[] {"Verifique la configuracion en la BDD"});
            BossError.gestionarError(e);
            System.exit(-1) ;
        }
        inicializar(wsEndpoint, wsNombre);
    }

    /**
     * Inicializa el BossMail con los datos del Web Service.
     * @param endpoint La URL del Web Service.
     * @param nombre El nombre del Web Service.
     */
    public static void inicializar(String endpoint, String nombre) {
    	logger.info("[BossMail] Inicializando BossMail ...");
        wsEndpoint = endpoint;
        wsNombre = nombre;
        if(!mailSenderStarted) {
        	logger.info("[BossMail] Inicializando MailSender ...");
        	mailSender = new MailSender(endpoint,nombre);
        	new Thread(mailSender).start();
        	mailSenderStarted = true;
        } else {
        	mailSender.setService(wsEndpoint,wsNombre);
        }
        logger.info("[BossMail] BossMail inicializado.");
    }

    
    private static long communicationTime = 0;
    private static long numberCalls = 0;
    
    public static int enviarMailInmediate (MailRequest eMail, String mailName){
    	long inicio = 0;
    	if (logger.isDebugEnabled()){
        	inicio = System.currentTimeMillis();
        }
    	try {
            //XML
            XStreamWrapper xs = new XStreamWrapper();
            xs.alias("Mail", MailRequest.class);
            xs.alias("destinatarios", ArrayList.class);
            xs.alias("Destinatario", DestinatarioRequest.class);
            String serializedMail = xs.serialize(eMail);
        	
        	
        	Service service = new Service();
            Call call = (Call)service.createCall();
            
            call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
            call.setOperationName(new QName("enviarMail"));
            call.addParameter("values", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(XMLType.XSD_INT);
            if ((Integer)call.invoke(new Object[] { serializedMail }) != 0) {
            	throw (new FWRuntimeException("El método enviarMail del Web Service " + wsNombre + " retornó un error.")); 
            }
        } catch(RuntimeException e) {
            return logErrorNReturn (e);
        } catch(RemoteException e) {
        	return logErrorNReturn (e);
		} catch(MalformedURLException e) {
			return logErrorNReturn (e);
		} catch(ServiceException e) {
			return logErrorNReturn (e);
		}
		if (logger.isDebugEnabled()){
			long tardanza = System.currentTimeMillis() - inicio;
			numberCalls++;
			communicationTime += tardanza;
			logger.debug("bossmail average:" + communicationTime/numberCalls);
			logger.debug("tardo:" + tardanza);
		}
		return BossMail.OK;
    }
    
    
    private static int logErrorNReturn(Exception e) {
		logger.error("Error en en el envio al ws"+ e.getMessage(),e);
		return BossMail.ERROR;
	}


	/**
     * Envía un E-Mail.
     * @param eMail El eMail a enviar.
     * @param observer El observador de envio de mail. 
     * @param mailName El nombre del mail (para identificar en el log y por el observer).
     * @return BossMail.OK Si se pudo encolar el mail para su envío al Web Service. Sino BossMail.ERROR.
     */
    public static int enviarMail(MailRequest eMail, MailObserver observer, String mailName) {
        String serializedMail;
        try {
            //XML
            XStreamWrapper xs = new XStreamWrapper();
            xs.alias("Mail", MailRequest.class);
            xs.alias("destinatarios", ArrayList.class);
            xs.alias("Destinatario", DestinatarioRequest.class);
            serializedMail = xs.serialize(eMail);
            mailSender.addPendingMailCall(serializedMail,observer,mailName);
            logger.info("[BossMail] Mail encolado para envío. Título: '" + mailName + "'");
           	return OK; //Se encoló para envío
        } catch(Exception e) { //Error antes de enviar
        	e.printStackTrace() ;
        	setMensajeError("No se pudo enviar el mail");
        	setUltimaExcepcion(e);
        	setTips(new String[] { "Compruebe que las direcciones ingresadas sean casillas de mail válidas"});
        	logger.error("[BossMail] No se pudo encolar mail: "+ mailName + ". " + e);
        	return ERROR;
        }
    }

    /**
     * Envía un E-Mail sin ningún observer.
     * @param eMail el eMail a enviar.
     * @return BossMail.OK si se pudo encolar el mail para su envío al Web Service. Sino BossMail.ERROR.
     */
    public static int enviarMail(MailRequest eMail) {
    	return enviarMail(eMail, null, eMail.getAsunto());
    }

    /**
     * Crea una lista de destinatarios (con objetos DestinatarioRequest) a partir de los
     * valores de la Hashtable: key (int)      = DestinatarioRequest.PARA
     *                                           DestinatarioRequest.CC
     *                                           DestinatarioRequest.CCO
     *                          value (String) = La dirección de E-Mail
     * 
     * @param valores La Hashtable con los pares tipo de destinatario y dirección de E-Mail.
     * @return destinatarios La lista de objetos DestinatarioRequest.
     */
    public List<DestinatarioRequest> crearDestinatarios(Hashtable valores) {
        List<DestinatarioRequest> destinatarios = new ArrayList<DestinatarioRequest>();
        for(Iterator i = valores.keySet().iterator(); i.hasNext();) {
            Integer key = (Integer)i.next();
            destinatarios.add(new DestinatarioRequest(key.intValue(), (String)valores.get(key)));
        }
        return destinatarios;
    }

    /**
     * @param toList La lista de direcciones de los destinatarios del mail.
     * @param ccList La lista de direcciones de los destinatarios de la copia del mail.
     * @param ccoList La lista de direcciones de los destinatarios de la copia oculta del mail.
     * @return la lista de destinatarios en el formato requerido por el MailRequest. 
     */
    public static List<DestinatarioRequest> crearDestinatarios(Collection<String> toList, Collection<String> ccList, Collection<String> ccoList) {
        List<DestinatarioRequest> destinatarios = new ArrayList<DestinatarioRequest>();
        destinatarios.addAll(crearDestinatarios(toList, DestinatarioRequest.PARA));
        destinatarios.addAll(crearDestinatarios(ccList, DestinatarioRequest.CC));
        destinatarios.addAll(crearDestinatarios(ccoList, DestinatarioRequest.CCO));
        return destinatarios;
    }

    /**
     * @param direcciones la lista de direcciones a partir de la cual se generará
     * la lista de destinatarios.
     * @param key el tipo de destinatario: DestinatarioRequest.PARA o
     *                                     DestinatarioRequest.CC o
     *                                     DestinatarioRequest.CCO.
     * @return la lista de destinatarios en el formato requerido por el MailRequest.
     */
    private static List<DestinatarioRequest> crearDestinatarios(Collection<String> direcciones, int key) {
        List<DestinatarioRequest> destinatarios = new ArrayList<DestinatarioRequest>();
        if(direcciones != null) {
            for (String direccion : direcciones) {
	            destinatarios.add(new DestinatarioRequest(key, direccion));
	        }
        }
        return destinatarios; 
    }

	/**
     * Devuelve el <b>endpoint</b> del Web Service.
	 * @return wsEndpoint El endpoint del Web Service.
	 */
	public static String getWsEndpoint() {
		return wsEndpoint;
	}

	/**
     * Devuelve el <b>nombre</b> del Web Service.
	 * @return wsNombre El nombre del Web Service.
	 */
	public static String getWsNombre() {
		return wsNombre;
	}

	public static String getMensajeError() {
		return mensajeError;
	}

	public static void setMensajeError(String mensajeError) {
		BossMail.mensajeError = mensajeError;
	}

	public static String[] getTips() {
		return tips;
	}

	public static void setTips(String[] tips) {
		BossMail.tips = tips;
	}

	public static Exception getUltimaExcepcion() {
		return ultimaExcepcion;
	}

	public static void setUltimaExcepcion(Exception ultimaExcepcion) {
		BossMail.ultimaExcepcion = ultimaExcepcion;
	}

}