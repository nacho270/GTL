package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarFacturasImpagasCliente extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private FWCheckBoxList<FacturaWrapper> checkBoxList;
	private boolean acepto;
	private List<Factura> facturaSelectedList;
	private List<Factura> allFacturaList;
	private Integer idCliente;

	private JPanel pnlBotones;
	private JPanel pnlDatos;

	private FacturaFacadeRemote facturaFacade;

	public JDialogSeleccionarFacturasImpagasCliente(JDialog owner, List<Factura> facturaSelectedList, Integer idCliente) {
		super(owner);
		this.facturaSelectedList = new ArrayList<Factura>(facturaSelectedList);
		this.idCliente = idCliente;
		setUpComponentes();
		setUpScreen();
		setDatos();
		if(facturaSelectedList!=null && facturaSelectedList.size()>0){
			chequearFacturasAnteriores();
		}
	}

	private void chequearFacturasAnteriores() {
		for(Factura f : facturaSelectedList){
			for(int i = 0; i < getClCheckBoxList().getItemCount(); i++){
				if(((FacturaWrapper)getClCheckBoxList().getItemAt(i)).getId().equals(f.getId())){
					getClCheckBoxList().setSelectedIndex(i);
				}
			}
		}
	}

	private void setDatos() {
	//	getClCheckBoxList().setValues(facturaSelectedList.toArray(new Object[facturaSelectedList.size()]));
		List<Factura> facturaListByClientAndEstado = getFacturaFacade().getAllFacturasByCliente(getIdCliente());
		getClCheckBoxList().setValues(toWrapper(facturaListByClientAndEstado).toArray(new Object[facturaListByClientAndEstado.size()]));
	}

	private void setUpScreen() {
		setTitle("Buscar factura");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 350));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes() {
		add(getPanelDatos(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones() {
		if (pnlBotones == null) {
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if (pnlDatos == null) {
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			JScrollPane scrollPane = new JScrollPane(getClCheckBoxList());
			scrollPane.setBorder(BorderFactory.createTitledBorder("FACTURAS"));
			pnlDatos.add(scrollPane, gridBagConstraints);
		}
		return pnlDatos;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						acepto = true;
						dispose();
					}
				}

				private boolean validar() {
					if (getClCheckBoxList().getSelectedValues().length == 0) {
						FWJOptionPane.showErrorMessage(JDialogSeleccionarFacturasImpagasCliente.this, "Debe seleccionar al menos una factura.", JDialogSeleccionarFacturasImpagasCliente.this
								.getTitle());
						return false;
					}
					return true;
				}	
			});
		}
		return btnAceptar;
	}

	private JButton getBtnCancelar() {
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

	private FWCheckBoxList<FacturaWrapper> getClCheckBoxList() {
		if (checkBoxList == null) {
			checkBoxList = new FWCheckBoxList<FacturaWrapper>() {

				private static final long serialVersionUID = -8028977693425752374L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						FacturaWrapper prod = (FacturaWrapper) item;
						if (!facturaSelectedList.contains(prod.getF())) {
							facturaSelectedList.add(prod.getF());
						}
					} else {
						facturaSelectedList.remove(((FacturaWrapper) item).getF());
					}
				}

			};
			allFacturaList = getFacturaFacade().getFacturaImpagaListByClient(getIdCliente());
			checkBoxList.setValues(toWrapper(allFacturaList).toArray(new Object[allFacturaList.size()]));
		}
		return checkBoxList;
	}

	private List<FacturaWrapper> toWrapper(List<Factura> facturas) {
		List<FacturaWrapper> result = new ArrayList<FacturaWrapper>();
		for(Factura f : facturas) {
			result.add(new FacturaWrapper(f));
		}
		return result;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public List<Factura> getFacturaSelectedList() {
		return facturaSelectedList;
	}

	private FacturaFacadeRemote getFacturaFacade() {
		if (facturaFacade == null) {
			facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
		}
		return facturaFacade;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	private static class FacturaWrapper {
		
		private Factura f;

		public FacturaWrapper(Factura f) {
			this.f = f;
		}
		
		public Integer getId() {
			return getF().getId();
		}

		public Factura getF() {
			return f;
		}

		public String toString() {
			return "Factura - " + f.getNroFactura() + " / REM. SALIDA: " + extractNros();
		}

		@Transient
		private String extractNros() {
			List<String> nros = new ArrayList<String>();
			for(RemitoSalida r : getF().getRemitos()) {
				nros.add(r.getNroRemito()+"");
			}
			return StringUtil.getCadena(nros, " - ");
		}

	}

}