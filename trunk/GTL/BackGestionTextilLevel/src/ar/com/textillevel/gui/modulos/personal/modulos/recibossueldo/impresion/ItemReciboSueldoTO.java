package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.impresion;

public class ItemReciboSueldoTO {

	private String concepto;
	private String codigo;
	private String unidades;
	private String haberes;
	private String retenciones;
	private String asignaciones;
	private String deducciones;
	private String noRemun;

	public ItemReciboSueldoTO() {
		setConcepto("");
		setCodigo("");
		setUnidades("");
		setHaberes("");
		setRetenciones("");
		setAsignaciones("");
		setDeducciones("");
		setNoRemun("");
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = " " + concepto;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		if(codigo == null) {
			this.codigo = "";
		} else {
			this.codigo = codigo;
		}
	}

	public String getUnidades() {
		return unidades;
	}

	public void setUnidades(String unidades) {
		this.unidades = unidades;
	}

	public String getHaberes() {
		return haberes;
	}

	public void setHaberes(String haberes) {
		this.haberes = haberes;
	}

	public String getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(String retenciones) {
		this.retenciones = retenciones;
	}

	public String getAsignaciones() {
		return asignaciones;
	}

	public void setAsignaciones(String asignaciones) {
		this.asignaciones = asignaciones;
	}

	public String getDeducciones() {
		return deducciones;
	}

	public void setDeducciones(String deducciones) {
		this.deducciones = deducciones;
	}

	public String getNoRemun() {
		return noRemun;
	}

	public void setNoRemun(String noRemun) {
		this.noRemun = noRemun;
	}

}