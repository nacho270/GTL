package ar.com.textillevel.web.struts2.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

@Namespace(value="/actions")
@ParentPackage("json-default")
@Action(value="personasJSON",results={@Result(name=ActionSupport.SUCCESS, type="json")})
public class TestJSON extends ActionSupport {

	private static final long serialVersionUID = -7628105257154101267L;
	
	//TENIENDO EL SETTER SOLO, HACE QUE SE BINDEEN LOS PARAMETROS CON LOS ATRIBUTOS. EL GETTER HACE QUE SE DEVUELVAN A LA PANTALLA, POR ESO LO BORRE. SI SE AGREGA, APARECE
	private String parametro;
	
	private List<Persona> personas;
	
	@Override
	public String execute() throws Exception {
		
		personas = new ArrayList<TestJSON.Persona>();
		
		personas.add(new Persona(50, "Nacho"));
		personas.add(new Persona(5340, "pepe"));
		System.out.println(parametro);
		return SUCCESS;
	}
	
	public class Persona implements Serializable{
		
		private static final long serialVersionUID = -2365923668890629560L;
		
		private Integer id;
		private String nombre;
		
		public Persona(Integer id, String nombre) {
			this.id = id;
			this.nombre = nombre;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}
	
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	
}
