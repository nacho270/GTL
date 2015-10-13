package ar.com.fwcommon.componentes;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.util.FileUtil;
import ar.com.fwcommon.util.StringUtil;

/**
 * Componente para la selección de archivos y directorios.
 * 
 * << Particularidades del JFileChooser >>
 * 
 * Permitiendo seleccionar múltiples archivos y/o directorios no resalta el archivo indicado con setSelectedFile
 * Lo muestra en el TextField pero no lo resalta en el panel.
 * 
 * Permitiendo seleccionar múltiples directorios cuando retorna no permite discriminar si seleccioné un directorio 
 * parado en el padre o si lo seleccioné estando adentro ...
 * 
 * Si se seleccionan múltiples archivos y/o directorios y se cambia de directorio manteniendo la selección, retorna
 * la lista de archivos y/o directorios sin el path ...
 * 
 * Algunas veces el directorio / archivo seleccionado está seleccionado pero no aparece en la parte visible del panel.
 * 
 * Si el panel abarca todas las entradas no se selecciona el archivo / directorio corriente (excepto si esta en modo DIRECTORIES_ONLY ...).
 * 
 * << Comportamiento CLFileChooser (para lidiar con las Particularidades del JFileChooser) >>
 * 
 * Si se seleccionan múltiples archivos y/o directorios el último archivo seleccionado queda en el padre de los mismos.
 * 
 * Si se selecciona un directorio en un diálogo que permite selección múltiple el último archivo seleccionado queda en ese directorio.
 * 
 * Si se selecciona un archivo en un diálogo que permite selección múltiple el último archivo seleccionado queda en el directorio padre del archivo seleccionado.
 *  
 * Cuando el último archivo seleccionado es un directorio y fue seleccionado con un diálogo que permitía selección 
 * múltiple el diálogo se abre en ese directorio.
 *
 * Si el último archivo seleccionado es un archivo y se abre el diálogo en modo directorios solamente o con un filtro
 * que haga que el archivo no aparezca, y el usuario cancela, no pierde la selección anterior.
 * 
 * Si saveOrOpen es OPEN no permite seleccionar archivos que no existen.
 * 
 */
public class FWFileSelector {

	public static final int SAVE = 0;
	public static final int OPEN = 1;
	public static final int FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES;
	public static final int FILES_ONLY = JFileChooser.FILES_ONLY;
	public static final int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;
	private static boolean fromMultiple = false;
	private static File lastSelectedFile = null;
	private static File lastSelectedFileBackup = null;

	/**
	 * Retorna una instacia de JFileChooser.
	 * @param mode El modo de selección. Posibles modos:
	 *             FILES_ONLY Solo selección de archivos.
	 *             DIRECTORIES_ONLY Solo selección de directorios.
	 *             FILES_AND_DIRECTORIES Selección de archivos y directorios.
	 * @param fileFilter El filtro de archivos a utilizar (Ej) para seleccionar solo archivos .zip)
	 * @param multiSelectionEnabled Flag para indicar si se puede seleccionar mas de un archivo o/y directorio.
	 * @return fileChooser La instancia del componente.
	 */
	private static JFileChooser getFileChooserInstance(int saveOrOpen, int fileSelectionMode, FileFilter fileFilter, boolean multiSelectionEnabled) {
		JFileChooser fileChooser = new JFileChooser();
		//fileChooser.setPreferredSize(new Dimension(600, 600)) ;
		fileChooser.setFileHidingEnabled(false);
		fileChooser.setFileSelectionMode(fileSelectionMode);
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setMultiSelectionEnabled(multiSelectionEnabled);
		if(getLastSelectedFile() == null) {
			return fileChooser;
		}
		//System.out.println("getLastSelectedFile: " + getLastSelectedFile()) ;
		File archivoSeleccionadoExistente = null;
		if(saveOrOpen == OPEN) {
			archivoSeleccionadoExistente = getParentExistFile(getLastSelectedFile());
		} else {
			archivoSeleccionadoExistente = getLastSelectedFile();
		}
		setLastSelectedFileBackup(archivoSeleccionadoExistente);
		if(fileFilter != null) {
			if(!fileFilter.accept(archivoSeleccionadoExistente)) {
				if(archivoSeleccionadoExistente.exists() && archivoSeleccionadoExistente.isFile()) {
					setLastSelectedFile(archivoSeleccionadoExistente.getParentFile());
					archivoSeleccionadoExistente = getLastSelectedFile();
				}
			}
		}
		//System.out.println("archivoSeleccionadoExistente: " + archivoSeleccionadoExistente) ;
		//En archivoSeleccionadoExistente tengo el 1er archivo o directorio existente que respeta el filtro
		if(fileSelectionMode == JFileChooser.FILES_ONLY) {
			if(archivoSeleccionadoExistente.isFile() || !archivoSeleccionadoExistente.exists()) {
				fileChooser.setCurrentDirectory(archivoSeleccionadoExistente.getParentFile());
				fileChooser.setSelectedFile(archivoSeleccionadoExistente);
			} else {
				fileChooser.setCurrentDirectory(archivoSeleccionadoExistente);
				fileChooser.setSelectedFile(null);
			}
		} else if(fileSelectionMode == JFileChooser.DIRECTORIES_ONLY) {
			if(archivoSeleccionadoExistente.isFile()) {
				fileChooser.setCurrentDirectory(archivoSeleccionadoExistente.getParentFile());
				fileChooser.setSelectedFile(null);
			} else {
				if(getFromMultiple()) {
					fileChooser.setCurrentDirectory(archivoSeleccionadoExistente);
					fileChooser.setSelectedFile(null);
				} else {
					fileChooser.setCurrentDirectory(archivoSeleccionadoExistente.getParentFile());
					fileChooser.setSelectedFile(archivoSeleccionadoExistente);
				}
			}
		} else if(fileSelectionMode == JFileChooser.FILES_AND_DIRECTORIES) {
			if(archivoSeleccionadoExistente.isFile() || !archivoSeleccionadoExistente.exists()) {
				fileChooser.setCurrentDirectory(archivoSeleccionadoExistente.getParentFile());
				fileChooser.setSelectedFile(archivoSeleccionadoExistente);
			} else {
				if(getFromMultiple()) {
					fileChooser.setCurrentDirectory(archivoSeleccionadoExistente);
					fileChooser.setSelectedFile(null);
				} else {
					fileChooser.setCurrentDirectory(archivoSeleccionadoExistente.getParentFile());
					fileChooser.setSelectedFile(archivoSeleccionadoExistente);
				}
			}
		} else {
			return null;
		}
		return fileChooser;
	}

	/**
	 * Abre un diálogo de selección de archivos acorde a la parametrización.
	 * @param saveOrOpen Indica si el diálogo debe decir Guardar o Abrir.
	 * @param fileSelectionMode El modo de selección. Posibles modos:
	 *             FILES_ONLY Solo selección de archivos.
	 *             DIRECTORIES_ONLY Solo selección de directorios.
	 *             FILES_AND_DIRECTORIES Selección de archivos y directorios.
	 * @param parent Permite indicar el componente padre del diálogo.
	 * @return File El archivo seleccionado o null si se canceló la selección.
	 */
	public static File obtenerArchivo(int saveOrOpen, int fileSelectionMode, Component parent) {
		return obtenerArchivo(saveOrOpen, fileSelectionMode, null, parent);
	}

	/**
	 * Abre un diálogo de selección de archivos acorde a la parametrización.
	 * @param saveOrOpen Indica si el diálogo de decir Guardar o Abrir.
	 * @param fileSelectionMode El modo de selección. Posibles modos:
	 *             FILES_ONLY Solo selección de archivos.
	 *             DIRECTORIES_ONLY Solo selección de directorios.
	 *             FILES_AND_DIRECTORIES Selección de archivos y directorios.
	 * @param fileFilter El filtro de archivos a utilizar (Ej) para seleccionar solo archivos .zip)
	 * @param parent Permite indicar el componente padre del diálogo.
	 * @return File El archivo seleccionado o null si se canceló la selección.
	 */
	public static File obtenerArchivo(int saveOrOpen, int fileSelectionMode, FileFilter filter, Component parent) {
		File selectedFile = null;
		JFileChooser fileChooser = getFileChooserInstance(saveOrOpen, fileSelectionMode, filter, false);
		int returnValue = mostrarDialogo(saveOrOpen, parent, fileChooser);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			setLastSelectedFile(selectedFile);
		} else {
			//Para no perder el archivo seleccionado en modo sólo directorios si el usuario cancela
			setLastSelectedFile(getLastSelectedFileBackup());
		}
		if(selectedFile != null && saveOrOpen == OPEN && !selectedFile.exists()) {
			String detalleTipo;
			String detalleAdv;
			if(fileSelectionMode == DIRECTORIES_ONLY){
				detalleTipo ="La ruta ";
				detalleAdv="Ruta ";
			}else{
				if(fileSelectionMode == FILES_AND_DIRECTORIES){
					detalleTipo="El archivo o la ruta ";
					detalleAdv ="Archivo/Ruta ";
				}else{
					detalleTipo="El archivo ";
					detalleAdv ="Archivo ";
				}
			}				
			FWJOptionPane.showInformationMessage(parent, detalleTipo + selectedFile.getAbsolutePath() + " no existe.", detalleAdv + "inexistente");
			selectedFile = obtenerArchivo(saveOrOpen, fileSelectionMode, filter, parent);
		}
		if(selectedFile != null && saveOrOpen == SAVE && selectedFile.exists()) {
			if(FWJOptionPane.showQuestionMessage(parent, "El archivo " + selectedFile.getAbsolutePath() + " ya existe. ¿Desea reemplazarlo?", "Archivo ya existe") == FWJOptionPane.NO_OPTION) {
				selectedFile = obtenerArchivo(saveOrOpen, fileSelectionMode, filter, parent);
			} else {
				if(FileUtil.isFileOpened(selectedFile)) {
					FWJOptionPane.showErrorMessage(parent, "El archivo " + selectedFile.getAbsolutePath() + " se encuentra abierto." + StringUtil.RETORNO_CARRO + "Debe cerrarlo antes de guardar.", "Archvo abierto");
					selectedFile = obtenerArchivo(saveOrOpen, fileSelectionMode, filter, parent);
				}
			}
		}
		return selectedFile;
	}

	/**
	 * Abre un diálogo de selección de archivos acorde a la parametrización (permite la selección de múltiples archivos y/o directorios).
	 * @param saveOrOpen Indica si el diálogo de decir Guardar o Abrir.
	 * @param fileSelectionMode El modo de selección. Posibles modos:
	 *             FILES_ONLY Solo selección de archivos.
	 *             DIRECTORIES_ONLY Solo selección de directorios.
	 *             FILES_AND_DIRECTORIES Selección de archivos y directorios.
	 * @param parent Permite indicar el componente padre del diálogo.
	 * @return File[] Los archivos seleccionados o null si se canceló la selección.
	 */
	public static File[] obtenerArchivos(int saveOrOpen, int fileSelectionMode, Component parent) {
		return obtenerArchivos(saveOrOpen, fileSelectionMode, null, parent);
	}

	/**
	 * Abre un diálogo de selección de archivos acorde a la parametrización (permite la selección de múltiples archivos y/o directorios).
	 * @param saveOrOpen Indica si el diálogo de decir Guardar o Abrir.
	 * @param fileSelectionMode El modo de selección. Posibles modos:
	 *             FILES_ONLY Solo selección de archivos.
	 *             DIRECTORIES_ONLY Solo selección de directorios.
	 *             FILES_AND_DIRECTORIES Selección de archivos y directorios.
	 * @param fileFilter El filtro de archivos a utilizar (Ej) para seleccionar solo archivos .zip)
	 * @param parent Permite indicar el componente padre del diálogo.
	 * @return File[] Los archivos seleccionados o null si se canceló la selección.
	 */
	public static File[] obtenerArchivos(int saveOrOpen, int fileSelectionMode, FileFilter filter, Component parent) {
		File[] selectedFiles = null;
		JFileChooser fileChooser = getFileChooserInstance(saveOrOpen, fileSelectionMode, filter, true);
		int returnValue = mostrarDialogo(saveOrOpen, parent, fileChooser);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFiles = fileChooser.getSelectedFiles();
			if(selectedFiles.length > 1) {
				for(int i = 0; i < selectedFiles.length; i++) {
					File file = selectedFiles[i];
					if(file.getParentFile() == null) {
						try {
							selectedFiles[i] = new File(fileChooser.getCurrentDirectory().getCanonicalPath() + File.separator + selectedFiles[i].getName());
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
				setLastSelectedFile(selectedFiles[0].getParentFile(), true);
			} else {
				if(fileChooser.getSelectedFile() != null && fileChooser.getSelectedFile().isDirectory()) {
					setLastSelectedFile(fileChooser.getSelectedFile(), true);
				} else {
					setLastSelectedFile(fileChooser.getCurrentDirectory(), true);
				}
			}
		} else {
			//Para no perder el archivo seleccionado en modo sólo directorios si el usuario cancela.
			setLastSelectedFile(getLastSelectedFileBackup());
		}
		if(selectedFiles != null && saveOrOpen == OPEN) {
			List<String> inexistentes = new ArrayList<String>();
			for(int i = 0; i < selectedFiles.length; i++) {
				if(!selectedFiles[i].exists()) {
					inexistentes.add(selectedFiles[i].getAbsolutePath());
				}
			}
			if(!inexistentes.isEmpty()) {
				StringBuffer archivos = new StringBuffer();
				for(String archivo : inexistentes) {
					if(archivos.length() > 0) {
						archivos.append(", ");
					}
					archivos.append(archivo);
				}
				String message = null;
				String title = null;
				if(inexistentes.size() > 1) {
					message = StringW.wordWrap("Los archivos "
							+ (archivos.length() > 2048 ? (archivos.toString().substring(1, 2048) + " ...") : archivos.toString()) + " no existen.");
					title = "Archivos inexistentes";
				} else {
					message = "El archivo " + archivos.toString() + " no existe.";
					title = "Archivo inexistente";
				}
				FWJOptionPane.showInformationMessage(null, message, title);
				selectedFiles = obtenerArchivos(saveOrOpen, fileSelectionMode, filter, parent);
			}
		}
		return selectedFiles;
	}

	private static int mostrarDialogo(int saveOrOpen, Component parent, JFileChooser fileChooser) {
		int returnValue;
		if(saveOrOpen == OPEN)
			returnValue = fileChooser.showOpenDialog(parent);
		else
			returnValue = fileChooser.showSaveDialog(parent);
		return returnValue;
	}

	private static File getParentExistFile(File file) {
		while(file != null && !file.exists()) {
			file = file.getParentFile();
		}
		return file;
	}

	/**
	 * Permite indicar el último archivo seleccionado (modificar la "memoria" del componente).
	 * @param lastSelectedFile El nuevo último archivo seleccionado.
	 */
	public static void setLastSelectedFile(File lastSelectedFile) {
		setLastSelectedFile(lastSelectedFile, false);
	}

	/**
	 * Permite indicar el último archivo seleccionado (modificar la "memoria" del componente).
	 * @param lastSelectedFile El nuevo último archivo seleccionado.
	 * @param fromMultiple Permite indicar explícitamente si la selección fué múltiple.
	 */
	public static void setLastSelectedFile(File lastSelectedFile, boolean fromMultiple) {
		FWFileSelector.lastSelectedFile = lastSelectedFile;
		FWFileSelector.fromMultiple = fromMultiple;
	}

	/**
	 * 
	 * @return El último archivo seleccionado.
	 */
	public static File getLastSelectedFile() {
		if(FWFileSelector.lastSelectedFile == null)
			FWFileSelector.setLastSelectedFile((new JFileChooser()).getFileSystemView().getDefaultDirectory());
		return FWFileSelector.lastSelectedFile;
	}

	private static boolean getFromMultiple() {
		return FWFileSelector.fromMultiple;
	}

	/**
	 * 
	 * @return El prefijo existente del último archivo seleccionado.
	 * Ej) Si el último archivo seleccionado es C:\TEMP\NO_EXISTE, y NO_EXISTE no existe, retorna C:\TEMP.
	 */
	public static File getLastSelectedFileExists() {
		return getParentExistFile(getLastSelectedFile());
	}

	private static File getLastSelectedFileBackup() {
		return lastSelectedFileBackup;
	}

	private static void setLastSelectedFileBackup(File lastSelectedFileBackup) {
		FWFileSelector.lastSelectedFileBackup = lastSelectedFileBackup;
	}

}