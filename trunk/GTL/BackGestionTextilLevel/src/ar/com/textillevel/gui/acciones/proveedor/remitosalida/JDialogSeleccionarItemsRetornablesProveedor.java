package ar.com.textillevel.gui.acciones.proveedor.remitosalida;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEvent;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEventListener;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarItemsRetornablesProveedor extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel panDetalle;
	private JTextField txtRazSoc;
	private JButton btnCancelar;
	private JButton btnAceptar;

	private JPanel pnlBotones;
	private final Frame owner;

	private final Proveedor proveedor;
	private PanelSeleccionarElementos<ContenedorMateriaPrima> panSelContenedores;
	private PanelSeleccionarElementos<PrecioMateriaPrima> panSelMatPrima;

	private final List<PrecioMateriaPrima> precioMatPrimaList;
	private PrecioMateriaPrimaFacadeRemote precioMatPrimaFacade;

	private final List<ContenedorMateriaPrima> contenedorListResult = new ArrayList<ContenedorMateriaPrima>();
	private List<ContenedorMateriaPrima> contenedoresList;
	private final List<PrecioMateriaPrima> pmpListResult = new ArrayList<PrecioMateriaPrima>();

	public JDialogSeleccionarItemsRetornablesProveedor(Frame owner, Proveedor proveedor, List<ContenedorMateriaPrima> contenedoresList) {
		super(owner);
		this.owner = owner;
		this.proveedor = proveedor;
		this.contenedoresList = contenedoresList; 
		this.precioMatPrimaList = getPrecioMatPrimaFacade().getAllWithStockByProveedorOrderByMateriaPrima(proveedor.getId());

		setModal(true);
		setSize(new Dimension(650, 300));
		setTitle("Seleccionar Items a devolver del proveedor");
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel(" PROVEEDOR:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtRazSoc(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanSelContenedores(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panDetalle.add(getPanSelMatPrima(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
		}
		return panDetalle;
	}

	private JTextField getTxtRazSoc() {
		if(txtRazSoc == null) {
			txtRazSoc = new JTextField();
			txtRazSoc.setEditable(false);
			txtRazSoc.setText(proveedor.getRazonSocial());
		}
		return txtRazSoc;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnCancelar;
	}
	
	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					contenedorListResult.addAll(getPanSelContenedores().getSelectedElements());
					pmpListResult.addAll(getPanSelMatPrima().getSelectedElements());
					dispose();
				}

			});
		}
		return btnAceptar;
	}

	public List<PrecioMateriaPrima> getPmpListResult() {
		return pmpListResult;
	}

	public List<ContenedorMateriaPrima> getContenedorListResult() {
		return contenedorListResult;
	}

	private PrecioMateriaPrimaFacadeRemote getPrecioMatPrimaFacade() {
		if(precioMatPrimaFacade == null) {
			this.precioMatPrimaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		}
		return precioMatPrimaFacade;
	}

	private PanelSeleccionarElementos<PrecioMateriaPrima> getPanSelMatPrima() {
		if(panSelMatPrima == null) {
			panSelMatPrima = new PanelSeleccionarElementos<PrecioMateriaPrima>(owner, precioMatPrimaList, "Materias Primas");			
		}
		return panSelMatPrima;
	}

	private PanelSeleccionarElementos<ContenedorMateriaPrima> getPanSelContenedores() {
		if(panSelContenedores == null) {
			panSelContenedores = new PanelSeleccionarElementos<ContenedorMateriaPrima>(owner, contenedoresList, "Contenedores");
			panSelContenedores.addPanelSeleccionarElementosListener(new PanelSeleccionarElementoEventListener<ContenedorMateriaPrima>() {

				public void elementsSelected(PanelSeleccionarElementoEvent<ContenedorMateriaPrima> evt) {
//					getPanSelMatPrima().setElements(evt.getElements());
					//TODO:
				}

			});
		}
		return panSelContenedores;
	}

}