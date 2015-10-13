package ar.com.fwcommon.templates.modulo.gui.totales;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;
import ar.com.fwcommon.templates.modulo.model.totales.Total;

/**
 * GUI que permite visualizar un total determinado
 * 
 */
@SuppressWarnings("serial")
public class GuiTotal<T> extends JPanel {

	private final ModuloTemplate<T, ?> owner;
    private Total<T> total;
    private JLabel jLabelTotalValue;
	private JLabel jLabelName;

    public GuiTotal(ModuloTemplate<T, ? > owner, Total<T> total) {
        super();
        this.owner = owner;
        setTotal(total);
        setLayout(new FlowLayout(FlowLayout.LEADING, 5, 0));
        add(getJLabelName());
        add(getJLabelTotalValue());
        setColor(total.getColor());   
    }

	/**
	 * Devuelve el Módulo Template al que pertenece esta GUI
	 * @return Modulo Template al que pertenece esta GUI
	 */
	protected ModuloTemplate<T, ?> getOwner() {
		return owner;
	}
	

	/**
	 * Devuelve el <code>JLabel</code> que es utilizado para mostrar el nombre
	 * del total
	 * 
	 * @return Componente utilizado para mostrar el nombre del total
	 */
	protected JLabel getJLabelName() {
		if (jLabelName == null) {
			jLabelName = new JLabel(getTotal().getNombre());
	        jLabelName.setFont(BossEstilos.getDefaultFont());
	        jLabelName.setToolTipText(getTotal().getTooltip());
		}
		return jLabelName;
	}
	
    /**
	 * Devuelve el <code>JLabel</code> que es utilizado para mostrar el valor
	 * en pantalla
	 * 
	 * @return Componente utilizado para mostrar el valor
	 */
    protected JLabel getJLabelTotalValue() {
        if(jLabelTotalValue == null) {
            jLabelTotalValue = new JLabel();
            jLabelTotalValue.setPreferredSize(new Dimension(40, 20));
            jLabelTotalValue.setBorder(new LineBorder(new Color(102, 108, 87)));
            jLabelTotalValue.setHorizontalAlignment(JLabel.CENTER);
            jLabelTotalValue.setFont(BossEstilos.getDefaultFont());
            jLabelTotalValue.setOpaque(true);
        }
        return jLabelTotalValue;
    }

    /**
     * Establece el valor al total
     * @param total establece el valor al total
     */
    private void setValue(int total) {
        getJLabelTotalValue().setText(String.valueOf(total));
    }

    /**
     * Setea el color de fondo al componente
     * @param color Color de fondo
     */
    private void setColor(Color color) {
        getJLabelTotalValue().setBackground(color);
    }

	/**
	 * Devuelve el modelo del total
	 * @return Modelo del total
	 */
	public Total<T> getTotal() {
		return total;
	}

	/**
	 * Setea el modelo del total
	 * @param total Modelo a establecer
	 */
	private void setTotal(final Total<T> total) {
		if (this.total != total) {
			if (this.total != null) this.total.removeModelChangeListener(getModelChangeListener());
			this.total = total;
			this.total.addModelChangeListener(getModelChangeListener());
		}
	}

	private ModelChangeListener modelChangeListener;
	private ModelChangeListener getModelChangeListener() {
		if (modelChangeListener == null) {
			modelChangeListener = new ModelChangeListener() {
				public void stateChanged(ModelChangeEvent e) {
					switch(e.getEventType()) {
					case Total.EVENT_TYPE_VALUE_CHANGE:
						actualizarValores();
						getJLabelTotalValue().setToolTipText(getTotal().getTooltip());
						getJLabelTotalValue().setBackground(getTotal().getColor());
						break;

					case Total.EVENT_TYPE_COLOR_CHANGE:
						setColor(getTotal().getColor());
						break;

					case Total.EVENT_TYPE_NAME_CHANGE:
						getJLabelName().setText(getTotal().getNombre());
						break;

					case Total.EVENT_TYPE_TOOLTIP_CHANGE:
						getJLabelName().setToolTipText(getTotal().getTooltip());
						break;
					}
				}			
			};
		} return modelChangeListener;
	}

	/**
	 * Actualiza la vista tomando el valor del modelo
	 */
	protected void actualizarValores() {
		setValue(getTotal().getValue());
	}
}