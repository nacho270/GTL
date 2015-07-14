package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import main.acciones.facturacion.gui.JDialogParamBusquedaCorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;

public class ConsultarCorreccionesAction implements Action{

	private Frame frame;
	
	public ConsultarCorreccionesAction(Frame frame){
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
		JDialogParamBusquedaCorreccionFactura dialogo = new JDialogParamBusquedaCorreccionFactura(frame);
		dialogo.setVisible(true);
		CorreccionFactura correccionFactura = dialogo.getCorreccionFactura();
		if(correccionFactura != null) {
			JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(frame, correccionFactura, true);
			dialogCargaFactura.setVisible(true);
		}
		
		/*
		try {
			CorreccionFacadeRemote ffr = GTLBeanFactory.getInstance().getBean(CorreccionFacadeRemote.class);
			CorreccionFactura correccion = null;
			boolean ok = false;
			do {
				String input = JOptionPane.showInputDialog(frame, "Ingrese el número: ", "Buscar notas de débito/crédito", JOptionPane.INFORMATION_MESSAGE);
				if(input == null){
					break;
				}
				if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
					CLJOptionPane.showErrorMessage(frame, "Ingreso incorrecto", "error");
				} else {
					String[] correcs= new String[ETipoCorreccionFactura.values().length];
					for(int i = 0 ; i<ETipoCorreccionFactura.values().length;i++){
						correcs[i] = ETipoCorreccionFactura.values()[i].getDescripcion();
					}
					Object opcion = JOptionPane.showInputDialog(frame, "Seleccione el tipo de nota:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, correcs,correcs[0]);
					if(opcion!=null){
						ETipoCorreccionFactura tipoCorrecion = ETipoCorreccionFactura.getByDescripcion((String)opcion);
						correccion = ffr.getCorreccionByNumero(Integer.valueOf(input.trim()),tipoCorrecion, null);
						if(correccion == null){
							CLJOptionPane.showErrorMessage(frame, "No se encontraron resultados", "Error");
						}else{
							ok = true;
							JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(frame,correccion, true);
							dialogCargaFactura.setVisible(true);
						}
					}
				}
			} while (!ok);

		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
		*/
	}
}
