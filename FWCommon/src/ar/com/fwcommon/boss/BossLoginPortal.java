package ar.com.fwcommon.boss;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Accion;
import ar.com.fwcommon.entidades.Aplicacion;
import ar.com.fwcommon.entidades.GrupoAccion;
import ar.com.fwcommon.entidades.Modulo;
import ar.com.fwcommon.entidades.Rol;
import ar.com.fwcommon.entidades.Usuario;
import ar.com.fwcommon.util.Configuracion;
import ar.com.fwcommon.util.StringUtil;

/**
 * Clase que permite comunicarse con el Web Service de Login y validar el acceso de un
 * usuario a una aplicación determinada.
 */
public class BossLoginPortal {

	private static BossLoginPortal instance;
	private String wsEndpoint;
	private String wsNombre;
	private Map<Integer, Aplicacion> cacheAplicacion;
	public static final int USUARIO_INVALIDO = -1;
	public static final int PASSWORD_INVALIDA = 0;
	public static final int ERR_CONEXION = -2;

	/* Constructor */
	private BossLoginPortal() {
		String wsEndpoint = Configuracion.getLoginEndpoint();
		String wsNombre = Configuracion.getLoginNombre();
		if(wsEndpoint == null || wsNombre == null) {
			FWException e = new FWException(BossError.ERR_APLICACION, "El nombre del Web Service o la Dirección del Web Service es nula.",
					"No se pudo cargar la configuración de la BDD", null, new String[] { "Verifique la configuración en la BDD" });
			BossError.gestionarError(e);
			System.exit(-1);
		}
		this.wsEndpoint = wsEndpoint;
		this.wsNombre = wsNombre;
		this.cacheAplicacion = new HashMap<Integer, Aplicacion>();
	}

	/** Método singleton */
	public static BossLoginPortal getInstance() {
		if(instance == null) {
			instance = new BossLoginPortal();
		}
		return instance;
	}

	/**
	 * Devuelve el <b>endpoint</b> del Web Service de Login.
	 * @return wsEndpoint El endpoint del servicio.
	 */
	public String getWsEndpoint() {
		return wsEndpoint;
	}

	/**
	 * Devuelve el <b>nombre</b> del Web Service de Login.
	 * @return wsNombre el nombre del servicio. 
	 */
	public String getWsNombre() {
		return wsNombre;
	}

	/**
	 * Chequea que el usuario sea válido a través del WS.
	 * El WS devuelve el <b>id</b> del usuario en caso de que sea válido.
	 * En caso contrario devuelve </b>BossLoginPortal.USUARIO_INVALIDO</b> si el usuario es inválido
	 * o <b>BossLoginPortal.PASSWORD_INVALIDA</b> si la password es inválida.
	 * @param usuario El nombre de usuario.
	 * @param password La contraseña del usuario.
	 * @return idUsuario El id del usuario logueado o alguno de los códigos de error.
	 * @throws FWException
	 */
	public int verificarUsuario(String usuario, String password) throws FWException {
		String wsDireccion = wsEndpoint + wsNombre;
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsDireccion));
			call.setOperationName(new QName("verificarUsuario"));
			call.addParameter("usuario", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_INT);
			return (Integer)call.invoke(new Object[] { usuario, password });
		} catch(Exception e) {
			throw new FWException("El servicio de autentificación " + wsDireccion + " no responde. Inténtelo mas tarde." + StringUtil.RETORNO_CARRO + "Si el problema persiste comuníquese con Mesa de Ayuda.", e);
		}
	}

	/**
	 * Chequea que el usuario sea válido a través del WS.
	 * El WS devuelve el <b>id</b> del usuario en caso de que sea válido.
	 * En caso contrario devuelve </b>BossLoginPortal.USUARIO_INVALIDO</b> si el usuario es inválido
	 * o <b>BossLoginPortal.PASSWORD_INVALIDA</b> si la password es inválida.
	 * @param usuario El nombre de usuario.
	 * @param password La contraseña del usuario.
	 * @return idUsuario El id del usuario logueado o alguno de los códigos de error.
	 * @throws FWException
	 */
	public int verificarUsuarioSeguro(String usuario, String password) throws FWException {
		String wsDireccion = wsEndpoint + wsNombre;
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsDireccion));
			call.setOperationName(new QName("verificarUsuarioSeguro"));
			call.addParameter("usuario", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_INT);
			return (Integer)call.invoke(new Object[] { usuario, password });
		} catch(Exception e) {
			throw new FWException("El servicio de autentificación " + wsDireccion + " no responde. Inténtelo mas tarde." + StringUtil.RETORNO_CARRO + "Si el problema persiste comuníquese con Mesa de Ayuda.", e);
		}
	}

	/**
	 * Devuelve un array con los distintos <b>contextos</b> del usuario.
	 * @param usuario El nombre de usuario.
	 * @return contextos Un array con los distintos contextos del usuario.
	 * @throws FWException
	 */
	public String[] getContextosUsuario(String usuario) throws FWException {
		String[] contextos = null;
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getContextosUsuario"));
			call.addParameter("usuario", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.SOAP_ARRAY);
			contextos = (String[])call.invoke(new Object[] { usuario });
			return contextos;
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar los contextos para el usuario " + usuario, e);
		}
	}

	/**
	 * Devuelve la <b>cantidad de contextos</b> para un usuario.
	 * @param usuario El nombre de usuario.
	 * @return La cantidad de contextos del usuario.
	 * @throws FWException
	 */
	public int getCantContextosUsuario(String usuario) throws FWException {
		String[] contextos = getContextosUsuario(usuario);
		return (contextos == null) ? 0 : contextos.length;
	}

	/**
	 * Accede al WS LoginService del cual obtiene un XML con los <b>módulos</b> a los cuales el usuario puede
	 * acceder para la aplicación pasada por parámetro.
	 * Se parsea el XML y se devuelve una lista de objetos <b>Modulo</b>.
	 * @param idUsuario El nro. de id del usuario.
	 * @param idAplicacion El nro. de id de la aplicación.
	 * @return modulos La lista de módulos de la aplicación a los que el usuario tiene acceso.
	 * @throws FWException
	 */
	public List<Modulo> getModulosPorUsuarioAplicacion(int idUsuario, int idAplicacion) throws FWException {
		try {
			List<Modulo> modulos = new ArrayList<Modulo>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getModulosPorAplicacion"));
			call.addParameter("idUsuario", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.addParameter("idAplicacion", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { new Integer(idUsuario), new Integer(idAplicacion) });
			//Lee el XML que devuelve el WS
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element root = document.getRootElement();
			for(int i = 0; i < root.getChildren().size(); i++) {
				Element element = (Element)root.getChildren().get(i);
				//Instancia un objeto de la clase Modulo y lo agrega a la lista
				Modulo modulo = new Modulo();
				modulo.setIdModulo(Integer.valueOf(element.getAttributeValue("id")).intValue());
				modulo.setNombre(element.getAttributeValue("nombre"));
				modulo.setUbicacion(element.getAttributeValue("ubicacion"));
				modulo.setOrden(element.getAttributeValue("orden") == null ? null : Integer.parseInt(element.getAttributeValue("orden")));
				modulos.add(modulo);
			}
			return modulos;
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar los módulos de la aplicación con id " + idAplicacion, e);
		}
	}

	/**
	 * Accede al WS LoginService del cual obtiene un XML con los datos de la aplicación (incluye los módulos).
	 * Se parsea el XML y se devuelve una lista de objetos <b>Modulo</b>.
	 * @param idUsuario El nro. de id del usuario.
	 * @param idAplicacion El nro. de id de la aplicación.
	 * @return modulos La lista de módulos de la aplicación a los que el usuario tiene acceso.
	 * @throws FWException
	 */
	public Aplicacion getDatosAplicacion(int idAplicacion) throws FWException {
		if(cacheAplicacion.containsKey(idAplicacion)) {
			return cacheAplicacion.get(idAplicacion);
		}
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getDatosAplicacion"));
			call.addParameter("idAplicacion", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { idAplicacion });
			//Lee el XML que devuelve el WS
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element root = document.getRootElement();
			//Obtener datos aplicacion
			Aplicacion aplicacion = new Aplicacion();
			aplicacion.setIdAplicacion(idAplicacion);
			aplicacion.setDescripcion(root.getAttributeValue("nombre"));
			Element elementModulos = (Element)root.getChildren().get(0);
			//Obtener modulos
			List<Modulo> modulos = new ArrayList<Modulo>();
			for(int i = 0; i < elementModulos.getChildren().size(); i++) {
				Element element = (Element)elementModulos.getChildren().get(i);
				//Instancia un objeto de la clase Modulo y lo agrega a la lista
				Modulo modulo = new Modulo();
				modulo.setIdModulo(Integer.valueOf(element.getAttributeValue("id")).intValue());
				modulo.setNombre(element.getAttributeValue("nombre"));
				modulo.setUbicacion(element.getAttributeValue("ubicacion"));
				modulo.setOrden(element.getAttributeValue("orden") == null ? null : Integer.parseInt(element.getAttributeValue("orden")));
				modulos.add(modulo);
			}
			aplicacion.setModulos(modulos);
			cacheAplicacion.put(idAplicacion, aplicacion);
			return aplicacion;
		} catch(Exception e) {
			throw new FWException(BossError.ERR_CONEXION, "No se pudieron recuperar los datos de la aplicación con id " + idAplicacion, e, new String[] {"Verifique conectividad con el Web Service '" + wsEndpoint + wsNombre + "'.", "Verifique conectividad con la BD del Portal de Aplicaciones."});
		}
	}

	/**
	 * Accede al WS LoginService del cual obtiene un XML con el <b>módulo</b>.
	 * Se parsea el XML y se devuelve una lista de objetos <b>Modulo</b>.
	 * @param idModulo El id del modulo.
	 * @return El Modulo con id idModulo.
	 * @throws FWException
	 */
	public Modulo getModulo(int idAplicacion, int idModulo) throws FWException {
		List<Modulo> modulos = getModulosPorAplicacion(idAplicacion);
		for(Modulo modulo : modulos) {
			if(modulo.getIdModulo() == idModulo) {
				return modulo;
			}
		}
		return null;
	}

	/**
	 * Accede al WS LoginService del cual obtiene un XML con la lista de <b>módulos</b>
	 * asociados a la aplicacion pasada por parámetro.
	 * Se parsea el XML y se devuelve una lista de objetos <b>Modulo</b>.
	 * @param idAplicacion El id de la aplicación.
	 * @return La lista de módulos de la aplicación.
	 * @throws FWException
	 */
	public List<Modulo> getModulosPorAplicacion(int idAplicacion) throws FWException {
		Aplicacion aplicacion = getDatosAplicacion(idAplicacion);
		return aplicacion.getModulos();
	}

	/**
	 * Devuelve los <b>nombres de usuario</b> que pueden acceder a la aplicación pasada por parámetro.
	 * @param idAplicacion El nro. de id de la aplicación.
	 * @return usuarios La lista de nombres de usuario que tienen acceso a la aplicación.
	 * @throws FWException
	 */
	public List<String> getNombresUsuarioPorAplicacion(int idAplicacion) throws FWException {
		try {
			List<String> usuarios = new ArrayList<String>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getUsuariosPorAplicacion"));
			call.addParameter("idAplicacion", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { new Integer(idAplicacion) });
			//Lee el XML que devuelve el WS
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element root = document.getRootElement();
			for(int i = 0; i < root.getChildren().size(); i++) {
				Element element = (Element)root.getChildren().get(i);
				//Agrega el usuario a la lista
				usuarios.add(element.getAttributeValue("nombre"));
			}
			return usuarios;
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar los nombres de usuario para la aplicación con id " + idAplicacion, e);
		}
	}

	/**
	 * Devuelve los <b>usuarios</b> que pueden acceder a la aplicación pasada por parámetro.
	 * @param idAplicacion El nro. de id de la aplicación.
	 * @return usuarios La lista de usuarios que tienen acceso a la aplicación.
	 * @throws FWException
	 */
	public List<Usuario> getUsuariosPorAplicacion(int idAplicacion) throws FWException {
		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getUsuariosPorAplicacion"));
			call.addParameter("idAplicacion", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { new Integer(idAplicacion) });
			//Lee el XML que devuelve el WS
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element root = document.getRootElement();
			for(int i = 0; i < root.getChildren().size(); i++) {
				Element element = (Element)root.getChildren().get(i);
				//Crea y carga el objeto usuario
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(Integer.valueOf(element.getAttributeValue("id")));
				usuario.setNombre(element.getAttributeValue("nombre"));
				usuario.setCommonName(element.getAttributeValue("commonName"));
				//Agrega el usuario a la lista
				usuarios.add(usuario);
			}
			return usuarios;
		} catch(Exception e) {
			String mensaje = "No se pudieron recuperar los usuarios para la aplicación con id " + idAplicacion ;
			throw new FWException(BossError.ERR_INDETERMINADO, mensaje, mensaje, e, null);
		}
	}

	/**
	 * Devuelve todos los roles disponibles para la aplicación pasada por parámetro.
	 * @param idAplicacion El nro. de id de la aplicación.
	 * @return roles La lista de roles disponibles.
	 * @throws FWException
	 */
	public List<Rol> getRolesPorAplicacion(int idAplicacion) throws FWException {
		try {
			List<Rol> roles = new ArrayList<Rol>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getRolesPorIdAplic"));
			call.addParameter("idAplicacion", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { new Integer(idAplicacion) });
			//Lee el XML que devuelve el WS
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element root = document.getRootElement();
			for(int i = 0; i < root.getChildren().size(); i++) {
				Element element = (Element)root.getChildren().get(i);
				//Agrega el rol a la lista
				Rol rol = new Rol();
				rol.setIdRol(Integer.valueOf(element.getAttributeValue("Id")));
				rol.setNombre(element.getAttributeValue("Nombre"));
				rol.setDescripcion(element.getAttributeValue("Descripcion"));
				roles.add(rol);
			}
			return roles;
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar los roles para la aplicación con id " + idAplicacion, e);
		}
	}

	/**
	 * Devuelve todos los roles disponibles para el usuario y la aplicación pasados por parámetro.
	 * @param idUsuario El nro. de id del usuario.
	 * @param idAplicacion El nro. de id de la aplicación.
	 * @return roles La lista de roles disponibles.
	 * @throws FWException
	 */
	public List<Rol> getRolesPorUsuarioAplicacion(int idUsuario, int idAplicacion) throws FWException {
		try {
			List<Rol> roles = new ArrayList<Rol>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getRolesPorIdUsuario"));
			call.addParameter("idUsuario", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.addParameter("idAplicacion", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { new Integer(idUsuario), new Integer(idAplicacion) });
			//Lee el XML que devuelve el WS
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element root = document.getRootElement();
			for(int i = 0; i < root.getChildren().size(); i++) {
				Element element = (Element)root.getChildren().get(i);
				//Agrega el rol a la lista
				Rol rol = new Rol();
				rol.setIdRol(Integer.valueOf(element.getAttributeValue("Id")));
				rol.setNombre(element.getAttributeValue("Nombre"));
				rol.setDescripcion(element.getAttributeValue("Descripcion"));
				roles.add(rol);
			}
			return roles;
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar los roles para el usuario con id " + idUsuario + " y la aplicación con id " + idAplicacion, e);
		}
	}

	/**
	 * Devuelve todos los usuarios a partir de un rol.
	 * @param idRol El nro. de id del rol.
	 * @return La lista de usuarios.
	 * @throws FWException
	 */
	public List<Usuario> getUsuariosPorRol(int idRol) throws FWException {
		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getUsuariosPorRol"));
			call.addParameter("idRol", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { idRol });
			if(xml != null) {
				//Lee el XML que devuelve el WS
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(new StringReader(xml));
				Element root = document.getRootElement();
				for(int i = 0; i < root.getChildren().size(); i++) {
					Element element = (Element)root.getChildren().get(i);
					//Agrega el usuario a la lista
					Usuario usuario = new Usuario();
					usuario.setIdUsuario(Integer.valueOf(element.getAttributeValue("id")));
					usuario.setNombre(element.getAttributeValue("nombre"));
					usuario.setCommonName(element.getAttributeValue("commonName"));
					usuarios.add(usuario);
				}
				return usuarios;
			} else {
				throw new FWException("No se pudieron recuperar los usuarios asociados al rol con id " + idRol);
			}
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar los usuarios asociados al rol con id " + idRol, e);
		}
	}

	/**
	 * Devuelve el <b>rol</b> asociado al usuario y la aplicación pasadas por parámetro.
	 * @param idUsuario El id del usuario.
	 * @param idAplicacion El id de la aplicación.
	 * @return El rol asociado al usuario.
	 * @throws FWException
	 */
	public Rol getRolPorUsuarioAplicacion(int idUsuario, int idAplicacion) throws FWException {
		List<Rol> roles = getRolesPorUsuarioAplicacion(idUsuario, idAplicacion);
        return ((roles == null) || (roles.isEmpty())) ? null : roles.get(0);
	}

	/**
	 * Graba un nuevo usuario a través del WS.
	 * @param nombre
	 * @param password
	 * @param commonName
	 * @param organization
	 * @param organizationalUnit
	 * @param idRol
	 * @return Si se pudo grabar el usuario devuelve <b>BossLoginPortal.OK</b>.
	 * 			En caso contrario devuelve <b>BossLoginPortal.ERROR</b>.
	 * @throws FWException
	 */
	public int grabarUsuario(String nombre, String password, String commonName, String organization, String organizationalUnit, int idRol) throws FWException {
		String wsDireccion = wsEndpoint + wsNombre;
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsDireccion));
			call.setOperationName(new QName("grabarUsuario"));
			call.addParameter("nombre", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("commonName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("organization", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("organizationalUnit", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("idRol", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_INT);
			return (Integer)call.invoke(new Object[] { nombre, password, commonName, organization, organizationalUnit, idRol });
		} catch(Exception e) {
			throw new FWException("El servicio de autentificación " + wsDireccion + " no responde. Inténtelo mas tarde." + StringUtil.RETORNO_CARRO + "Si el problema persiste comuníquese con Mesa de Ayuda.", e);
		}
	}

	/**
	 * Modifica un nuevo usuario a través del WS.
	 * @param idUsuario
	 * @param nombre
	 * @param password
	 * @param commonName
	 * @param organization
	 * @param organizationalUnit
	 * @param idRol
	 * @param idAplicacion
	 * @return Si se pudo grabar el usuario devuelve <b>BossLoginPortal.OK</b>.
	 * 			En caso contrario devuelve <b>BossLoginPortal.ERROR</b>.
	 * @throws FWException
	 */
	public int modificarUsuario(int idUsuario, String nombre, String password, String commonName, String organization, String organizationalUnit, int idRol, int idAplicacion) throws FWException {
		String wsDireccion = wsEndpoint + wsNombre;
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsDireccion));
			call.setOperationName(new QName("modificarUsuario"));
			call.addParameter("idUsuario", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.addParameter("nombre", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("commonName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("organization", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("organizationalUnit", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("idRol", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.addParameter("idAplicacion", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_INT);
			return (Integer)call.invoke(new Object[] { idUsuario, nombre, password, commonName, organization, organizationalUnit, idRol, idAplicacion });
		} catch(Exception e) {
			throw new FWException("El servicio de autentificación " + wsDireccion + " no responde. Inténtelo mas tarde." + StringUtil.RETORNO_CARRO + "Si el problema persiste comuníquese con Mesa de Ayuda.", e);
		}
	}

	/**
	 * @param ids El array de ids de usuarios.
	 * @return Una lista de usuarios del Portal a partir de un array de ids de usuarios.
	 * @throws FWException
	 */
	public List<Usuario> getUsuariosPorIds(Integer[] ids) throws FWException {
		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getUsuariosPorIds"));
			call.addParameter("ids", XMLType.SOAP_ARRAY, ParameterMode.IN); //TODO: Verificar XMLType.SOAP_ARRAY
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { ids });
			if(xml != null) {
				//Lee el XML que devuelve el WS
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(new StringReader(xml));
				Element root = document.getRootElement();
				for(int i = 0; i < root.getChildren().size(); i++) {
					Element element = (Element)root.getChildren().get(i);
					//Agrega el usuario a la lista
					Usuario usuario = new Usuario();
					usuario.setIdUsuario(Integer.valueOf(element.getAttributeValue("id")));
					usuario.setNombre(element.getAttributeValue("nombre"));
					usuario.setCommonName(element.getAttributeValue("commonName"));
					usuarios.add(usuario);
				}
				return usuarios;
			} else {
				throw new FWException("No se pudieron recuperar los usuarios");
			}
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar los usuarios", e);
		}
	}

	/**
	 * Retorna las acciones que deben mostrarse en el módulo modulo cuando el mismo es accedido
	 * utilizando el rol rol.
	 * @throws FWException
	 */
	public List<Accion> getAccionList(Rol rol, Integer idModulo) throws FWException {
		try {
			List<Accion> accionList = new ArrayList<Accion>();
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsEndpoint + wsNombre));
			call.setOperationName(new QName("getSortedAccionList"));
			call.addParameter("idRol", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.addParameter("idModulo", XMLType.XSD_INTEGER, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String xml = (String)call.invoke(new Object[] { rol.getIdRol(), idModulo });
			//Lee el XML que devuelve el WS
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(xml));
			Element root = document.getRootElement();
			for(int i = 0; i < root.getChildren().size(); i++) {
				Element element = (Element)root.getChildren().get(i);
				Accion accion = new Accion();
				accion.setIdAccion(Integer.valueOf(element.getAttributeValue("idAccion")));
				accion.setNombre(element.getAttributeValue("nombre"));
				accion.setUbicacion(element.getAttributeValue("ubicacion"));
				accion.setDescripcion(element.getAttributeValue("descripcion"));
				accion.setImagenActiva(element.getAttributeValue("imagenActiva"));
				accion.setImagenInactiva(element.getAttributeValue("imagenInactiva"));
				accion.setToolTip(element.getAttributeValue("toolTip"));
				accion.setIndependienteSeleccion(Boolean.valueOf(element.getAttributeValue("independienteSeleccion")));
				GrupoAccion grupoAccion = new GrupoAccion() ;
				grupoAccion.setIdGrupoAccion(Integer.valueOf(element.getAttributeValue("idGrupoAccion")));
				grupoAccion.setNombre(element.getAttributeValue("nombreGrupoAccion"));
				accion.setGrupoAccion(grupoAccion) ;
				accionList.add(accion);
			}
			return accionList;
		} catch(Exception e) {
			throw new FWException("No se pudieron recuperar las acciones para el rol " + rol.getClass() + " y el modulo con id " + idModulo, e);
		}
	}
	
}