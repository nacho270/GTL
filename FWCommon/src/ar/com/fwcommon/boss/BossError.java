package ar.com.fwcommon.boss;

import ar.com.fwcommon.auditoria.AuditoriaException;
import ar.com.fwcommon.componentes.error.FWErrorDialog;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.notificadores.INotificadorErrores;
import ar.com.fwcommon.componentes.error.notificadores.NotificadorErroresConsola;
import ar.com.fwcommon.excepciones.InterpreteExcepciones;

/**
 * Clase para el manejo de errores. Muestra en pantalla un cuadro de diálogo con la
 * descripción del error.
 * ar.com.fwcommon.componentes.error.CLErrorDialog
 */
public class BossError {

	private static INotificadorErrores notificadorErrores;
	private static int idAplicacion; 
	public static final int ERR_APLICACION = 0;
	public static final int ERR_CONEXION = 1;
	public static final int ERR_OPERACION = 2;
	public static final int ERR_INDETERMINADO = 3;

	static {
		notificadorErrores = NotificadorErroresConsola.getInstance();
	}

	/**
	 * Setea el notificador de errores.
	 * @param notificadorErrores
	 */
	public static void setNotificadorErrores(INotificadorErrores notificadorErrores, int idAplicacion) {
		BossError.notificadorErrores = notificadorErrores;
		BossError.idAplicacion = idAplicacion;
	}

	/**
	 * Devuelve el notificador de errores utilizado.
	 * @return BossError2.notificadorErrores
	 */
	public static INotificadorErrores getNotificadorErrores() {
		return BossError.notificadorErrores;
	}

	/**
	 * Abre el diálogo de gestión de errores.
	 * @param tipoDeError
	 * @param mensajeLlamador
	 * @param mensajeLlamado
	 * @param e
	 * @param tips
	 */
	public static void gestionarError(int tipoDeError, String mensajeLlamador, String mensajeLlamado, Exception e, String[] tips) {
		e.printStackTrace();
		gestionarError(new FWException(tipoDeError, mensajeLlamador, mensajeLlamado, e, tips));
	}

	/**
	 * Abre el diálogo de gestión de errores.
	 * @param contexto
	 * @param e
	 */
	public static void gestionarError(String contexto, FWException e) {
		e.printStackTrace();
		if(e.getMensajeContexto() != null) {
			e.setMensajeContexto(contexto + " - " + e.getMensajeContexto());
		} else {
			e.setMensajeContexto(contexto);
		}

		gestionarError(e);
	}
	
	public static void gestionarError (RuntimeException ex){
		ex.printStackTrace();
		BossError.gestionarError(new FWException(interpreteExcepciones.interpretarMensajeExcepcion(ex),ex));
	}
	
	public static void gestionarError (AuditoriaException ex){
		ex.printStackTrace();
		BossError.gestionarError(new FWException("Error de Auditoria, se realizaron los cambios pero no se pudo auditar:" +  ex.getMensajeSistema(),ex));
	}
	

	/**
	 * Abre el diálogo de gestión de errores.
	 * @param e
	 */
	public static void gestionarError(FWException e) {
		e.printStackTrace();
		e.setIdAplicacion(idAplicacion);
		String batchMode = System.getProperty("batch_mode");
		if(batchMode != null && batchMode.equalsIgnoreCase("S")) {
			throw new RuntimeException(BossIdiomas.getInstance("fwcommon").getString("notificar_error_como_runtime_exception", new Object[] {e.getMensajeContexto(), e.getMensajeError()}), e); 
		}
		if(e.getTipoError() != ERR_OPERACION)
			notificadorErrores.notificarError(e);
		if(e.getCause() == null) {
			new FWErrorDialog(e.getTipoError(), e.getMensajeContexto(), e.getMensajeError(), e.getLlamadaParametrizada(), e, e.getTips()).setVisible(true);
		} else {
			new FWErrorDialog(e.getTipoError(), e.getMensajeContexto(), e.getMensajeError(), e.getLlamadaParametrizada(), e.getCause(), e.getTips()).setVisible(true);
		}
	}

	private static InterpreteExcepciones interpreteExcepciones = new InterpreteExcepciones(){
		public String interpretarMensajeExcepcion(RuntimeException ex) {
			return ex.getLocalizedMessage();
		}
	};
	
	
	public static String interpretarMensajeExcepcion (RuntimeException ex){
		return interpreteExcepciones.interpretarMensajeExcepcion(ex);
	}

	
	public static void setInterpreteExcepciones(InterpreteExcepciones interpreteExcepciones) {
		BossError.interpreteExcepciones = interpreteExcepciones;
	}
	
}