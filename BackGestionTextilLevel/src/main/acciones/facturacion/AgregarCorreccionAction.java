package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class AgregarCorreccionAction implements Action{

	private final Frame frame;
	
	public AgregarCorreccionAction(Frame frame){
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
			ClienteFacadeRemote ffr = GTLBeanFactory.getInstance().getBean(ClienteFacadeRemote.class);
			Cliente cliente = null;
			boolean ok = false;
			do {
				String input = JOptionPane.showInputDialog(frame, "Ingrese el número de cliente: ", "Buscar cliente", JOptionPane.INFORMATION_MESSAGE);
				if(input == null){
					break;
				}
				if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
					CLJOptionPane.showErrorMessage(frame, "Ingreso incorrecto", "error");
				} else {
					cliente = ffr.getClienteByNumero(Integer.valueOf(input.trim()));
					if(cliente == null){
						CLJOptionPane.showErrorMessage(frame, "Cliente no encontrado", "Error");
					}else{
						ok = true;
						String[] correcs= new String[ETipoCorreccionFactura.values().length];
						for(int i = 0 ; i<ETipoCorreccionFactura.values().length;i++){
							correcs[i] = ETipoCorreccionFactura.values()[i].getDescripcion();
						}
						Object opcion = JOptionPane.showInputDialog(null, "Seleccione el tipo de nota:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, correcs,correcs[0]);
						if(opcion!=null){
							ETipoCorreccionFactura tipoCorrecion = ETipoCorreccionFactura.getByDescripcion((String)opcion);
							JDialogCargaFactura jdcf = new JDialogCargaFactura(frame, cliente, tipoCorrecion);
							jdcf.setVisible(true);
						}
					}
				}
			} while (!ok);

		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
	}
}
