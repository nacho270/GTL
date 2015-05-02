package main.acciones.compras;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.Action;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.gui.acciones.proveedor.remitosalida.JDialogAgregarRemitoSalidaProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;

public class AgregarRemitoSalidaProveedorAction implements Action{

	private Frame frame;
	
	public AgregarRemitoSalidaProveedorAction(Frame padre){
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
		JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(frame);
		GuiUtil.centrar(dialogSeleccionarProveedor);
		dialogSeleccionarProveedor.setVisible(true);
		Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
		if(proveedorElegido != null) {
			RemitoSalida remitoSalidaProveedor = new RemitoSalida();
			remitoSalidaProveedor.setTipoRemitoSalida(ETipoRemitoSalida.PROVEEDOR);
			remitoSalidaProveedor.setNroFactura(0);
			remitoSalidaProveedor.setNroOrden(0);
			remitoSalidaProveedor.setPesoTotal(new BigDecimal(0));
			remitoSalidaProveedor.setPorcentajeMerma(new BigDecimal(0));
			remitoSalidaProveedor.setProveedor(proveedorElegido);
			JDialogAgregarRemitoSalidaProveedor jdarsp = new JDialogAgregarRemitoSalidaProveedor(frame, false, remitoSalidaProveedor);
			jdarsp.setVisible(true);
		}
	}
}
