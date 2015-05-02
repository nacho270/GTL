package ar.com.textillevel.gui.util;



/**
 * Clase encargada de renderizar los reportes. 
 * 
 *  
 * 
 * @author sdominguez
 *
 */
@Deprecated
public class ServicioImpresionJasper{
	
//	private String baseDir   ="";
//	private String outputDir ="";
//	private static String WEB_SEPARATOR= "/";
//	
//	private static Logger log = Logger.getLogger(ServicioImpresionJasper.class);
//	
//	public JasperPrint fillReport( Object[] datos, String reportName , Map parametros ) throws Exception {
//		
//		log.debug("Intentando cargar el reporte " + reportName);
//		InputStream reporte = this.getClass().getClassLoader().getResourceAsStream("jasperreports/" +reportName );
//		if (reporte == null){
//			reporte = ClassLoader.getSystemClassLoader().getResourceAsStream("jasperreports/" +reportName);
//		}
//		if(reporte == null){
//			log.error("No se pudo cargar el reporte " + reportName );
//			throw new CLException("No se pudo cargar el reporte " + reportName );
//		}
//
//		log.debug("Cargando el dataSources para el reporte");
//		JRDataSource ds = new JRBeanArrayDataSource(datos); 
//		//return JasperFillManager.fillReport(jasperReport,parametros,ds);
//		return JasperFillManager.fillReport(reporte,parametros,ds);
//
//		
//	}	
//	
//	@SuppressWarnings("unchecked")
//	public String generarPdf(Object[] datos, String reportName, Map parametros , String outFileName, Boolean muestraPDF) throws Exception {
//		
//		log.debug("Completo el reporte con los datos correspondientes");
//		JasperPrint jp = fillReport(datos, reportName, parametros);
//		
//		log.debug("Exportando el reporte a PDF.");
//		byte[] bytes = JasperExportManager.exportReportToPdf(jp) ;
//		
//		log.debug("Ingresando el javascript correspondiente.");
//				
//		log.debug("Seteando las propiedades del pdf.");
//		PdfReader reader = new PdfReader(bytes);
//		String now = String.valueOf(new Date().getTime());
//		String outPutFile = getOutputDir()+  System.getProperty("file.separator")  + outFileName +"_"+now+".pdf";
//		log.debug("outPutFile= " + outPutFile);
//		
//		String outPutFileWeb = getOutputDir()+  WEB_SEPARATOR  + outFileName +"_"+now+".pdf";
//		
//		log.debug("Creando el archivo " + getBaseDir()+ outPutFile);
//		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(getBaseDir()+outPutFile));
//		
//		StringBuffer javascript = new StringBuffer();
//		
//		if(muestraPDF) {
//		
//			javascript.append(" app.toolbar = false; ");
//		
//		} else {
//
//			javascript.append(" this.print({bUI: false,bSilent: false,bShrinkToFit: true}); ");
//		}
//
//		stamper.addJavaScript(javascript.toString());		
//		stamper.close();
//		
//		log.debug("Archivo creado satisfactoriamente" +  getBaseDir()+ outPutFile);
//		log.debug("Devuelvo el nombre del archivo, con el formato web");
//		log.debug("outPutFileWeb= " + outPutFileWeb);
//		return outPutFileWeb;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public byte[] generarPdfToBytes(Object[] datos, String reportName, Map parametros) throws Exception {
//		
//		log.debug("Completo el reporte con los datos correspondientes");
//		JasperPrint jp = fillReport(datos,reportName,parametros);
//		
//		log.debug("Exportando el reporte a PDF.");
//		byte[] bytes = JasperExportManager.exportReportToPdf(jp) ;
//
//		log.debug("Ingresando el javascript correspondiente.");
//		StringBuffer javascript = new StringBuffer();
//		javascript.append(" app.toolbar = false; ");
//		
//		log.debug("Seteando las propiedades del pdf.");
//		PdfReader reader = new PdfReader(bytes);
//		
//		//String n = String.valueOf(new Date().getTime());		
//		ByteArrayOutputStream ba = new ByteArrayOutputStream();
//		//PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(getBaseDir()+outPutFile));
//		PdfStamper stamper = new PdfStamper(reader, ba);
//		stamper.addJavaScript(javascript.toString());
//		stamper.close();
//		return ba.toByteArray();
//	}
//
//	/**
//	 * Devuelve el directorio donde se guardaran los reportes generados (Archivos.pdf)
//	 * 
//	 * 
//	 * @return
//	 */
//	public String getOutputDir() {
//		return outputDir;
//	}
//
//	public void setOutputDir(String outputDir) {
//		this.outputDir = outputDir;
//	}
//
//	/**
//	 * Devuelve el directorio absoluto que se toma como base para 
//	 * lo demas reportes relativos.
//	 * 
//	 * @return
//	 */
//	public String getBaseDir() {
//		return baseDir="/";
//	}
//
//	/**
//	 * Setea el directorio absoluto que se toma como base para 
//	 * lo demas reportes relativos.
//	 * 
//	 * @return
//	 */
//	public void setBaseDir(String baseDir) {
//		this.baseDir = baseDir;
//	}
}
