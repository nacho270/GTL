package ar.com.textillevel.gui.acciones.remitosalida;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionremito.ImprimirRemitoHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarRemitoSalidaDibujo extends JDialog {

	private static final long serialVersionUID = -3548401744452579538L;

	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;

	private JPanel panDetalle;
	private FWJTextField txtRazonSocial;
	private FWJTextField txtDibujo;
	private JPanel pnlBotones;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JButton btnImprimir;
	private JPanel panelDatosCliente;
	private JTextField txtNroRemito;
	private FWDateField txtFechaEmision;

	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;
	private RemitoSalida remitoSalida;
	private boolean modoConsulta;
	private boolean acepto;

	public JDialogAgregarRemitoSalidaDibujo(Frame owner, RemitoSalida remitoSalida, boolean modoConsulta) {
		super(owner);
		this.remitoSalida = remitoSalida;
		this.modoConsulta = modoConsulta;
		setSize(new Dimension(400, 250));
		setResizable(false);
		GuiUtil.centrar(this);
		if (modoConsulta) {
			setTitle("Consulta de Remito de Salida Dibujo");
		} else {
			setTitle("Alta de Remito de Salida Dibujo");
		}
		construct();
		setDatos();
		setModal(true);
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getPanelDatosCliente(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
			panDetalle.add(new JLabel(" FECHA:"), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtFechaEmision(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panDetalle.add(new JLabel(" DIBUJO:"), GenericUtils.createGridBagConstraints(2, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtDibujo(), GenericUtils.createGridBagConstraints(3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

		}
		getTxtFechaEmision().setEnabled(!modoConsulta);

		return panDetalle;
	}

	private JPanel getPanelDatosCliente() {
		if (panelDatosCliente == null) {
			panelDatosCliente = new JPanel();
			panelDatosCliente.setLayout(new GridBagLayout());
			panelDatosCliente.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosCliente.add(new JLabel("Señor/es: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Remito Nº: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		}
		return panelDatosCliente;
	}

	private JPanel getPanelBotones() {
		if (pnlBotones == null) {
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
			if (modoConsulta) {
				pnlBotones.add(getBtnImprimir());
			}
			getBtnCancelar().setEnabled(!modoConsulta);
		}
		return pnlBotones;
	}

	private void setDatos() {
		Cliente cliente = remitoSalida.getCliente();
		if (cliente != null) {
			getTxtRazonSocial().setText(cliente.getRazonSocial());
		}
		Proveedor proveedor = remitoSalida.getProveedor();
		if (proveedor != null) {
			getTxtRazonSocial().setText(proveedor.getRazonSocial());
		}
		if (modoConsulta || remitoSalida.getId() != null) {
			getTxtFechaEmision().setFecha(remitoSalida.getFechaEmision());
		} else {
			getTxtFechaEmision().setFecha(DateUtil.getHoy());
		}
		getTxtNroRemito().setText(remitoSalida.getNroRemito().toString());
		getTxtDibujo().setText(remitoSalida.getDibujoEstampado().toString());
	}

	private FWDateField getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new FWDateField();
			if (modoConsulta || remitoSalida.getId() != null) {
				txtFechaEmision.setFecha(remitoSalida.getFechaEmision());
			}
		}
		return txtFechaEmision;
	}

	private FWJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField(MAX_LONGITUD_RAZ_SOCIAL);
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private JTextField getTxtNroRemito() {
		if (txtNroRemito == null) {
			txtNroRemito = new FWJTextField();
			txtNroRemito.setEditable(false);
			if (modoConsulta || remitoSalida.getId() != null) {
				txtNroRemito.setText(remitoSalida.getNroRemito().toString());
			}
		}
		return txtNroRemito;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ImprimirRemitoHandler imprimirRemitoHandler = new ImprimirRemitoHandler(getRemitoSalida(), getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal(), JDialogAgregarRemitoSalidaDibujo.this);
					imprimirRemitoHandler.imprimir();
				}

			});

		}
		return btnImprimir;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (modoConsulta) {
						dispose();
						return;
					}
					remitoSalida.setFechaEmision(getTxtFechaEmision().getFecha());
					setAcepto(true);
					dispose();
				}

			});
		}
		return btnAceptar;
	}

	private ParametrosGeneralesFacadeRemote getParametrosGeneralesFacade() {
		if (parametrosGeneralesFacade == null) {
			parametrosGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		}
		return parametrosGeneralesFacade;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public RemitoSalida getRemitoSalida() {
		return remitoSalida;
	}

	public boolean isModoConsulta() {
		return modoConsulta;
	}

	public void setModoConsulta(boolean modoConsulta) {
		this.modoConsulta = modoConsulta;
	}

	public FWJTextField getTxtDibujo() {
		if(txtDibujo == null) {
			txtDibujo = new FWJTextField();
			txtDibujo.setEditable(false);
		}
		return txtDibujo;
	}
}
