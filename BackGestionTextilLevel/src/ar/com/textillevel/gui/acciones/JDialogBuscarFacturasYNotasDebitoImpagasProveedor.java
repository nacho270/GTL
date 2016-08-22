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
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogBuscarFacturasYNotasDebitoImpagasProveedor extends JDialog {

	private static final long serialVersionUID = -8770126836455381944L;

	private List<FacturaProveedor> facturasSeleccionadas;
	private List<NotaDebitoProveedor> notasDebitoSeleccionadas;

	private JTabbedPane panelTabs;
	private FWCheckBoxList<FacturaProveedor> listaCheckFactura;
	private FWCheckBoxList<NotaDebitoProveedor> listaCheckNotaDebito;
	private JPanel panelSur;
	private JPanel panelTabCentralFacturas;
	private JPanel panelTabCentralNotasDebito;

	private JButton btnAceptar;
	private JButton btnSalir;

	private boolean acepto;
	
	private FacturaProveedorFacadeRemote facturaFacade;
	private CorreccionFacturaProveedorFacadeRemote correccionFacade;

	private Proveedor proveedor;
	
	private List<Integer> idsFacturasYaUsadas;
	private List<Integer> idsNotasDebitoYaUsadas;
	
	public JDialogBuscarFacturasYNotasDebitoImpagasProveedor(Dialog padre, Proveedor prov, List<Integer> idsFacturasYaUsados, List<Integer> idsNotasDebitoYaUsadas) {
		super(padre);
		setProveedor(prov);
		facturasSeleccionadas = new ArrayList<FacturaProveedor>();
		notasDebitoSeleccionadas = new ArrayList<NotaDebitoProveedor>();
		setIdsFacturasYaUsadas(idsFacturasYaUsados);
		setIdsNotasDebitoYaUsadas(idsNotasDebitoYaUsadas);
		setAcepto(false);
		setUpComponentes();
		setUpScreen();
		buscarFacturas();
		buscarNotasDeDebito();
	}

	private void buscarFacturas() {
		List<FacturaProveedor> lista = getFacturaFacade().getFacturasImpagas(getProveedor().getId());
		if(lista!=null){
			List<FacturaProveedor> listaNueva = new ArrayList<FacturaProveedor>();
			for(FacturaProveedor f : lista){
				if(!getIdsFacturasYaUsadas().contains(f.getId())){
					listaNueva.add(f);
				}
			}
			getListaCheckFactura().setValues(listaNueva.toArray());
		}
	}

	private void buscarNotasDeDebito() {
		List<NotaDebitoProveedor> lista = getCorreccionFacade().getNotasDeDebitoImpagas(getProveedor().getId());
		if(lista!=null){
			List<NotaDebitoProveedor> listaNueva = new ArrayList<NotaDebitoProveedor>();
			for(NotaDebitoProveedor f : lista){
				if(!getIdsNotasDebitoYaUsadas().contains(f.getId())){
					listaNueva.add(f);
				}
			}
			getListaCheckNotaDebito().setValues(listaNueva.toArray());
		}
	}

	private void setUpScreen() {
		setTitle("Elegir facturas y notas de débito impagas");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 500));
		setModal(true);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelTabs(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	public List<FacturaProveedor> getFacturasSeleccionadas() {
		return facturasSeleccionadas;
	}

	public void setFacturasSeleccionadas(List<FacturaProveedor> facturasSeleccionadas) {
		this.facturasSeleccionadas = facturasSeleccionadas;
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
			panelTabs.addTab("Facturas", getPanelTabCentralFacturas());
			panelTabs.addTab("Notas de débito", getPanelTabCentralNotasDebito());
		}
		return panelTabs;
	}

	public FWCheckBoxList<FacturaProveedor> getListaCheckFactura() {
		if (listaCheckFactura == null) {
			listaCheckFactura = new FWCheckBoxList<FacturaProveedor>(){

				private static final long serialVersionUID = 4501839806754196510L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						FacturaProveedor prod = (FacturaProveedor) item;
						if (!getFacturasSeleccionadas().contains(prod)) {
							getFacturasSeleccionadas().add(prod);
						}
					} else {
						getFacturasSeleccionadas().remove(item);
					}
				}
			};
		}
		return listaCheckFactura;
	}

	public FWCheckBoxList<NotaDebitoProveedor> getListaCheckNotaDebito() {
		if (listaCheckNotaDebito == null) {
			listaCheckNotaDebito = new FWCheckBoxList<NotaDebitoProveedor>(){

				private static final long serialVersionUID = -4016361015164714193L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						NotaDebitoProveedor prod = (NotaDebitoProveedor) item;
						if (!getNotasDebitoSeleccionadas().contains(prod)) {
							getNotasDebitoSeleccionadas().add(prod);
						}
					} else {
						getNotasDebitoSeleccionadas().remove(item);
					}
				}
			};
		}
		return listaCheckNotaDebito;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(getFacturasSeleccionadas().size()==0 && getNotasDebitoSeleccionadas().size() == 0){
						FWJOptionPane.showErrorMessage(JDialogBuscarFacturasYNotasDebitoImpagasProveedor.this, "Debe elegir al menos una factura o nota de débito", "Error");
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

	public JPanel getPanelTabCentralFacturas() {
		if(panelTabCentralFacturas == null){
			panelTabCentralFacturas = new JPanel();
			JScrollPane jsp = new JScrollPane(getListaCheckFactura(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setPreferredSize(new Dimension(550, 400));
			panelTabCentralFacturas.add(jsp, BorderLayout.CENTER);
		}
		return panelTabCentralFacturas;
	}
	
	public JPanel getPanelTabCentralNotasDebito() {
		if(panelTabCentralNotasDebito == null){
			panelTabCentralNotasDebito = new JPanel();
			JScrollPane jsp = new JScrollPane(getListaCheckNotaDebito(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setPreferredSize(new Dimension(550, 400));
			panelTabCentralNotasDebito.add(jsp, BorderLayout.CENTER);
		}
		return panelTabCentralNotasDebito;
	}
	
	public FacturaProveedorFacadeRemote getFacturaFacade() {
		if(facturaFacade == null){
			facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class);
		}
		return facturaFacade;
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

	
	public List<Integer> getIdsFacturasYaUsadas() {
		return idsFacturasYaUsadas;
	}

	
	public void setIdsFacturasYaUsadas(List<Integer> idsFacturasYaUsadas) {
		this.idsFacturasYaUsadas = idsFacturasYaUsadas;
	}

	
	public List<NotaDebitoProveedor> getNotasDebitoSeleccionadas() {
		return notasDebitoSeleccionadas;
	}

	
	public void setNotasDebitoSeleccionadas(List<NotaDebitoProveedor> notasDebitoSeleccionadas) {
		this.notasDebitoSeleccionadas = notasDebitoSeleccionadas;
	}

	
	public List<Integer> getIdsNotasDebitoYaUsadas() {
		return idsNotasDebitoYaUsadas;
	}

	
	public void setIdsNotasDebitoYaUsadas(List<Integer> idsNotasDebitoYaUsadas) {
		this.idsNotasDebitoYaUsadas = idsNotasDebitoYaUsadas;
	}
}
