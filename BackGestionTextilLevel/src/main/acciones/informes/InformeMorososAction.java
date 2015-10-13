package main.acciones.informes;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogInformeClientesMorosos;
import ar.com.textillevel.gui.util.ClienteMorosoTO;
import ar.com.textillevel.gui.util.ETipoInformeDeuda;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class InformeMorososAction implements Action{

	private final Frame frame;
	
	public InformeMorososAction(Frame frame){
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
		List<ClienteDeudaTO> cls = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class).getClientesDeudores();
		if(cls!=null && !cls.isEmpty()){
			List<ClienteMorosoTO> clientes = new ArrayList<ClienteMorosoTO>();
			BigDecimal suma = new BigDecimal(0d);
			for(ClienteDeudaTO cld : cls){
				clientes.add(new ClienteMorosoTO(cld.getRazonSocial(),"$ " +  GenericUtils.getDecimalFormat().format(cld.getDeuda())));
				suma = suma.add(cld.getDeuda());
			}
			new JDialogInformeClientesMorosos(frame, clientes, suma, ETipoInformeDeuda.CLIENTE).setVisible(true);
		}else{
			FWJOptionPane.showInformationMessage(frame, "No se han encontrado clientes con deuda", "Información");
		}
	}
}
