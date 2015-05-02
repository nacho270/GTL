package ar.com.textillevel.modulos.personal.scheduler;

import java.util.Date;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.apache.log4j.Logger;
import org.jboss.varia.scheduler.Schedulable;

import ar.com.textillevel.modulos.personal.utils.ProcesadorFichadasExcelHelper;

public class ScheduleProcesadorExcelFichadas implements Schedulable{

	private static final Logger logger = Logger.getLogger(ScheduleProcesadorExcelFichadas.class);
	
	public void perform(Date arg0, long arg1) {
		initAppParams();
		String path = System.getProperty("path_archivos_fichadas");
		if (path == null) {
			logger.warn("NO ESTA CONFIGURADO EL PATH PARA PROCESAR LOS ARCHIVOS EXCEL DE FICHADAS.");
			return;
		}
		int mask = JNotify.FILE_CREATED;
		boolean watchSubtree = false;

		try {
			FileSystemListener fsListener = new FileSystemListener( Thread.currentThread().getContextClassLoader());
			JNotify.addWatch(path, mask, watchSubtree, fsListener);
			logger.info("ESPERANDO ARCHIVO DE FICHADAS EN EL DIRECTORIO: " + path);
		} catch (JNotifyException e) {
			logger.error("SE HA PRODUCIDO UN ERROR AL QUERER INICIALIZAR EL WATCHER DEL DIRECTORIO",e);
		}
	}

	private void initAppParams() {
		System.getProperties().setProperty("applicationName", "GTL");
		System.getProperties().setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		System.getProperties().setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		System.getProperties().setProperty("cltimezone","GMT-3");
		if(System.getProperty("java.naming.provider.url")==null){
			System.getProperties().setProperty("java.naming.provider.url", "localhost:1099");
		}
	}
	
	private class FileSystemListener implements JNotifyListener {

		private final ClassLoader miCl;
		
		public FileSystemListener(ClassLoader classLoader) {
			this.miCl=classLoader;
		}

		public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
			
		}

		public void fileModified(int wd, String rootPath, String name) {
			
		}

		public void fileDeleted(int wd, String rootPath, String name) {

		}

		public void fileCreated(int wd, String rootPath, String name) {
			if(name.toLowerCase().endsWith(".xls")){
				Thread.currentThread().setContextClassLoader(this.miCl);
				ProcesadorFichadasExcelHelper.procesarArchivo(rootPath+name);
				logger.info("ESPERANDO ARCHIVO DE FICHADAS EN EL DIRECTORIO: " + rootPath);
			}
		}
	}
}
