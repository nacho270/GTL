package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogBuscarNotasDeCreditoNoUtilizadas extends JDialog {

	private static final long serialVersionUID = -8770126836455381944L;

	private final List<NotaCreditoProveedor> notasCreditoSeleccionadas;

	private JTabbedPane panelTabs;
	private FWCheckBoxList<NotaCreditoProveedor> listaCheckNotaCreditoProveedor;
	private JPanel panelSur;
	private JPanel panelTabCentralFacturas;

	private JButton btnAceptar;
	private JButton btnSalir;

	private boolean acepto;
	
	private CorreccionFacturaProveedorFacadeRemote correccionFacade;

	private Proveedor proveedor;
	
	private List<Integer> idsNotasCreditoYaUsadas;
	
	public JDialogBuscarNotasDeCreditoNoUtilizadas(Dialog padre, Proveedor prov, List<Integer> idsNotasCredito) {
		super(padre);
		setProveedor(prov);
		notasCreditoSeleccionadas = new ArrayList<NotaCreditoProveedor>();
		setIdsNotasCreditoYaUsadas(idsNotasCredito);
		setAcepto(false);
		setUpComponentes();
		setUpScreen();
		buscarFacturas();
	}

	private void buscarFacturas() {
		List<NotaCreditoProveedor> lista = getCorreccionFacade().getNotasCreditoNoUsadas(getProveedor().getId());
		if(lista!=null){
			List<NotaCreditoProveedor> listaNueva = new ArrayList<NotaCreditoProveedor>();
			for(NotaCreditoProveedor f : lista){
				if(!getIdsNotasCreditoYaUsadas().contains(f.getId())){
					listaNueva.add(f);
				}
			}
			getListaCheckNotaCredito().setValues(listaNueva.toArray());
		}
	}

	private void setUpScreen() {
		setTitle("Elegir facturas y notas de crédito impagas");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 500));
		setModal(true);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelTabs(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public JTabbedPane getPanelTabs() {
		if (panelTabs == null) {
			panelTabs = new JTabbedPane();
			panelTabs.addTab("Notas de crédito", getPanelTabCentralNotasCredito());
		}
		return panelTabs;
	}

	public FWCheckBoxList<NotaCreditoProveedor> getListaCheckNotaCredito() {
		if (listaCheckNotaCreditoProveedor == null) {
			listaCheckNotaCreditoProveedor = new FWCheckBoxList<NotaCreditoProveedor>(){

				private static final long serialVersionUID = 4501839806754196510L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						NotaCreditoProveedor prod = (NotaCreditoProveedor) item;
						if (!getNotasCreditoSeleccionadas().contains(prod)) {
							getNotasCreditoSeleccionadas().add(prod);
						}
					} else {
						getNotasCreditoSeleccionadas().remove(item);
					}
				}
			};
		}
		return listaCheckNotaCreditoProveedor;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(getNotasCreditoSeleccionadas().size()==0){
						FWJOptionPane.showErrorMessage(JDialogBuscarNotasDeCreditoNoUtilizadas.this, "Debe elegir al menos una nota de crédito", "Error");
						return;
					}else{
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel();
			panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}

	public JPanel getPanelTabCentralNotasCredito() {
		if(panelTabCentralFacturas == null){
			panelTabCentralFacturas = new JPanel();
			JScrollPane jsp = new JScrollPane(getListaCheckNotaCredito(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setPreferredSize(new Dimension(550, 400));
			panelTabCentralFacturas.add(jsp, BorderLayout.CENTER);
		}
		return panelTabCentralFacturas;
	}
	
	public CorreccionFacturaProveedorFacadeRemote getCorreccionFacade() {
		if(correccionFacade == null){
			correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacturaProveedorFacadeRemote.class);
		}
		return correccionFacade;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	
	public List<Integer> getIdsNotasCreditoYaUsadas() {
		return idsNotasCreditoYaUsadas;
	}

	
	public void setIdsNotasCreditoYaUsadas(List<Integer> idsNotasCreditoYaUsadas) {
		this.idsNotasCreditoYaUsadas = idsNotasCreditoYaUsadas;
	}

	
	public List<NotaCreditoProveedor> getNotasCreditoSeleccionadas() {
		return notasCreditoSeleccionadas;
	}
}
