package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.IFormulaClienteExplotadaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.IInstruccionProcedimientoVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTexto;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTipoProducto;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

public class InstruccionProcedimientoRenderer {

	public static interface FiltroInstrucciones{
		public boolean esValida(InstruccionProcedimiento instruccion);
	}
	
	public static String renderInstruccionASHTML(InstruccionProcedimiento elemento) {
		return renderInstruccionASHTML(elemento, false);
	}

	public static String renderInstruccionesASHTML(List<InstruccionProcedimiento> instrucciones, boolean explotadas, FiltroInstrucciones filtro) {
		String html = "";
		for(InstruccionProcedimiento ins : instrucciones){
			if(filtro == null || filtro.esValida(ins)){
				String htmlInstruccion = renderInstruccionASHTML(ins, explotadas).replaceAll("<html>", "");
				if(ins instanceof InstruccionProcedimientoTipoProducto && ((InstruccionProcedimientoTipoProducto)ins).getTipoProducto() == ETipoProducto.ESTAMPADO && ((InstruccionProcedimientoTipoProducto)ins).getFormula() == null){
					InstruccionProcedimientoTipoProducto itp = (InstruccionProcedimientoTipoProducto) ins;
					String descripcion = "<b>"+itp.getTipoProducto().getDescripcion().toUpperCase()+"</b>" + " " + itp.getTipoArticulo().getNombre().toUpperCase();
					htmlInstruccion = htmlInstruccion.replace(descripcion, "VER HOJA ESTAMPADO");
				}
				htmlInstruccion = htmlInstruccion.replaceAll("</html>", "");
				/* ESTE SUBRAYADO NO SALE BIEN EN LA IMPRESION... POR LO MENOS CON PDF CREATOR */
				htmlInstruccion = htmlInstruccion.replaceAll("</u>", "");
				htmlInstruccion = htmlInstruccion.replaceAll("<u>", "");
				htmlInstruccion += "<br>";
				html += htmlInstruccion;
			}
		}
		return "<html>" + html + "</html>";
	}
	
	public static String renderObservacionesInstruccionesASHTML(List<InstruccionProcedimiento> instrucciones, FiltroInstrucciones filtroInstrucciones) {
		String html = "";
		for(InstruccionProcedimiento ins : instrucciones){
			if(!StringUtil.isNullOrEmpty(ins.getObservaciones()) && (filtroInstrucciones == null || filtroInstrucciones.esValida(ins))){
				html += "* " + ins.getObservaciones() + "<br>";
			}
		}
		if(StringUtil.isNullOrEmpty(html)){
			return "";
		}
		return "<html>" + html + "</html>";
	}
	
	public static String getDescripcionInstruccion(InstruccionProcedimiento instruccion) {
		DescripcionNormalInstruccionVisitor visitor = new InstruccionProcedimientoRenderer.DescripcionNormalInstruccionVisitor();
		instruccion.accept(visitor);
		return visitor.getDescripcion();
	}

	public static String getDescripcionDetalladaInstruccion(InstruccionProcedimiento instruccion) {
		DescripcionDetalladaInstruccionVisitor visitor = new InstruccionProcedimientoRenderer.DescripcionDetalladaInstruccionVisitor();
		instruccion.accept(visitor);
		return visitor.getDescripcion();
	}

	public static String renderInstruccionASHTML(InstruccionProcedimiento elemento, boolean explotada) {
		String descripcion = "";
		if (elemento instanceof InstruccionProcedimientoPasadas) {
			if (((InstruccionProcedimientoPasadas) elemento).getAccion() != null) {
				descripcion += "<b>" + ((InstruccionProcedimientoPasadas) elemento).getAccion().getNombre() + "</b><br>";
			}
		}
		if (explotada) {
			String[] descrArray = getDescripcionDetalladaInstruccion(elemento).split(" ");
			descripcion += "<b>"+descrArray[0].toUpperCase()+"</b> " + (descrArray.length > 1 ? StringUtil.getCadena(Arrays.asList(descrArray).subList(1, descrArray.length), " ") : "");
		} else {
			String[] descrArray = getDescripcionInstruccion(elemento).split(" ");
			descripcion += "<b>"+descrArray[0].toUpperCase()+"</b> " + (descrArray.length > 1 ? StringUtil.getCadena(Arrays.asList(descrArray).subList(1, descrArray.length), " ") : "");
		}
		
		descripcion = descripcion.replace("con", "<br>* ");
		descripcion = descripcion.replace(", ", "<br>* ");
		descripcion = descripcion.replace(" y ", "<br>* ");

		Pattern p = Pattern.compile("\\d{1,3}\\,\\d+");
		Matcher m = p.matcher(descripcion);
		while(m.find()){
			String texto = m.group();
			descripcion = descripcion.replace(texto, GenericUtils.formatAsSuperIndex(texto,true));
		}
		return "<html>" + descripcion + "</html>";
	}

	private static abstract class DescripcionInstruccionVisitor implements IInstruccionProcedimientoVisitor {

		private String descripcion;

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
	}

	private static class DescripcionDetalladaInstruccionVisitor extends DescripcionInstruccionVisitor {

		public void visit(InstruccionProcedimientoPasadas instruccion) {
			String descrQuimicos = "";
			if (!instruccion.getQuimicosExplotados().isEmpty()) {
				descrQuimicos += "con ";
				descrQuimicos += generarDescripcion(instruccion.getQuimicosExplotados(),null);
			}
			setDescripcion(instruccion.getCantidadPasadas() + " vuelta(s)/pasada(s) a " + instruccion.getTemperatura() + " ºC " + ". Velocidad: " + instruccion.getVelocidad() + " M/S " + descrQuimicos);
		}

		public void visit(InstruccionProcedimientoTexto instruccion) {
			setDescripcion(instruccion.getEspecificacion());
		}

		public void visit(InstruccionProcedimientoTipoProducto instruccion) {
			String descripcion = instruccion.getTipoProducto().getDescripcion().toLowerCase() + " " + instruccion.getTipoArticulo().getNombre();
			String descripcionFormula = "";
			if(instruccion.getFormula()!=null){
				DescripcionDetalladaInstruccionTipoProductoVisitor visitor = new DescripcionDetalladaInstruccionTipoProductoVisitor();
				instruccion.getFormula().accept(visitor);
				descripcionFormula = visitor.getDescripcion();
			}
			setDescripcion(descripcion + descripcionFormula);
		}
	}
	
	private static <T extends Formulable> String generarDescripcion(List<MateriaPrimaCantidadExplotada<T>> materiasPrimasExplotadas, EUnidad unidad){
		String descripcion = "";
		if(materiasPrimasExplotadas.size() == 1){
			MateriaPrimaCantidadExplotada<T> mpCantidad  = materiasPrimasExplotadas.get(0);
			String descripcionUnidad = (unidad!=null&& unidad == EUnidad.PORCENTAJE?unidad.getDescripcion().replace(" (KG)", ""):mpCantidad.getMateriaPrimaCantidadDesencadenante().getUnidad().getDescripcion());
			descripcion += GenericUtils.getDecimalFormat3().format(mpCantidad.getMateriaPrimaCantidadDesencadenante().getCantidad()) + " " + descripcionUnidad + " - " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getDescripcion() + ": " + GenericUtils.getDecimalFormat3().format(mpCantidad.getCantidadExplotada()) + " " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion();
		}else {
			for (int i = 0; i < materiasPrimasExplotadas.size(); i++) {
				MateriaPrimaCantidadExplotada<T> mpCantidad  = materiasPrimasExplotadas.get(i);
				String descripcionUnidad = (unidad!=null&& unidad == EUnidad.PORCENTAJE?unidad.getDescripcion().replace(" (KG)", ""):mpCantidad.getMateriaPrimaCantidadDesencadenante().getUnidad().getDescripcion());
				if (i != 0 && i == materiasPrimasExplotadas.size() - 1) {
					descripcion = descripcion.substring(0, descripcion.length() - 2);
					descripcion += " y ";
					descripcion += GenericUtils.getDecimalFormat3().format(mpCantidad.getMateriaPrimaCantidadDesencadenante().getCantidad()) + " " + descripcionUnidad + " - " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getDescripcion() + ": " + GenericUtils.getDecimalFormat3().format(mpCantidad.getCantidadExplotada()) + " " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion();
				} else {
					descripcion += GenericUtils.getDecimalFormat3().format(mpCantidad.getMateriaPrimaCantidadDesencadenante().getCantidad()) + " " + descripcionUnidad + " - " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getDescripcion() + ": " + GenericUtils.getDecimalFormat3().format(mpCantidad.getCantidadExplotada()) + " " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion() + ", ";
				}
			}
		}
		return descripcion;
	}

	
	private static class DescripcionDetalladaInstruccionTipoProductoVisitor implements IFormulaClienteExplotadaVisitor{

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
				descripcionPigmentos = generarDescripcion(pigmentos,null);
			}
			
			if(fece.getQuimicos()!=null && fece.getQuimicos().isEmpty()){
				List<MateriaPrimaCantidadExplotada<Quimico>> quimicos = fece.getQuimicos();
				descripcionQuimicos = generarDescripcion(quimicos,null);
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
			descripcionAnilinas += generarDescripcion(materiasPrimasExplotadas,EUnidad.PORCENTAJE);
			setDescripcion(descripcionAnilinas);
		}
		
		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
	}

	private static class DescripcionNormalInstruccionVisitor extends DescripcionInstruccionVisitor {

		public void visit(InstruccionProcedimientoPasadas instruccion) {
			String descrQuimicos = "";
			if (!instruccion.getQuimicos().isEmpty()) {
				descrQuimicos += "con ";
				if (instruccion.getQuimicos().size() == 1) {
					descrQuimicos += instruccion.getQuimicos().get(0).getMateriaPrima().getDescripcion();
				} else {
					for (int i = 0; i < instruccion.getQuimicos().size(); i++) {
						if (i != 0 && i == instruccion.getQuimicos().size() - 1) {
							descrQuimicos = descrQuimicos.substring(0, descrQuimicos.length() - 2);
							descrQuimicos += " y ";
							descrQuimicos += instruccion.getQuimicos().get(i).getMateriaPrima().getDescripcion();
						} else {
							descrQuimicos += instruccion.getQuimicos().get(i).getMateriaPrima().getDescripcion() + ", ";
						}
					}
				}
			}
			setDescripcion(instruccion.getCantidadPasadas() + " vuelta(s)/pasada(s) a " + instruccion.getTemperatura() + "ºC " + " y " + instruccion.getVelocidad() + " M/S " + descrQuimicos);
		}

		public void visit(InstruccionProcedimientoTexto instruccion) {
			setDescripcion(instruccion.getEspecificacion());
		}

		public void visit(InstruccionProcedimientoTipoProducto instruccion) {
			setDescripcion(instruccion.getTipoProducto().getDescripcion().toLowerCase() + " " + instruccion.getTipoArticulo().getNombre());
		}
	}

	public static String getResumenSectorHTML(ESectorMaquina sector, List<PasoSecuenciaODT> pasos) {
		String html = "";
		
		for(PasoSecuenciaODT p : pasos){
			if(p.getSector().getSectorMaquina() != sector){
				continue;
			}
			ResumenHTMLProductosInstruccionVisitor v = new InstruccionProcedimientoRenderer.ResumenHTMLProductosInstruccionVisitor();
			for(InstruccionProcedimiento ip : p.getSubProceso().getPasos()){
				ip.accept(v);
				html += v.getResumenHTML();
			}
		}
		
		return "<html>"+html+"</html>";
	}
	
	public static String getResumenQuimicos(List<PasoSecuenciaODT> pasos) {
		String html = "";
		for (PasoSecuenciaODT p : pasos) {
			ResumenHTMLQuimicosVisitor v = new InstruccionProcedimientoRenderer.ResumenHTMLQuimicosVisitor();
			for (InstruccionProcedimiento ip : p.getSubProceso().getPasos()) {
				ip.accept(v);
				html += v.getResumenHTML();
			}
		}
	
	return "<html>"+html+"</html>";
	}
	
	public static String getResumenAlgodon(List<PasoSecuenciaODT> pasos) {
		return getResumenTipoArituclo("A", pasos);
	}
	
	public static String getResumenPoliester(List<PasoSecuenciaODT> pasos) {
		return getResumenTipoArituclo("P", pasos);
	}
	
	private static String getResumenTipoArituclo(String sigla, List<PasoSecuenciaODT> pasos) {
		String html = "";
		for(PasoSecuenciaODT p : pasos){
			ResumenHTMLTipoProductosInstruccionVisitor v = new InstruccionProcedimientoRenderer.ResumenHTMLTipoProductosInstruccionVisitor(sigla);
			for(InstruccionProcedimiento ip : p.getSubProceso().getPasos()){
				ip.accept(v);
				html += v.getResumenHTML();
			}
		}
		
		return "<html>"+html+"</html>";
	}
	
	private static class ResumenHTMLQuimicosVisitor implements IInstruccionProcedimientoVisitor{

		private String resumenHTML = "";
		
		public void visit(InstruccionProcedimientoPasadas instruccion) {
			if(instruccion.getQuimicosExplotados() != null && !instruccion.getQuimicosExplotados().isEmpty()){
				setResumenHTML(generarDescripcionProductosHTML(instruccion.getQuimicosExplotados(), null));
				return;
			}
			setResumenHTML("");
		}

		public void visit(InstruccionProcedimientoTexto instruccion) {
		}

		public void visit(InstruccionProcedimientoTipoProducto instruccion) {
			if(instruccion.getFormula() == null){
				setResumenHTML("");
				return;
			}
			ResumenHTMLQuimicosFormulaVisitor v = new ResumenHTMLQuimicosFormulaVisitor();
			instruccion.getFormula().accept(v);
			setResumenHTML(v.getResumenHTML());
		}

		public String getResumenHTML() {
			return resumenHTML;
		}
		
		public void setResumenHTML(String resumenHTML) {
			this.resumenHTML = resumenHTML;
		}
	}
	
	private static class ResumenHTMLTipoProductosInstruccionVisitor implements IInstruccionProcedimientoVisitor{

		private String resumenHTML = "";
		private String sigla;
		
		public ResumenHTMLTipoProductosInstruccionVisitor(String sigla) {
			this.sigla = sigla;
		}

		public void visit(InstruccionProcedimientoPasadas instruccion) {
		}

		public void visit(InstruccionProcedimientoTexto instruccion) {
		}

		public void visit(InstruccionProcedimientoTipoProducto instruccion) {
			if(instruccion.getFormula() == null){
				setResumenHTML("");
				return;
			}
			ResumenHTMLTipoProductosFormulaVisitor v = new ResumenHTMLTipoProductosFormulaVisitor(sigla);
			instruccion.getFormula().accept(v);
			setResumenHTML(v.getResumenHTML());
		}

		public String getResumenHTML() {
			return resumenHTML;
		}
		
		public void setResumenHTML(String resumenHTML) {
			this.resumenHTML = resumenHTML;
		}
	}
	
	private static class ResumenHTMLProductosInstruccionVisitor implements IInstruccionProcedimientoVisitor{

		private String resumenHTML;
		
		public void visit(InstruccionProcedimientoPasadas instruccion) {
			if(instruccion.getQuimicosExplotados() != null && !instruccion.getQuimicosExplotados().isEmpty()){
				setResumenHTML(generarDescripcionProductosHTML(instruccion.getQuimicosExplotados(), null));
				return;
			}
			setResumenHTML("");
		}

		public void visit(InstruccionProcedimientoTexto instruccion) {
			setResumenHTML("");
		}

		public void visit(InstruccionProcedimientoTipoProducto instruccion) {
			if(instruccion.getFormula() == null){
				setResumenHTML("");
				return;
			}
			ResumenHTMLProductosFormulaVisitor v = new ResumenHTMLProductosFormulaVisitor();
			instruccion.getFormula().accept(v);
			setResumenHTML(v.getResumenHTML());
		}

		public String getResumenHTML() {
			return resumenHTML;
		}
		
		public void setResumenHTML(String resumenHTML) {
			this.resumenHTML = resumenHTML;
		}
	}
	
	private static <T extends Formulable> String generarDescripcionProductosHTML(List<MateriaPrimaCantidadExplotada<T>> materiasPrimasExplotadas, String siglaAFiltrar){
		String html = "";
		for(MateriaPrimaCantidadExplotada<T> mp : materiasPrimasExplotadas){
			if (siglaAFiltrar == null || (siglaAFiltrar != null && mp.getTipoArticulo().getSigla().startsWith(siglaAFiltrar))){
				if(mp.getMateriaPrimaCantidadDesencadenante().getMateriaPrima() instanceof Anilina){
					html += "* " + mp.getTipoArticulo().getSigla() + ": " + mp.getMateriaPrimaCantidadDesencadenante().getDescripcion() + ": " + GenericUtils.getDecimalFormat3().format(mp.getCantidadExplotada()) + " " + mp.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion() + "<br>";
				}else{
					html += "* " + mp.getMateriaPrimaCantidadDesencadenante().getDescripcion() + ": " + GenericUtils.getDecimalFormat3().format(mp.getCantidadExplotada()) + " " + mp.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion() + "<br>";
				}
			}
		}
		return html;
	}
	
	private static class ResumenHTMLQuimicosFormulaVisitor implements IFormulaClienteExplotadaVisitor{

		private String resumenHTML = "";
		
		public void visit(FormulaEstampadoClienteExplotada fece) {
			if(fece.getPigmentos()!=null && !fece.getPigmentos().isEmpty()){
				setResumenHTML(getResumenHTML() + generarDescripcionProductosHTML(fece.getPigmentos(), null));
			}
			if(fece.getQuimicos()!= null && !fece.getQuimicos().isEmpty()){
				setResumenHTML(getResumenHTML() + generarDescripcionProductosHTML(fece.getQuimicos(), null));
			}
		}

		public void visit(FormulaTenidoClienteExplotada ftce) {
		}
		
		public String getResumenHTML() {
			return resumenHTML;
		}

		public void setResumenHTML(String resumenHTML) {
			this.resumenHTML = resumenHTML;
		}
	}
	
	private static class ResumenHTMLProductosFormulaVisitor implements IFormulaClienteExplotadaVisitor{

		private String resumenHTML = "";
		
		public void visit(FormulaEstampadoClienteExplotada fece) {
			if(fece.getPigmentos()!=null && !fece.getPigmentos().isEmpty()){
				setResumenHTML(getResumenHTML() + generarDescripcionProductosHTML(fece.getPigmentos(), null));
			}
			if(fece.getQuimicos()!= null && !fece.getQuimicos().isEmpty()){
				setResumenHTML(getResumenHTML() + generarDescripcionProductosHTML(fece.getQuimicos(), null));
			}
		}

		public void visit(FormulaTenidoClienteExplotada ftce) {
			if(ftce.getMateriasPrimas()!=null && !ftce.getMateriasPrimas().isEmpty()){
				setResumenHTML(getResumenHTML() + generarDescripcionProductosHTML(ftce.getMateriasPrimas(), null));
			}
		}
		
		public String getResumenHTML() {
			return resumenHTML;
		}

		public void setResumenHTML(String resumenHTML) {
			this.resumenHTML = resumenHTML;
		}
	}
	
	private static class ResumenHTMLTipoProductosFormulaVisitor implements IFormulaClienteExplotadaVisitor{

		private String resumenHTML = "";
		private String sigla = "";
		
		public ResumenHTMLTipoProductosFormulaVisitor(String sigla) {
			this.sigla = sigla;
		}

		public void visit(FormulaEstampadoClienteExplotada fece) {
		}

		public void visit(FormulaTenidoClienteExplotada ftce) {
			if (ftce.getFormulaDesencadenante() instanceof FormulaTenidoCliente){
				if(ftce.getMateriasPrimas()!=null && !ftce.getMateriasPrimas().isEmpty()){
					setResumenHTML(getResumenHTML() + generarDescripcionProductosHTML(ftce.getMateriasPrimas(), this.sigla));
				}
			}
		}
		
		public String getResumenHTML() {
			return resumenHTML;
		}

		public void setResumenHTML(String resumenHTML) {
			this.resumenHTML = resumenHTML;
		}
	}
}
