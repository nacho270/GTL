package main.acciones.informes;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.swing.Action;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.enums.ETipoInformeProduccion;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.informeproduccion.ClienteCantidadTO;
import ar.com.textillevel.entidades.to.informeproduccion.InformeProduccionTO;
import ar.com.textillevel.entidades.to.informeproduccion.ItemInformeProduccionTO;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogInformeProduccion;
import ar.com.textillevel.gui.acciones.JDialogInformeProduccionPreview;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class InformeProduccionAction implements Action{

	private final Frame padre;
	
	public InformeProduccionAction(Frame frame) {
		padre = frame;
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
		JDialogInformeProduccion dialog = new JDialogInformeProduccion(padre);
		dialog.setVisible(true);
		if(dialog.isAcepto()){
			Cliente cliente = dialog.getClienteElegido();
			ETipoInformeProduccion tipoInforme = dialog.getTipoInformeElegido();
			Date fechaDesde = dialog.getFechaDesdeElegida();
			Date fechaHasta = dialog.getFechaHastaElegida();
			Map<Date, List<Map<String, BigDecimal>>> mapaInforme = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class).getInformeProduccion(fechaDesde, fechaHasta, cliente, tipoInforme);
			InformeProduccionTO informeTO = new InformeProduccionTO();
			BigDecimal totalInforme = new BigDecimal(0d);
			if(mapaInforme!=null && !mapaInforme.isEmpty()){
				for(Date fecha : mapaInforme.keySet()){
					ItemInformeProduccionTO itemTO = new ItemInformeProduccionTO();
					itemTO.setDia(DateUtil.dateToString(fecha, DateUtil.SHORT_DATE));
					for(Map<String, BigDecimal> m : mapaInforme.get(fecha)){
						for(String key : m.keySet()){
							ClienteCantidadTO ccTO = new ClienteCantidadTO();
							ccTO.setNombre(key);
							totalInforme = totalInforme.add(m.get(key));
							ccTO.setCantidad(GenericUtils.getDecimalFormat().format(m.get(key))+ " " + tipoInforme.getDescripcion());
							itemTO.getClienteCantidadList().add(ccTO);
						}
					}
					informeTO.getItems().add(itemTO);
				}
			}
			informeTO.setTotal(GenericUtils.getDecimalFormat().format(totalInforme.doubleValue()) + " " +tipoInforme.getDescripcion() );
			new JDialogInformeProduccionPreview(padre, informeTO, 
					DateUtil.dateToString(fechaDesde, DateUtil.SHORT_DATE), 
					DateUtil.dateToString(fechaHasta, DateUtil.SHORT_DATE),
					GenericUtils.restarFechas(fechaDesde,fechaHasta)).setVisible(true);
		}
	}
}
