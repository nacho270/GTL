package ar.com.fwcommon.mail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import ar.com.fwcommon.util.log.FWLogger;

/**
 * Se encarga de encolar mails y enviarlos en intervalos periódicos.
 */
public class MailSender implements Runnable {

    public static class MailInfo {
        final String mailName;
        final boolean sent;
        final String message;
        final Exception exception;

        public MailInfo(String mailName, boolean sent, String message, Exception exception) {
            if(mailName == null) {
                throw new IllegalArgumentException("Debe especificarse un nombre para el mail.");
            }
            this.mailName = mailName;
            this.sent = sent;
            this.message = message;
            this.exception = exception;
        }

        public String toString() {
            return message;
        }

        public String getMailName() {
            return mailName;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSent() {
            return sent;
        }

        public Exception getException() {
            return exception;
        }
    }

    static class MailCall {
        final String mailName;
        final String mail;
        final MailObserver observer;

        public MailCall(String mailName, String mail, MailObserver observer) {
            if(mailName == null) {
                throw new IllegalArgumentException("Debe especificarse un nombre para el mail.");
            }
            this.mailName = mailName;
            this.mail = mail;
            this.observer = observer;
        }

        public String getMailName() {
            return mailName;
        }

        public void execute(String endpoint, String nombre) throws Exception {
            try {
                Service service = new Service();
                Call call = (Call)service.createCall();
                call.setTargetEndpointAddress(new URL(endpoint + nombre));
                call.setOperationName(new QName("enviarMail"));
                call.addParameter("values", XMLType.XSD_STRING, ParameterMode.IN);
                call.setReturnType(XMLType.XSD_INT);
                if ((Integer)call.invoke(new Object[] { mail }) != 0) {
                	throw (new Exception("El método enviarMail del Web Service " + nombre + " retornó un error.")); 
                }
            } catch(Exception e) {
                if(observer != null) {
                    observer.update(new MailInfo(mailName, false, "No se pudo enviar el mail: " + mailName + ". Se reintentará el envío en " + (WAIT_INTERVAL / 1000) + " segundos.", e));
                }
                throw e;
            }
            if(observer != null) {
                observer.update(new MailInfo(mailName, true, "Mail enviado al Web Service con éxito: " + mailName, null));
            }
        }
    }

    private FWLogger logger = new FWLogger(MailSender.class);
    private final List<MailCall> pendingMailCalls = new ArrayList<MailCall>();
    private final List<MailCall> newPendingMailCalls = new ArrayList<MailCall>();
    private boolean end = false;
    private static final int WAIT_INTERVAL = 5000; //En milisegundos
    private String wsEndpoint;
    private String wsNombre;

    public MailSender(String wsEndpoint, String wsNombre) {
    	this.wsEndpoint = wsEndpoint;
    	this.wsNombre = wsNombre;
    }

    /**
     * Encola el envío del E-Mail.
     * @param serializedMail El E-Mail serializado.
     * @param observer El observador de los eventos del envío del E-Mail (o null,
     *                 si no hay observador).
     */
    public synchronized void addPendingMailCall(String serializedMail, MailObserver observer, String mailName) {
        newPendingMailCalls.add(new MailCall(mailName, serializedMail, observer));
    }

    /**
     * Agrega los E-Mails ingresados desde la última iteración.
     */
    private synchronized void addNewMails() {
        pendingMailCalls.addAll(newPendingMailCalls);
        newPendingMailCalls.clear();
    }

    public synchronized void setService(String wsEndpoint, String wsNombre) {
    	this.wsEndpoint = wsEndpoint;
    	this.wsNombre = wsNombre;
    }

    /**
     * Intenta enviar todos los E-Mails encolados. Cuando un E-Mail se envía con
     * éxito se elimina de la cola y se continúa enviando más E-Mails.
     */
    public void sendPendingMails() {
        addNewMails();
        for(Iterator<MailCall> it = pendingMailCalls.iterator(); it.hasNext();) {
            try {
                MailCall mailCall = it.next();
                mailCall.execute(wsEndpoint,wsNombre);
                logger.info("[MailSender] E-Mail enviado con éxito. Título: '" + mailCall.getMailName() + "'");
                it.remove();
            } catch(Exception e) {
            	e.printStackTrace() ;
                logger.warn("[MailSender] No se pudo enviar E-Mail: " + e);
            }
        }
    }

    public void run() {
        while(!end) {
            try {
                Thread.sleep(WAIT_INTERVAL);
                sendPendingMails();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void end() {
        end = true;
    }

}