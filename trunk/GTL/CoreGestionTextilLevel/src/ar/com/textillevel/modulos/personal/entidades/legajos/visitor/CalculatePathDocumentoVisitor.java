package ar.com.textillevel.modulos.personal.entidades.legajos.visitor;

import java.io.File;

import ar.clarin.fwjava.util.FileUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;

@SuppressWarnings("rawtypes") 
public class CalculatePathDocumentoVisitor implements IAccionHistoricaVisitor {
	
	private File file;
	private Empleado empleado;
	private String path;

	private static final String SANCIONES = "SANCIONES";
	private static final String VALES_DE_ATENCION = "VALES_DE_ATENCION";

	public CalculatePathDocumentoVisitor() {
	}

	public CalculatePathDocumentoVisitor(Empleado empleado, File file) {
		this.empleado = empleado;
		this.file = file;
	}

	public void visit(AccionValeAtencion accva) {
		path = empleado.getNombre() + " "  + empleado.getApellido() + "-"  + empleado.getLegajo().getNroLegajo() + "/" + VALES_DE_ATENCION + "/" + accva.calculateNombreDocumento() + "." + FileUtil.getFileExtension(file);
	}

	public void visit(AccionSancion accs) {
		path = empleado.getNombre() + " "  + empleado.getApellido() + "-"  + empleado.getLegajo().getNroLegajo() + "/" + SANCIONES + "/" + accs.calculateNombreDocumento() + "." + FileUtil.getFileExtension(file);
	}
	
	public String getPath() {
		return path;
	}

}
