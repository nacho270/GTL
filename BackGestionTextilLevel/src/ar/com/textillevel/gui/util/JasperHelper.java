package ar.com.textillevel.gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.JTableHeader;

import main.GTLGlobalCache;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fwcommon.AnchorTrick;
import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.ElementoListado;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.jasper.JasperWrapperProperties;

public class JasperHelper {

	private final static int ANCHO_L = 801;
	private final static int ALTO_L = 468;
	private final static int ANCHO_P = 525;
	private final static int ALTO_P = 729;
	
	public static JasperReport loadReporte(String jasperPath) {
		try {
			return (JasperReport) JRLoader.loadObject(new JasperHelper().getClass().getResourceAsStream(jasperPath));
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends Map<String,Object>> JasperPrint fillReport(JasperReport report, T parameters) {
		try {
			return JasperFillManager.fillReport(report, parameters);
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends Map<String,Object>> JasperPrint fillReport(JasperReport report, T parameters, Collection<?> coleccion) {
		try {
			return JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(coleccion));
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static<T extends Map<String,Object>> JasperPrint fillReport(JasperReport report, T parameters, Connection jdbcConnection) {
		try {
			return JasperFillManager.fillReport(report, parameters, jdbcConnection);
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void exportarAPDF(JasperPrint reporte, String pdfPath) throws JRException {
		JasperExportManager.exportReportToPdfFile(reporte, pdfPath);
	}

	public static void exportarAPDF(JasperPrint reporte, File tempFile) throws JRException, FileNotFoundException {
		JasperExportManager.exportReportToPdfStream(reporte, new FileOutputStream(tempFile));
	}
	
	public static void visualizarReporte(JasperPrint reporte) {
		JasperViewer.viewReport(reporte, false);
	}

	public static Integer imprimirReporte(JasperPrint reporte, boolean conDialogoImpresion, boolean esperarPorCopia, int copias) throws JRException {
		Integer ret = 0;
		try{
			for (int i = 0; i < copias; i++) {
				JasperPrintManager.printReport(reporte, conDialogoImpresion && i==0);
				ret++;
				if (esperarPorCopia && (i+1)<copias) {
					if (FWJOptionPane.showQuestionMessage(null, "Continuar?", "Impresión") == FWJOptionPane.NO_OPTION) {
						break;
					}
				}
			}
		}catch (JRException je) {
			if (je.getCause() != null && je.getCause().getMessage().equals("Printer is not accepting job.")) {
				//throw new CLException(BossError.ERR_CONEXION, "No se puede imprimir", "La Impresora no responde o no esta accesible", je.getCause(), new String[] {"Verifique el estado de la impresora"});
				FWJOptionPane.showErrorMessage(null, "No se puede imprimir, la impresora no responde o no esta disponible. Por favor, verifique el estado", "Error");
			} else {
				//CLJOptionPane.showErrorMessage(null, "No se puede imprimir debido a que no se ha detectado una impresora.", "Error");
				//throw new CLException(BossError.ERR_CONEXION, "No se puede imprimir", "La Impresora no responde o no esta accesible", je.getCause(), null);
				throw je;
			}
		}
		return ret;
	}
	
	public static JasperPrint generarReporte(JasperWrapperProperties properties) throws JRException {
		ClassLoader cl = new AnchorTrick().getClass().getClassLoader();
		InputStream xmlCompileFile = cl.getResourceAsStream(properties.getXmlReport());
		JasperPrint jasperPrint = JasperFillManager.fillReport(xmlCompileFile, properties.getParameters(), new JRBeanCollectionDataSource(properties.getData()));
		return jasperPrint;
	}
	
	public static void imprimirListado(FWJTable tabla, String titulo, String subtitulo, String filtros, boolean confirmacion) {
		if( tabla.getRowCount()>0) {
			imprimirReporte(getPropiedadesImpresionListado(titulo, subtitulo,filtros, getData(tabla), tabla.getWidth() > ANCHO_P), confirmacion, titulo, new Dimension(810, 630), confirmacion);
		}
	}
	
	public static void listadoAPDF(FWJTable tabla, String titulo, String subtitulo, String filtros, boolean confirmacion,String path) {
		if( tabla.getRowCount()>0) {
			try {
				exportarAPDF(generarReporte(getPropiedadesImpresionListado(titulo, subtitulo,filtros, getData(tabla), tabla.getWidth() > ANCHO_P)),path);
			} catch (JRException e) {
				BossError.gestionarError(new FWException("No se pudo convertir a PDF"));
			}
		}
	}
	
	public static JasperPrint generarJasperPrint(FWJTable tabla, String titulo, String subtitulo, String filtros) {
		if(tabla.getRowCount()>0) {
			try {
				return generarReporte(getPropiedadesImpresionListado(titulo, subtitulo,filtros, getData(tabla), tabla.getWidth() > ANCHO_P));
			} catch (JRException e) {
				BossError.gestionarError(new FWException("No se pudo convertir a PDF"));
			}
		}
		return null;
	}
	
	public static void imprimirReporte(JasperWrapperProperties properties, boolean preview, String titulo, Dimension dimension, boolean confirmacion) {
		try {
			JasperPrint jasperPrint = generarReporte(properties);
			imprimirComprobante(jasperPrint, true);
		}catch (FWException e) {
			BossError.gestionarError(e);
		}
		catch (JRException e) {
			int tipoDeError = BossError.ERR_INDETERMINADO ;
			if (e.getCause() != null && e.getCause() instanceof InvalidClassException) {
				tipoDeError = BossError.ERR_APLICACION ;
			}
			BossError.gestionarError(new FWException(tipoDeError, "No se pudo imprimir el comprobante", "Error al generar el Reporte", e, new String[] {}));
		}
	}
	
	public static void imprimirComprobante(JasperPrint jasperPrint, boolean confirmacion) throws FWException {
		try {
			JasperPrintManager.printReport(jasperPrint, confirmacion);
		} catch (JRException je) {
			if (je.getCause() != null && je.getCause().getMessage().equals("Printer is not accepting job.")) {
				//throw new CLException(BossError.ERR_CONEXION, "No se puede imprimir", "La Impresora no responde o no esta accesible", je.getCause(), new String[] {"Verifique el estado de la impresora"});
				FWJOptionPane.showErrorMessage(null, "No se puede imprimir, la impresora no responde o no esta disponible. Por favor, verifique el estado", "Error");
			} else {
				FWJOptionPane.showErrorMessage(null, "No se puede imprimir debido a que no se ha detectado una impresora.", "Error");
				//throw new CLException(BossError.ERR_CONEXION, "No se puede imprimir", "La Impresora no responde o no esta accesible", je.getCause(), null);
			}
		}
	}
	
	private static JasperWrapperProperties getPropiedadesImpresionListado(String titulo, String subtitulo,String filtros, List<ElementoListado> data, boolean landscape) {
		JasperWrapperProperties properties = new JasperWrapperProperties();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("Logo", "ar/com/textillevel/imagenes/logogtl-impresion.jpg");
		parameters.put("Usuario", GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
		parameters.put("FechaImpresion", new SimpleDateFormat(DateUtil.SHORT_DATE_WITH_HOUR).format(DateUtil.getAhora()));
		parameters.put("Titulo", titulo);
		parameters.put("SubTitulo", subtitulo);
		parameters.put("Filtros", filtros);
		parameters.put("Sistema", "Gestion Textil Level");
		
		properties.setParameters(parameters);
		if(landscape) {
			properties.setXmlReport("ar/com/fwcommon/impresion/listadoTablaHorizontal.jasper");
		} else {
			properties.setXmlReport("ar/com/fwcommon/impresion/listadoTablaVertical.jasper");
		}
		properties.setData(data);
		return properties;
	}
	
	private static List<ElementoListado> getData(FWJTable tabla) {
		tabla.clearSelection();

		int anchoTabla = tabla.getWidth();
		int anchoDisponible;
		int altoDisponible;
		boolean landscape = anchoTabla > ANCHO_P;
		if(landscape) {
			anchoDisponible = ANCHO_L;
			altoDisponible = ALTO_L;
		} else {
			anchoDisponible = ANCHO_P;
			altoDisponible = ALTO_P;
		}

		int anchoImagen = Math.max(anchoTabla, anchoDisponible);
		double escala = (anchoTabla > anchoDisponible) ? (anchoDisponible / (double)anchoTabla) : 1;
		JTableHeader header = tabla.getTableHeader();
		int altoHeader = header.getHeight();
		double altoHeaderEscalado = Math.rint(altoHeader * escala);
		double altoDisponibleFilasPaginas = altoDisponible - altoHeaderEscalado;
		
		boolean copiar = tabla.getRowCount() > 0;
		List<ElementoListado> data = new ArrayList<ElementoListado>();

		BufferedImage imagenTabla = new BufferedImage(anchoTabla, tabla.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2ImagenTabla = getGraphics(imagenTabla);
		tabla.paint(g2ImagenTabla);
		
		/* Después del paint se actualiza la altura de las celdas pero no la altura de la tabla... */
		int alturaTotal = 0;
		for(int i = 0; i < tabla.getRowCount(); i++) {
			alturaTotal += tabla.getRowHeight(i);
		}
		tabla.setSize(tabla.getWidth(), alturaTotal);
		
		/* Este 2do paint es para tomar una imagen de la tabla completamente repintadad (alto multilines no visibles actualizado) */
		imagenTabla = new BufferedImage(anchoTabla, alturaTotal, BufferedImage.TYPE_INT_ARGB);
		g2ImagenTabla = getGraphics(imagenTabla);
		tabla.getHeight();
		tabla.paint(g2ImagenTabla);
		
//		int indicePagina = 0;
		int filaCorriente = 0;
		int altoAnterior = 0;
		while(copiar) {
			//Calcular alto (el alto de la imagen a incluir en la hoja)
			int alto = 0;
			while (filaCorriente < tabla.getRowCount()) {
				if ( (alto + tabla.getRowHeight(filaCorriente)) * escala < altoDisponibleFilasPaginas) {
					alto += tabla.getRowHeight(filaCorriente);
					filaCorriente++;
				} else {
					//Por si la fila corriente es mas ancha que el espacio reservado para las filas
					if(alto == 0) {
						alto += tabla.getRowHeight(filaCorriente);
						filaCorriente++;						
					}
					break;
				}
			}
			if(filaCorriente == tabla.getRowCount()) {
				copiar = false;
			} 
			if(alto > 0 && anchoTabla >  0) {
				BufferedImage subimagenTabla = imagenTabla.getSubimage(0, altoAnterior, anchoTabla, alto);
				BufferedImage imagenHoja = new BufferedImage(anchoImagen + 1, (int)(altoDisponible/escala), BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g2ImagenHoja = getGraphics(imagenHoja);
				//Poner la imagen en blanco
				g2ImagenHoja.setColor(Color.WHITE);
				g2ImagenHoja.fillRect(0, 0, anchoImagen + 1, (int)(altoDisponible/escala));
				//Dibujar el header
				g2ImagenHoja.translate(2, 0);
				header.paint(g2ImagenHoja);
				//?
				g2ImagenHoja.setColor(Color.WHITE);
				g2ImagenHoja.fillRect(header.getX() + anchoTabla, 0, anchoDisponible - anchoTabla, altoHeader);
				//Filas
				g2ImagenHoja.translate(-2, 0);
				g2ImagenHoja.drawImage(subimagenTabla, 2, altoHeader, Color.WHITE, null);
				//?
				g2ImagenHoja.setColor(tabla.getGridColor());
				g2ImagenHoja.drawLine(anchoTabla, 0, anchoTabla, alto + altoHeader);
				g2ImagenHoja.setColor(tabla.getGridColor());
				g2ImagenHoja.drawLine(1, 0, 1, alto + altoHeader);
				g2ImagenHoja.setColor(tabla.getGridColor());
				g2ImagenHoja.drawLine(0, alto + altoHeader - 1, anchoTabla, alto + altoHeader - 1);
				g2ImagenHoja.scale(escala, escala);
				data.add(new ElementoListado(imagenHoja));
//				indicePagina++;
			}
			altoAnterior += alto;
		}
		return data;
	}
	
	private static Graphics2D getGraphics(BufferedImage bi) {
		Graphics2D g = bi.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return g;
	}
}
