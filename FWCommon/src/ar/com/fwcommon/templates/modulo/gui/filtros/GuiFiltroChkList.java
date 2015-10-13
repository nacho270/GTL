package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWCheckBoxListDialog;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroLista;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRenderingInformation;

/**
 * GUI que muestra filtros que son una lista de múltiple selección
 * 
 * 
 * 
 * @param <T> Tipo de datos que va a filtrar
 * @param <E> Elementos que se colocarán en la lista
 */
@SuppressWarnings("serial")
public class GuiFiltroChkList<T, E> extends GuiFiltro<T, List<E>> {

	private FWCheckBoxListDialog checkBoxList = null;
	private JButton jButtonMostrarChecks = null;

	public GuiFiltroChkList(ModuloTemplate<T, ? extends Cabecera> owner, FiltroLista<T, E> filtro) {
		super(owner, filtro);
		add(getJButtonMostrarChecks());
		getCheckBoxList().setValores(filtro.getValoresSeleccionables(), true/*isConservarSeleccion()*/);
		setValue(getFiltro().getValue());
	}

	/**
	 * Devuelve el dialogo que se va a mostrar con la lista de elementos para seleccionar
	 * @return Dialogo con la lista de elementos a mostrar
	 */
	protected FWCheckBoxListDialog getCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new FWCheckBoxListDialog();
			checkBoxList.setTitle("");
			checkBoxList.getBtnAceptar().addActionListener(new FiltroActionListener());
			//guiChks.addMouseListener(new ChksMouseListener());
			//guiChks.getCheckBoxList().addListSelectionListener(new ChksListSelectionListener());
		}
		return checkBoxList;
	}

	/**
	 * Devuelve el botón que permite mostrar el checkbox list
	 * @return Botón que muestra
	 */
	protected JButton getJButtonMostrarChecks() {
		if(jButtonMostrarChecks == null) {
			jButtonMostrarChecks = new JButton(getFiltro().getNombre());
			BossEstilos.decorateButton(jButtonMostrarChecks);
			jButtonMostrarChecks.setPreferredSize(new Dimension(90, 20));
			jButtonMostrarChecks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					getCheckBoxList().setVisible(true);
				}
			});
			final FiltroRenderingInformation info = getFiltro().getRenderingInformation();
			if (info.getMinimumSize() != null) {
				jButtonMostrarChecks.setMinimumSize(info.getMinimumSize());
				jButtonMostrarChecks.setPreferredSize(info.getMinimumSize());
			}
			if (info.getMaximumSize() != null) {
				jButtonMostrarChecks.setMaximumSize(info.getMaximumSize());
			}
		}
		return jButtonMostrarChecks;
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshData()
	 */
	@Override
	protected void refreshData() {
		FiltroLista filtro = (FiltroLista<T,E>)getFiltro();
		getCheckBoxList().setValores(filtro.getValoresSeleccionables(), true/*isConservarSeleccion()*/);		
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshName()
	 */
	@Override
	protected void refreshName() {
		getJButtonMostrarChecks().setText(getFiltro().getNombre());
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(List<E> value) {
		if (value != null) {
			getCheckBoxList().setValores(value, true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#canGrow()
	 */
	@Override
	boolean canGrow() {
		if (getFiltro().getRenderingInformation().isAjustable() != null)
			return getFiltro().getRenderingInformation().isAjustable();
		return false;
	}
	
	private class FiltroActionListener implements ActionListener {
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent ae) {
			fireFilterChangeListener(getCheckBoxList().getValoresSeleccionados());
		}
	}
}