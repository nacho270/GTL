package main.acciones.compras;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import main.GTLGlobalCache;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarRemitoEntradaProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class AgregarFacturaProveedorAction implements Action {

	private Frame frame;

	public AgregarFacturaProveedorAction(Frame padre){
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
			List<RemitoEntradaProveedor> remitosNoAsocByProveedor = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class).getRemitosNoAsocByProveedor(proveedorSel);
			JDialogSeleccionarRemitoEntradaProveedor jdsrep = new JDialogSeleccionarRemitoEntradaProveedor(frame, proveedorSel, remitosNoAsocByProveedor); 
			GuiUtil.centrar(jdsrep);
			jdsrep.setVisible(true);
			List<RemitoEntradaProveedor> remitoEntradaList = toEager(jdsrep.getRemitoEntradaList(), proveedorSel);
			if(!remitoEntradaList.isEmpty()) {
				FacturaProveedor fp = new FacturaProveedor();
				fp.setTipoFacturaProveedor(ETipoFacturaProveedor.NORMAL);
				fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				fp.setProveedor(proveedorSel);
				fp.getRemitoList().addAll(remitoEntradaList);
				JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(frame, fp, false, remitosNoAsocByProveedor);
				GuiUtil.centrar(jdcfp);
				jdcfp.setVisible(true);
			}
		}
	}

	private List<RemitoEntradaProveedor> toEager(List<RemitoEntradaProveedor> remitoEntradaList, Proveedor proveedorSel) {
		List<RemitoEntradaProveedor> remitoListEager = new ArrayList<RemitoEntradaProveedor>();
		RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
		for(RemitoEntradaProveedor rep : remitoEntradaList) {
			RemitoEntradaProveedor repEager = repfr.getByIdEager(rep.getId());
			repEager.setProveedor(proveedorSel);
			remitoListEager.add(repEager);
			
		}
		return remitoListEager;
	}

}