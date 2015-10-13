package ar.com.fwcommon.util.log;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

public class FWLogger {

	public final static int ERROR_INT = Priority.ERROR_INT;
	public final static int WARN_INT = Priority.WARN_INT;
	public final static int INFO_INT = Priority.INFO_INT;

	private Logger logger;
	private static boolean configured = false ;

	public FWLogger(Class<?> c) {
		configurar() ;
		logger = Logger.getLogger(c);
	}

	private synchronized void configurar() {
		if (configured) {
//			System.err.println("configurar: configured == true");
			return ;
		}
//		System.err.println("configurar: configured == false");
		configured = true ;
		// Si existe un log4j.xml / log4j.properties usarlo, si no configurar en duro
		Logger root = Logger.getRootLogger();
		if (root.getAllAppenders().hasMoreElements()) {
			System.out.println("FWLoger.configurar: log4j.xml/log4j.properties");
		} else {
			// Configuracion desktop y autómatas
			root.setLevel(Level.INFO) ;
			Appender appender = null ;
			String iniciarEn = null ;
			if (System.getProperty("iniciarEn") != null) { // autómatas
				if (new File (System.getProperty("iniciarEn")).exists()) {
					iniciarEn = System.getProperty("iniciarEn") ;
				} else {
					System.out.println (System.getProperty("iniciarEn") + " no existe, se va a utilizar el directorio corriente.") ;
				}
				try {
					String pattern = System.getProperty("FWLogger.pattern", "%-4r %d [%t] %-5p %c %x - %m%n") ;
					String logFile = System.getProperty("FWLogger.logFile", "automata.log") ;
					DailyRollingFileAppender drfa = new DailyRollingFileAppender (new PatternLayout(pattern), (iniciarEn == null ? "" : (iniciarEn + File.separator)) + logFile, "'.'yyyy-MM-dd");
					drfa.setAppend(true) ;				
					appender = drfa ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { // desktops
				appender = new ConsoleAppender () ;
			}
			if (appender != null) {
				root.addAppender(appender) ;
			}
		}
	}

	public void debug(Object message) {
		try {
			logger.debug(message);
		} catch(Exception e) {
		}
	}
	
	public void debug(Throwable t) {
		debug(null, t);
	}

	public void debug(Object message, Throwable t) {
		try {
			logger.debug(message, t);
		} catch(Exception e) {
		}
	}

	public void info(Object message) {
		try {
			logger.info(message);
		} catch(Exception e) {
		}
	}

	public void info(Throwable t) {
		debug(null, t);
	}

	public void info(Object message, Throwable t) {
		try {
			logger.info(message, t);
		} catch(Exception e) {
		}
	}

	public void warn(Object message) {
		try {
			logger.warn(message);
		} catch(Exception e) {
		}
	}

	public void warn(Throwable t) {
		warn(null, t);
	}

	public void warn(Object message, Throwable t) {
		try {
			logger.warn(message, t);
		} catch(Exception e) {
		}
	}

	public void error(Object message) {
		try {
			logger.error(message);
		} catch(Exception e) {
		}
	}

	public void error(Throwable t) {
		error(null, t);
	}

	public void error(Object message, Throwable t) {
		try {
			logger.error(message, t);
		} catch(Exception e) {
		}
	}

	public String getName() {
		return logger.getName();
	}

	public boolean isDebugEnabled (){
		return logger.isDebugEnabled();
	}

	public boolean isInfoEnabled (){
		return logger.isInfoEnabled();
	}	
	
	static public void setLevelWarn(String paquete) {
		Logger.getLogger(paquete).setLevel(Level.WARN);
	}

}