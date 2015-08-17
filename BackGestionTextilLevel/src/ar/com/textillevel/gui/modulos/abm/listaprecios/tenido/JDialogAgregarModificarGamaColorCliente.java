package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarGamaColorCliente extends JDialog {

	private static final long serialVersionUID = 3326067841037313062L;

	private CLJList listaGamas;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private CLCheckBoxList<Color> listaColores;
	
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
		setUpComponentes();
		setUpScreen();
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		
		JScrollPane jsp = new JScrollPane(getListaGamas(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(150, 200));
		add(jsp, BorderLayout.WEST);
		
		add(new JScrollPane(getListaColores(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		add(panelSur, BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar gamas de " + getCliente().getRazonSocial());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(500, 300));
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
			for(GamaColor gc : gamasDefault) {
				listaGamas.addItem(gc);
			}
			listaGamas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listaGamas.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if(!e.getValueIsAdjusting()) {
						getListaColores().removeAll();
						GamaColorCliente gcc = getGamaClienteFacade().getByGama(((GamaColor)listaGamas.getSelectedValue()).getId());
						if (gcc != null) {
							
						}
					}
				}
			});
		}
		return listaGamas;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
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
					super.itemListaSeleccionado(item, seleccionado);
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
			dispose();
		}
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
}
