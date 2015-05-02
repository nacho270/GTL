package ar.com.textillevel.gui.modulos.personal.abm.configuracion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.configuracion.DatosAlarmaFinContrato;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.facade.api.remote.ParametrosGeneralesPersonalFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogParametrosGenerales extends JDialog {

	private static final long serialVersionUID = -5301394965626565404L;

	private ParametrosGeneralesPersonal parametrosGenerales;
	private ParametrosGeneralesPersonalFacadeRemote parametrosFacade;

	private CLJNumericTextField txtMaximoTiempoFueraDeLaEmpresa;
	private CLJNumericTextField txtToleranciaHorasExtra;

	private boolean acepto;

	private JButton btnGuardar;
	private JButton btnSalir;
	private JTabbedPane panelDetalles;
	private JPanel panelBotones;

	private JPanel panelTabVarios;

	public JDialogParametrosGenerales(Frame frame, ParametrosGeneralesPersonal pg) {
		super(frame);
		this.setParametrosGenerales(pg);
		construct();
	}

	private void construct() {
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
		this.add(getPanelDetalles(), BorderLayout.CENTER);
		this.add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private void salir() {
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin guardar, esta seguro?", "Parametros generales");
		if (ret == CLJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private void setUpScreen() {
		this.setTitle("Parametros generales");
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setSize(new Dimension(600, 300));
		this.setResizable(false);
		GuiUtil.centrar(this);
		this.setModal(true);
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					guardar();
				}
			});
		}
		return btnGuardar;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	private JTabbedPane getPanelDetalles() {
		if (panelDetalles == null) {
			panelDetalles = new JTabbedPane();
			panelDetalles.addTab("Varios", getPanelTabVarios());
		}
		return panelDetalles;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnGuardar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	private JPanel getPanelTabVarios() {
		if (panelTabVarios == null) {
			panelTabVarios = new JPanel();
			panelTabVarios.setLayout(new GridBagLayout());
			panelTabVarios.add(new JLabel("Tolerancia de horas extra (minutos): "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtToleranciaHorasExtra(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			
			panelTabVarios.add(new JLabel("Máximo tiempo fuera de la empresa (meses): "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtMaximoTiempoFueraDeLaEmpresa(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			
			List<String> tipos = new ArrayList<String>();
			for (ETipoContrato t : ETipoContrato.values()) {
				tipos.add(String.valueOf(t.getId()));
			}
			tipos.remove(String.valueOf(ETipoContrato.EFECTIVO.getId()));
			JPanel pnlAlarmas = new JPanel(new GridLayout(tipos.size(), 1));
			pnlAlarmas.setBorder(BorderFactory.createTitledBorder("Tiempos de aviso para fin de contrato (días)"));
			if (getParametrosGenerales().getAlarmasFinContrato() == null || getParametrosGenerales().getAlarmasFinContrato().isEmpty()) {
				for (String t : tipos) {
					pnlAlarmas.add(new PanelAlarmaFinContrato(ETipoContrato.getById(Integer.valueOf(t))));
				}
			} else {
				for (DatosAlarmaFinContrato d : getParametrosGenerales().getAlarmasFinContrato()) {
					pnlAlarmas.add(new PanelAlarmaFinContrato(d));
					tipos.remove(String.valueOf(d.getTipoContrato().getId()));
				}
				for (String t : tipos) {
					pnlAlarmas.add(new PanelAlarmaFinContrato(ETipoContrato.getById(Integer.valueOf(t))));
				}
			}
			panelTabVarios.add(pnlAlarmas,GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		}

		return panelTabVarios;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public ParametrosGeneralesPersonal getParametrosGenerales() {
		return parametrosGenerales;
	}

	public void setParametrosGenerales(ParametrosGeneralesPersonal parametrosGenerales) {
		if(parametrosGenerales==null){
			this.parametrosGenerales = new ParametrosGeneralesPersonal();
		}else{
			this.parametrosGenerales = parametrosGenerales;
		}
	}

	private void guardar() {
		if (validar()) {
			try {
				getParametrosGenerales().setMaximoPeriodoFueraDeLaEmpresa(getTxtMaximoTiempoFueraDeLaEmpresa().getValue());
				getParametrosGenerales().setToleranciaParaHorasExtra(getTxtToleranciaHorasExtra().getValue());
				getParametrosFacade().save(getParametrosGenerales());
				setAcepto(true);
				CLJOptionPane.showInformationMessage(this, "La configuración ha sido guarda con éxito", "Parametros generales");
				dispose();
			} catch (CLException cle) {
				BossError.gestionarError(cle);
			}
		}
	}

	private boolean validar() {
		if(getTxtToleranciaHorasExtra().getValueWithNull()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad de minutos de tolerancia de para las fichadas", "Error");
			getTxtToleranciaHorasExtra().requestFocus();
			return false;
		}
		if(getTxtMaximoTiempoFueraDeLaEmpresa().getValueWithNull()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el tiempo máximo que puede permanecer una persona sin trabajar en la empresa para no perder su antigüedad", "Error");
			getTxtMaximoTiempoFueraDeLaEmpresa().requestFocus();
			return false;
		}
		if(getParametrosGenerales().getAlarmasFinContrato() != null && !getParametrosGenerales().getAlarmasFinContrato().isEmpty()){
			for(DatosAlarmaFinContrato d : getParametrosGenerales().getAlarmasFinContrato()){
				if(d.getDiasAntes()==null){
					CLJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad de días para el aviso del fin de los contratos: " + d.getTipoContrato().getDescripcion(), "Error");
					return false;
				}
			}
		}
		return true;
	}

	public ParametrosGeneralesPersonalFacadeRemote getParametrosFacade() {
		if (parametrosFacade == null) {
			parametrosFacade = GTLPersonalBeanFactory.getInstance().getBean2(ParametrosGeneralesPersonalFacadeRemote.class);
		}
		return parametrosFacade;
	}

	public CLJNumericTextField getTxtMaximoTiempoFueraDeLaEmpresa() {
		if(txtMaximoTiempoFueraDeLaEmpresa == null){
			txtMaximoTiempoFueraDeLaEmpresa = new CLJNumericTextField();
			if(getParametrosGenerales()!=null && getParametrosGenerales().getMaximoPeriodoFueraDeLaEmpresa()!=null){
				txtMaximoTiempoFueraDeLaEmpresa.setValue(getParametrosGenerales().getMaximoPeriodoFueraDeLaEmpresa().longValue());
			}

		}
		return txtMaximoTiempoFueraDeLaEmpresa;
	}

	public CLJNumericTextField getTxtToleranciaHorasExtra() {
		if(txtToleranciaHorasExtra == null){
			txtToleranciaHorasExtra = new CLJNumericTextField();
			if(getParametrosGenerales()!=null && getParametrosGenerales().getToleranciaParaHorasExtra()!=null){
				txtToleranciaHorasExtra.setValue(getParametrosGenerales().getToleranciaParaHorasExtra().longValue());
			}
		}
		return txtToleranciaHorasExtra;
	}
	
	private class PanelAlarmaFinContrato extends JPanel{

		private static final long serialVersionUID = 5155237126852329355L;
		
		private JCheckBox chkHabilitar;
		private CLJNumericTextField txtDias;
		
		private DatosAlarmaFinContrato datosAlarma;
		
		public PanelAlarmaFinContrato(ETipoContrato tipoContrato){
			setDatosAlarma(new DatosAlarmaFinContrato(tipoContrato));
			construct();
		}
		
		public PanelAlarmaFinContrato(DatosAlarmaFinContrato datos){
			setDatosAlarma(datos);
			construct();
			getChkHabilitar().setSelected(true);
			getTxtDias().setEditable(true);
		}
		
		private void construct(){
			setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			add(getChkHabilitar());
			add(getTxtDias());
			add(new JLabel("días"));
		}
		
		public JCheckBox getChkHabilitar() {
			if(chkHabilitar == null){
				chkHabilitar = new JCheckBox(getDatosAlarma().getTipoContrato().getDescripcion());
				chkHabilitar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getTxtDias().setEditable(getChkHabilitar().isSelected());
						getChkHabilitar().setEnabled(true);
						if(getChkHabilitar().isSelected()){
							getParametrosGenerales().getAlarmasFinContrato().add(getDatosAlarma());
						}else{
							getParametrosGenerales().getAlarmasFinContrato().remove(getDatosAlarma());
						}
					}
				});
			}
			return chkHabilitar;
		}
		
		public CLJNumericTextField getTxtDias() {
			if(txtDias == null){
				txtDias = new CLJNumericTextField();
				txtDias.setPreferredSize(new Dimension(120, 20));
				txtDias.setEditable(false);
				txtDias.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						getDatosAlarma().setDiasAntes(getTxtDias().getValueWithNull());
					}
				});
				if(getDatosAlarma().getDiasAntes()!=null){
					txtDias.setValue(getDatosAlarma().getDiasAntes().longValue());
				}
			}
			return txtDias;
		}
		
		public DatosAlarmaFinContrato getDatosAlarma() {
			return datosAlarma;
		}

		public void setDatosAlarma(DatosAlarmaFinContrato datosAlarma) {
			this.datosAlarma = datosAlarma;
		}
	}
}
