package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import ar.com.fwcommon.util.FileUtil;

public class FileReaderWrapper {

	private final String file;

	public FileReaderWrapper(String file) {
		this.file = file;
	}

	public String obtenerContenido() throws Exception{
		StringBuffer contenido = new StringBuffer();
		DataInputStream in = new DataInputStream(FileUtil.getResourceAsStream(file));
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String linea = null;
		while ( (linea = reader.readLine()) != null){
			contenido.append(linea);
		}
		in.close();
		return contenido.toString();
	}
}
