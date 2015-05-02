package main.acciones.compras;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import main.GTLGlobalCache;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaServicioProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;

public class AgregarFacturaProveedorServicioAction implements Action {

	private Frame frame;

	public AgregarFacturaProveedorServicioAction(Frame padre){
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
			FacturaProveedor fp = new FacturaProveedor();
			fp.setTipoFacturaProveedor(ETipoFacturaProveedor.SERVICIO);			
			fp.setProveedor(proveedorSel);
			fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());			
			JDialogCargarFacturaServicioProveedor jdcfp = new JDialogCargarFacturaServicioProveedor(frame, fp, false);
			GuiUtil.centrar(jdcfp);
			jdcfp.setVisible(true);
		}
	}

}