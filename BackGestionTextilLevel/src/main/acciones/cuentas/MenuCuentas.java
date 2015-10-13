package main.acciones.cuentas;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

import ar.com.fwcommon.templates.main.menu.FWJMenu;

public class MenuCuentas extends FWJMenu{

	private static final long serialVersionUID = 1849667827454370225L;

	private final Frame frame;
	private JMenuItem itemVerMovimientos;
	private JMenuItem itemVerMovimientosProveedor;

	public MenuCuentas(Frame frame) {
		super("Cuentas", 'C');
		setMnemonic(KeyEvent.VK_U);
		this.frame = frame;
		this.add(getItemVerMovimientos());
		this.add(getItemVerMovimientosProveedor());
	}

	public JMenuItem getItemVerMovimientos() {
		if(itemVerMovimientos == null){
			itemVerMovimientos = new JMenuItem(new VerMovimientosCuentaAction(frame));
			itemVerMovimientos.setText("Ver movimientos de cuentas de clientes");
		}
		return itemVerMovimientos;
	}
	
	public JMenuItem getItemVerMovimientosProveedor() {
		if(itemVerMovimientosProveedor == null){
			itemVerMovimientosProveedor = new JMenuItem(new VerMovimientosCuentaProveedorAction(frame));
			itemVerMovimientosProveedor.setText("Ver movimientos de cuentas de proveedores");
		}
		return itemVerMovimientosProveedor;
	}
}
