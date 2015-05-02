package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Action;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.acciones.JDialogElegirRemitosClienteSinFactura;
import ar.com.textillevel.gui.acciones.JDialogQuestionNumberInput;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;

public class AgregarFacturaAction implements Action {

	private final Frame frame;

	public AgregarFacturaAction(Frame frame) {
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
		JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(frame);
		GuiUtil.centrar(dialogSeleccionarCliente);
		dialogSeleccionarCliente.setVisible(true);
		Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
		if (clienteElegido != null) {
			JDialogElegirRemitosClienteSinFactura d = new JDialogElegirRemitosClienteSinFactura(frame, clienteElegido);
			d.setVisible(true);
			if(d.isAcepto()){
				List<RemitoSalida> remitosSeleccionados = d.getRemitosSeleccionados();
				Integer nroPiezas = 0;
				for(RemitoSalida r : remitosSeleccionados){
					nroPiezas += r.getCantidadPiezas();
				}
				JDialogQuestionNumberInput dialogQuestionNumberInput = new JDialogQuestionNumberInput(frame, "Confirmación", "¿Confirma la carga de la factura?", "Cantidad de tubos:", nroPiezas);
				GuiUtil.centrar(dialogQuestionNumberInput);
				dialogQuestionNumberInput.setVisible(true);
				if(dialogQuestionNumberInput.isAcepto()){
					Integer cantTubosIngresada = dialogQuestionNumberInput.getNumberInput();
					if (cantTubosIngresada == null) {
						cantTubosIngresada=0;
					}
					JDialogCargaFactura jdcf = new JDialogCargaFactura(frame, remitosSeleccionados, remitosSeleccionados.get(0).getNroFactura(), cantTubosIngresada, true, clienteElegido);
					jdcf.setVisible(true);
				}

			}
		}		
	}

//	public void actionPerformed(ActionEvent e) {
//		try {
//			RemitoSalidaFacadeRemote rfr = GTLBeanFactory.getInstance().getBean(RemitoSalidaFacadeRemote.class);
//			boolean ok = false;
//			Integer nroRemito = null;
//			RemitoSalida remito = null;
//			do {
//				String input = JOptionPane.showInputDialog(frame, "Ingrese el número de remito: ", "Buscar remito", JOptionPane.INFORMATION_MESSAGE);
//				if(input == null){
//					break;
//				}
//				if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
//					CLJOptionPane.showErrorMessage(frame, "Ingreso incorrecto", "error");
//				} else {
//					nroRemito = Integer.valueOf(input.trim());
//					remito = rfr.getByNroRemitoConPiezasYProductos(nroRemito);
//					if(remito == null){
//						CLJOptionPane.showErrorMessage(frame, "Remito no encontrado", "Error");
//					}else {
//						if(remito.getProveedor() != null) {
//							CLJOptionPane.showErrorMessage(frame, "No se puede facturar este remito porque fue hecho para un proveedor", "Error");
//						} else {
//							if(remito.getFactura()!=null){
//								CLJOptionPane.showErrorMessage(frame, "Ya existe una factura asignada a este remito", "Error");
//							}else{
//								ok = true;
//								JDialogQuestionNumberInput dialogQuestionNumberInput = new JDialogQuestionNumberInput(frame, "Confirmación", "¿Desea Cargar una factura?", "Cantidad de tubos:", remito.getCantidadPiezas());
//								GuiUtil.centrar(dialogQuestionNumberInput);
//								dialogQuestionNumberInput.setVisible(true);
//								if(dialogQuestionNumberInput.isAcepto()){
//									Integer cantTubosIngresada = dialogQuestionNumberInput.getNumberInput();
//									if (cantTubosIngresada == null) {
//										cantTubosIngresada=0;
//									}
//									JDialogCargaFactura jdcf = new JDialogCargaFactura(frame, Collections.singletonList(remito), remito.getNroFactura(), cantTubosIngresada, true, remito.getCliente());
//									jdcf.setVisible(true);
//								}
//							}
//						}
//					}
//				}
//			} while (!ok);
//
//		} catch (CLException e1) {
//			BossError.gestionarError(e1);
//		}
//	}
}
