package ar.com.textillevel.modulos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.jboss.varia.scheduler.Schedulable;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.FileUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.util.Utils;

public class SchedulerBackupDatabase implements Schedulable {

	private static final Logger logger = Logger.getLogger(SchedulerBackupDatabase.class);

	private static final Integer DEJAR_ULTIMOS_N_DEFAULT = 6;
	private static final String HORA_DEFAULT = "18:00";

	public void perform(Date arg0, long arg1) {
		String horaEjecucion = System.getProperty("textillevel.backup.horaEjecucion", HORA_DEFAULT);
	    //Si no es la hora de ejecución sale
	    Timestamp now = DateUtil.getAhora();
		int horas = DateUtil.getHoras(now);
		int minutos = DateUtil.getMinutos(now);
		if(!horaEjecucion.equals(horas +":" + minutos)) {
	    	return;
	    }

	    boolean habilitado = Utils.esAfirmativo(System.getProperty("textillevel.backup.habilitado", "false"));
		if (!habilitado) {
			logger.info("LA REALIZACION DE BACKUP NO SE ENCUENTRA HABILITADA.....");
			return;
		}
		logger.info("REALIZANDO BACKUP.....");
		String username = "root";
	    String database = System.getProperty("textillevel.backup.db", "gtl");
	    String pass = System.getProperty("textillevel.backup.pass", "s4l3m");
	    String mysqldumpPath = System.getProperty("textillevel.backup.MySQLDumpPath");
	    String backupPath = System.getProperty("textillevel.backup.directorioBackup");
	    String tempFile = System.getProperty("java.io.tmpdir") + "temp-backup.sql";
	    String dejarUltimosN = System.getProperty("textillevel.backup.dejarUltimosN");
	    java.sql.Date hoy = DateUtil.getHoy();


	    String fileName = DateUtil.getAnio(hoy) + "-" + (DateUtil.getMes(hoy) + 1) + "-" + DateUtil.getDia(hoy) + "_BACKUP-"+database+".zip";

	    String command = "cmd.exe /c \"" +mysqldumpPath + " -u " + username  + " -p"+ pass + " " + database + " -r " + tempFile +"\"";
	    logger.info("EJECUTANDO: " + command);
	    try {
	        Process runtimeProcess = Runtime.getRuntime().exec(command);
	        
	        logger.info("VACIANDO BUFFER... " + readInputStream(runtimeProcess.getInputStream()));

	        int processComplete = runtimeProcess.waitFor();
	        if (processComplete == 0) {
	        	logger.info("BACKUP REALIZADO..... COMPRIMIENDO... ");
	        	String zipFileName = crearZip(tempFile);
	        	logger.info("COMPRESION FINALIZADA.... MOVIENDO A: " + backupPath);
	        	FileUtil.moverArchivo(new File(zipFileName), new File(backupPath + "/" + fileName));
	        	new File(tempFile).delete();
	        	logger.info("BACKUP FINALIZADO EXITOSAMENTE");
	        	logger.info("BACKUP REALIZADO..... BORRANDO BACKUPS VIEJOS ... ");
	        	List<String> nombresArchivosBorrados = borrarBackupsAnteriores(backupPath, dejarUltimosN == null ? DEJAR_ULTIMOS_N_DEFAULT : Integer.valueOf(dejarUltimosN));
	        	if(nombresArchivosBorrados.isEmpty()) {
	        		logger.info("BACKUP REALIZADO..... NO HABIA BACKUPS PARA BORRAR.");
	        	} else {
	        		logger.info("BACKUP REALIZADO..... SE BORRARON LOS BACKUPS: " + StringUtil.getCadena(nombresArchivosBorrados, " "));
	        	}
	        } else {
	        	logger.error("EL BACKUP NO SE HA PODIDO LLEVAR A CABO: " + readInputStream(runtimeProcess.getErrorStream()));
	        	return;
	        }
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private List<String> borrarBackupsAnteriores(String backupPath, int dejarUltimosN) {
		List<String> nombresArchivosBorrados = new ArrayList<String>();
		File dirBackupPath = new File(backupPath);
		File[] listFiles = dirBackupPath.listFiles();
		Arrays.sort(listFiles);
		List<File> listFilesParaBorrar = new ArrayList<File>();
		for(int i=0; i < listFiles.length; i++) {
			if(listFiles.length - i > dejarUltimosN) {
				listFilesParaBorrar.add(listFiles[i]);
			}
		}
		for(File f : listFilesParaBorrar) {
			boolean borrado = f.delete();
			if(borrado) {
				nombresArchivosBorrados.add(f.getName());
			}
		}
		return nombresArchivosBorrados;
	}

	private String crearZip(String tempFile) throws FileNotFoundException, IOException {
		String zipFileName = tempFile.replace("sql", "zip");
		FileOutputStream fos = new FileOutputStream(zipFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		File file = new File(tempFile);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry("backup.sql");
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
		
		zos.close();
		fos.close();
		return zipFileName;
	}

	private StringBuilder readInputStream(InputStream in) throws IOException {
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();
		while(read != null) {
		    sb.append(read);
		    read =br.readLine();
		}
		return sb;
	}

}