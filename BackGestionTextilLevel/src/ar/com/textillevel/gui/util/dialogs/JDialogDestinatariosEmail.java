package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.batik.ext.swing.GridBagConstants;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogDestinatariosEmail extends JDialog {

	private static final long serialVersionUID = 147824910107465311L;

	private PanelListaEmails txtTO;
	private PanelListaEmails txtCC;
	private JButton btnAgregarTO;
	private JButton btnAgregarCC;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private PerformEnvioEmailHandler handler;
	private String defaultTO;

	public JDialogDestinatariosEmail(JDialog owner, String defaultTo, PerformEnvioEmailHandler handler) {
		super(owner);
		this.handler = handler;
		this.defaultTO = defaultTo;
		setUpComponentes();
		setUpScreen();
	}
	
	public JDialogDestinatariosEmail(Frame owner, String defaultTo, PerformEnvioEmailHandler handler) {
		super(owner);
		this.handler = handler;
		this.defaultTO = defaultTo;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Destinatarios email");
		setModal(true);
		setSize(new Dimension(600, 250));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		JPanel pnlCentro = new JPanel(new GridBagLayout());

		pnlCentro.add(new JLabel("Para: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstants.WEST, GridBagConstants.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		pnlCentro.add(getTxtTO(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstants.WEST, GridBagConstants.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		pnlCentro.add(getBtnAgregarTO(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstants.WEST, GridBagConstants.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		pnlCentro.add(new JLabel("Copia: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstants.WEST, GridBagConstants.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		pnlCentro.add(getTxtCC(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstants.WEST, GridBagConstants.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		pnlCentro.add(getBtnAgregarCC(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstants.WEST, GridBagConstants.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pnlSur.add(getBtnAceptar());
		pnlSur.add(getBtnCancelar());

		add(pnlCentro, BorderLayout.CENTER);
		add(pnlSur, BorderLayout.SOUTH);
	}

	public JButton getBtnAgregarTO() {
		if (btnAgregarTO == null) {
			btnAgregarTO = new JButton("+");
			btnAgregarTO.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					internalAddEmail(getTxtTO());
				}
			});
		}
		return btnAgregarTO;
	}

	public JButton getBtnAgregarCC() {
		if (btnAgregarCC == null) {
			btnAgregarCC = new JButton("+");
			btnAgregarCC.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					internalAddEmail(getTxtCC());
				}
			});
		}
		return btnAgregarCC;
	}

	private void internalAddEmail(PanelListaEmails txt) {
		boolean ok = false;
		do {
			String input = JOptionPane.showInputDialog(JDialogDestinatariosEmail.this, "Ingrese destinatario: ", "Agregar email", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				break;
			}
			if (input.trim().length()==0 || !GenericUtils.isEmailValido(input)) {
				FWJOptionPane.showErrorMessage(JDialogDestinatariosEmail.this, "Ingreso incorrecto", "error");
			} else {
				ok = true;
				txt.agregarEmail(input);
			}
		} while (!ok);
	}
	
	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialogDestinatariosEmail.this.dispose();
					JDialogDestinatariosEmail.this.handler.performEnvio(getTxtTO().getEmails(), getTxtCC().getEmails());
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
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public PanelListaEmails getTxtTO() {
		if (txtTO == null) {
			txtTO = new PanelListaEmails();
			txtTO.agregarEmail(this.defaultTO);
		}
		return txtTO;
	}

	public PanelListaEmails getTxtCC() {
		if (txtCC == null) {
			txtCC = new PanelListaEmails();
		}
		return txtCC;
	}

	private class PanelListaEmails extends JPanel {

		private static final long serialVersionUID = 7288898542363914684L;
		private List<PanelEmail> panelEmails;

		public PanelListaEmails() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			setBackground(Color.WHITE);
			setOpaque(true);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setSize(new Dimension(200, 80));
			setPreferredSize(new Dimension(200, 80));
			panelEmails = new ArrayList<JDialogDestinatariosEmail.PanelListaEmails.PanelEmail>();
		}

		private void eliminarEmail(PanelEmail panel) {
			this.panelEmails.remove(panel);
			refresh();
		}
		
		public void agregarEmail(String email) {
			PanelEmail p = new PanelEmail(this, email);
			if(!this.panelEmails.contains(p)) {
				this.panelEmails.add(p);
			}
			refresh();
		}
		
		private void refresh() {
			this.removeAll();
			for(PanelEmail p : this.panelEmails) {
				this.add(p);
			}
			invalidate();
			updateUI();
		}

		public List<String> getEmails() {
			List<String> lista = new ArrayList<String>();
			for(PanelEmail p : panelEmails) {
				lista.add(p.email);
			}
			return lista;
		}
		
		private class PanelEmail extends JPanel {

			private static final long serialVersionUID = 5925461377686914203L;
			
			private String email;

			public PanelEmail(final PanelListaEmails parent, String email) {
				setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
				setBackground(Color.RED.darker());
				setOpaque(true);
				this.email = email;

				JLabel lblEmail = new JLabel(email);
				lblEmail.setForeground(Color.WHITE);
				add(lblEmail);

				JLabel lblEliminar = new JLabel("x");
				lblEliminar.setForeground(Color.WHITE);
				lblEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
				Font font = lblEliminar.getFont();
				lblEliminar.setFont(new Font(font.getName(), Font.BOLD, font.getSize() + 2));
				lblEliminar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						parent.eliminarEmail(PanelEmail.this);
					}
				});
				add(lblEliminar);
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((email == null) ? 0 : email.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				PanelEmail other = (PanelEmail) obj;
				if (email == null) {
					if (other.email != null)
						return false;
				} else if (!email.equals(other.email))
					return false;
				return true;
			}

		}
	}

	public static abstract class PerformEnvioEmailHandler {
		public abstract void performEnvio(final List<String> to, final List<String> cc);
	}

}