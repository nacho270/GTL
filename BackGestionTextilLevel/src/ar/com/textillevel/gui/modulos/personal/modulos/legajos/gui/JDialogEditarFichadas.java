package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLDateField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.ETipoFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.FichadaLegajoTO;
import ar.com.textillevel.modulos.personal.facade.api.remote.FichadaLegajoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogEditarFichadas extends JDialog {

	private static final long serialVersionUID = -4036160203265311374L;

	private FichadaLegajoTO fichada;
	private String nombreEmpleado;
	
	private PanelTablaFichada panelTabla;
	private CLJTextField txtNombreEmpleado;
	private CLDateField txtFechaFichada;

	private JButton btnAceptar;
	private JButton btnSalir;
	
	private boolean acepto;
	
	private final List<FichadaLegajo> fichadasBorradas;
	
	private FichadaLegajoFacadeRemote fichadaFacade;
	
	public JDialogEditarFichadas(Dialog padre, FichadaLegajoTO fichadaTO, String nombreEmpleado) {
		super(padre);
		setFichada(fichadaTO);
		setNombreEmpleado(nombreEmpleado);
		fichadasBorradas = new ArrayList<FichadaLegajo>();
		setUpComponentes();
		setUpScreen();
		llenarTabla();
	}

	private void llenarTabla() {
		getPanelTabla().limpiar();
		Collections.sort(getFichada().getFichadasComprendidas(), new Comparator<FichadaLegajo>() {
			public int compare(FichadaLegajo o1, FichadaLegajo o2) {
				return o1.getHorario().compareTo(o2.getHorario());
			}
		});
		for(FichadaLegajo fl : getFichada().getFichadasComprendidas()){
			getPanelTabla().agregarElemento(fl);
		}
	}

	private void setUpScreen() {
		setTitle("Editar fichadas");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		setSize(new Dimension(getWidth(), 250));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(),BorderLayout.NORTH);
		add(getPanelTabla(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	public JPanel getPanelNorte(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Empleado: "));
		panel.add(getTxtNombreEmpleado());
		panel.add(new JLabel("Fichadas del día: "));
		panel.add(getTxtFechaFichada());
		return panel;
	}

	public JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnSalir());
		return panel;
	}
	
	public FichadaLegajoTO getFichada() {
		return fichada;
	}

	public void setFichada(FichadaLegajoTO fichada) {
		this.fichada = fichada;
	}

	private class PanelTablaFichada extends PanelTabla<FichadaLegajo> {

		private static final long serialVersionUID = 6410877267572092063L;

		private static final int CANT_COLS = 4;
		private static final int COL_TIPO_FICHADA = 0;
		private static final int COL_HORA_FICHADA = 1;
		private static final int COL_FORMA_INGRESO_FICHADA = 2;
		private static final int COL_OBJ = 3;

		public PanelTablaFichada() {
			super();
			agregarBotonModificar();
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_TIPO_FICHADA, "Tipo fichada", 80, 80, true);
			tabla.setTimeColumn(COL_HORA_FICHADA, "Horario", 120, true);
			tabla.setStringColumn(COL_FORMA_INGRESO_FICHADA, "Forma de ingreso", 120,120, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(FichadaLegajo elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_TIPO_FICHADA] = elemento.getTipoFichada();
			row[COL_HORA_FICHADA] = elemento.getHorario();
			row[COL_FORMA_INGRESO_FICHADA] = elemento.getFormaIngresoFichada();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FichadaLegajo getElemento(int fila) {
			return (FichadaLegajo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogAgregarEditarFichada dialog = new JDialogAgregarEditarFichada(JDialogEditarFichadas.this,getFichada().getDia());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				dialog.getFichada().setLegajo(getFichada().getFichadasComprendidas().get(0).getLegajo());
				getFichada().getFichadasComprendidas().add(dialog.getFichada());
				llenarTabla();
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			FichadaLegajo f = getElemento(getTabla().getSelectedRow());
			getFichadasBorradas().add(f);
			getFichada().getFichadasComprendidas().remove(f);
			return true;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogAgregarEditarFichada dialog = new JDialogAgregarEditarFichada(JDialogEditarFichadas.this,getElemento(filaSeleccionada));
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getFichada().getFichadasComprendidas().set(filaSeleccionada, dialog.getFichada());
				llenarTabla();
			}
		}
	}

	public PanelTablaFichada getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaFichada();
		}
		return panelTabla;
	}

	public CLJTextField getTxtNombreEmpleado() {
		if (txtNombreEmpleado == null) {
			txtNombreEmpleado = new CLJTextField(getNombreEmpleado());
			txtNombreEmpleado.setPreferredSize(new Dimension(200, 20));
			txtNombreEmpleado.setEditable(false);
		}
		return txtNombreEmpleado;
	}

	public CLDateField getTxtFechaFichada() {
		if (txtFechaFichada == null) {
			txtFechaFichada = new CLDateField(getFichada().getDia());
			txtFechaFichada.setPreferredSize(new Dimension(90, 20));
			txtFechaFichada.setEditable(false);
		}
		return txtFechaFichada;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						//borrar las fichadas de la lista de borradas
						for(FichadaLegajo fl : getFichadasBorradas()){
							if(fl.getId()!=null){
								getFichadaFacade().remove(fl);
							}
						}
						//grabar las de la lista de fichadas relacionadas
						for(FichadaLegajo fl : getFichada().getFichadasComprendidas()){
							getFichadaFacade().save(fl);
						}
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}
	
	private boolean validar(){
		ETipoFichada tipoFichada;
		Timestamp tsAnterior =  getFichada().getFichadasComprendidas().get(0).getHorario();
		for(int i = 0; i<getFichada().getFichadasComprendidas().size();i++){
			tipoFichada = i%2==0?ETipoFichada.ENTRADA:ETipoFichada.SALIDA;
			FichadaLegajo fl = getFichada().getFichadasComprendidas().get(i);
			if(fl.getTipoFichada()!=tipoFichada){
				CLJOptionPane.showErrorMessage(this, "Las fichadas son incorrectas. Deben empezar con 'Entrada' y luego intercalarse.", "Error");
				return false;
			}
			if(fl.getHorario().before(tsAnterior)){
				CLJOptionPane.showErrorMessage(this, "Las fichadas son incorrectas. Deben estar ordenadas cronologicamente y de forma ascendente.", "Error");
				return false;
			}
			tsAnterior = fl.getHorario();
		}
		return true;
	}

	public JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					limpiarTransients();
					setAcepto(false);
					dispose();
				}

				private void limpiarTransients() {
					List<Integer> indicesBorrar = new ArrayList<Integer>();
					for(int i = 0; i<getFichada().getFichadasComprendidas().size();i++){
						FichadaLegajo fl = getFichada().getFichadasComprendidas().get(i);
						if(fl.getId()==null){
							indicesBorrar.add(i);
						}
					}
					if(!indicesBorrar.isEmpty()){
						for(Integer i : indicesBorrar){
							getFichada().getFichadasComprendidas().remove(i.intValue());
						}
					}
				}
			});
		}
		return btnSalir;
	}
	
	public String getNombreEmpleado() {
		return nombreEmpleado;
	}
	
	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}
	
	public boolean isAcepto() {
		return acepto;
	}
	
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public List<FichadaLegajo> getFichadasBorradas() {
		return fichadasBorradas;
	}

	public FichadaLegajoFacadeRemote getFichadaFacade() {
		if(fichadaFacade == null){
			fichadaFacade = GTLPersonalBeanFactory.getInstance().getBean2(FichadaLegajoFacadeRemote.class);
		}
		return fichadaFacade;
	}
}
