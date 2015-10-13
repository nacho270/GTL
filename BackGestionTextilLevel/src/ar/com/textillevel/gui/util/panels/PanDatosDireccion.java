package ar.com.textillevel.gui.util.panels;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.modulo.model.listeners.ListChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ListChangeListener;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.gente.InfoDireccion;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.gui.util.dialogs.JDialogCargaInfoLocalidad;

public class PanDatosDireccion extends JPanel {

	private static final long serialVersionUID = -7656267152056184608L;

	private static final int MAX_LONGITUD_DIRECCION = 50;

	public static final Integer OTRO = -1;

	private final Frame frame;
	private FWJTextField txtDireccion;
	private FWJNumericTextField txtCP;
	private JComboBox cmbLocalidad;
	private InfoDireccion direccion;
	private List<InfoLocalidad> infoLocalidadList;
	private final String title;
	
	public PanDatosDireccion(Frame frame, String title) {
		this.frame = frame;
		construct(title);
		this.title = title;
	}
	
	public void setDireccion(InfoDireccion direccion, List<InfoLocalidad> infoLocalidadList) {
		this.direccion = direccion;
		this.infoLocalidadList = infoLocalidadList;
		llenarComboLocalidad();
		setDatos();
	}

	private void llenarComboLocalidad() {
		GuiUtil.llenarCombo(getCmbLocalidad(), infoLocalidadList, true);
		getCmbLocalidad().setSelectedIndex(-1);
	}

	private void setDatos() {
		if(direccion != null) {
			getTxtDireccion().setText(direccion.getDireccion());
			if(direccion.getLocalidad() != null) {
				getTxtCP().setText(direccion.getLocalidad().getCodigoPostal().toString());
				getCmbLocalidad().setSelectedItem(direccion.getLocalidad());
			}
		}
	}

	private void construct(String title) {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 10, 0, 20);
		gc.anchor = GridBagConstraints.WEST;
		add(new JLabel("CALLE:"), gc);
		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 0.9;
		gc.anchor = GridBagConstraints.WEST;
		add(getTxtDireccion(), gc);
		gc = new GridBagConstraints();
		gc.gridx = 2;
		gc.gridy = 0;
		gc.insets = new Insets(0, 10, 0, 5);
		add(new JLabel("LOC.:"), gc);
		gc = new GridBagConstraints();
		gc.gridx = 3;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new Insets(0, 0, 0, 10);
		add(getCmbLocalidad(), gc);
		gc = new GridBagConstraints();
		gc.gridx = 4;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.WEST;
		add(new JLabel("CP:"), gc);
		gc = new GridBagConstraints();
		gc.gridx = 5;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 0.1;
		add(getTxtCP(), gc);
		setBorder(BorderFactory.createTitledBorder(title));
	}

	private InfoDireccion getDireccion() {
		if(direccion == null) {
			direccion = new InfoDireccion();
		}
		return direccion;
	}

	private FWJTextField getTxtDireccion() {
		if(txtDireccion == null) {
			txtDireccion = new FWJTextField(MAX_LONGITUD_DIRECCION);
		}
		return txtDireccion;
	}

	private FWJNumericTextField getTxtCP() {
		if(txtCP == null) {
			txtCP = new FWJNumericTextField();
			txtCP.setEditable(false);
		}
		return txtCP;
	}

	public InfoDireccion getInfoDireccion() {
		getDireccion().setDireccion(getTxtDireccion().getText().trim().toUpperCase());
		getDireccion().setLocalidad((InfoLocalidad)getCmbLocalidad().getSelectedItem());
		return getDireccion();
	}

	private JComboBox getCmbLocalidad() {
		if(cmbLocalidad == null) {
			cmbLocalidad = new JComboBox();
			cmbLocalidad.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(getCmbLocalidad().isEnabled()) {
							InfoLocalidad infoLocalidadSelected = ((InfoLocalidad)getCmbLocalidad().getSelectedItem());
							if(getCmbLocalidad().getSelectedItem() != null && infoLocalidadSelected.getId() == OTRO) {
								JDialogCargaInfoLocalidad dialogCargaInfoLocalidad = new JDialogCargaInfoLocalidad(frame);
								GuiUtil.centrarEnFramePadre(dialogCargaInfoLocalidad);
								dialogCargaInfoLocalidad.setVisible(true);
								InfoLocalidad infoLocalidad = dialogCargaInfoLocalidad.getInfoLocalidad();
								if(infoLocalidad != null) {
									getDireccion().setLocalidad(infoLocalidad);
									fireNewInfoLocalidadAddedListener(infoLocalidad);
								} else {
									getCmbLocalidad().setSelectedItem(getDireccion().getLocalidad());
								}
							} else if(infoLocalidadSelected != null) {
								getDireccion().setLocalidad(infoLocalidadSelected);
								getTxtCP().setText(infoLocalidadSelected.getCodigoPostal().toString());
								fireNewInfoLocalidadSelectedListener(infoLocalidadSelected);
							}
					}
				}

			});
		}
		return cmbLocalidad;
	}

	public void seleccionarLocalidad(InfoLocalidad infoLocalidad) {
		llenarComboLocalidad();
		if(infoLocalidad != null) {
			getCmbLocalidad().setSelectedItem(infoLocalidad);
			getTxtCP().setText(infoLocalidad.getCodigoPostal().toString());
		}
	}

	public void limpiarDatos() {
		getTxtCP().setText(null);
		getCmbLocalidad().setSelectedIndex(-1);
		getTxtDireccion().setText(null);
		direccion = null;
	}

	public String validar() {
		if(StringUtil.isNullOrEmpty(getTxtDireccion().getText().trim())) {
			getTxtDireccion().requestFocus();
			return "Falta completar la calle del campo '" + title + "'";
		}
		if(getCmbLocalidad().getSelectedItem() == null) {
			getCmbLocalidad().requestFocus();
			return "Falta seleccionar la localidad del campo '" + title + "'";
		}
		return null;
	}
	
	public void addPanDireccionListener(PanDireccionEventListener l) {
		listenerList.add(PanDireccionEventListener.class, l);
	}
	
	public void removePanDireccionListener(PanDireccionEventListener l) {
		listenerList.remove(PanDireccionEventListener.class, l);
	}
	
	public void addListChangeListener(ListChangeListener l) {
		listenerList.add(ListChangeListener.class, l);
	}

	protected final void fireNewInfoLocalidadAddedListener(final InfoLocalidad infoLocalidad) {
		final PanDireccionEvent e = new PanDireccionEvent(this, infoLocalidad);
		final PanDireccionEventListener listeners[] = listenerList.getListeners(PanDireccionEventListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < listeners.length; i++) {
					try {
						listeners[i].newInfoLocalidadAdded(e);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	protected final void fireNewInfoLocalidadSelectedListener(final InfoLocalidad infoLocalidad) {
		final PanDireccionEvent e = new PanDireccionEvent(this, infoLocalidad);
		final PanDireccionEventListener listeners[] = listenerList.getListeners(PanDireccionEventListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < listeners.length; i++) {
					try {
						listeners[i].newInfoLocalidadSelected(e);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	
	
	public void removeListChangeListener(ListChangeListener l) {
		listenerList.remove(ListChangeListener.class, l);
	}

	protected final void fireElementsAddedListener(final List<? extends Object> elements) {
		final ListChangeEvent e = new ListChangeEvent(this, elements);
		final ListChangeListener listeners[] = listenerList.getListeners(ListChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < listeners.length; i++) {
					try {
						listeners[i].elementsAdded(e);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	protected final void fireElementsRemovedListener(final List<? extends Object> elements) {
		final ListChangeEvent e = new ListChangeEvent(this, elements);
		final ListChangeListener listeners[] = listenerList.getListeners(ListChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < listeners.length; i++) {
					try {
						listeners[i].elementsRemoved(e);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

}
