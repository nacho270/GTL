package ar.com.fwcommon.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import com.google.common.base.Predicate;
//import com.google.common.collect.Collections2;


public class GrupoModulos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private boolean esDefault;
	//private List<Integer> listaModulos = new ArrayList<Integer>();
	private List<GrupoModulos> subgrupos = new ArrayList<GrupoModulos>();
	private boolean yaCreado=true;
	private GrupoModulos padre = null;
	//private List<NewItemMenu> newItems = new ArrayList<NewItemMenu>();//items no registrados en portal
	//private String metodoOrdenacion = null;
	
	private List<Object> idsModuloYNuevos = new ArrayList<Object>();
	
	
	public static class NewItemMenu extends Modulo implements Serializable {
		private GrupoModulos grupoPadre;
		
		public NewItemMenu(GrupoModulos grupo, int idModulo, String nombre, String clase, boolean action) {
			super(idModulo,nombre,clase,-1, action);
			this.grupoPadre = grupo;
		}

		private static final long serialVersionUID = 1L;

		
		public GrupoModulos getGrupoPadre() {
			return grupoPadre;
		}
		
		

	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public boolean isEsDefault() {
		return esDefault;
	}
	
	public void setEsDefault(boolean esDefault) {
		this.esDefault = esDefault;
	}
	
	public List<Integer> getListaModulos() {
		List<Integer> tmp = new ArrayList<Integer>();
		for (Object idMod : this.idsModuloYNuevos){
			if (idMod instanceof Integer){
				tmp.add((Integer)idMod);
			}
		}
		return Collections.unmodifiableList(tmp);
	}
	
	/*
	public void setListaModulos(List<Integer> listaModulos) {
		this.listaModulos = listaModulos;
	}
	*/

	
	public List<GrupoModulos> getSubgrupos() {
		return subgrupos;
	}

	
	public void setSubgrupos(List<GrupoModulos> subgrupos) {
		this.subgrupos = subgrupos;
	}

	
	public boolean isYaCreado() {
		return yaCreado;
	}

	
	public void setYaCreado(boolean yaCreado) {
		this.yaCreado = yaCreado;
	}

	public GrupoModulos getSubgrupoPorIdModulo(Integer idModulo) {
		for (GrupoModulos subgrupo: this.subgrupos){
			if (subgrupo.getListaModulos().contains(idModulo)){
				return subgrupo;
			}
				
		}
		return null;
	}

	
	public GrupoModulos getPadre() {
		return padre;
	}

	
	public void setPadre(GrupoModulos padre) {
		this.padre = padre;
	}
	
	public String toString (){
		return this.nombre + " - (" + this.idsModuloYNuevos + "| " + this.subgrupos + ")" ;
	}

	
	public List<NewItemMenu> getNewItems() {
		List<NewItemMenu> tmp = new ArrayList<NewItemMenu>();
		for (Object idMod : this.idsModuloYNuevos){
			if (idMod instanceof NewItemMenu){
				tmp.add((NewItemMenu)idMod);
			}
		}
		return Collections.unmodifiableList(tmp);
	}

	/*
	public void setNewItems(List<NewItemMenu> newItems) {
		this.newItems = newItems;
	}
	*/

	public void completarModulosSinPortal(List<Modulo> modulosUsuarioAplicacion) {
		for (NewItemMenu newItem: this.getNewItems()){
			modulosUsuarioAplicacion.add(newItem);
		}
		for (GrupoModulos subgrupo: this.subgrupos){
			subgrupo.completarModulosSinPortal(modulosUsuarioAplicacion);
		}
		
	}

/*	
	public void setMetodoOrdenacion(String metodoOrdenacion) {
		this.metodoOrdenacion = metodoOrdenacion;
	}
*/
	
	public List<Object> getIdsModuloYNuevos() {
		return idsModuloYNuevos;
	}

	
	public void setIdsModuloYNuevos(List<Object> listaOrdenadaModulos) {
		this.idsModuloYNuevos = listaOrdenadaModulos;
	}
	
}
