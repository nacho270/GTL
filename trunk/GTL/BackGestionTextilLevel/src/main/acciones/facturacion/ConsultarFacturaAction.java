package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class ConsultarFacturaAction implements Action{
	
	private final Frame frame;
	
	public ConsultarFacturaAction(Frame frame){
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
		try {
			FacturaFacadeRemote ffr = GTLBeanFactory.getInstance().getBean(FacturaFacadeRemote.class);
			Factura factura = null;
			boolean ok = false;
			do {
				String input = JOptionPane.showInputDialog(frame, "Ingrese el número de factura: ", "Buscar factura", JOptionPane.INFORMATION_MESSAGE);
				if(input == null){
					break;
				}
				if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
					CLJOptionPane.showErrorMessage(frame, "Ingreso incorrecto", "error");
				} else {
					factura = ffr.getByNroFacturaConItems(Integer.valueOf(input.trim()));
					if(factura == null){
						CLJOptionPane.showErrorMessage(frame, "Factura no encontrada", "Error");
					}else{
						ok = true;
						JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(frame,factura, true);						
						dialogCargaFactura.setVisible(true);
					}
				}
			} while (!ok);

		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
	}
}
