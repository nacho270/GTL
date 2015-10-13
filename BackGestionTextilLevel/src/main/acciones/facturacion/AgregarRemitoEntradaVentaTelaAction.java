package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Action;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaCompraTela;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.GTLBeanFactory;

public class AgregarRemitoEntradaVentaTelaAction implements Action {

	private final Frame frame;

	public AgregarRemitoEntradaVentaTelaAction(Frame frame) {
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
		JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(frame);
		GuiUtil.centrar(dialogSeleccionarProveedor);
		dialogSeleccionarProveedor.setVisible(true);
		Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
		if(proveedorElegido != null) {
			RemitoEntrada remitoEntrada = new RemitoEntrada();
			remitoEntrada.setProveedor(proveedorElegido);
			JDialogAgregarRemitoEntradaCompraTela dialogAgregarRemitoEntrada = new JDialogAgregarRemitoEntradaCompraTela(frame, remitoEntrada, new ArrayList<OrdenDeTrabajo>(), false);
			GuiUtil.centrar(dialogAgregarRemitoEntrada);		
			dialogAgregarRemitoEntrada.setVisible(true);
			RemitoEntradaProveedor remitoEntradaProveedor = dialogAgregarRemitoEntrada.getRemitoEntradaProveedor();
			if(remitoEntradaProveedor != null) {
				if(FWJOptionPane.showQuestionMessage(frame, "¿Desea Cargar la Factura del Proveedor?", "Confirmación") == FWJOptionPane.YES_OPTION) {
					RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
					remitoEntradaProveedor = repfr.getByIdEager(remitoEntradaProveedor.getId());
					FacturaProveedor fp = new FacturaProveedor();
					fp.setTipoFacturaProveedor(ETipoFacturaProveedor.NORMAL);					
					fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					fp.setProveedor(proveedorElegido);
					fp.getRemitoList().add(remitoEntradaProveedor);
					JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(frame, fp, false, new ArrayList<RemitoEntradaProveedor>());
					GuiUtil.centrar(jdcfp);
					jdcfp.setVisible(true);
				}
			}
		}
	}

}