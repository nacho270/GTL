package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoInformeProduccion;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogInformeProduccion extends JDialog {

	private static final long serialVersionUID = 7911373413802981336L;

	private JComboBox cmbTipoInformeProduccion;
	private JComboBox cmbCliente;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;

	private JButton btnAceptar;
	private JButton btnSalir;

	private ClienteFacadeRemote clienteFacade;

	private boolean acepto;
	private Cliente clienteElegido;
	private Date fechaDesdeElegida;
	private Date fechaHastaElegida;
	private ETipoInformeProduccion tipoInformeElegido;

	private JPanel panelCentro;
	private JPanel panelSur;

	public JDialogInformeProduccion(Frame padre) {
		super(padre);
		setAcepto(false);
		setComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Informe de producción");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setComponentes() {
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	public JComboBox getCmbTipoInformeProduccion() {
		if (cmbTipoInformeProduccion == null) {
			cmbTipoInformeProduccion = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoInformeProduccion, Arrays.asList(ETipoInformeProduccion.values()), true);
		}
		return cmbTipoInformeProduccion;
	}

	public JComboBox getCmbCliente() {
		if (cmbCliente == null) {
			cmbCliente = new JComboBox();
			cmbCliente.addItem("TODOS");
			for (Cliente c : getClienteFacade().getAllOrderByName()) {
				cmbCliente.addItem(c);
			}
		}
		return cmbCliente;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Fecha desde: ");
		}
		return panelFechaDesde;
	}

	public PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Fecha hasta: ");
		}
		return panelFechaHasta;
	}

	public ClienteFacadeRemote getClienteFacade() {
		if (clienteFacade == null) {
			clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacade;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getCmbCliente().getSelectedItem().equals("TODOS")) {
						setClienteElegido(null);
					} else {
						setClienteElegido((Cliente) getCmbCliente().getSelectedItem());
					}
					setTipoInformeElegido((ETipoInformeProduccion) getCmbTipoInformeProduccion().getSelectedItem());
					setFechaDesdeElegida(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
					setFechaHastaElegida(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public Cliente getClienteElegido() {
		return clienteElegido;
	}

	public void setClienteElegido(Cliente clienteElegido) {
		this.clienteElegido = clienteElegido;
	}

	public Date getFechaDesdeElegida() {
		return fechaDesdeElegida;
	}

	public void setFechaDesdeElegida(Date fechaDesdeElegida) {
		this.fechaDesdeElegida = fechaDesdeElegida;
	}

	public Date getFechaHastaElegida() {
		return fechaHastaElegida;
	}

	public void setFechaHastaElegida(Date fechaHastaElegida) {
		this.fechaHastaElegida = fechaHastaElegida;
	}

	public ETipoInformeProduccion getTipoInformeElegido() {
		return tipoInformeElegido;
	}

	public void setTipoInformeElegido(ETipoInformeProduccion tipoInformeElegido) {
		this.tipoInformeElegido = tipoInformeElegido;
	}

	public JPanel getPanelCentro() {
		if(panelCentro == null){
			panelCentro = new JPanel();
			panelCentro.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
			JPanel panHor1 = new JPanel();
			panHor1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			JPanel panHor2 = new JPanel();
			panHor2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			panHor1.add(new JLabel("Cliente: "));
			panHor1.add(getCmbCliente());
			panHor2.add(getPanelFechaDesde());
			panHor2.add(getPanelFechaHasta());
			panHor1.add(new JLabel("Tipo de informe: "));
			panHor1.add(getCmbTipoInformeProduccion());
			panelCentro.add(panHor1);
			panelCentro.add(panHor2);
		}
		return panelCentro;
	}

	public JPanel getPanelSur() {
		if(panelSur == null){
			panelSur = new JPanel();
			panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}
}
