package ar.com.fwcommon.util.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ar.com.fwcommon.componentes.error.FWRuntimeException;


/**
 * Clase con funciones varias XML.
 */
public class XmlUtils {

	private static Logger logger = Logger.getLogger(XmlUtils.class);
	
	/**
	 * Toma un Documento XML (DOM?) y lo serializa con xalan a UTF-8 
	 *  
	 * @param document
	 * @param outFile
	 */
	public static void serializeUTF8 (Document document, File outFile) throws FWRuntimeException{
         try {
        	 //hardcodeo el parser XML
        	 System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
             //
        	 TransformerFactory transformerFactory = TransformerFactory.newInstance();
             Transformer transformer = transformerFactory.newTransformer();
             transformer.setOutputProperty(OutputKeys.INDENT, "yes");
             transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

             DOMSource inputSource = new DOMSource(document);

             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8"));
			 StreamResult streamResult = new StreamResult(outputStreamWriter);

             transformer.transform(inputSource, streamResult);
         } catch (FileNotFoundException e) {
             logger.error("x",e);
             throw new FWRuntimeException(e);
         } catch (TransformerException e) {
        	 logger.error("x",e);
		   	 logger.info("javax.xml.parsers.DocumentBuilderFactory:" + javax.xml.parsers.DocumentBuilderFactory.newInstance().getClass());
		   	 logger.info("javax.xml.parsers.SAXParserFactory:" + javax.xml.parsers.SAXParserFactory.newInstance().getClass());	        	 
             throw new FWRuntimeException(e);
         }
 	 }

	/**
	 * Toma un Documento XML (DOM?) y lo serializa con xalan a ISO-8859-1 (~ANSI/ISO-LATIN-1/CP1252 son todos parecidos)  
	 *  
	 * @param document
	 * @param outFile
	 */
	 public static void serializeANSI(Document document, File outFile) {
         try {
        	 //hardcodeo el parser XML
        	 System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
             //
        	 TransformerFactory transformerFactory = TransformerFactory.newInstance();
             Transformer transformer = transformerFactory.newTransformer();
             transformer.setOutputProperty(OutputKeys.INDENT, "yes");
             transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
             
             DOMSource inputSource = new DOMSource(document);
             FileWriter fileWriter = new FileWriter(outFile);
             StreamResult streamResult = new StreamResult(fileWriter);

             transformer.transform(inputSource, streamResult);
         } catch (FileNotFoundException e) {
        	 logger.error("x",e);
        	 throw new FWRuntimeException(e);
         } catch (TransformerException e) {
        	 logger.error("x",e);
		   	 logger.info("javax.xml.parsers.DocumentBuilderFactory:" + javax.xml.parsers.DocumentBuilderFactory.newInstance().getClass());
		   	 logger.info("javax.xml.parsers.SAXParserFactory:" + javax.xml.parsers.SAXParserFactory.newInstance().getClass());        	 
        	 throw new FWRuntimeException(e);
         } catch (IOException e) {
        	 logger.error("x",e);
        	 throw new FWRuntimeException(e);
         }
 	 }	

	 /**
	  * Un error listener del Transfomer de xalan, util para loguear algun error.
	  * @return
	  */
	 public static ErrorListener getXmlErrorListener(){
		 return xmlErrorListener;
	 }
	 
     private static ErrorListener xmlErrorListener = new ErrorListener(){

			public void error(TransformerException ex) throws TransformerException {
				logger.error("Transformer Exception:" + ex.getMessage(), ex);
				SourceLocator locator = ex.getLocator();
				logger.error("Detalles: MyL=" + ex.getMessageAndLocation() );
				if (locator!=null)
					logger.error("Location:" + "| linea:"+ locator.getLineNumber() + "|col:"+ locator.getPublicId());
			}	
			
			public void fatalError(TransformerException ex) throws TransformerException {
				logger.error("Transformer Exception:" + ex.getMessage(), ex);
				SourceLocator locator = ex.getLocator();
				logger.error("Detalles: MyL=" + ex.getMessageAndLocation() );
				if (locator!=null)
					logger.error("Location:" + "| linea:"+ locator.getLineNumber() + "|col:"+ locator.getPublicId());
			}

			public void warning(TransformerException ex) throws TransformerException {
				logger.error("Detalles: MyL=" + ex.getMessageAndLocation() + "| linea:"+ ex.getLocator().getLineNumber() + "|col:"+ ex.getLocator().getPublicId() );
				SourceLocator locator = ex.getLocator();
				logger.error("Detalles: MyL=" + ex.getMessageAndLocation() );
				if (locator!=null)
					logger.error("Location:" + "| linea:"+ locator.getLineNumber() + "|col:"+ locator.getPublicId());					
			}
  	 
   };	 	 

   /**
    * Parse the XML file and create Document
    * @param is
    * @return Document
    */
   public static Document parse(InputStream is) {
     Document document = null;
     // Initiate DocumentBuilderFactory
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

     // To get a validating parser
     factory.setValidating(false);
     // To get one that understands namespaces
     factory.setNamespaceAware(true);

     try {
       // Get DocumentBuilder
       DocumentBuilder builder = factory.newDocumentBuilder();
       // Parse and load into memory the Document
       document = builder.parse( is);
       //builder.pa
       
       
       return document;

     } catch (SAXParseException spe) {
       // Error generated by the parser
       System.out.println("\n** Parsing error , line " + spe.getLineNumber()
                          + ", uri " + spe.getSystemId());
       System.out.println(" " + spe.getMessage() );
       // Use the contained exception, if any
       Exception x = spe;
       if (spe.getException() != null)
         x = spe.getException();
       x.printStackTrace();
     } catch (SAXException sxe) {
       // Error generated during parsing
       Exception x = sxe;
       if (sxe.getException() != null)
         x = sxe.getException();
       x.printStackTrace();
     } catch (ParserConfigurationException pce) {
       // Parser with specified options can't be built
       pce.printStackTrace();
     } catch (IOException ioe) {
       // I/O error
       ioe.printStackTrace();
     }

     return null;
   }
   
   

}