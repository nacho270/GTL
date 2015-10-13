package main.acciones.informes;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.util.List;

import javax.swing.Action;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.recibo.to.ResumenReciboTO;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogInformeIVAVentas;
import ar.com.textillevel.gui.acciones.JDialogReporteRecibosPreview;
import ar.com.textillevel.util.GTLBeanFactory;

public class InformeRecibosAction implements Action{

	private Frame frame;
	
	public InformeRecibosAction(Frame frame){
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
			Integer idCliente = cliente == null ? null : cliente.getId();
			List<ResumenReciboTO> resumenReciboList = GTLBeanFactory.getInstance().getBean2(ReciboFacadeRemote.class).getResumenReciboList(idCliente, fechaDesde,  DateUtil.getManiana(fechaHasta));
			if(!resumenReciboList.isEmpty()){
				JDialogReporteRecibosPreview dialogo = new JDialogReporteRecibosPreview(frame, resumenReciboList, cliente, fechaDesde,  fechaHasta);
				dialogo.setVisible(true);
			}else{
				FWJOptionPane.showWarningMessage(frame, "No se han encontrado resultados", "Advertencia");
			}
		}
	}
}
