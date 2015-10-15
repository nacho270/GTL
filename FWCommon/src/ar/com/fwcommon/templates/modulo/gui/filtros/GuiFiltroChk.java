package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroOpcion;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRenderingInformation;

/**
 * GUI que muestra filtros del tipo CheckBox
 * 
 *
 * @param <T> Tipo de datos que va a filtrar
 */
@SuppressWarnings("serial")
public class GuiFiltroChk<T> extends GuiFiltro<T, Boolean> {

	private JCheckBox jCheckBox = null;

	public GuiFiltroChk(ModuloTemplate<T, ? extends Cabecera<?>> owner, FiltroOpcion<T> filtro) {
		super(owner, filtro);
		this.getJCheckBox().setText(filtro.getNombre());
		this.add(getJCheckBox());
		setValue(getFiltro().getValue());
	}

	private JCheckBox getJCheckBox() {
		if(jCheckBox == null) {
			jCheckBox = new JCheckBox();
			BossEstilos.decorateCheckBox(jCheckBox);
			jCheckBox.addItemListener(new ChkItemListener());
			final FiltroRenderingInformation info = getFiltro().getRenderingInformation();
			if (info.getMinimumSize() != null) {
				jCheckBox.setMinimumSize(info.getMinimumSize());
				jCheckBox.setPreferredSize(info.getMinimumSize());

			}
			if (info.getMaximumSize() != null) {
				jCheckBox.setMaximumSize(info.getMaximumSize());
			}
			//TODO Debe mejorse este Metodo.
			//Permite construir filtro ocultos
			if (info.getMaximumSize() != null && info.getMinimumSize().width  ==0){
				jCheckBox.setVisible(false);
			}
		}
		return jCheckBox;
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshData()
	 */
	@Override
	protected void refreshData() {}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshName()
	 */
	@Override
	protected void refreshName() {
		getJCheckBox().setText(getFiltro().getNombre());
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(Boolean value) {
		if (value != null) {
			getJCheckBox().setSelected(value);
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
	
	private class ChkItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			fireFilterChangeListener(getJCheckBox().isSelected());
		}
	}
}