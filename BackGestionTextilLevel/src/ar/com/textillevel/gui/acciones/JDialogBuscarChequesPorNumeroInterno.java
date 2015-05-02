package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogBuscarChequesPorNumeroInterno extends JDialog {

	private static final long serialVersionUID = 7473745819598381285L;

	private CLCheckBoxList<Cheque> chkCheques;
	private JButton btnSeleccionarTodo;
	private JButton btnDeseleccionarTodo;
	private JButton btnAceptar;
	private JButton btnSalir;

	private List<Cheque> chequesSeleccionados;
	private List<Integer> idsUtilizadosList;
	private List<NumeracionCheque> listaNumeracionIngresada; 
	
	private ChequeFacadeRemote chequeFacade;
	
	private boolean acepto;

	public JDialogBuscarChequesPorNumeroInterno(Dialog padre, List<Integer> idsUtilizados,List<NumeracionCheque> lista) {
		super(padre);
		chequesSeleccionados = new ArrayList<Cheque>();
		idsUtilizadosList = idsUtilizados;
		setListaNumeracionIngresada(lista);
		acepto = false;
		setUpComponentes();
		setUpScreen();
		buscar();
	}

	private void setUpScreen() {
		setTitle("Buscar cheques");
		setSize(new Dimension(550, 480));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelBusqueda(), BorderLayout.CENTER);
		JPanel panelSur = new JPanel();
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnSalir());
		add(panelSur, BorderLayout.SOUTH);
	}

	private JPanel getPanelBusqueda() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		JScrollPane jsp = new JScrollPane(getChkCheques(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(450, 350));
		panel.add(jsp, GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 4, 1, 1, 1));
		
		JPanel panelSur = new JPanel();
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnSeleccionarTodo());
		panelSur.add(getBtnDeseleccionarTodo());
		
		panel.add(panelSur, GenericUtils.createGridBagConstraints(1, 5, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		return panel;
	}

	private CLCheckBoxList<Cheque> getChkCheques() {
		if (chkCheques == null) {
			chkCheques = new CLCheckBoxList<Cheque>() {

				private static final long serialVersionUID = 4501839806754196510L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						Cheque cheque = (Cheque) item;
						if (!getChequesSeleccionados().contains(cheque)) {
							getChequesSeleccionados().add(cheque);
						}
					} else {
						getChequesSeleccionados().remove(item);
					}
				}
			};
		}
		return chkCheques;
	}

	private JButton getBtnDeseleccionarTodo() {
		if (btnDeseleccionarTodo == null) {
			btnDeseleccionarTodo = new JButton("Deseleccionar todo");
			btnDeseleccionarTodo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getChkCheques().setAllSelectedItems(false);
					getChequesSeleccionados().clear();
				}
			});
		}
		return btnDeseleccionarTodo;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(getChequesSeleccionados().isEmpty()){
						CLJOptionPane.showErrorMessage(JDialogBuscarChequesPorNumeroInterno.this, "Debe seleccionar al menos un cheque", "Error");
						return;
					}
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	private void buscar() {
		List<Cheque> cheques = getChequeFacade().getChequesByNrosInternos(getListaNumeracionIngresada(), getIdsUtilizadosList());
		if(cheques==null || (cheques!=null && cheques.isEmpty())){
			CLJOptionPane.showErrorMessage(this, "No se han encontrado resultados", "Error");
			return;
		}
		List<Cheque> lista2 = new ArrayList<Cheque>();
		for(Cheque cheque : cheques ){
			if(cheque.getEstadoCheque() == EEstadoCheque.EN_CARTERA){
				lista2.add(cheque);
			}else{
				CLJOptionPane.showWarningMessage(this, StringW.wordWrap("El cheque " + cheque.getNumeracion() + " no se puede utilizar debido a que tiene estado: " + cheque.getEstadoCheque()), "Búsqueda de cheques");
			}
		}
		if(lista2.isEmpty()){
			setAcepto(false);
			dispose();
		}else{
			getChkCheques().setValues(lista2.toArray());
			setVisible(true);
		}
		
	}

	private JButton getBtnSeleccionarTodo() {
		if (btnSeleccionarTodo == null) {
			btnSeleccionarTodo = new JButton("Seleccionar todo");
			btnSeleccionarTodo.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getChkCheques().setAllSelectedItems(true);
					for(int i = 0;i<getChkCheques().getSelectedCount();i++){
						Cheque cheque =(Cheque) getChkCheques().getItemAt(i);
						if(!getChequesSeleccionados().contains(cheque)){
							getChequesSeleccionados().add(cheque);
						}
					}
				}
			});
		}
		return btnSeleccionarTodo;
	}

	public List<Cheque> getChequesSeleccionados() {
		return chequesSeleccionados;
	}

	public void setChequesSeleccionados(List<Cheque> chequesSeleccionados) {
		this.chequesSeleccionados = chequesSeleccionados;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	private JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public List<Integer> getIdsUtilizadosList() {
		return idsUtilizadosList;
	}
	
	public void setIdsUtilizadosList(List<Integer> idsUtilizadosList) {
		this.idsUtilizadosList = idsUtilizadosList;
	}
	
	private ChequeFacadeRemote getChequeFacade() {
		if(chequeFacade == null){
			chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		}
		return chequeFacade;
	}

	
	public List<NumeracionCheque> getListaNumeracionIngresada() {
		return listaNumeracionIngresada;
	}

	
	public void setListaNumeracionIngresada(List<NumeracionCheque> listaNumeracionIngresada) {
		this.listaNumeracionIngresada = listaNumeracionIngresada;
	}
}
