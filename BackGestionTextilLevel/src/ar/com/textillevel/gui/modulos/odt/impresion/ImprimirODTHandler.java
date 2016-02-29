package ar.com.textillevel.gui.modulos.odt.impresion;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import main.GTLGlobalCache;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.gui.modulos.odt.gui.procedimientos.InstruccionProcedimientoRenderer;
import ar.com.textillevel.gui.modulos.odt.util.ODTDatosMostradoHelper;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTipoProducto;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.ODTCodigoHelper;

public class ImprimirODTHandler {

	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/odt.jasper";
	private static final String ARCHIVO_JASPER_SECUENCIA = "/ar/com/textillevel/reportes/odt-secuencia.jasper";
	private static final String ARCHIVO_JASPER_PROCEDIMIENTO = "/ar/com/textillevel/reportes/odt-procedimiento.jasper";
	private static final String ARCHIVO_JASPER_RESUMEN_ARTICULO = "/ar/com/textillevel/reportes/odt-procedimiento_resumen_x_articulo.jasper";
//	private Frame frameOwner;
//	private Dialog dialogOwner;
	private final OrdenDeTrabajo odt;
	
	private EFormaImpresionODT formaImpresion;

	public ImprimirODTHandler(OrdenDeTrabajo odt, Frame frameOwner) {
		this.odt = odt;
//		this.frameOwner = frameOwner;
		formaImpresion = seleccionarFormaImpresion(frameOwner);
		if(formaImpresion == null){
			return;
		}
	}

	private EFormaImpresionODT seleccionarFormaImpresion(Window parent) {
		EFormaImpresionODT forma = null;
		boolean valida = false;
		do{
			forma = (EFormaImpresionODT) JOptionPane.showInputDialog(parent, "Que desea imprimir?", "Pregunta", JOptionPane.QUESTION_MESSAGE, null, EFormaImpresionODT.values(), EFormaImpresionODT.RESUMEN_ARTIULOS);
			if(forma == null){
				valida = true;
			}else{
				valida = true;
				if(forma != EFormaImpresionODT.ENCABEZADO && odt.getSecuenciaDeTrabajo() == null){
					FWJOptionPane.showErrorMessage(parent, "La orden de trabajo no tiene secuencia asignada", "Error");
					valida = false;
				}else if( (forma == EFormaImpresionODT.ENCABEZADO_SECUENCIA || forma == EFormaImpresionODT.AMBOS || forma == EFormaImpresionODT.RESUMEN_ARTIULOS) && !tieneFormula(ETipoProducto.TENIDO)){
					FWJOptionPane.showErrorMessage(parent, "Para imprimir la secuencia, debe tener cargada la formula de teñido", "Error");
					valida = false;
				}else if(forma == EFormaImpresionODT.ESTAMPADO && !tieneFormula(ETipoProducto.ESTAMPADO)){
					FWJOptionPane.showErrorMessage(parent, "Para imprimir la secuencia de estampado, debe tener cargada la formula", "Error");
					valida = false;
				}
			}
		}while(!valida);
		return forma;
	}

	private boolean tieneFormula(ETipoProducto tipoProducto){
		for(PasoSecuenciaODT paso : odt.getSecuenciaDeTrabajo().getPasos()){
			for(InstruccionProcedimiento ins : paso.getSubProceso().getPasos()){
				if(ins instanceof InstruccionProcedimientoTipoProducto){
					InstruccionProcedimientoTipoProducto itp = (InstruccionProcedimientoTipoProducto)ins;
					if(itp.getTipoProducto() == tipoProducto && itp.getFormula() == null){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public ImprimirODTHandler(OrdenDeTrabajo odt, Dialog dialogOwner) {
		this.odt = odt;
//		this.dialogOwner = dialogOwner;
		formaImpresion = seleccionarFormaImpresion(dialogOwner);
	}

	public void imprimir() throws IOException {
//		COMENTO EL DIALOGO DE INPUT DE CANTIDAD, QUEDA 1 HARDCODETTI
		
//		boolean ok = false;
//		do {
//			String cantImprimirStr = JOptionPane.showInputDialog(getOwner(), "Ingrese la cantidad de copias: ", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
//			if (cantImprimirStr == null) {
//				break;
//			}
//			if (cantImprimirStr.trim().length() == 0 || !GenericUtils.esNumerico(cantImprimirStr)) {
//				CLJOptionPane.showErrorMessage(getOwner(), "Ingreso incorrecto", "error");
//			} else {
//				ok = true;
//				internalImprimir(Integer.valueOf(cantImprimirStr));
//			}
//		} while (!ok);
		if(formaImpresion == null){
			return;
		}
		internalImprimir(1);
		if(formaImpresion == EFormaImpresionODT.ENCABEZADO_SECUENCIA || formaImpresion == EFormaImpresionODT.AMBOS){
			odt.setEstadoODT(EEstadoODT.IMPRESA);
			GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).grabarODT(odt, GTLGlobalCache.getInstance().getUsuarioSistema());
		}
	}

	private void internalImprimir(Integer cantidad) throws IOException {
		JasperReport reporte = null;
		if(formaImpresion != EFormaImpresionODT.AMBOS){
			if(formaImpresion == EFormaImpresionODT.ENCABEZADO){
				reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
			}else if(formaImpresion == EFormaImpresionODT.ENCABEZADO_SECUENCIA || formaImpresion == EFormaImpresionODT.ESTAMPADO){
				reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_SECUENCIA);
			}else if(formaImpresion == EFormaImpresionODT.ENCABEZADO_PROCEDIMIENTO){
				reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_PROCEDIMIENTO);
			} else if(formaImpresion == EFormaImpresionODT.RESUMEN_ARTIULOS){
				reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_RESUMEN_ARTICULO);
			}
			ODTTO odtto = new ODTTO(this.odt,formaImpresion);
			try {
				JasperPrint jasperPrint = null;
				Map<String, Object> mapa = null;
				if(this.odt.getSecuenciaDeTrabajo() == null){
					mapa = odtto.getMapaParametros();
				}else{
					mapa = odtto.getMapaParametrosSecuencia(this.odt.getSecuenciaDeTrabajo().getPasos());
				}
				jasperPrint = JasperHelper.fillReport(reporte, mapa , Collections.singletonList(odtto));
				Integer cantidadAImprimir = Integer.valueOf(cantidad);
				JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
			} catch (JRException e) {
				e.printStackTrace();
			}
		}else{
			ODTTO odtto = new ODTTO(this.odt,formaImpresion);
			try {
				reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_SECUENCIA);
				JasperPrint jasperPrint = null;
				jasperPrint = JasperHelper.fillReport(reporte, odtto.getMapaParametrosSecuencia(this.odt.getSecuenciaDeTrabajo().getPasos()), Collections.singletonList(odtto));
				Integer cantidadAImprimir = Integer.valueOf(cantidad);
				JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
				
				reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_PROCEDIMIENTO);
				JasperPrint jasperPrint2 = JasperHelper.fillReport(reporte, odtto.getMapaParametrosSecuencia(this.odt.getSecuenciaDeTrabajo().getPasos()), Collections.singletonList(odtto));
				cantidadAImprimir = Integer.valueOf(cantidad);
				JasperHelper.imprimirReporte(jasperPrint2, true, true, cantidadAImprimir);
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
	}

	public static class ODTTO implements Serializable {

		private static final long serialVersionUID = -2142756333577045834L;

		private final String codigo;
		private final BigDecimal metros;
		private final BigDecimal kilos;
		private final String gramaje;
		private final Integer nroRemito;
		private final String cliente; // String para el caso 01/74 //TODO:
		private final Integer cantidadPiezas;
		private final String articulo;
		private final String anchoFinal;
		private final String anchoCrudo;
		private final String tarima;
		private final String color;
		private SecuenciaODTTO secuencia;
		private List<DummyPiezaTablaImpresion> piezasDummy1;
		private List<DummyPiezaTablaImpresion> piezasDummy2;
		private String resumenSectorSeco;
		private String resumenSectorHumedo;
		private String resumenSectorEstampado;
		private String resumenQuimicos;
		private String resumenAlgodon;
		private String resumenPoliester;


		// private String maquina;
		
		public static class DummyPiezaTablaImpresion implements Serializable{

			private static final long serialVersionUID = -2518421002298666124L;

			private Short nroPieza;
			private String metros;
			private String metrosFrac;
			
			public DummyPiezaTablaImpresion(Short nroPieza) {
				this.nroPieza = nroPieza;
			}

			public Short getNroPieza() {
				return nroPieza;
			}
			
			public void setNroPieza(Short nroPieza) {
				this.nroPieza = nroPieza;
			}
			
			public String getMetros() {
				return metros;
			}
			
			public void setMetros(String metros) {
				this.metros = metros;
			}
			
			public String getMetrosFrac() {
				return metrosFrac;
			}
			
			public void setMetrosFrac(String metrosFrac) {
				this.metrosFrac = metrosFrac;
			}
		}
		
		public static class SecuenciaODTTO implements Serializable{

			private static final long serialVersionUID = -257587342081470157L;
			
			public List<PasoSecuenciaODTTO> pasos;
			
			public SecuenciaODTTO(SecuenciaODT secuencia,EFormaImpresionODT formaImp) {
				crearPasos(secuencia.getPasos(),formaImp);
			}
			
			private void crearPasos(List<PasoSecuenciaODT> pasos2, EFormaImpresionODT formaImp) {
				pasos = new ArrayList<ImprimirODTHandler.ODTTO.SecuenciaODTTO.PasoSecuenciaODTTO>();
				for(PasoSecuenciaODT ps : pasos2){
					if(formaImp == EFormaImpresionODT.ESTAMPADO && ps.getSector().getSectorMaquina() != ESectorMaquina.SECTOR_ESTAMPERIA){
						continue;
					}
					pasos.add(new PasoSecuenciaODTTO(ps, formaImp));
				}
			}
			
			public List<PasoSecuenciaODTTO> getPasos() {
				return pasos;
			}
			
			public void setPasos(List<PasoSecuenciaODTTO> pasos) {
				this.pasos = pasos;
			}

			public static class PasoSecuenciaODTTO implements Serializable{

				private static final long serialVersionUID = 3751007110322089047L;
				
				public String sector;
				public String procSubproc;
				public String instrucciones;
				public String observaciones;
				public String observacionesGenerales;
				
				public PasoSecuenciaODTTO(PasoSecuenciaODT paso, final EFormaImpresionODT formaImp){
					this.sector = "<html><b>" + paso.getSector().getNombre().toUpperCase().replace("SECTOR ", "")+ "</b></html>";
					this.procSubproc = paso.getProceso().getNombre() + " / " + paso.getSubProceso().getNombre();
					this.observacionesGenerales = paso.getObservaciones();
					if(formaImp == EFormaImpresionODT.ENCABEZADO_SECUENCIA || formaImp == EFormaImpresionODT.ESTAMPADO || formaImp == EFormaImpresionODT.AMBOS){
						this.instrucciones = InstruccionProcedimientoRenderer.renderInstruccionesASHTML(paso.getSubProceso().getPasos(), true,null);
						this.observaciones = InstruccionProcedimientoRenderer.renderObservacionesInstruccionesASHTML(paso.getSubProceso().getPasos(), null);
					}
				}
				
				public String getSector() {
					return sector;
				}
				
				public void setSector(String sector) {
					this.sector = sector;
				}
				
				public String getProcSubproc() {
					return procSubproc;
				}
				
				public void setProcSubproc(String procSubproc) {
					this.procSubproc = procSubproc;
				}
				
				public String getInstrucciones() {
					return instrucciones;
				}
				
				public void setInstrucciones(String instrucciones) {
					this.instrucciones = instrucciones;
				}
				
				public String getObservaciones() {
					return observaciones;
				}
				
				public void setObservaciones(String observaciones) {
					this.observaciones = observaciones;
				}

				
				public String getObservacionesGenerales() {
					return observacionesGenerales;
				}

				
				public void setObservacionesGenerales(String observacionesGenerales) {
					this.observacionesGenerales = observacionesGenerales;
				}
			}
		}
		
		public ODTTO(final OrdenDeTrabajo odt, EFormaImpresionODT formaImp) {
			ODTDatosMostradoHelper odtDatosHelper = new ODTDatosMostradoHelper(odt);
			
			this.codigo = odt.getCodigo();
			this.metros = odt.getTotalMetros();
			this.kilos = odt.getRemito().getPesoTotal();
			this.gramaje = odtDatosHelper.getDescGramaje();
			this.nroRemito = odt.getRemito().getNroRemito();
			this.cliente = odtDatosHelper.getDescCliente();
			this.cantidadPiezas = odt.getPiezas().size(); // esto es lo mismo que las piezas remito?
			this.articulo = odtDatosHelper.getDescArticulo();
			this.color = odtDatosHelper.getDescColor();
			// this.maquina
			this.tarima = odtDatosHelper.getDescTarima();
			this.anchoFinal = odtDatosHelper.getDescAnchoFinal();
			this.anchoCrudo = odtDatosHelper.getDescAnchoCrudo();
			if(odt.getSecuenciaDeTrabajo()!=null){
				this.secuencia = new SecuenciaODTTO(odt.getSecuenciaDeTrabajo(),formaImp);
			}
			if(formaImp == EFormaImpresionODT.AMBOS || formaImp == EFormaImpresionODT.RESUMEN_ARTIULOS || formaImp == EFormaImpresionODT.ENCABEZADO_PROCEDIMIENTO){
				crearPiezasDummy();
				if(formaImp == EFormaImpresionODT.RESUMEN_ARTIULOS){
					this.resumenAlgodon = InstruccionProcedimientoRenderer.getResumenAlgodon(odt.getSecuenciaDeTrabajo().getPasos());
					this.resumenPoliester = InstruccionProcedimientoRenderer.getResumenPoliester(odt.getSecuenciaDeTrabajo().getPasos());
					this.resumenQuimicos = InstruccionProcedimientoRenderer.getResumenQuimicos(odt.getSecuenciaDeTrabajo().getPasos());
				}else if(odt.getSecuenciaDeTrabajo()!=null){
					this.resumenSectorEstampado = InstruccionProcedimientoRenderer.getResumenSectorHTML(ESectorMaquina.SECTOR_ESTAMPERIA,odt.getSecuenciaDeTrabajo().getPasos());
					this.resumenSectorSeco = InstruccionProcedimientoRenderer.getResumenSectorHTML(ESectorMaquina.SECTOR_SECO,odt.getSecuenciaDeTrabajo().getPasos());
					this.resumenSectorHumedo = InstruccionProcedimientoRenderer.getResumenSectorHTML(ESectorMaquina.SECTOR_HUMEDO,odt.getSecuenciaDeTrabajo().getPasos());
				}
			}
		}
		
		private void crearPiezasDummy() {
			piezasDummy1 = new ArrayList<ImprimirODTHandler.ODTTO.DummyPiezaTablaImpresion>();
			piezasDummy2 = new ArrayList<ImprimirODTHandler.ODTTO.DummyPiezaTablaImpresion>();
			int i = 1;
			int medio = (cantidadPiezas % 2 ==0?cantidadPiezas/2:(cantidadPiezas+1)/2);
			while(i <= this.cantidadPiezas){
				if( i <= medio){
					piezasDummy1.add(new DummyPiezaTablaImpresion((short) i));
				}else{
					piezasDummy2.add(new DummyPiezaTablaImpresion((short) i));
				}
				i++;
			}
		}

		public Map<String, Object> getMapaParametros() throws IOException {
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("BAR_CODE", this.codigo);
			mapa.put("IMAGEN", GenericUtils.createBarCode(this.codigo));
			mapa.put("ANIO_ODT", Integer.valueOf(String.valueOf(ODTCodigoHelper.getInstance().getAnioFromCodigo(this.codigo)).substring(2, 4)));
			mapa.put("MES_ODT", ODTCodigoHelper.getInstance().getMesFromCodigo(this.codigo));
			mapa.put("NRO_ODT", ODTCodigoHelper.getInstance().getNroODTFromCodigo(this.codigo));
			mapa.put("METROS", GenericUtils.getDecimalFormat().format(this.metros.doubleValue()));
			mapa.put("KILOS", GenericUtils.getDecimalFormat().format(this.kilos.doubleValue()));
			mapa.put("GRAMAJE", this.gramaje);
			mapa.put("NRO_REMITO", this.nroRemito);
			mapa.put("CLIENTE", this.cliente);
			mapa.put("PIEZAS", this.cantidadPiezas);
			mapa.put("ARTICULO", this.articulo);
			
			mapa.put("COLOR", this.color);
			mapa.put("TARIMA", this.tarima);
//			mapa.put("MAQUINA", this.codigo);
			mapa.put("ANCHO_CRUDO", this.anchoCrudo);
			mapa.put("ANCHO_FINAL", this.anchoFinal);
			mapa.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			return mapa;
		}
		
		public Map<String, Object> getMapaParametrosSecuencia(List<PasoSecuenciaODT> pasos) throws IOException {
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("BAR_CODE", this.codigo);
			mapa.put("IMAGEN", GenericUtils.createBarCode(this.codigo));
			mapa.put("METROS", GenericUtils.getDecimalFormat().format(this.metros.doubleValue()));
			mapa.put("KILOS", GenericUtils.getDecimalFormat().format(this.kilos.doubleValue()));
			mapa.put("GRAMAJE", this.gramaje);
			mapa.put("NRO_REMITO", this.nroRemito);
			mapa.put("CLIENTE", this.cliente);
			mapa.put("PIEZAS", this.cantidadPiezas);
			mapa.put("ARTICULO", this.articulo);
			mapa.put("CODIGO_ODT", ODTCodigoHelper.getInstance().formatCodigo(this.codigo));
			mapa.put("COLOR", this.color);
			mapa.put("TARIMA", this.tarima);
//			mapa.put("MAQUINA", this.codigo);
			mapa.put("ANCHO_CRUDO", this.anchoCrudo);
			mapa.put("ANCHO_FINAL", this.anchoFinal);
			if(piezasDummy1!=null && piezasDummy2 != null){
				mapa.put("piezasDS1", new JRBeanCollectionDataSource(piezasDummy1));
				mapa.put("piezasDS2", new JRBeanCollectionDataSource(piezasDummy2));
			}
			if(resumenSectorEstampado!=null){
				mapa.put("RESUMEN_SECTOR_ESTAMPADO", resumenSectorEstampado);
			}
			if(resumenSectorHumedo != null){
				mapa.put("RESUMEN_SECTOR_HUMEDO", resumenSectorHumedo);
			}
			if(resumenSectorSeco != null){
				mapa.put("RESUMEN_SECTOR_SECO", resumenSectorSeco);
			}
			if(resumenQuimicos!=null){
				mapa.put("RESUMEN_QUIMICOS", resumenQuimicos);
			}
			if(resumenAlgodon != null){
				mapa.put("RESUMEN_ALGODON", resumenAlgodon);
			}
			if(resumenPoliester != null){
				mapa.put("RESUMEN_POLIESTER", resumenPoliester);
			}
			mapa.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			mapa.put("pasosDS", new JRBeanCollectionDataSource(secuencia.pasos));
			mapa.put("con", new JRBeanCollectionDataSource(Collections.singletonList(this)));
			return mapa;
		}
	}

//	private Component getOwner() {
//		return frameOwner == null ? dialogOwner : frameOwner;
//	}
}
