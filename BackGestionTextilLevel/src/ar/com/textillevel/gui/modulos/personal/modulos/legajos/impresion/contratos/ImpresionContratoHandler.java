package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos;

import java.awt.Frame;
import java.util.Collections;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.strategy.CreacionContratoStrategyFactory;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ImpresionContratoHandler {

	private Empleado empleado;
	private Frame frame;
	
	public ImpresionContratoHandler(Empleado empleado, Frame frame){
		this.setEmpleado(empleado);
		this.setFrame(frame);
	}

	public void previsualizarContrato(){
		String pathArchivoContrato = getEmpleado().getContratoEmpleado().getContrato().getPathArchivoContrato();
		if(pathArchivoContrato != null){
			String path =  "/ar/com/textillevel/reportes/"+pathArchivoContrato;
			JasperReport reporte = JasperHelper.loadReporte(path);
			if(reporte == null){
				FWJOptionPane.showErrorMessage(getFrame(), "No se ha podido imprimir el contrato debido a que no se ha encontrado el modelo llamado: " + pathArchivoContrato, "Error");
				return;
			}
			Map<String, Object> parametrosContrato = getParametrosContrato( getEmpleado().getContratoEmpleado().getContrato().getTipoContrato());
			if(parametrosContrato!=null){
				JasperPrint jasperPrint = JasperHelper.fillReport(reporte,parametrosContrato,Collections.singletonList("A"));
					JasperHelper.visualizarReporte(jasperPrint);
			}else{
				FWJOptionPane.showErrorMessage(getFrame(), "Debe ingresar la persona que firma. Se ha cancelado la impresión", "Error");
				return;
			}
		}else{
			FWJOptionPane.showErrorMessage(getFrame(), "No se ha podido imprimir el contrato debido a que no se ha definido el modelo del mismo.", "Error");
			return;
		}
	}
	
	public void imprimirContrato() {
		String pathArchivoContrato = getEmpleado().getContratoEmpleado().getContrato().getPathArchivoContrato();
		if(pathArchivoContrato != null){
			String path =  "/ar/com/textillevel/reportes/"+pathArchivoContrato;
			JasperReport reporte = JasperHelper.loadReporte(path);
			if(reporte == null){
				FWJOptionPane.showErrorMessage(getFrame(), "No se ha podido imprimir el contrato debido a que no se ha encontrado el modelo llamado: " + pathArchivoContrato, "Error");
				return;
			}
			try {
				Map<String, Object> parametrosContrato = getParametrosContrato(getEmpleado().getContratoEmpleado().getContrato().getTipoContrato());
				if(parametrosContrato!=null){
					JasperPrint jasperPrint = JasperHelper.fillReport(reporte,parametrosContrato,Collections.singletonList("A"));
					if(FWJOptionPane.showQuestionMessage(getFrame(), "¿Desea previsualizar el contrato?", "Pregunta")==FWJOptionPane.YES_OPTION){
						JasperHelper.visualizarReporte(jasperPrint);
					}else{
						JasperHelper.imprimirReporte(jasperPrint, true, true, 1);
					}
				}else{
					FWJOptionPane.showErrorMessage(getFrame(), "Debe ingresar la persona que firma. Se ha cancelado la impresión", "Error");
					return;
				}
			} catch (JRException e) {
				e.printStackTrace();
			}
		}else{
			FWJOptionPane.showErrorMessage(getFrame(), "No se ha podido imprimir el contrato debido a que no se ha definido el modelo del mismo.", "Error");
			return;
		}
	}

	private Map<String, Object> getParametrosContrato(ETipoContrato tipoDeContrato) {
		return CreacionContratoStrategyFactory.crearStrategy(tipoDeContrato).crearContrato(getEmpleado());
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}
}
