package main.acciones.informes;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;

import javax.swing.Action;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ivaventas.IVAVentasTO;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogInformeIVAVentas;
import ar.com.textillevel.gui.acciones.JDialogReporteIVAVentasPreview;
import ar.com.textillevel.util.GTLBeanFactory;

public class InformeIVAVentasAction implements Action{

	private final Frame frame;
	
	public InformeIVAVentasAction(Frame frame){
		this.frame = frame;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public Object getValue(String key) {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public void putValue(String key, Object value) {
		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public void setEnabled(boolean b) {
		
	}

	public void actionPerformed(ActionEvent e) {
		JDialogInformeIVAVentas jdiiv = new JDialogInformeIVAVentas(frame);
		jdiiv.setVisible(true);
		if(jdiiv.isAcepto()){
			Date fechaDesde = jdiiv.getFechaDesde();
			Date fechaHasta = jdiiv.getFechaHasta();
			Cliente cliente = jdiiv.getCliente();
			IVAVentasTO ivaVentas = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class).calcularIVAVentas(fechaDesde,  fechaHasta, jdiiv.getTipoFactura(),cliente);
			if(ivaVentas.getFacturas()!=null && !ivaVentas.getFacturas().isEmpty()){
				JDialogReporteIVAVentasPreview jDialogReporteIVAVentasPreview = new JDialogReporteIVAVentasPreview(frame, ivaVentas, fechaDesde, fechaHasta);
				jDialogReporteIVAVentasPreview.setVisible(true);
			}else{
				CLJOptionPane.showWarningMessage(frame, "No se han encontrado resultados", "Advertencia");
			}
		}
	}
}
