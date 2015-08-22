package ar.com.textillevel.modulos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.jboss.varia.scheduler.Schedulable;

import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.FileUtil;
import ar.com.textillevel.util.Utils;

public class SchedulerBackupDatabase implements Schedulable{

	private static final Logger logger = Logger.getLogger(SchedulerBackupDatabase.class);
	
	public void perform(Date arg0, long arg1) {
		boolean habilitado = Utils.esAfirmativo(System.getProperty("textillevel.backup.habilitado", "false"));
		if (!habilitado) {
			logger.info("LA REALIZACION DE BACKUP NO SE ENCUENTRA HABILITADA.....");
			return;
		}
		logger.info("REALIZANDO BACKUP.....");
		String username = "root";
	    String database = System.getProperty("textillevel.backup.db", "gtl");
	    String mysqldumpPath = System.getProperty("textillevel.backup.MySQLDumpPath");
	    String backupPath = System.getProperty("textillevel.backup.directorioBackup");
	    String tempFile = System.getProperty("java.io.tmpdir") + "temp-backup.sql";
	    java.sql.Date hoy = DateUtil.getHoy();
	    String fileName = DateUtil.getAnio(hoy) + "-" + DateUtil.getMes(hoy) + "-" + DateUtil.getDia(hoy) + "_BACKUP-"+database+".zip";

	    String command = "cmd.exe /c \"" +mysqldumpPath + " -u " + username  + " " + database + " -r " + tempFile +"\"";
	    logger.info("EJECUTANDO: " + command);
	    try {
	        Process runtimeProcess = Runtime.getRuntime().exec(command);
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(runtimeProcess.getInputStream()));
	        while ((reader.readLine()) != null) {
	        	System.out.println("a");
	        }
	        
	        int processComplete = runtimeProcess.waitFor();
	        if (processComplete == 0) {
	        	logger.info("BACKUP REALIZADO..... COMPRIMIENDO... ");
	        	String zipFileName = crearZip(tempFile);
	        	logger.info("COMPRESION FINALIZADA.... MOVIENDO A: " + backupPath);
	        	FileUtil.moverArchivo(new File(zipFileName), new File(backupPath + "/" + fileName));
	        	new File(tempFile).delete();
	        	logger.info("BACKUP FINALIZADO EXITOSAMENTE");
	        } else {
	        	logger.info("EL BACKUP NO SE HA PODIDO LLEVAR A CABO");
	        }
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private String crearZip(String tempFile) throws FileNotFoundException, IOException {
		String zipFileName = tempFile.replace("sql", "zip");
		FileOutputStream fos = new FileOutputStream(zipFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		File file = new File(tempFile);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry("temp-backup.sql");
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

}
