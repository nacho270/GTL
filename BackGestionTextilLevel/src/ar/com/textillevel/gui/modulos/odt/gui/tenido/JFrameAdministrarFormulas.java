package ar.com.textillevel.gui.modulos.odt.gui.tenido;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.gui.util.controles.PanelBusquedaClienteMinimal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.facade.api.remote.FormulaTenidoClienteFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JFrameAdministrarFormulas extends JFrame {

	private static final long serialVersionUID = -1338412573256810225L;

//	private JButton btnSalir;
	private JButton btnGrabar;
	private JButton btnModificarCancelar;

	private JPanel panBotones;
	private PanelBusquedaClienteMinimal panelBusquedaCliente;
	private TabPaneFormulas tabPaneFormulas;
	private FormulaTenidoClienteFacadeRemote formulaFacade;
	private Cliente cliente;

	private boolean isModificar = true;
	private List<Integer> idsFormulasDeleteDetect;

	private static final String LABEL_MODIFICAR = "Modificar";
	private static final String LABEL_CANCELAR = "Cancelar";

	public JFrameAdministrarFormulas(Frame padre) {
		setUpComponentes();
		setUpScreen();
		this.idsFormulasDeleteDetect = new ArrayList<Integer>();
		cargarFormulas();
	}

	private void setUpScreen() {
		setSize(new Dimension(700, 600));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Administrar Fórmulas");
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we) {
				salir();
			}
		});
		this.add(getPanelBusquedaCliente(), BorderLayout.NORTH);
		this.add(getTabPaneFormulas(), BorderLayout.CENTER);
		this.add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones() {
		if(panBotones == null) {
			panBotones = new JPanel();
			panBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			panBotones.add(getBtnGrabar());
			panBotones.add(getBtnModificarCancelar());
		}
		return panBotones;
	}

	private TabPaneFormulas getTabPaneFormulas() {
		if(tabPaneFormulas == null) {
			tabPaneFormulas = new TabPaneFormulas(JFrameAdministrarFormulas.this, true);
		}
		return tabPaneFormulas;
	}

	private void salir() {
		int ret = FWJOptionPane.showQuestionMessage(this, "Va a cerrar el módulo, esta seguro?", "Administrar Fórmulas");
		if (ret == FWJOptionPane.YES_OPTION) {
			dispose();
		}
	}

//	private JButton getBtnSalir() {
//		if (btnSalir == null) {
//			btnSalir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_exit.png", "ar/com/textillevel/imagenes/b_exit.png"); 
//			btnSalir.setMnemonic(KeyEvent.VK_S);
//			btnSalir.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					salir();
//				}
//			});
//		}
//		return btnSalir;
//	}

	public JButton getBtnGrabar() {
		if(btnGrabar == null) {
			btnGrabar = new JButton("Grabar");
			btnGrabar.setEnabled(false);
			btnGrabar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						capturarSetearDatos();
						getFormulaFacade().saveFormulas(getTabPaneFormulas().getPersisterFormulaHandler().getFormulasToSave(), getTabPaneFormulas().getPersisterFormulaHandler().getIdsFormulasToBorrar(), false, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						isModificar = false;
						handlerHabilitacionModificarCancelar();
						isModificar = !isModificar;
						getTabPaneFormulas().getPersisterFormulaHandler().clear();
					} catch (ValidacionException e1) {
						FWJOptionPane.showErrorMessage(JFrameAdministrarFormulas.this, StringW.wordWrap(e1.getMensajeError()), "Administrar Fórmulas");
					}
				}

			});
		}
		return btnGrabar;
	}

	private void capturarSetearDatos() {
		List<FormulaCliente> formulas = getTabPaneFormulas().getFormulas();
		for(FormulaCliente f : formulas) {
			f.setCliente(cliente);
		}
	}

	public JButton getBtnModificarCancelar() {
		if(btnModificarCancelar == null) {
			btnModificarCancelar = new JButton(LABEL_MODIFICAR);
			btnModificarCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					handlerHabilitacionModificarCancelar();
					if(!isModificar) {
						cargarFormulas();
						getTabPaneFormulas().getPersisterFormulaHandler().clear();
					}
					isModificar = !isModificar;
				}

			});
		}
		return btnModificarCancelar;
	}

	private void cargarFormulas() {
		List<FormulaCliente> formulas = getFormulaFacade().getAllDefaultOrByCliente(cliente);
		getTabPaneFormulas().llenarTabs(formulas);
		idsFormulasDeleteDetect.clear();
		for(FormulaCliente f : formulas) {
			idsFormulasDeleteDetect.add(f.getId());
		}
	}

	private void handlerHabilitacionModificarCancelar() {
		getTabPaneFormulas().setModoConsulta(!isModificar);
		getBtnModificarCancelar().setText(isModificar ? LABEL_CANCELAR : LABEL_MODIFICAR);
		getBtnGrabar().setEnabled(isModificar);
		GuiUtil.setEstadoPanel(getPanelBusquedaCliente(), !isModificar);
	}

	private PanelBusquedaClienteMinimal getPanelBusquedaCliente() {
		if(panelBusquedaCliente == null) {
			panelBusquedaCliente = new PanelBusquedaClienteMinimal() {

				private static final long serialVersionUID = 1L;

				@Override
				protected void clienteEncontrado(Cliente cliente) {
					JFrameAdministrarFormulas.this.cliente = cliente;
					cargarFormulas();
					isModificar = true;
				}

				@Override
				protected void botonLimpiarPresionado() {
					getTabPaneFormulas().llenarTabs(new ArrayList<FormulaCliente>());
					getBtnGrabar().setEnabled(false);
					JFrameAdministrarFormulas.this.cliente = null;
					cargarFormulas();
				}

			};
		}
		return panelBusquedaCliente;
	}

	public FormulaTenidoClienteFacadeRemote getFormulaFacade() {
		if(formulaFacade == null) {
			formulaFacade = GTLBeanFactory.getInstance().getBean2(FormulaTenidoClienteFacadeRemote.class);
		}
		return formulaFacade;
	}

	public static void main(String[] args) {
		System.getProperties().setProperty("applicationName", "GTL");
		System.getProperties().setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		System.getProperties().setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		System.getProperties().setProperty("cltimezone","GMT-3");
		if(System.getProperty("java.naming.provider.url")==null){
			System.getProperties().setProperty("java.naming.provider.url", "localhost:1099");
		}
		
		UsuarioSistema usuarioSistema = new UsuarioSistema();
		usuarioSistema.setUsrName("admin");
		GTLGlobalCache.getInstance().setUsuarioSistema(usuarioSistema);
		
		JFrameAdministrarFormulas frame = new JFrameAdministrarFormulas(null);
		frame.setVisible(true);
	}

}