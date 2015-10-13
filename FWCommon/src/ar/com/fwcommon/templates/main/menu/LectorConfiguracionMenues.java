/**
 * 
 */
package ar.com.fwcommon.templates.main.menu;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.entidades.GrupoModulos;
import ar.com.fwcommon.entidades.GrupoModulos.NewItemMenu;
import ar.com.fwcommon.entidades.Modulo;
import ar.com.fwcommon.util.FileUtil;
import ar.com.fwcommon.util.xml.XmlUtils;
/**
 * Lee la configuracion desde organizacionMenues.xml
 * Si encuentra el archivo crea una estructura en memoria, 
 * 	que indica la configuración de los menues.
 * 
 * Provee una api para acceder a esa configuración.
 * 
 * Usado por ManejadorMenues, que es quien organiza los menues del CLMainTemplate
 * 
 */
class LectorConfiguracionMenues {
	private List<GrupoModulos> gruposModulos;

	private static List<GrupoModulos> leerGruposModulos(InputStream streamMenues) throws XPathExpressionException {
		Document doc = XmlUtils.parse(streamMenues);
		
	    XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr = xpath.compile("/menues/GrupoModulos");

	    NodeList nodosGrupos = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		List<GrupoModulos> gruposModulos = new ArrayList<GrupoModulos>();
		for ( int i=0; i<nodosGrupos.getLength() ;i++ ){
			Node item = nodosGrupos.item(i);
			GrupoModulos grupo = armarGrupoModulos(item);
			grupo.setYaCreado(true);//no se crean en demanda
			gruposModulos.add(grupo);
		}
		
		//orden
		/* todavia no se usa
		expr = xpath.compile("/menues/ordenacion[@metodo = 'reordena-alf' or @metodo = 'ord-declarado' ]/@metodo");
		String metodoOrd = (String)expr.evaluate(doc, XPathConstants.STRING);
		for (GrupoModulos grupo: gruposModulos){
			if (grupo.getMetodoOrdenacion() == null){
				grupo.setMetodoOrdenacion(metodoOrd);
			}
		}
		*/
		
		return gruposModulos;
	}	

	
	private static GrupoModulos armarGrupoModulos(Node item) throws XPathExpressionException {
		if(!item.getNodeName().equals("GrupoModulos")){
			throw new FWRuntimeException("parametro no valido. NOdo: " + item.getNodeName());
		}
		
		//inicializar el grupo
		GrupoModulos grupo = new GrupoModulos();
		grupo.setNombre(item.getAttributes().getNamedItem("nombre").getNodeValue());
		Node niEsDefault = item.getAttributes().getNamedItem("esDefault");
		if (niEsDefault!=null && niEsDefault.getNodeValue() != null){
			grupo.setEsDefault(niEsDefault.getNodeValue().equals("true"));	
		}
		
		//--------------------
		//leer: ids de modulos E 
		//items creados manualmente (sin portal)
	    XPath xpath2 = XPathFactory.newInstance().newXPath();
	    XPathExpression expr2 = xpath2.compile("./listaModulos/item/text() | ./listaModulos/newItem");
	    NodeList listaIdsModuloOnewItem = (NodeList)expr2.evaluate(item, XPathConstants.NODESET);
	    
		for(int j=0; j < listaIdsModuloOnewItem.getLength() ;j++ ) {
			Node miModulo = listaIdsModuloOnewItem.item(j);
			if (miModulo.getNodeName().equals("newItem")){
				NamedNodeMap attr = miModulo.getAttributes();

				boolean action = false;
				Node namedItem = attr.getNamedItem("action");
				action = namedItem != null && namedItem.getNodeValue().equals("true");
				
				NewItemMenu newItem = new NewItemMenu(grupo,Integer.parseInt(attr.getNamedItem("id").getNodeValue()),attr.getNamedItem("nombre").getNodeValue(),attr.getNamedItem("class").getNodeValue(), action);
				grupo.getIdsModuloYNuevos().add(newItem);
			}else{
				grupo.getIdsModuloYNuevos().add(Integer.parseInt(miModulo.getNodeValue()));
			}
			//System.out.println("nv:" + idModulo.getNodeValue());
		}
		
		
		//---------------
		// leer subgrupos
		XPathExpression expr3 = XPathFactory.newInstance().newXPath().compile("./listaModulos/GrupoModulos");
		NodeList nlListaSubgrupos = (NodeList)expr3.evaluate(item, XPathConstants.NODESET);
		for(int j=0; j < nlListaSubgrupos.getLength() ;j++ ) {
			Node nGrupo = nlListaSubgrupos.item(j);
			GrupoModulos subgrupoArmado = armarGrupoModulos(nGrupo);
			subgrupoArmado.setYaCreado(false);//se va a crear por demanda
			subgrupoArmado.setPadre(grupo);
			grupo.getSubgrupos().add(subgrupoArmado);
		}
		//--------------
		return grupo;
	}	

	
	public void crearConfiguracion (){
		// Intento leer lo' menuce'...
		InputStream streamMenues = FileUtil.getResourceAsStream("organizacionMenues.xml");
		gruposModulos = new ArrayList<GrupoModulos>();
		try {
			if (streamMenues != null){
				gruposModulos = leerGruposModulos(streamMenues);	
			}
		} catch(XPathExpressionException e) {
			ManejadorMenues.logger.error("Error procesando xml de menues",e);
		}
		
		
		
		if (gruposModulos.isEmpty()){
			GrupoModulos gm = new GrupoModulos();
			gm.setNombre("Módulos");
			gm.setEsDefault(true);
			gruposModulos.add(gm);
		}
		
	}


	public  List<GrupoModulos> getGruposModulos() {
		return gruposModulos;
	}
	
	GrupoModulos getGrupoDefault(){
		for (GrupoModulos grupo: getGruposModulos()){
			if (grupo.isEsDefault()){
				return grupo;
			}
		}
		return null;
	}		
	
	
	public GrupoModulos getGrupoPorModulo (Modulo modulo){
		//para modulos creados desde el XML
		if (modulo instanceof NewItemMenu){
			GrupoModulos grupo = ((NewItemMenu) modulo).getGrupoPadre();
			//registerMenu(grupo);
			return grupo;
		}
		// para modulos normales (portal)
		for(GrupoModulos grupo : this.getGruposModulos()){
			if (grupo.getListaModulos().contains(modulo.getIdModulo())){
				return grupo;
			}else{
				GrupoModulos subgrupo= grupo.getSubgrupoPorIdModulo (modulo.getIdModulo());
				if (subgrupo != null){
					//registerMenu(subgrupo);
					return subgrupo;
				}
			}
		}
		return this.getGrupoDefault();
	}		
	
	
}