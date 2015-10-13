package ar.com.fwcommon.entidades;

import java.io.Serializable;

public class Rol implements Comparable<Rol>, Serializable {

	private static final long serialVersionUID = 1034436461154427693L;

	private int idRol;
    private String nombre;
    private String descripcion;

    /** Método constructor */
    public Rol() {
    }

    /**
     * Método constructor.
     * @param idRol El nro. de id del rol.
     * @param nombre El nombre del rol.
     */
    public Rol(int idRol, String nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
    }

    /**
     * Devuelve el nro. de id del rol.
     * @return idRol El nro. de id del rol.
     */
    public int getIdRol() {
        return idRol;
    }

    /**
     * Setea el nro. de id del rol.
     * @param idRol El nro. de id del rol.
     */
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    /**
     * Devuelve la descripción del rol.
     * @return descripcion La descripción del rol.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Setea la descripción del rol.
     * @param descripcion La descripción del rol.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el nombre del rol.
     * @return nombre El nombre del rol.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setea el nombre del rol.
     * @param nombre El nombre del rol.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Método toString */
    public String toString() {
        return nombre;
    }

    /** Método equals */
    public boolean equals(Object o) {
        if(o instanceof Rol) {
            Rol rol = (Rol)o;
            if(idRol == rol.getIdRol() && nombre.equals(rol.getNombre()) && descripcion.equals(rol.getDescripcion())) {
                return true;
            }
        }
        return false;
    }

    /** Método compareTo */
	public int compareTo(Rol r) {
		return this.getNombre().compareToIgnoreCase(r.getNombre());
	}

}