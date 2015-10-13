package ar.com.fwcommon.entidades;

import java.io.Serializable;

public class Usuario implements Comparable<Usuario>, Serializable {

	private static final long serialVersionUID = 7048290809912048372L;

	protected int idUsuario;
	protected String nombre;
	protected String commonName;
	protected Rol rol;
	protected String password;
	private String organization;
	private String organizationalUnit;

	/** Método constructor */
    public Usuario() {
    }

    /**
     * Método constructor.
     * @param idUsuario El nro. de id del usuario.
     * @param nombre El nombre de usuario.
     */
    public Usuario(int idUsuario, String nombre) {
    	this(idUsuario, nombre, null);
    }

    /**
     * Método constructor.
     * @param idUsuario El nro. de id del usuario.
     * @param nombre El nombre de usuario.
     * @param commonName El common name del usuario.
     */
    public Usuario(int idUsuario, String nombre, String commonName) {
    	this(idUsuario, nombre, commonName, null);
    }

    /**
     * Método constructor.
     * @param idUsuario El nro. de id del usuario.
     * @param nombre El nombre de usuario.
     * @param commonName El common name del usuario.
     * @param rol El rol del usuario.
     */
    public Usuario(int idUsuario, String nombre, String commonName, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.commonName = commonName;
        this.rol = rol;
    }

    /**
     * Devuelve el <b>id</b> del usuario.
     * @return idUsuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

	/**
	 * Setea el <b>id</b> del usuario.
	 * @param idUsuario
	 */
    public void setIdUsuario(int idUsuario) {
    	this.idUsuario = idUsuario;
    }

	/**
	 * Devuelve el <b>nombre</b> del usuario.
	 * @return nombre
	 */
    public String getNombre() { 
    	return nombre; 
    }

	/**
	 * Setea el <b>nombre</b> del usuario.
	 * @param nombre
	 */
    public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }

    /**
     * Devuelve el <b>common name</b> del usuario.
     * @return commonName
     */
    public String getCommonName() {
    	return commonName;
    }

    /**
     * Setea el <b>common name</b> del usuario.
     * @param commonName
     */
    public void setCommonName(String commonName) {
    	this.commonName = commonName;
    }

    /**
     * Devuelve el <b>rol</b> del usuario.
     * @return rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Setea el <b>rol</b> del usuario.
     * @param rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /** Método equals */
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Usuario) {
			if(((Usuario)obj).getIdUsuario() == idUsuario) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return idUsuario >> 3 ^ idUsuario >> 3;
	}

	/** Método toString */
	public String toString() {
		if(nombre == null || nombre.trim().length() == 0) {
			return commonName;
		} else {
			return nombre;
		}
	}

	/** Método compareTo */
	public int compareTo(Usuario u) {
		return this.toString().compareToIgnoreCase(u.toString());
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getOrganization() {
		return organization;
	}

	
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationalUnit() {
		return organizationalUnit;
	}

	
	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}

}