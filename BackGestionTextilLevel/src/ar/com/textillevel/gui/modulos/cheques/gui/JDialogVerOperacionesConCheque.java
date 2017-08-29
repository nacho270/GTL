package ar.com.textillevel.gui.modulos.cheques.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.to.OperacionSobreChequeTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDeDepositoFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoPersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDePago;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDePagoAPersona;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDeposito;
import ar.com.textillevel.gui.acciones.JDialogCargaRecibo;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogVerOperacionesConCheque extends JDialog {

	private static final long serialVersionUID = -7779285867046161661L;

	private JButton btnCerrar;

	private JPanel panelCentral;
	private JPanel panelBotones;

	private ChequeFacadeRemote chequeFacade;
	private List<OperacionSobreChequeTO> operaciones;
	
	private Frame padre;

	public JDialogVerOperacionesConCheque(Frame padre, Cheque cheque) {
		super(padre);
		this.padre = padre;
		this.operaciones = getChequeFacade().getOperacionSobreChequeTOList(cheque);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Operaciones sobre cheque");
		setResizable(false);
		setSize(new Dimension(300, 250));
		setModal(true);
	}

	private void setUpComponentes() {
		setLayout(new GridBagLayout());
		JScrollPane spList = new JScrollPane(getPanelCentral());
		add(spList, GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCerrar;
	}

	public JPanel getPanelCentral() {
		if(panelCentral == null){
			panelCentral = new JPanel();
			panelCentral.setLayout(new GridBagLayout());
			int y=0;
			for(OperacionSobreChequeTO op : operaciones) {
				panelCentral.add(new ItemOperacionSobreChequeView(op), GenericUtils.createGridBagConstraints(0, y, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
				y++;
			}
		}
		return panelCentral;
	}

	public JPanel getPanelBotones() {
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnCerrar());
		}
		return panelBotones;
	}

	public ChequeFacadeRemote getChequeFacade() {
		if (chequeFacade == null) {
			chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		}
		return chequeFacade;
	}

	private class ItemOperacionSobreChequeView extends JPanel {

		private static final long serialVersionUID = 4287202330811675319L;

		private OperacionSobreChequeTO op;
		private JTextField txtFecha;
		private JLabel lblEstado;
		private OperacionSobreChequeTOLinkableLabel linkeableDocLabel;

		public ItemOperacionSobreChequeView(OperacionSobreChequeTO op) {
			this.op = op;
			construct();
		}

		private void construct() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createEtchedBorder());
			add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			add(getTxtFecha(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			add(getLblEstado(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
			add(getLinkeableLabel(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
		}

		private JTextField getTxtFecha() {
			if(txtFecha == null) {
				txtFecha = new JTextField();
				txtFecha.setEditable(false);
				txtFecha.setText(DateUtil.dateToString(op.getFechaOp(), DateUtil.SHORT_DATE_WITH_HOUR));
			}
			return txtFecha;
		}

		private JLabel getLblEstado() {
			if(lblEstado == null) {
				lblEstado = new JLabel("ESTADO: " + op.getPasoAEstado());
			}
			return lblEstado;
		}
		
		private OperacionSobreChequeTOLinkableLabel getLinkeableLabel() {
			if(linkeableDocLabel == null) {
				linkeableDocLabel = new OperacionSobreChequeTOLinkableLabel();
				linkeableDocLabel.setOperacionSobreChequeTO(op);
			}
			return linkeableDocLabel;
		}
	}

	
	public class OperacionSobreChequeTOLinkableLabel extends LinkableLabel {

		private static final long serialVersionUID = -8690402922462933808L;

		private OperacionSobreChequeTO op;

		public OperacionSobreChequeTOLinkableLabel() {
			super("x");
		}

		@Override
		public void labelClickeada(MouseEvent e) {
			if (e.getClickCount() == 1 && op!=null) {
				ETipoDocumento tipoDoc = op.getTipoDocumento();
				Integer idDoc = op.getIdDocumento();
				if(tipoDoc == ETipoDocumento.NOTA_DEBITO) {
					showND(idDoc);
				}
				if(tipoDoc == ETipoDocumento.ORDEN_DE_DEPOSITO) {
					showODD(idDoc);
				}
				if(tipoDoc == ETipoDocumento.RECIBO) {
					showRC(idDoc);
				}
				if(tipoDoc == ETipoDocumento.ORDEN_PAGO) {
					showOP(idDoc);
				}
				if(tipoDoc == ETipoDocumento.ORDEN_PAGO_PERSONA) {
					showOPP(idDoc);
				}
			}
		}

		private void showOPP(Integer nroOrden) {
			OrdenDePagoAPersona ordenDePagoAPersona = GTLBeanFactory.getInstance().getBean2(OrdenDePagoPersonaFacadeRemote.class).getOrdenByNro(nroOrden);
			new JDialogCargaOrdenDePagoAPersona(JDialogVerOperacionesConCheque.this.padre, ordenDePagoAPersona, true).setVisible(true);
		}

		private void showOP(Integer nroOrden) {
			OrdenDePagoFacadeRemote opfr = GTLBeanFactory.getInstance().getBean2(OrdenDePagoFacadeRemote.class);
			OrdenDePago orden = opfr.getOrdenDePagoByNroOrdenEager(nroOrden);
			JDialogCargaOrdenDePago dialog = new JDialogCargaOrdenDePago(JDialogVerOperacionesConCheque.this.padre, orden, true);
			dialog.setVisible(true);
		}

		private void showRC(Integer nroRecibo) {
			ReciboFacadeRemote rfr = GTLBeanFactory.getInstance().getBean2(ReciboFacadeRemote.class);
			Recibo recibo = rfr.getByNroReciboEager(nroRecibo);
			JDialogCargaRecibo dialogCargaRecibo = new JDialogCargaRecibo(JDialogVerOperacionesConCheque.this.padre, recibo, true);
			GuiUtil.centrar(dialogCargaRecibo);
			dialogCargaRecibo.setVisible(true);
		}

		private void showODD(Integer nroOrden) {
			OrdenDeDepositoFacadeRemote oddfr = GTLBeanFactory.getInstance().getBean2(OrdenDeDepositoFacadeRemote.class);
			OrdenDeDeposito orden = oddfr.getOrdenByNro(nroOrden);
			new JDialogCargaOrdenDeposito(JDialogVerOperacionesConCheque.this.padre, orden).setVisible(true);			
		}

		private void showND(Integer idND) {
			try {
				CorreccionFacadeRemote cfr = GTLBeanFactory.getInstance().getBean(CorreccionFacadeRemote.class);
				CorreccionFactura correccion = cfr.getCorreccionById(idND);
				JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(JDialogVerOperacionesConCheque.this.padre, correccion, true);
				dialogCargaFactura.setVisible(true);
			} catch (FWException e) {
				BossError.gestionarError(e);
			}
		}

		public void setOperacionSobreChequeTO(OperacionSobreChequeTO op) {
			this.op = op;
			if(op != null) {
				setTexto(op.getDescripcionDoc());
				refreshLabel();
			}
		}

	}

}