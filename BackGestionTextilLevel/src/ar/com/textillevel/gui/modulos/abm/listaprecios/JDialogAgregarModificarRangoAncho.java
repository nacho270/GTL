package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.gui.util.GenericUtils;

public abstract class JDialogAgregarModificarRangoAncho extends JDialog {

	private static final long serialVersionUID = 8960260699615490518L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	private CLJNumericTextField txtAnchoInicial;
	private CLJNumericTextField txtAnchoFinal;
	private PanelTablaRango<?> panelRango;
	
	private boolean acepto;
	private List<RangoAncho> rangosActuales;
	private RangoAncho rango;
	
	public JDialogAgregarModificarRangoAncho(JDialog padre, List<RangoAncho> rangosActuales) {
		super(padre);
		this.rangosActuales = rangosActuales;
		this.rango = crearNuevoRango();
		setUpScreen();
		setUpComponentes();
	}

	public JDialogAgregarModificarRangoAncho(JDialog padre, List<RangoAncho> rangosActuales, RangoAncho rangoActual) {
		super(padre);
		this.rangosActuales = rangosActuales;
		this.rango = rangoActual;
	}
	
	private void setUpScreen() {
		setTitle("Agregar/modificar rango");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(300, 300));
		setModal(true);
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		JPanel panelNorte = new JPanel(new GridBagLayout());
		panelNorte.add(new JLabel("Ancho inicial: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getTxtAnchoInicial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panelNorte.add(new JLabel("Ancho Final: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getTxtAnchoFinal(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		
		add(panelNorte, BorderLayout.NORTH);
		add(getPanelRango(), BorderLayout.CENTER);
		add(panelSur, BorderLayout.SOUTH);
	}
	
	private void salir() {
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, desea continuar?", "Alta de cheque");
		if (ret == CLJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}
	
	public boolean isAcepto() {
		return acepto;
	}

	public RangoAncho getRango() {
		return rango;
	}

	public List<RangoAncho> getRangosActuales() {
		return rangosActuales;
	}

	public void setRangosActuales(List<RangoAncho> rangosActuales) {
		this.rangosActuales = rangosActuales;
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

	public CLJNumericTextField getTxtAnchoInicial() {
		if (txtAnchoInicial == null) {
			txtAnchoInicial = new CLJNumericTextField(0, 10);
			txtAnchoInicial.setSize(100, 20);
		}
		return txtAnchoInicial;
	}

	public CLJNumericTextField getTxtAnchoFinal() {
		if (txtAnchoFinal == null) {
			txtAnchoFinal = new CLJNumericTextField(0, 10);
		}
		return txtAnchoFinal;
	}
	
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public PanelTablaRango<?> getPanelRango() {
		if (panelRango == null) {
			panelRango = crearPanelRango();
		}
		return panelRango;
	}
	
	/** ABSTRACTOS */
	protected abstract RangoAncho crearNuevoRango();
	protected abstract PanelTablaRango<?> crearPanelRango();
}
