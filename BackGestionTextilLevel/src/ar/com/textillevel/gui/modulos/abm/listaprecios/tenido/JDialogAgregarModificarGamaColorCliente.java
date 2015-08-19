package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarGamaColorCliente extends JDialog {

	private static final long serialVersionUID = 3326067841037313062L;

	private CLJList listaGamas;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private CLCheckBoxList<Color> listaColores;
	private JLabel lblAdvertencias;
	
	private Cliente cliente;
	private List<GamaColorCliente> gamasActuales;
	private List<GamaColor> gamasDefault;
	private boolean acepto;

	private GamaColorClienteFacadeRemote gamaClienteFacade;
	private GamaColorFacadeRemote gamaColorFacade;
	private ColorFacadeRemote colorFacade;
	
	public JDialogAgregarModificarGamaColorCliente(Dialog padre, Cliente cliente ) {
		super(padre);
		setCliente(cliente);
		this.gamasActuales = getGamaClienteFacade().getByCliente(cliente.getId());
		this.gamasDefault = getGamaColorFacade().getAllOrderByName();
		if (this.gamasActuales == null || this.gamasActuales.isEmpty()) {
			this.gamasActuales = copiaInicialDeGamas();
		}
		setUpComponentes();
		setUpScreen();
		getListaGamas().setSelectedIndex(0);
		seleccionarColores();
	}

	private List<GamaColorCliente> copiaInicialDeGamas() {
		List<GamaColorCliente> gamas = new ArrayList<GamaColorCliente>();
		for(GamaColor gc : getGamasDefault()) {
			GamaColorCliente gcc = new GamaColorCliente();
			gcc.setCliente(getCliente());
			gcc.setNombre(gc.getNombre() + " - " + getCliente().getRazonSocial());
			gcc.setGamaOriginal(gc);
			gcc.setColores(gc.getColores());
			gamas.add(gcc);
		}
		return gamas;
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		JPanel panelSurBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSurBotones.add(getBtnAceptar());
		panelSurBotones.add(getBtnCancelar());
		
		JScrollPane jsp = new JScrollPane(getListaGamas(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(150, 200));
		add(jsp, BorderLayout.WEST);
		
		JPanel panelSur = new JPanel(new GridBagLayout());
		panelSur.add(getLblAdvertencias(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		panelSur.add(panelSurBotones, GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		
		add(new JScrollPane(getListaColores(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		add(panelSur, BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar gamas de " + getCliente().getRazonSocial());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(500, 350));
		setModal(true);
		setResizable(false);
		GuiUtil.centrar(this);		
	}

	public JDialogAgregarModificarGamaColorCliente(JDialog padre, Cliente cliente, GamaColorCliente gamaAModificar) {
		super(padre);
		setCliente(cliente);
		this.gamasActuales = getGamaClienteFacade().getByCliente(cliente.getId());
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public GamaColorClienteFacadeRemote getGamaClienteFacade() {
		if (gamaClienteFacade == null) {
			gamaClienteFacade = GTLBeanFactory.getInstance().getBean2(GamaColorClienteFacadeRemote.class);
		}
		return gamaClienteFacade;
	}

	public GamaColorFacadeRemote getGamaColorFacade() {
		if (gamaColorFacade == null) {
			gamaColorFacade = GTLBeanFactory.getInstance().getBean2(GamaColorFacadeRemote.class);
		}
		return gamaColorFacade;
	}

	public List<GamaColorCliente> getGamasActuales() {
		return gamasActuales;
	}

	public CLJList getListaGamas() {
		if (listaGamas == null) {
			listaGamas = new CLJList();
			for(GamaColorCliente gcc : getGamasActuales()) {
				listaGamas.addItem(gcc);
			}
			listaGamas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listaGamas.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if(!e.getValueIsAdjusting()) {
						seleccionarColores();
					}
				}

			});
		}
		return listaGamas;
	}

	private void seleccionarColores() {
		getListaColores().setAllSelectedItems(false);
		GamaColorCliente gcc = (GamaColorCliente)listaGamas.getSelectedValue();
		if (gcc != null) {
			List<Color> coloresGama = gcc.getColores();
			int i = -1;
			for(Object o : getListaColores().getItemList()){
				Color c = (Color) o;
				i++;
				for(Color color : coloresGama){
					if(color.getId().equals(c.getId())){
						getListaColores().setSelectedIndex(i);
					}
				}
			}
		}
	}

	private boolean validar() {
		for (GamaColorCliente gcc : getGamasActuales()) {
			for (Color c : gcc.getColores()) {
				for (GamaColorCliente gcc2 : getGamasActuales()) {
					if (!gcc.equals(gcc2)) {
						for (Color c2 : gcc2.getColores()) {
							if (c2.equals(c)){
								CLJOptionPane.showErrorMessage(JDialogAgregarModificarGamaColorCliente.this, StringW.wordWrap("El color '" + c.getNombre() + "' existe en las gamas: \n- " + gcc.getNombre() + "\n-" + gcc2.getNombre() + "\n\nPor favor, eliminelo de una."), "Error");
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						getGamaClienteFacade().save(getGamasActuales());
						CLJOptionPane.showInformationMessage(JDialogAgregarModificarGamaColorCliente.this, "Las gamas del cliente se han guardado exitosamente", "Información");
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public CLCheckBoxList<Color> getListaColores() {
		if (listaColores == null) {
			listaColores = new CLCheckBoxList<Color>() {

				private static final long serialVersionUID = 393647970475448036L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					Color color = (Color) item;
					GamaColorCliente gamaSeleccionada = (GamaColorCliente)listaGamas.getSelectedValue();
					if (seleccionado) {
						for (GamaColorCliente gcc : getGamasActuales()) {
							if (!gcc.equals(gamaSeleccionada)) {
								int indiceARemover = -1;
								boolean found = false;
								for (Color c : gcc.getColores()) {
									indiceARemover++;
									if (c.equals(color)) {
										found = true;
										break;
									}
								}
								if (found) {
									gcc.getColores().remove(indiceARemover);
									getLblAdvertencias().setText("Se quita el color " + color.getNombre() + " de la gama: " + gcc.getNombre());
									break;
								}
							}
						}
						gamaSeleccionada.getColores().add(color);
					} else {
						gamaSeleccionada.getColores().remove(color);
					}
				}
			};
			List<Color> colorList = getColorFacade().getAllOrderByNameGamaEager();
			Object[] colores = new Object[colorList.size()];
			int i=0;
			for(Color c : colorList) {
				colores[i++] = c;
			}
			getListaColores().setValues(colores);
		}
		return listaColores;
	}
	
	public ColorFacadeRemote getColorFacade() {
		if(colorFacade == null){
			colorFacade = GTLBeanFactory.getInstance().getBean2(ColorFacadeRemote.class);
		}
		return colorFacade;
	}
	
	private void salir() {
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, desea continuar?", "Agregar/modificar gamas");
		if (ret == CLJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public List<GamaColor> getGamasDefault() {
		return gamasDefault;
	}

	public JLabel getLblAdvertencias() {
		if (lblAdvertencias == null) {
			lblAdvertencias = new JLabel();
			Font f = lblAdvertencias.getFont();
			Font nueva = new Font(f.getName(), Font.BOLD, f.getSize());
			lblAdvertencias.setFont(nueva);
			lblAdvertencias.setForeground(java.awt.Color.RED);
		}
		return lblAdvertencias;
	}
}
