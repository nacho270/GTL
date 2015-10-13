/**
 * 
 */
package ar.com.fwcommon.templates.main.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JSeparator;

import org.apache.log4j.Logger;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.entidades.GrupoModulos;
import ar.com.fwcommon.entidades.GrupoModulos.NewItemMenu;
import ar.com.fwcommon.entidades.Modulo;
import ar.com.fwcommon.templates.main.FWMainTemplate;

/**
 * <p>Maneja los menues de modulos para un CLMainTemplate</p>
 * Delega la lectura y acceso a la configuración de menues a <b>LectorConfiguracionMenues</b> 
 * 
 * Esta clase, basandose en la configuración de menues, se encarga de agregar los menues y sus modulos
 * ; siempre llamando a la api provista por CLMainTemplate
 * 
 * Los menues de modulos se crean exclusivamente desde esta clase.
 * Hay otros menues que no (Ayuda/Ventana/Impresion...)
 *   
 */
public class ManejadorMenues {

	static Logger logger = Logger.getLogger(ManejadorMenues.class);
	
	 
	private FWMainTemplate mainTemplate;
	private Map<GrupoModulos,MenuModulosSimple> mapGrupoMenu = new HashMap<GrupoModulos,MenuModulosSimple>();
	private static ManejadorMenues _instance;
	private LectorConfiguracionMenues configuracionMenues;
	
	public  List<GrupoModulos> getGruposModulos() {
		if (configuracionMenues==null)
			throw new FWRuntimeException("this.configuracionMenues es NULL- Debe leer el xml de menues antes de pedirlo");
		
		return configuracionMenues.getGruposModulos();
	}
	
	private LectorConfiguracionMenues getConfiguracionMenues (){
		return this.configuracionMenues;
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized static ManejadorMenues getManejadorMenues (FWMainTemplate mainTemplate) {
		if (_instance != null)
			return _instance;
		
		
		ManejadorMenues manejadorMenues = new ManejadorMenues (mainTemplate);
		manejadorMenues.configuracionMenues = new LectorConfiguracionMenues();
		
		manejadorMenues.configuracionMenues.crearConfiguracion();
		
		
		manejadorMenues.inicializarMenues();
		_instance = manejadorMenues;
		return manejadorMenues;
	}
	

	 
	
	private void inicializarMenues() {
		GrupoModulos grupoDefault = this.configuracionMenues.getGrupoDefault();
		MenuModulosPrincipal menuModulosDefault = new MenuModulosPrincipal();
		menuModulosDefault.setText(grupoDefault.getNombre());
		menuModulosDefault.getMenuItemCambiarUsuario().addActionListener(mainTemplate.new CambiarUsuarioListener());
		menuModulosDefault.getMenuItemSalir().addActionListener( mainTemplate.new SalirListener());
		this.mainTemplate.agregarMenu(menuModulosDefault, 0);
		Set<GrupoModulos> yaprocesados  = new HashSet<GrupoModulos>();
		yaprocesados.add(grupoDefault);
		//menues.add(menuModulosDefault);
		mapGrupoMenu.put(grupoDefault, menuModulosDefault);
		
		
		int orden =1;
		for (GrupoModulos grupo: this.getGruposModulos()){
			if (yaprocesados.contains(grupo))
				continue;
			MenuModulosSimple menuModulos = new MenuModulosSimple();				
			menuModulos.setText(grupo.getNombre());
			this.mainTemplate.agregarMenu(menuModulos, orden);
			//menues.add(menuModulos);
			mapGrupoMenu.put(grupo,menuModulos);
			orden++;
		}
		
	}


	private ManejadorMenues (FWMainTemplate mainTemplate) {
		this.mainTemplate = mainTemplate;
	}
	

	
	public MenuModulosPrincipal getMenuDefault() {
		return (MenuModulosPrincipal)registerMenu(this.getConfiguracionMenues().getGrupoDefault());
	}
	
	
	private MenuModulosSimple getMenuPorModulo (Modulo modulo){
		GrupoModulos grupo = getConfiguracionMenues().getGrupoPorModulo(modulo);
		return registerMenu(grupo);
	}		
	
	
	
	private MenuModulosSimple registerMenu (GrupoModulos grupo){
		MenuModulosSimple menuGrupo = mapGrupoMenu.get(grupo);
		if (menuGrupo == null){
			menuGrupo = new MenuModulosSimple();
			menuGrupo.setText( grupo.getNombre());
			mapGrupoMenu.put(grupo, menuGrupo);
		}
		return menuGrupo;
	}
	
	

	/**
	 * limpia todos los menues de modulos. Incluye submenues.
	 */
	public void limpiarModulos(){
		for (MenuModulosSimple menu: this.mapGrupoMenu.values()){
			menu.limpiarModulos();
		}
	}
	
	/**
	 * Recibe un modulo.
	 * Determina en qué menu debe insertarlo (s/conf), y lo inserta.
	 * Crea submenues a medida que los necesita
	 * 
	 * @param modulo
	 */
	@SuppressWarnings("unchecked")
	public void agregarModulo(Modulo modulo) {
		
		if(modulo.isSeparador()) {
			int pos = this.getMenuDefault().getMenuComponentCount() - 3;
			if (pos <= 0 || !(this.getMenuDefault().getMenuComponents()[pos-1] instanceof JSeparator)){
				this.getMenuDefault().add(new JSeparator(), pos);
			}
			return;
		} 
		
		//chequeo que existe el submenu donde va este modulo
		checkExisteSubmenu(modulo);
		
		MenuModulosSimple menuModulos = getMenuPorModulo(modulo);
		
		
		
		try {
			if(menuModulos.hasDecorator()) {
				//Carga el ícono al módulo
				Icon icono = menuModulos.getMenuDecorator().getIcono(modulo.getUbicacion());
				//Agrega el módulo al menú
				menuModulos.agregarModulo(modulo.getNombre(), icono, mainTemplate.new ModuloListener(modulo));
			} else {
				//Agrega el módulo al menú
				menuModulos.agregarModulo(modulo.getNombre(), mainTemplate.new ModuloListener(modulo));
			}
		} catch(ClassNotFoundException e) { //Si el nombre de la clase es inválido
			menuModulos.agregarModulo(modulo.getNombre(), null);
			FWException exception = new FWException(BossError.ERR_APLICACION, "Problema construyendo el menú de la aplicación",
					"No se pudo cargar la clase (" + modulo.getUbicacion() + ") asociada al módulo " + modulo.getNombre(), e,
					new String[] { "Verifique la información del módulo en el Portal de Aplicaciones" });
			BossError.gestionarError(exception);
		} catch(NullPointerException e) { //Si la ubicación es null
			menuModulos.agregarModulo(modulo.getNombre(), null);
			FWException exception = new FWException(BossError.ERR_APLICACION, "Problema construyendo el menú de la aplicación",
					"La ubicación del módulo '" + modulo.getNombre() + "' es nula", e,
					new String[] { "Verifique la información del módulo en el Portal de Aplicaciones" });
			BossError.gestionarError(exception);
		}
	
	}

	private void checkExisteSubmenu(Modulo modulo) {
		GrupoModulos grupo = getConfiguracionMenues().getGrupoPorModulo(modulo);
		MenuModulosSimple menuModulosGrupo = registerMenu(grupo);
		
		if (grupo.getPadre() != null){
			MenuModulosSimple menuPadre = mapGrupoMenu.get(grupo.getPadre());
			if (!Arrays.asList(menuPadre.getMenuComponents()).contains(menuModulosGrupo)){
				menuPadre.agregarSubmenu(menuModulosGrupo);
			}
		}
	}

	/**
	 * mostrar Iconos en TODOS los menues de modulos
	 */
	public void mostrarIconos() {
		for ( MenuModulosSimple menu: this.mapGrupoMenu.values()){
			if(menu.hasDecorator()) {
				menu.mostrarIconos();
			}	
		}
	}

	/**
	 * agrega modulos que no se leyeron desde el portal 
	 * 
	 * @param modulosUsuarioAplicacion
	 */
	public void completarModulosSinPortal(List<Modulo> modulosUsuarioAplicacion) {
		for (GrupoModulos grupo: this.getGruposModulos()){
			grupo.completarModulosSinPortal(modulosUsuarioAplicacion);	
		}
	}

	/**
	 * Informa cuantos <b>menues de modulos</b> se crearon o se crearán Arriba.
	 * No toma en cuenta submenues, ni otros menues (ej: no cuenta Ayuda/Impresion...)
	 * 
	 * @return
	 */
	public byte getCantidadMenuesArriba() {
		byte cnt = 0;
		for (GrupoModulos grupo: this.getConfiguracionMenues().getGruposModulos()){
			cnt += (grupo.getPadre() == null?1:0);
		}
		return cnt;
	}

	/**
	 * Crea e inserta los submenues especificados en el XML
	 * Se insertaran en el orden declarado.
	 * Se insertaran al inicio, (como hace windows con los directorios)
	 * 
	 */
	public void iniciarSubmenues() {
		for (GrupoModulos grupo: this.getConfiguracionMenues().getGruposModulos()){
			if (grupo.getPadre() == null){
				/* sin ordenación
				Collections.sort(grupo.getSubgrupos(), new Comparator<GrupoModulos>(){
					public int compare(GrupoModulos g1, GrupoModulos g2) {
						return g1.getNombre().compareTo(g2.getNombre());
					}
				});
				*/
				
				for (GrupoModulos subgrupo : grupo.getSubgrupos()){
					MenuModulosSimple submenu = registerMenu(subgrupo);
					mapGrupoMenu.get(grupo).agregarSubmenu(submenu);
				}
				
			}
		}
	}

	/**
	 * Util que busca un modulo en una lista según el Id. 
	 * @param modulos
	 * @param idMod
	 * @return
	 */
	private Modulo findModulo (List<Modulo> modulos, Integer idMod){
		for (Modulo m: modulos){
			if (m.getIdModulo() == idMod )
				return m;
		}
		return null;
	}
	
	/**
	 * recursivo.
	 * Recorrido en orden de un arbol.
	 * Ver {@link #reordenamiento(List)}
	 * 
	 * @param todosLosModulos
	 * @param modulosOrdenandose
	 * @param grupos
	 */
	private void reordenamiento (List<Modulo> todosLosModulos,List<Modulo> modulosOrdenandose, List<GrupoModulos> grupos){
		for (GrupoModulos grupo: grupos){
			if (!grupo.getSubgrupos().isEmpty()){
				reordenamiento (todosLosModulos, modulosOrdenandose,grupo.getSubgrupos());
			}
			for (Object modulo : grupo.getIdsModuloYNuevos()){
				if (modulo instanceof Integer){
					Modulo m = findModulo(todosLosModulos, (Integer)modulo);
					if (m!=null){
						modulosOrdenandose.add(m);
					}
				}else if (modulo instanceof NewItemMenu){
					modulosOrdenandose.add((Modulo)modulo);
				}
			}
		}	
	}
	
	/**
	 * Reordenamiento de los modulos, según XML.
	 * <ol>
	 * <li>Toma el xml, y reordena los modulos del listado.</li>
	 * <li>Recorre el arbol, el orden es: primero los hijos, luego yo (recursivo).</li>
	 * <li>Luego inserta los que no están en el árbol.</li>
	 * </ol>
	 * @param modulos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Modulo> reordenamiento(List<Modulo> modulos) {
		
		List<Modulo> modulosOrdenandose = new ArrayList<Modulo>(); 
		reordenamiento(modulos, modulosOrdenandose, this.getConfiguracionMenues().getGruposModulos());
		
		Collection disjuncion = restaConjuntosOrdenada(modulos,modulosOrdenandose );
		modulosOrdenandose.addAll(disjuncion);
		
		return modulosOrdenandose;
		
	}
	
	/**
	 *{p tq/ p pert IZQ ^ p no pert DER}
	 * @param <T>
	 * @param izq
	 * @param der
	 * @return
	 */
	private<T> Collection<T> restaConjuntosOrdenada (Collection<T> izq, Collection<T> der){
		ArrayList<T> aux = new ArrayList<T>();
		for (T obj: izq){
			if (!der.contains(obj)){
				aux.add(obj);
			}
		}
		return aux;
	}
	
	
	
}