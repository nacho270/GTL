package ar.com.fwcommon.templates.modulo.gui.status;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;
import ar.com.fwcommon.templates.modulo.model.status.Status;

@SuppressWarnings("serial")
public class GuiStatus<T> extends JPanel {
	
	private final ModuloTemplate<T, ?> owner;
	private Status<T> status;
	private JLabel jLabelNombre;
	private JLabel jLabelValue;

	public GuiStatus(ModuloTemplate<T, ?> owner, Status<T> status) {
		super();
		this.owner = owner;
		setStatus(status);
		construct();
	}

	/**
	 * Construye gráficamente los componentes
	 */
	private void construct() {
		this.setLayout(new GridBagLayout());
		this.add(getJLabelNombre(), 
				new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,0,0),10,2));
		this.add(getJLabelValue(), 
				new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),10,2));		
	}

	/**
	 * Devuelve el Módulo Template al que pertenece esta GUI
	 * @return Modulo Template al que pertenece esta GUI
	 */
	public ModuloTemplate<T, ?> getOwner() {
		return owner;
	}
	
	/**
	 * Devuelve el modelo
	 * @return Modelo
	 */
	public Status<T> getStatus() {
		return status;
	}

	/**
	 * Establece el modelo
	 * @param status modelo
	 */
	public void setStatus(final Status<T> status) {
		if (this.status != status) {
			if (this.status != null) this.status.removeModelChangeListener(getModelChangeListener());
			this.status = status;
			this.status.addModelChangeListener(getModelChangeListener());
		}
	}

	private ModelChangeListener modelChangeListener;
	private ModelChangeListener getModelChangeListener() {
		if (modelChangeListener == null) {
			modelChangeListener = new ModelChangeListener() {
				public void stateChanged(ModelChangeEvent e) {
					switch(e.getEventType()) {
					case Status.EVENT_TYPE_VALUE_CHANGE:
						getJLabelValue().setText(getStatus().getValue());
						getJLabelValue().setToolTipText(getStatus().getTooltip());
						getJLabelValue().setBackground(getStatus().getColor());
						break;

					case Status.EVENT_TYPE_COLOR_CHANGE:
						getJLabelValue().setBackground(getStatus().getColor());
						getJLabelNombre().setBackground(getStatus().getInfoColor());
						break;

					case Status.EVENT_TYPE_NAME_CHANGE:
						getJLabelNombre().setText(getStatus().getNombre());
						break;

					case Status.EVENT_TYPE_TOOLTIP_CHANGE:
						getJLabelValue().setToolTipText(getStatus().getTooltip());
						break;
					}
				}			
			};
		}
		return modelChangeListener;
	}

	protected JLabel getJLabelValue() {
		if (jLabelValue == null) {
			jLabelValue = BossEstilos.createLabel(
					getStatus().getValue(),
					BorderFactory.createLineBorder(Color.GRAY));
			jLabelValue.setOpaque(true);
			jLabelValue.setBackground(getStatus().getColor());			
		}
		return jLabelValue;
	}

	protected JLabel getJLabelNombre() {
		if (jLabelNombre == null) {
			jLabelNombre = BossEstilos.createLabel(
					getStatus().getNombre(),
					BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY));
			jLabelNombre.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelNombre.setOpaque(true);
			//jLabelNombre.setBackground(new Color(198, 213, 185));
			jLabelNombre.setBackground(getStatus().getInfoColor());
		}
		return jLabelNombre;
	}
}
