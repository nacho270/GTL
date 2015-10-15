package ar.com.fwcommon.boss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Boss encargado de majenar todas las operaciones de IO del filesystem
 * 
 * 
 */
public class BossIO {

	private static final BossIO instance = new BossIO();
	private static final int DEFAULT_LENGTH = 8192;
	
	private BossIO() {
		super();
	}

	public static final BossIO getInstance() {
		return instance;
	}

	/**
	 * Lee un objeto del file seleccionado
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object readObjectFromFile(File file) throws IOException, ClassNotFoundException {
		FileInputStream fin = null;
		ObjectInputStream oin = null;
		
		try {
			fin = new FileInputStream(file);
			oin = new ObjectInputStream(fin);
			
			Object object = oin.readObject();
			return (object);
		} finally {
			if (oin != null) oin.close();
			if (fin != null) fin.close();
		}
	}

	/**
	 * Salva un objeto en el File especificado
	 * 
	 * @param object Objecto a Salvar
	 * @param file File donde guardar
	 * @throws IOException
	 */
	public void saveObject(Object object, File file) throws IOException {
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		
		try {
			fout = new FileOutputStream(file);
			oout = new ObjectOutputStream(fout);
			oout.writeObject(object);
			oout.flush();
		} finally {
			if (oout != null) oout.close();			
			if (fout != null) fout.close();
		}
	}

	/**
	 * Zipea el directorio especificado y sus subdirectorios recursivamente
	 * 
	 * @param dirToZip Directorio a Zipear
	 * @param zos OutputStream del zipFile
	 * @throws IOException
	 */
	public void zipDir(File dirToZip, OutputStream out) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(out);
		int numberEntries = 0;
		try {
			zos.setLevel(9);
			byte[] readBuffer = new byte[DEFAULT_LENGTH];
			int bytesIn = 0;
			final int lengthRootPath = dirToZip.getAbsolutePath().length();
			Queue<File> queueDirs = new LinkedList<File>();
			queueDirs.add(dirToZip);
			while (!queueDirs.isEmpty()) {
				File[] dirList = queueDirs.poll().listFiles();
				for (File file : dirList) {
					if (file.isDirectory()) {
						queueDirs.add(file);
					} else {
						FileInputStream fis = new FileInputStream(file);
						ZipEntry anEntry = new ZipEntry(file.getAbsolutePath().substring(lengthRootPath));
						zos.putNextEntry(anEntry);
						numberEntries++;
						while ((bytesIn = fis.read(readBuffer)) != -1) {
							zos.write(readBuffer, 0, bytesIn);
						}
						fis.close();
					}
				}
			}
			if (numberEntries == 0) {
				//BugFIX: Si no hay entries cargadas, da un error.
				zos.putNextEntry(new ZipEntry("empty.txt"));
			}
			zos.flush();
			
		} finally {
			zos.close();			
		}
	}

	/**
	 * Deszipea un file al directorio especificado
	 * 
	 * @param in InputStream del que se obtendrán los datos comprimidos
	 * @param destination Directorio de destino
	 */
	public void unZipDir(InputStream in, File destination) throws IOException {
		OutputStream out = null;
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(new BufferedInputStream(in));
			int count;
			byte data[] = new byte[8192];
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					File destFN = new File(destination, entry.getName());
					destFN.getParentFile().mkdirs();
					FileOutputStream fos = new FileOutputStream(destFN);
					out = new BufferedOutputStream(fos);
					while ((count = zis.read(data, 0, data.length)) != -1) {
						out.write(data, 0, count);
					}
					out.flush();
					out.close();
				}
			}
		} finally {			
			if (zis != null) zis.close();
			if (out != null) out.close();
		}
	}
	
	/**
	 * Dado un ZipInputStream lee la información de un entry y la escribe en el archivo pasado por parámetro
	 * 
	 * @param in 
	 * @param file Archivo a escribir
	 * @throws IOException
	 */
	public void unZipSingleEntry(ZipInputStream in, File file) throws IOException {
		int count;
		byte data[] = new byte[DEFAULT_LENGTH];
		OutputStream out = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			out = new BufferedOutputStream(fos);
			while ((count = in.read(data, 0, data.length)) != -1) {
				out.write(data, 0, count);
			}
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * Devuelve el ByteArray del archivo especificado
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public byte[] zipDir(File file) throws IOException {
		ByteArrayOutputStream bout = null;
		bout = new ByteArrayOutputStream();
		zipDir(file, bout);
		bout.flush();
		return bout.toByteArray();
	}

	/**
	 * Lee un archivo de texto y devuelve el string correspondiente
	 * 
	 * @param file Archivo a leer
	 * @return String con el contenido del archivo
	 * @throws IOException
	 */
	public String getStringFromFile(String file) throws IOException {
		BufferedReader reader = null;
		StringBuilder stringBuilder = new StringBuilder();
		
		try {
			char[] buffer = new char[DEFAULT_LENGTH];
			reader = new BufferedReader(new FileReader(file));
			int count = 0;			
			while ((count = reader.read(buffer)) != -1) {
				stringBuilder.append(buffer, 0, count);
			}
			return stringBuilder.toString();
			
		} finally {
			if (reader != null) reader.close();
		}
	}

	/**
	 * Escribe el <Code>binaryData</Code> en el <Code>file</Code> especificado.
	 * @param file Archivo en el cual se escribirá <Code>binaryData</Code>
	 * @param binaryData Contenido a escribir
	 * @throws IOException
	 */
	public void writeFile(File file, byte[] binaryData) throws IOException {
		FileOutputStream fout = null;
		BufferedOutputStream out = null;
		try {
			fout = new FileOutputStream(file);
			out = new BufferedOutputStream(fout);
			out.write(binaryData);
			out.flush();
		} finally {
			if (out != null) out.close();
			if (fout != null) fout.close();
		}
	}
	
	/**
	 * Escribe el toda la información que puede leerse en el InputStream en el
	 * <Code>file</Code> especificado.
	 * 
	 * @param file Archivo en el cual se escribirá <Code>binaryData</Code>
	 * @param binaryData Contenido a escribir
	 * @throws IOException
	 */
	public void writeFile(File file, InputStream in) throws IOException {
		OutputStream out = null;
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			out = new BufferedOutputStream(fout);
			BufferedInputStream bin = new BufferedInputStream(in);
			int read=0;
			byte[] buffer = new byte[DEFAULT_LENGTH];
			while ((read=bin.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			out.flush();
			
		} finally {
			if (out != null) out.close();
			if (fout != null) fout.close();
		}
	}

	/**
	 * Devuelve un array de bytes con toda la información del {@link InputStream} <code>in</code>.
	 * @param in
	 * @return Un array de bytes con toda la información del {@link InputStream} <code>in</code>
	 * @throws IOException
	 */
	public byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[DEFAULT_LENGTH];
			int numRead = 0;
			while((numRead = in.read(buffer)) != -1)
				out.write(buffer, 0, numRead);
			out.flush();
		} finally {
			if(in != null)
				in.close();
		}
		return out.toByteArray();
	}

	/**
	 * Devuelve el <Code>byte[]</Code> (contenido), de un <Code>file</Code> especificado. 
	 * @param file Archivo del cual se iniciará la lectura.
	 * @return 
	 * @throws IOException
	 */
	public byte[] readFile(File file) throws IOException {
		
		long fileLength = file.length();
		int bufferLength = (fileLength<DEFAULT_LENGTH)?DEFAULT_LENGTH:(int)fileLength;
		ByteArrayOutputStream out = new ByteArrayOutputStream(bufferLength);
		readFile(file, out);
		return out.toByteArray();
	}
	
	/**
	 * Lee un archivo y lo escribe en un {@link OutputStream} determinado. Este
	 * método NO cierra el output stream.
	 * 
	 * @param file Archivo a leer
	 * @param out OutputStream en donde escribir el contenido del archivo
	 * @throws IOException
	 */
	public void readFile(File file, OutputStream out) throws IOException {
		InputStream in = null;
		try {			
			in = new BufferedInputStream(new FileInputStream(file));
	        byte[] buffer = new byte[DEFAULT_LENGTH];
	    
	        int numRead = 0;
	        while ((numRead=in.read(buffer)) != -1)
	        	out.write(buffer, 0, numRead);
	        
	        out.flush();
		} finally {
			if (in != null) in.close();
		}
	}
	
	/**
	 * Escribe las propiedades a un archivo
	 * 
	 * @param prop propiedades a escribir
	 * @param file archivo en donde escribir las propiedades
	 * @throws IOException
	 */
	public void writeProperties(File file, Properties prop) throws IOException {
		writeProperties(file, prop, "");
	}

	/**
	 * Escribe las propiedades a un archivo
	 * 
	 * @param prop propiedades a escribir
	 * @param file archivo en donde escribir las propiedades
	 * @param comments comentarios a incluir en el archivo
	 * @throws IOException
	 */
	public void writeProperties(File file, Properties prop, String comments) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			prop.store(out, comments);
			out.flush();
		} finally {
			if (out != null) out.close();			
		}
	}

	/**
	 * Lee las propiedades de un archivo
	 * 
	 * @param file Archivo a leer
	 * @return Propiedades del archivo. <code>NULL</code> si el archivo no
	 *         existe
	 * @throws IOException
	 */
	public Properties readProperties(File file) throws IOException {
		if (!file.exists()) return null;
		InputStream in = null;
		
		try {
			Properties prop = new Properties();
			in = new FileInputStream(file);
			prop.load(in);
			return prop;
			
		} finally {
			if (in != null) in.close();			
		}
	}
}
