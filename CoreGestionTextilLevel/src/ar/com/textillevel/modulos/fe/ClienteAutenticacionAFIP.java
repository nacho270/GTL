package ar.com.textillevel.modulos.fe;


import java.io.FileInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.Base64;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class ClienteAutenticacionAFIP {
	
	private static final Logger logger = Logger.getLogger(ClienteAutenticacionAFIP.class);

	private static String invocarAutenticacion(byte[] loginTicketRequestXMLCMS, String endpoint) throws Exception {
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		call.setOperationName("loginCms");
		call.addParameter("request", XMLType.XSD_STRING, ParameterMode.IN);
		call.setReturnType(XMLType.XSD_STRING);
		return (String) call.invoke(new Object[] { Base64.encode(loginTicketRequestXMLCMS) });
	}

	private static byte[] crearCMS(String p12file, String p12pass, String signer, String dstDN, String service, Long ticketTime) throws Exception {
		KeyStore ks = KeyStore.getInstance("pkcs12");
		FileInputStream p12stream = new FileInputStream(p12file);
		ks.load(p12stream, p12pass.toCharArray());
		p12stream.close();

		PrivateKey pKey = (PrivateKey) ks.getKey(signer, p12pass.toCharArray());
		X509Certificate pCertificate = (X509Certificate) ks.getCertificate(signer);
		String signerDN = pCertificate.getSubjectDN().toString();

		ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
		certList.add(pCertificate);

		if (Security.getProvider("BC") == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		CertStore cstore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
		String loginTicketRequestXML = crearLoginTicketRequest(signerDN, dstDN, service, ticketTime);

		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		gen.addSigner(pKey, pCertificate, CMSSignedDataGenerator.DIGEST_SHA1);
		gen.addCertificatesAndCRLs(cstore);
		CMSProcessable data = new CMSProcessableByteArray(loginTicketRequestXML.getBytes());
		CMSSignedData signed = gen.generate(data, true, "BC");
		return signed.getEncoded();
	}

	private static String crearLoginTicketRequest(String signerDN, String dstDN, String service, Long ticketTime) {
		String loginTicketRequestXML;
		Date GenTime = new Date();
		GregorianCalendar gentime = new GregorianCalendar();
		GregorianCalendar exptime = new GregorianCalendar();
		String uniqueId = new Long(GenTime.getTime() / 1000).toString();
		exptime.setTime(new Date(GenTime.getTime() + ticketTime));
		XMLGregorianCalendarImpl xmlGenTime = new XMLGregorianCalendarImpl(gentime);
		XMLGregorianCalendarImpl xmlExpTime = new XMLGregorianCalendarImpl(exptime);

		loginTicketRequestXML = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+"<loginTicketRequest version=\"1.0\">"
				+"<header>"
					+"<source>" + signerDN + "</source>"
					+"<destination>" + dstDN + "</destination>"
					+"<uniqueId>" + uniqueId + "</uniqueId>"
					+"<generationTime>" + xmlGenTime + "</generationTime>"
					+"<expirationTime>" + xmlExpTime + "</expirationTime>"
				+"</header>"
				+"<service>" + service + "</service>"
			+"</loginTicketRequest>";
		return loginTicketRequestXML;
	}
	
	public static AuthAFIPData crearAutorizacion(ConfiguracionFacturaElectronica configFE) throws Exception {
		logger.info("Creando pedido de autorización AFIP...");
		byte[] LoginTicketRequest_xml_cms = crearCMS(configFE.getKeyStore(), 
													 configFE.getKeyStorePass(), 
													 configFE.getKeyStoreSigner(),
													 configFE.getDestinoServicio(),
													 configFE.getServicio(),
													 configFE.getDuracion());
		logger.info("invocando servicio de autorización AFIP...");
		String LoginTicketResponse = invocarAutenticacion(LoginTicketRequest_xml_cms, configFE.getEndpointAutenticacion());
		Reader tokenReader = new StringReader(LoginTicketResponse);
		Document tokenDoc = new SAXReader(false).read(tokenReader);

		String token = tokenDoc.valueOf("/loginTicketResponse/credentials/token");
		String sign = tokenDoc.valueOf("/loginTicketResponse/credentials/sign");
		Long cuitEmpresa = Long.valueOf(System.getProperty("textillevel.fe.cuitEmpresa"));
		logger.info("autorización AFIP obtenida...");
		return new AuthAFIPData(token, sign, cuitEmpresa);
	}
}