package ar.com.textillevel.gui.modulos.abm.listaprecios;

import ar.clarin.fwjava.componentes.PanelTabla;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;

public abstract class PanelTablaRango <T extends RangoAncho, E> extends PanelTabla<T> {

	private static final long serialVersionUID = 7325870283327165165L;

	protected JDialogAgregarModificarDefinicionPrecios<T, E> parent;
	
	public PanelTablaRango(JDialogAgregarModificarDefinicionPrecios<T, E> parent) {
		this.parent = parent;
		getBotonAgregar().setVisible(false);
		agregarBotonModificar();
	}

	public abstract int getColObj();

	public void selectElement(E elemHoja) {
		for(int i=0; i < getTabla().getRowCount(); i++) {
			if(getTabla().getValueAt(i, getColObj()) == elemHoja) {
				getTabla().setRowSelectionInterval(i, i);
				parent.setElemHojaSiendoEditado(elemHoja, false);
				break;
			}
		}
	}
	
	public void setTextoBotonGuardar(String texto) {
		parent.getBtnAgregar().setText(texto);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void filaTablaSeleccionada() {
		if(getTabla().getSelectedRow() != -1) {
			E elemHoja = (E)getTabla().getValueAt(getTabla().getSelectedRow(), getColObj());
			parent.setElemHojaSiendoEditado(elemHoja, false);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void botonModificarPresionado(int filaSeleccionada) {
		E rangoCobertura = (E)getTabla().getValueAt(filaSeleccionada, getColObj());
		parent.setElemHojaSiendoEditado(rangoCobertura, true);
		setTextoBotonGuardar("Guardar");
	}

	@Override
	protected String validarElemento(int fila) {
		return null;
	}

	@Override
	public boolean validarQuitar() {
		return true;
	}
	
	@Override
	protected void botonAgregarPresionado() {

	}


}