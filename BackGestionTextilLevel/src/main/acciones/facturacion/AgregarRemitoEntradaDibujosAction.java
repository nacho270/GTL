package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaDibujoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;
import main.acciones.facturacion.gui.JDialogIngresarDibujosEstampado;

public class AgregarRemitoEntradaDibujosAction implements Action {
	
	private Frame frame;
	private RemitoEntradaDibujoFacadeRemote remitoEntradaDibujoFacade;

	public AgregarRemitoEntradaDibujosAction(Frame frame) {
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
		JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(frame, EModoDialogo.MODO_ID);
		GuiUtil.centrar(dialogSeleccionarCliente);
		dialogSeleccionarCliente.setVisible(true);
		Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
		if(clienteElegido != null) {
			boolean ok = false;
			do {
				String cantImprimirStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad de cilindros: ", "Ingreso de dibujos", JOptionPane.INFORMATION_MESSAGE);
				if(cantImprimirStr == null){
					break;
				}
				if (cantImprimirStr.trim().length()==0 || !GenericUtils.esNumerico(cantImprimirStr)) {
					FWJOptionPane.showErrorMessage(frame, "Ingreso incorrecto", "error");
				} else {
					ok = true;
					RemitoEntradaDibujo red = new RemitoEntradaDibujo();
					red.setCliente(clienteElegido);
					JDialogIngresarDibujosEstampado dialogo = new JDialogIngresarDibujosEstampado(frame, red, Integer.valueOf(cantImprimirStr));
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						getRemitoEntradaDibujoFacade().grabarREDibujo(red, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					}
				}
			} while (!ok);
		}
	}

	private RemitoEntradaDibujoFacadeRemote getRemitoEntradaDibujoFacade() {
		if(remitoEntradaDibujoFacade == null) {
			remitoEntradaDibujoFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaDibujoFacadeRemote.class);
		}
		return remitoEntradaDibujoFacade;
	}

}