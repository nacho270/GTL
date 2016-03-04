package ar.com.textillevel.modulos.odt.util;

import java.util.List;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.IFormulaClienteExplotadaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;

public class DescripcionDetalladaInstruccionTipoProductoVisitor implements IFormulaClienteExplotadaVisitor {

	private String descripcion;

	public void visit(FormulaEstampadoClienteExplotada fece) {
		if( (fece.getPigmentos()==null || fece.getPigmentos().isEmpty()) && ((fece.getQuimicos()==null || fece.getQuimicos().isEmpty())) ){
			return;
		}
		String descripcion ="con ";
		String descripcionPigmentos = "";
		String descripcionQuimicos = "";
		if(fece.getPigmentos()!=null && fece.getPigmentos().isEmpty()){
			List<MateriaPrimaCantidadExplotada<Pigmento>> pigmentos = fece.getPigmentos();
			descripcionPigmentos = GeneradorDescripcionMateriaPrimaUtil.generarDescripcion(pigmentos,null);
		}
		if(fece.getQuimicos()!=null && fece.getQuimicos().isEmpty()){
			List<MateriaPrimaCantidadExplotada<Quimico>> quimicos = fece.getQuimicos();
			descripcionQuimicos = GeneradorDescripcionMateriaPrimaUtil.generarDescripcion(quimicos,null);
		}
		if(!descripcionPigmentos.equals("")){
			descripcion += descripcionPigmentos;
		}
		if(!descripcionQuimicos.equals("")){
			descripcion +="Y con " + descripcionPigmentos;
		}
		setDescripcion(descripcion);
	}
	
	public void visit(FormulaTenidoClienteExplotada ftce) {
		String descripcionAnilinas ="con ";
		List<MateriaPrimaCantidadExplotada<Anilina>> materiasPrimasExplotadas = ftce.getMateriasPrimas();
		descripcionAnilinas += GeneradorDescripcionMateriaPrimaUtil.generarDescripcion(materiasPrimasExplotadas,EUnidad.PORCENTAJE);
		setDescripcion(descripcionAnilinas);
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}