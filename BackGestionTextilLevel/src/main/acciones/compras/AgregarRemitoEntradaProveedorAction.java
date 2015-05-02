package main.acciones.compras;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Action;

import main.GTLGlobalCache;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaProveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class AgregarRemitoEntradaProveedorAction implements Action{

	private Frame frame;
	
	public AgregarRemitoEntradaProveedorAction(Frame padre){
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
			JDialogAgregarRemitoEntradaProveedor jdarep = new JDialogAgregarRemitoEntradaProveedor(frame, false,proveedorElegido);
			jdarep.setVisible(true);
			RemitoEntradaProveedor remitoEntrada = jdarep.getRemitoEntrada();
			if(remitoEntrada != null) {
				if(CLJOptionPane.showQuestionMessage(frame, "¿Desea Cargar la Factura del Proveedor?", "Confirmación") == CLJOptionPane.YES_OPTION) {
					RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
					remitoEntrada = repfr.getByIdEager(remitoEntrada.getId());
					FacturaProveedor fp = new FacturaProveedor();
					fp.setTipoFacturaProveedor(ETipoFacturaProveedor.NORMAL);					
					fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					fp.setProveedor(proveedorElegido);
					fp.getRemitoList().add(remitoEntrada);
					JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(frame, fp, false, new ArrayList<RemitoEntradaProveedor>());
					GuiUtil.centrar(jdcfp);
					jdcfp.setVisible(true);
				}
			}
		}
	}

}
