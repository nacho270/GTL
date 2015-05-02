package main.acciones.compras;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarNotaDeCreditoProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;

public class AgregarNotaCreditoProveedorAction implements Action {

	private Frame frame;

	public AgregarNotaCreditoProveedorAction(Frame padre){
		this.frame = padre;
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
		JDialogSeleccionarProveedor jdsp = new JDialogSeleccionarProveedor(frame);
		GuiUtil.centrar(jdsp);
		jdsp.setVisible(true);
		Proveedor proveedorSel = jdsp.getProveedor();
		if(proveedorSel != null) {
			String[] correcs= new String[ETipoCorreccionFactura.values().length];
			for(int i = 0 ; i<ETipoCorreccionFactura.values().length;i++){
				correcs[i] = ETipoCorreccionFactura.values()[i].getDescripcion();
			}
			Object opcion = JOptionPane.showInputDialog(null, "Seleccione el tipo de nota:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, correcs,correcs[0]);
			if(opcion!=null) {
				JDialogCargarNotaDeCreditoProveedor jdcfp = null;
				ETipoCorreccionFactura tipoCorrecion = ETipoCorreccionFactura.getByDescripcion((String)opcion);
				if(tipoCorrecion == ETipoCorreccionFactura.NOTA_CREDITO) {
					NotaCreditoProveedor ncp = new NotaCreditoProveedor();
					jdcfp = new JDialogCargarNotaDeCreditoProveedor(frame, proveedorSel, ncp, "Nota de Crédito", false);
				} else {
					NotaDebitoProveedor ncp = new NotaDebitoProveedor();
					jdcfp = new JDialogCargarNotaDeCreditoProveedor(frame, proveedorSel, ncp, "Nota de Débito", false);
				}
				GuiUtil.centrar(jdcfp);
				jdcfp.setVisible(true);
			}
		}
	}

}