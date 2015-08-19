package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.facade.api.remote.GamaColorClienteFacadeRemote;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarDefinicionPreciosTenido extends JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloTenido> {

	private static final long serialVersionUID = -6851805146971694269L;
	
	private JComboBox cmbGama;
	private LinkableLabel linkableLabelEditarGamaCliente;
	
	private List<GamaColorCliente> gamas;
	private GamaColorClienteFacadeRemote gamaClienteFacade;
	
	public JDialogAgregarModificarDefinicionPreciosTenido(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		super(padre, cliente, tipoProducto);
		gamas = getGamaClienteFacade().getByCliente(getCliente().getId());
		if (gamas == null || gamas.isEmpty()) {
			CLJOptionPane.showWarningMessage(this, "El cliente no cuenta con gamas definidas. Debe ingresarlas.", "Advertencia");
			JDialogAgregarModificarGamaColorCliente d = new JDialogAgregarModificarGamaColorCliente(this, getCliente());
			d.setVisible(true);
			if (d.isAcepto()) {
				
			}
		}
	}

	public JDialogAgregarModificarDefinicionPreciosTenido(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
		gamas = getGamaClienteFacade().getByCliente(getCliente().getId());
	}
	
	@Override
	protected JPanel createPanelDatosEspecificos() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Gama: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
		panel.add(getCmbGama(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		panel.add(getLinkableLabelEditarGamaCliente(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		return panel;
	}

	public JComboBox getCmbGama() {
		if (cmbGama == null) {
			cmbGama = new JComboBox();
			GuiUtil.llenarCombo(cmbGama, getGamas(), true);
		}
		return cmbGama;
	}

	@Override
	protected PanelTablaRango<RangoAnchoArticuloTenido> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloTenido> parent) {
		return new PanelTablaRangoTenido(parent);
	}

	public GamaColorClienteFacadeRemote getGamaClienteFacade() {
		if (gamaClienteFacade == null) {
			gamaClienteFacade = GTLBeanFactory.getInstance().getBean2(GamaColorClienteFacadeRemote.class);
		}
		return gamaClienteFacade;
	}

	public List<GamaColorCliente> getGamas() {
		return gamas;
	}

	public LinkableLabel getLinkableLabelEditarGamaCliente() {
		if (linkableLabelEditarGamaCliente == null) {
			linkableLabelEditarGamaCliente = new LinkableLabel("Editar gama") {
				
				private static final long serialVersionUID = -1762575664503960563L;

				@Override
				public void labelClickeada(MouseEvent e) {
					if (getCmbGama().getSelectedItem() != null) {
						JDialogAgregarModificarGamaColorCliente d = new JDialogAgregarModificarGamaColorCliente(JDialogAgregarModificarDefinicionPreciosTenido.this, getCliente(), (GamaColorCliente)getCmbGama().getSelectedItem() );
					}
				}
			};
		}
		return linkableLabelEditarGamaCliente;
	}

	@Override
	protected void botonAgregarPresionado() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean validar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setModoEdicionExtended(boolean modoEdicion) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void limpiarDatosExtended() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void botonAgregarOrCancelarPresionado() {
		// TODO Auto-generated method stub
	}

}