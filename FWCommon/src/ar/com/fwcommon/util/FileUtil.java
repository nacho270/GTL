package ar.com.fwcommon.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.apache.commons.io.FileSystemUtils;

import ar.com.fwcommon.AnchorTrick;
import ar.com.fwcommon.componentes.error.FWException;

/**
 * Clase con funciones útiles relacionadas con archivos y carpetas.
 */
@SuppressWarnings("rawtypes")
public class FileUtil {

	public static final long MEGABYTE = 1024 * 1024;

    /**
     * Devuelve la URL del recurso situado en la ruta especificada.
     * @param path La ruta donde está situado el recurso.
     * @return
     */
	public static URL getResource(String path) {
        ClassLoader cl = new AnchorTrick().getClass().getClassLoader();
        return cl.getResource(path);
    }

    /**
     * Devuelve la URL del recurso situado en la ruta especificada.
     * @param c
     * @param path La ruta donde está situado el recurso.
     * @return
     */
    public static URL getResource(Class c, String path) {
        return c.getClassLoader().getResource(path);
    }

    /**
     * Devuelve el recurso situado en la ruta especificada como un java.util.File.
     * @param c
     * @param path La ruta donde está situado el recurso.
     * @return
     * @throws URISyntaxException
     */
    public static File getResourceFile(Class c, String path) throws URISyntaxException {
    	return new File(getResource(c, path).toURI());
    }

    /**
     * Devuelve el recurso situado en la ruta especificada como stream.
     * @param path La ruta donde está situado el recurso.
     * @return
     */
    public static InputStream getResourceAsStream(String path) {
        ClassLoader cl = new AnchorTrick().getClass().getClassLoader();
        return cl.getResourceAsStream(path);
    }

    /**
     * Devuelve el <b>CRC-32 checksum</b> del archivo <b>file</b>.  
     * @param file el archivo a calcular el checksum
     * @return  checksum un long con el CRC-32 Checksum del archivo
     * @throws Exception
     */
    @SuppressWarnings("resource")
	public static long getChecksum(File file) throws Exception {
        long checksum = -1;
        CheckedInputStream cis = null;
        //Computa el CRC-32 checksum
        cis = new CheckedInputStream(new FileInputStream(file), new CRC32());
        byte[] buf = new byte[128];
        while(cis.read(buf) >= 0) {
        }
        checksum = cis.getChecksum().getValue();
        return checksum;
    }

    /**
     * Valida que el path contenga como separador sólo '/' o '\' pero no ambos a la vez.
     * @param path El path a validar.
     * @return True o false.
     */
    public static boolean validarPath(String path) {
        if((path.indexOf("/") != -1) && (path.indexOf("\\") != -1)) {
            return false;
        }
        return true;
    }

    /**
     * Transforma el path de un archivo al formato utilizado en el equipo.
     * Ej) /tmp/file.txt => \tmp\file.txt
     * @param in El path a corregir.
     * @return El path corregido.
     */
    public static String fixPath(String in) {
        StringBuffer out = new StringBuffer();
        String separatorLocal = File.separator;
        String separatorError = null;
        if(separatorLocal.equals("\\")) {
            separatorError = "/";
        } else {
            separatorError = "\\";
        }
        int i = in.indexOf(separatorError);
        if(i == -1) {
            out.append(in);
        } else {
            //String[] sa = in.split(separatorClient);
            String[] sa = split(in, separatorError);
            for(int j = 0; j < sa.length; j++) {
                out.append(sa[j]);
                if(j < sa.length - 1) {
                    out.append(separatorLocal);
                }
            }
        }
        return out.toString();
    }

    private static String[] split(String source, String separator) {
        String[] ret = null;
        int i = source.indexOf(separator);
        if(i == -1) {
            ret = new String[1];
            ret[0] = source;
            return ret;
        } else {
            Vector<String> aux = new Vector<String>();
            aux.add(source.substring(0, i));
            int j = source.indexOf(separator, i + separator.length());
            while(j != -1) {
                aux.add(source.substring(i + separator.length(), j));
                i = j;
                j = source.indexOf(separator, i + separator.length());
            }
            if(source.substring(i + 1).length() > 0) {
                aux.add(source.substring(i + 1));
            }
            ret = new String[aux.size()];
            int k = 0;
            for(Iterator iter = aux.iterator(); iter.hasNext();) {
                ret[k++] = (String)iter.next();
            }
            return ret;
        }
    }

    /**
     * Copia un directorio completo (con todas los archivos y subcarpetas que contiene)
     * a otro directorio.
     * @param origen El directorio a copiar.
     * @param destino El directorio donde se copiará el directorio de origen.
     * @throws IOException
     */
	public static void copiarDirectorio(File origen, File destino) throws IOException {
		if(origen.isDirectory()) {
			if(!destino.exists()) {
				destino.mkdir();
			}
			String[] hijos = origen.list();
			for(String hijo : hijos) {
				copiarDirectorio(new File(origen, hijo), new File(destino, hijo));
			}
		} else {
			copiarArchivo(origen, destino);
		}
	}

    /**
     * Copia un archivo.
     * @param origen El archivo a copiar.
     * @param destino El archivo de destino de la copia.
     * @throws IOException
     */
	public static void copiarArchivo(File origen, File destino) throws IOException {
		writeToFile(new FileInputStream(origen), destino);
		/*
		byte[] buffer = new byte[1024];
		int len;
		InputStream fis = new FileInputStream(origen);
        OutputStream fos = new FileOutputStream(destino);
        while((len = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fis.close();
        fos.close();
        */
	}


	
	
	/**
	 * Mueve un archivo
	 * @param origen El archivo a mover
	 * @param destino El archivo de destino
	 * @return True si se pudo mover el archivo 
	 */
	public static boolean moverArchivo(File origen, File destino) {
		return origen.renameTo(destino);
	}

    /**
     * Borra un directorio con todos sus archivos y subcarpetas.
     * @param dir El directorio a borrar.
     */
    public static void borrarDirectorio(File dir) {
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            for(File f : files) {
                if(f.isDirectory()) {
                    borrarDirectorio(f);
                } else {
                    f.delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * Borra un directorio con todos sus archivos y subcarpetas.
     * @param dir El directorio a borrar.
     */
    public static void borrarDirectorio(String nombreDir) {
        borrarDirectorio(new File(nombreDir));
    }

    /**
     * Crea un directorio.
     * @param dir El directorio a crear.
     */
    public static boolean crearDirectorio(File dir) {
        if(dir.exists()) {
            borrarDirectorio(dir);
        }
        return dir.mkdir();
    }

    /**
     * Crea un directorio.
     * @param nombreDir El directorio a crear.
     */
    public static boolean crearDirectorio(String nombreDir) {
        return crearDirectorio(new File(nombreDir));
    }

    /**
     * Devuelve la extensión del archivo especificado.
     * Por ej. para el archivo con nombre <b>ArchivoComprimido.zip</b>
     * devuelve <b>zip</b>.
     * @param archivo
     * @return
     */
    public static String getFileExtension(File archivo) {
        return getFileExtension(archivo.getName());
    }

    /**
     * Devuelve la extensión del archivo especificado.
     * Por ej. para el archivo con nombre <b>ArchivoComprimido.zip</b>
     * devuelve <b>zip</b>.
     * @param nombreArchivo
     * @return
     */
    public static String getFileExtension(String nombreArchivo) {
    	int i = nombreArchivo.lastIndexOf('.');
    	return (i == -1) ? null : nombreArchivo.substring(i + 1);
    }

    /**
     * Devuelve el nombre del archivo sin la extensión.
     * Por ej. para el archivo con nombre <b>ArchivoComprimido.zip</b>
     * devuelve <b>ArchivoComprimido</b>.
     * @param archivo
     * @return
     */
    public static String stripFileExtension(File archivo) {
        return stripFileExtension(archivo.getName());
    }

    /**
     * Devuelve el nombre del archivo sin la extensión.
     * Por ej. para el archivo con nombre <b>ArchivoComprimido.zip</b>
     * devuelve <b>ArchivoComprimido</b>.
     * @param nombreArchivo
     * @return
     */
    public static String stripFileExtension(String nombreArchivo) {
        return nombreArchivo.substring(0, nombreArchivo.indexOf('.'));
    }

    /**
     * Busca un archivo en el directorio especificado.
     * Si lo encuentra lo devuelve. Si no lo encuentra devuelve null.
     * @param dir El directorio donde se busca el archivo.
     * @param nombreArchivo El nombre del archivo a buscar.
     * @return
     */
    public static File buscarArchivo(File dir, String nombreArchivo) {
        File[] files = dir.listFiles();
        for(File file : files) {
            if(file.getName().compareToIgnoreCase(nombreArchivo) == 0) {
                return file;
            }
        }
        return null;
    }

    /**
     * @param archivo
     * @return True si existe el archivo.
     */
    public static boolean existeArchivo(File archivo) {
    	return archivo.exists();
    }

    /**
     * Devuelve <b>true</b> si el path especificado contiene espacios en blanco.
     * @param path
     * @return
     */
    public static boolean contieneEspacios(String path) {
        String space = StringUtil.ESPACIO_BLANCO;
        String sep = (path.indexOf("\\") != -1) ? "\\" : "/";
        String[] tokens = StringUtil.getTokens(path, sep);
        for(String token : tokens) {
            if(token.startsWith(space) || token.endsWith(space)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina los espacios en blanco del path especificado y devuelve
     * el path resultante.
     * @param path
     * @return
     */
    public static String eliminarEspacios(String path) {
        StringBuffer newPath = new StringBuffer("");
        String sep = (path.indexOf("\\") != -1) ? "\\" : "/";
        boolean existeSeparadorFinal = (path.lastIndexOf(sep) == (path.length() - 1));
        String[] tokens = StringUtil.getTokens(path, sep);
        for(String token : tokens) {
            if(newPath.toString().compareTo("") != 0) {
                newPath.append(sep);
            }
            newPath.append(token.trim());
        }
        if(existeSeparadorFinal) {
            newPath.append(sep);
        }
        return newPath.toString();
    }

    /**
     * Devuelve el espacio libre en el disco en bytes.
     * @return La cantidad en bytes de espacio libre en el disco.
     * @throws IOException
     */
    public static long getEspacioEnDisco() throws IOException {
        return FileSystemUtils.freeSpace(getDirectorioPorDefecto().getAbsolutePath());
    }

    /**
     * Devuelve <b>true</b> si el tamaño del archivo especificado es
     * menor que el espacio total en el disco.
     * @param archivo
     * @return
     * @throws IOException
     */
    public static boolean espacioEnDisco(File archivo) throws IOException {
        return espacioEnDisco(archivo.length());
    }

    /**
     * Devuelve <b>true</b> si el tamaño del archivo especificado es
     * menor que el espacio total en el disco.
     * @param tamanioArchivo El tamaño del archivo en bytes.
     * @return
     * @throws IOException
     */
    public static boolean espacioEnDisco(long tamanioArchivo) throws IOException {
        return tamanioArchivo < getEspacioEnDisco();
    }

    /**
     * Devuelve el directorio por defecto del Sistema Operativo.
     * En el caso de MS Windows devuelve el directorio <b>Mis Documentos</b>.
     * @return
     */
    public static File getDirectorioPorDefecto() {
        return new File(System.getProperty("user.home"));
    }

    public URL toURL(File file) throws MalformedURLException {
    	String path = file.getAbsolutePath();
    	if((File.separatorChar != '/')) {
    		path = path.replace(File.separatorChar, '/');
    	}
    	if((!path.startsWith("/"))) {
    		path = "/" + path;
    	}
    	if((!path.endsWith("/") && file.isDirectory())) {
    		path = path + "/";
    	}
    	return new URL("file", "", path);
    }

    /**
     * Devuelve <b>true</b> si el archivo pasado por parámetro ya se encuentra abierto.
     * @param file El archivo a verficar.
     * @return
     */
    public static boolean isFileOpened(File file) {
    	boolean rv = false;
    	FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch(FileNotFoundException e) {
			rv = true;
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch(IOException e) {
			}
		}
    	return rv;
    }

    /**
     * También en BossIO
     * Returns the contents of the file in a byte array
     * 
     */
	@SuppressWarnings("resource" )
	public static byte[] getBytesFromFile(File file) throws FWException {
		InputStream is;
		try {
			is = new FileInputStream(file);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new FWException("El archivo no existe", e);
		}
		long length = file.length();
		if(length > Integer.MAX_VALUE) {
			throw new FWException("El archivo es demasiado grande");
		}
		byte[] bytes = new byte[(int)length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		try {
			while(offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
		} catch(IOException e) {
			e.printStackTrace();
			throw new FWException("No se pudo leer del archivo", e);
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
				throw new FWException("No se pudo cerrar el archivo", e);
			}
		}
		// Ensure all the bytes have been read in
		if(offset < bytes.length) {
			throw new FWException("No se pudo leer completamente el archivo " + file.getName());
		}
		return bytes;
	}

	
	/**
	 * Tambien en BossIO. 
	 * Write the contents of the byte array in a file
	 * @param bytes
	 * @param file
	 * @throws FWException
	 */
	public static void writeToFile(byte[] bytes, File file) throws FWException {
		//TODO: quitar este metodo o llamar a BossIO desde aqui.
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new FWException("El archivo no existe", e);
		}
		// Write out the bytes
		try {
			fos.write(bytes);
		} catch(IOException e) {
			e.printStackTrace();
			throw new FWException("No se pudo escribir el archivo", e);
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
				throw new FWException("No se pudo cerrar el archivo", e);
			}
		}
	}
	
	/**
	 * Tambien en BossIO
	 * @param origen
	 * @param destino
	 * @throws IOException
	 */
	public static void writeToFile(InputStream origen, File destino) throws IOException {
		//TODO: quitar este metodo o llamar a BossIO desde aqui.
		byte[] buffer = new byte[1024];
		int len;
		InputStream fis = origen;
        OutputStream fos = new FileOutputStream(destino);
        while((len = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fis.close();
        fos.close();		
	}

    /**
     * Verifica si los directorios que contiene el archivo existen y si no los crea.
     * @param archivo
     * @throws IOException
     */
    public static void verificarYCrearDirectorios(String archivo) throws IOException {
        File directorioTemporal;
        char separador = File.separatorChar;
        int indiceSeparador = archivo.lastIndexOf(separador);
        String path = archivo.substring(0, indiceSeparador + 1);
        String pathTruncado = path;
        String pathTemporal = "";
        while(pathTruncado.compareTo("") != 0) {
            indiceSeparador = pathTruncado.indexOf(separador) + pathTemporal.length();
            pathTemporal = path.substring(0, indiceSeparador + 1);
            directorioTemporal = new File(pathTemporal);
            if(directorioTemporal.exists()) {
            } else {
                //Creo el directorio si no existe
                if(directorioTemporal.mkdir()) {
                } else {
                    System.err.println("verificarDirectorio: ERROR - No se pudo crear el directorio " + directorioTemporal.getName());
                    throw new IOException("verificarDirectorio: ERROR - No se pudo crear el directorio " + directorioTemporal.getName());
                }
            }
            pathTruncado = path.substring(indiceSeparador + 1, path.length());
        }
    }

    /**
     * Verifica que el directorio exista y si no existe intenta crearlo.
     * @param directorio
     * @throws FWException
     */
    public static void mkdirs(String directorio) throws FWException {
    	File file = new File (directorio) ;
    	try {
    		file.mkdirs() ;
    	} catch (Exception e) {
    		throw new FWException("No se pudo crear el directorio " + directorio, e) ;
    	}
    	if (!file.exists()) {
    		throw new FWException ("No se pudo crear el directorio " + directorio) ;
    	} 
    }

    public static String concatenarPaths(String dir1, String dir2,char c) {
		try {
			String ultimoChar1 = dir1.substring(dir1.length() - 1);
			String primerChar2 = dir2.substring(0, 1);
			if(isSeparator(ultimoChar1)) {
				dir1 = dir1.substring(0, dir1.length() - 1);
			}
			if(isSeparator(primerChar2)) {
				dir2.substring(1);
			}
		} catch(NullPointerException e) {
		} catch(IndexOutOfBoundsException e) {
		}
		return dir1 + c + dir2;

	}
 
    
	public static String concatenarPaths(String dir1, String dir2) {
		return concatenarPaths(dir1, dir2,File.separatorChar);
	}

	private static boolean isSeparator(String str) {
		if(str.equals("/") || str.equals("\\")) {
			return true;
		}
		return false;
	}	
	
	public static String eliminarDrive(String path) {
		if (path.charAt(1) == ':')
			return path.substring(2) ;
		return path ;
	}
}