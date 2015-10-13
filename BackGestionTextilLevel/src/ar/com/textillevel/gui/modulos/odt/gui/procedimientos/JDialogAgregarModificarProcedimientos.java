package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

public class JDialogAgregarModificarProcedimientos extends JDialog {

	private static final long serialVersionUID = -2335567081797221849L;

	private FWJTextField txtNombreProcedimiento;
	private PanelTablaInstruccionesConAcciones panelTabla;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private boolean acepto;
	private ProcedimientoTipoArticulo procedimientoActual;
	private final ESectorMaquina sectorMaquina;
	private ProcesoTipoMaquina proceso;

	public JDialogAgregarModificarProcedimientos(Dialog padre, TipoArticulo tipoArticulo, ESectorMaquina sectorMaquina, ProcesoTipoMaquina proceso) {
		super(padre);
		this.sectorMaquina = sectorMaquina;
		this.proceso = proceso;
		setProcedimientoActual(new ProcedimientoTipoArticulo());
		getProcedimientoActual().setTipoArticulo(tipoArticulo);
		setUpScreen();
		setUpComponentes();
	}

	public JDialogAgregarModificarProcedimientos(Dialog padre, ProcedimientoTipoArticulo procedimientoActual, ESectorMaquina sectorMaquina, ProcesoTipoMaquina proceso) {
		super(padre);
		this.sectorMaquina = sectorMaquina;
		this.proceso = proceso;
		setProcedimientoActual(procedimientoActual);
		setUpScreen();
		setUpComponentes();
		loadData();
	}

	private void loadData() {
		getTxtNombreProcedimiento().setText(getProcedimientoActual().getNombre());
		getPanelTabla().agregarElementos(getProcedimientoActual().getPasos());
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getPanelTabla(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelNorte() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNombreProcedimiento(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		return panel;
	}

	private void setUpScreen() {
		setTitle("Alta/modificación de procedimientos");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(500, 550));
		GuiUtil.centrar(this);
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private class PanelTablaInstruccionesConAcciones extends PanelTablaInstrucciones {

		private static final long serialVersionUID = 5002844689448032616L;

		private final ESectorMaquina sectorMaquina;

		public PanelTablaInstruccionesConAcciones(ESectorMaquina sectorMaquina) {
			this.sectorMaquina = sectorMaquina;
		}

		@Override
		public boolean validarAgregar() {
			JDialogSeleccionarInstruccion dialog = new JDialogSeleccionarInstruccion(JDialogAgregarModificarProcedimientos.this,getProcedimientoActual().getTipoArticulo(), sectorMaquina, proceso);
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getProcedimientoActual().getPasos().add(dialog.getInstruccionFinal());
				limpiar();
				agregarElementos(getProcedimientoActual().getPasos());
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			getProcedimientoActual().getPasos().remove(getTabla().getSelectedRow());
			limpiar();
			agregarElementos(getProcedimientoActual().getPasos());
			return false;
		}

		@Override
		protected void dobleClickTabla(int filaSeleccionada) {
			if(filaSeleccionada>-1){
				botonModificarPresionado(filaSeleccionada);
			}
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogSeleccionarInstruccion dialog = new JDialogSeleccionarInstruccion(JDialogAgregarModificarProcedimientos.this,getProcedimientoActual().getTipoArticulo(),getElemento(filaSeleccionada), sectorMaquina, proceso);
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getProcedimientoActual().getPasos().set(filaSeleccionada,dialog.getInstruccionFinal());
				limpiar();
				agregarElementos(getProcedimientoActual().getPasos());
			}
		}

		@Override
		protected void botonBajarPresionado() {
			swapInstruccion(1);
		}

		@Override
		protected void botonSubirPresionado() {
			swapInstruccion(-1);
		}

		private void swapInstruccion(int offset) {
			int selectedRow = getTabla().getSelectedRow();
			InstruccionProcedimiento instruccionActual = getElemento(selectedRow);
			getProcedimientoActual().getPasos().set(selectedRow + offset, instruccionActual);
			getProcedimientoActual().getPasos().set(selectedRow, getElemento(selectedRow + offset));
			limpiar();
			agregarElementos(getProcedimientoActual().getPasos());
			getTabla().setRowSelectionInterval(selectedRow + offset, selectedRow + offset);
		}
		
		@Override
		protected void filaTablaSeleccionada() {
			if(!modoConsulta) {
				getBotonBajar().setEnabled(getTabla().getSelectedRow() < getTabla().getRowCount() - 1);
				getBotonSubir().setEnabled(getTabla().getSelectedRow() > 0);
			}
		}

	}

	public FWJTextField getTxtNombreProcedimiento() {
		if (txtNombreProcedimiento == null) {
			txtNombreProcedimiento = new FWJTextField(50);
		}
		return txtNombreProcedimiento;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public PanelTablaInstruccionesConAcciones getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaInstruccionesConAcciones(sectorMaquina);
		}
		return panelTabla;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()){
						capturarDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void capturarDatos() {
		getProcedimientoActual().setNombre(getTxtNombreProcedimiento().getText().trim().toUpperCase());
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtNombreProcedimiento().getText())){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del procedimiento", "Error");
			getTxtNombreProcedimiento().requestFocus();
			return false;
		}
		if(getProcedimientoActual().getPasos().isEmpty()){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar los pasos del procedimiento", "Error");
			return false;
		}
		return true;
	}
	
	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//limpio las instrucciones transients creadas
					Set<InstruccionProcedimiento> instruccionesBorrar = new HashSet<InstruccionProcedimiento>(); 
					for(InstruccionProcedimiento ip : procedimientoActual.getPasos()) {
						if(ip.getId() < 0) {//es transient!
							instruccionesBorrar.add(ip);
						}
					}
					proceso.getInstrucciones().removeAll(instruccionesBorrar);
					salir();
				}

			});
		}
		return btnCancelar;
	}

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(JDialogAgregarModificarProcedimientos.this, "Va a salir sin grabar los cambios. Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}

	public ProcedimientoTipoArticulo getProcedimientoActual() {
		return procedimientoActual;
	}

	public void setProcedimientoActual(ProcedimientoTipoArticulo procedimientoActual) {
		this.procedimientoActual = procedimientoActual;
	}
}
