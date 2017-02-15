package main.acciones.pagos.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.acciones.EntregaReingresoDocumentosBusinessDelegate;
import ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceResponse;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.GenericUtils.BackgroundTask;

public class JDialogEntregaReingresoDocumentos extends JDialog {

	private static final long serialVersionUID = 4738458653013701810L;

	private final JTextField txtIngreso = new JTextField();
	private Modo modo = Modo.SALIDA;
	private EntregaReingresoDocumentosBusinessDelegate delegate = new EntregaReingresoDocumentosBusinessDelegate();

	public JDialogEntregaReingresoDocumentos(final Frame frame) {
		super(frame);
		setupScreen();
		setupComponentes();
	}

	private void setupScreen() {
		setTitle("Entrega/Reingreso de documentos");
		setSize(new Dimension(450, 270));
//		setIconImage(ImageUtil.iconToImage(ImageUtil.loadIcon("ar/com/lite/textillevel/imagenes/logo.jpg")));
		GuiUtil.centrar(this);
	}

	private void setupComponentes() {
		final JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		final JLabel lblEstado = new JLabel("ESPERANDO......");
		lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD).deriveFont(20f));
		panelNorte.add(lblEstado);

		final JPanel panelSur = new JPanel(new GridBagLayout());
		final ButtonGroup group = new ButtonGroup();
		final JToggleButton tgbSalida = new JToggleButton("SALIDA", true);
		final JToggleButton tgbReingreso = new JToggleButton("REINGRESO");
		tgbSalida.addActionListener(new ButtonListener(Modo.SALIDA));
		tgbSalida.setPreferredSize(new Dimension(170, 70));
		tgbReingreso.addActionListener(new ButtonListener(Modo.REINGRESO));
		tgbReingreso.setPreferredSize(new Dimension(170, 70));
		group.add(tgbSalida);
		panelSur.add(tgbSalida, GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		group.add(tgbReingreso);
		panelSur.add(tgbReingreso, GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));

		final JPanel panelCentro = new JPanel(new GridBagLayout());
		txtIngreso.setFont(txtIngreso.getFont().deriveFont(Font.BOLD).deriveFont(45f));
		panelCentro.add(txtIngreso, GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 10, 5, 10), 1, 1, 1, 1));
		txtIngreso.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (txtIngreso.getText().trim().length() == 0) {
						FWJOptionPane.showErrorMessage(JDialogEntregaReingresoDocumentos.this, "No se ha leido el codigo", "Error");
						return;
					}
					lblEstado.setText("FIN LECTURA");
					finLectura();
					return;
				}
				lblEstado.setText("LEYENDO......");
			}

			private void finLectura() {
				final String msg = "DANDO " + modo.toString().toUpperCase();
				GenericUtils.realizarOperacionConDialogoDeEspera(msg, new BackgroundTask() {

					@Override
					public void perform() {
						try {
							TerminalServiceResponse resp;
							if (modo == Modo.SALIDA) {
								resp = delegate.marcarEntregado(txtIngreso.getText());
							} else {
								resp = delegate.reingresar(txtIngreso.getText());
							}
							if (resp.isError()) {
								FWJOptionPane.showErrorMessage(JDialogEntregaReingresoDocumentos.this, resp.getCodigoError() + " - " + resp.getMensajeError(), "Error");
							} else {
								FWJOptionPane.showInformationMessage(JDialogEntregaReingresoDocumentos.this, "Operacion exitosa", "Error");
							}
						} catch (final Exception re) {
							FWJOptionPane.showErrorMessage(JDialogEntregaReingresoDocumentos.this, "Se ha producido un error al comunicarse con el servidor", "Error");
							re.printStackTrace();
						} finally {
							reset();
						}
					}
				});
			}

			private void reset() {
				lblEstado.setText("ESPERANDO......");
				txtIngreso.setText("");
				txtIngreso.requestFocus();
				txtIngreso.requestFocusInWindow();
			}
		});
		add(panelNorte, BorderLayout.NORTH);
		add(panelCentro, BorderLayout.CENTER);
		add(panelSur, BorderLayout.SOUTH);
		setFocusTraversalPolicy(crearPoliticaFocus());
	}

	private FocusTraversalPolicy crearPoliticaFocus() {
		return new FocusTraversalPolicy() {

			@Override
			public Component getLastComponent(final Container aContainer) {
				return txtIngreso;
			}

			@Override
			public Component getFirstComponent(final Container aContainer) {
				return txtIngreso;
			}

			@Override
			public Component getDefaultComponent(final Container aContainer) {
				return txtIngreso;
			}

			@Override
			public Component getComponentBefore(final Container aContainer, final Component aComponent) {
				return txtIngreso;
			}

			@Override
			public Component getComponentAfter(final Container aContainer, final Component aComponent) {
				return txtIngreso;
			}
		};
	}

	private enum Modo {
		SALIDA, REINGRESO;
	}

	private class ButtonListener implements ActionListener {

		private final Modo modo;

		public ButtonListener(final Modo modo) {
			this.modo = modo;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			JDialogEntregaReingresoDocumentos.this.modo = modo;
			txtIngreso.requestFocus();
			txtIngreso.requestFocusInWindow();
		}
	}
}
