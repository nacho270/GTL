package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioGama;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.facade.api.remote.GamaColorClienteFacadeRemote;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarDefinicionPreciosTenido extends JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloTenido, PrecioGama> {

	private static final long serialVersionUID = -6851805146971694269L;

	private JComboBox cmbGama;
	private LinkableLabel linkableLabelEditarGamaCliente;
	private JButton btnAgregarTodos;
	private JPanel panelDatosPropios;

	private List<GamaColorCliente> gamas;
	private GamaColorClienteFacadeRemote gamaClienteFacade;
	
	private boolean editable = true;
	
	public JDialogAgregarModificarDefinicionPreciosTenido(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		this(padre, cliente, tipoProducto, new DefinicionPrecio());
	}

	public JDialogAgregarModificarDefinicionPreciosTenido(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
		gamas = getGamaClienteFacade().getByCliente(getCliente().getId());
		if (definicionAModificar.getId() == null) {
			if (gamas == null || gamas.isEmpty()) {
				CLJOptionPane.showWarningMessage(this, "El cliente no cuenta con gamas definidas. Debe ingresarlas.", "Advertencia");
				JDialogAgregarModificarGamaColorCliente d = new JDialogAgregarModificarGamaColorCliente(this, getCliente());
				d.setVisible(true);
				if (d.isAcepto()) {
					gamas = getGamaClienteFacade().getByCliente(getCliente().getId());
				} else {
					CLJOptionPane.showWarningMessage(this, StringW.wordWrap("No se puede dar de alta la lista de precios para teñido si tener definidas las gamas cliente."), "Advertencia");
					setAcepto(false);
					dispose();
				}
			}
			setAcepto(true);
		}
		GuiUtil.llenarCombo(getCmbGama(), getGamas(), true);
		setModoEdicion(true);
	}
	
	@Override
	protected JPanel createPanelDatosEspecificos() {
		if (panelDatosPropios == null) {
			panelDatosPropios = new JPanel(new GridBagLayout());
			panelDatosPropios.add(new JLabel("Gama: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelDatosPropios.add(getCmbGama(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panelDatosPropios.add(getLinkableLabelEditarGamaCliente(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelDatosPropios.add(getBtnAgregarTodos(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 1, 1));
		}
		return panelDatosPropios;
	}

	public JComboBox getCmbGama() {
		if (cmbGama == null) {
			cmbGama = new JComboBox();
			GuiUtil.llenarCombo(cmbGama, getGamas(), true);
		}
		return cmbGama;
	}

	@Override
	protected PanelTablaRango<RangoAnchoArticuloTenido, PrecioGama> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloTenido, PrecioGama> parent) {
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
					if (editable) {
						JDialogAgregarModificarGamaColorCliente d = new JDialogAgregarModificarGamaColorCliente(JDialogAgregarModificarDefinicionPreciosTenido.this, getCliente());
						d.setVisible(true);
						if (d.isAcepto()) {
							gamas = getGamaClienteFacade().getByCliente(getCliente().getId());
							getCmbGama().removeAllItems();
							GuiUtil.llenarCombo(getCmbGama(), getGamas(), true);
						}
					}
				}
			};
		}
		return linkableLabelEditarGamaCliente;
	}

	@Override
	public void setElemHojaSiendoEditado(PrecioGama precioGamaSiendoEditado, boolean modoEdicion) {
		this.elemSiendoEditado = precioGamaSiendoEditado;

		setModoEdicion(modoEdicion);

		getCmbGama().setSelectedItem(precioGamaSiendoEditado.getGamaCliente());
		GrupoTipoArticuloGama grupoTipoArticuloBase = precioGamaSiendoEditado.getGrupoTipoArticuloGama();
		getCmbTipoArticulo().setSelectedItem(grupoTipoArticuloBase.getTipoArticulo());

		RangoAnchoArticuloTenido rangoAnchoArticulo = grupoTipoArticuloBase.getRangoAnchoArticuloTenido();
		getTxtAnchoInicial().setValue(rangoAnchoArticulo.getAnchoMinimo() == null ? null : rangoAnchoArticulo.getAnchoMinimo().doubleValue());
		getTxtAnchoFinal().setValue(rangoAnchoArticulo.getAnchoMaximo() == null ? null :rangoAnchoArticulo.getAnchoMaximo().doubleValue());
		if(rangoAnchoArticulo.getAnchoExacto() != null) {
			getTxtAnchoExacto().setValue(rangoAnchoArticulo.getAnchoExacto().doubleValue());
			getChkAnchoExacto().setSelected(true);
		} else {
			getTxtAnchoExacto().setValue(null);
			getChkAnchoExacto().setSelected(false);
		}
		getTxtPrecio().setValue(precioGamaSiendoEditado.getPrecio().doubleValue());
	}
	
	@Override
	protected void botonAgregarPresionado() {
		agregarInterno((GamaColorCliente) getCmbGama().getSelectedItem(), getPrecio(),false);
	}

	private void agregarInterno(GamaColorCliente gcc, Float precio, boolean isBulk) {
		if(!isBulk && elemSiendoEditado != null) {
			elemSiendoEditado.deepRemove();
		}

		//Definicion
		DefinicionPrecio definicion = getDefinicion();
		if(definicion == null) {
			setDefinicion(new DefinicionPrecio());
		}
		//Rango
		Float min = getAnchoInicial();
		Float max = getAnchoFinal();
		Float exacto = null;
		if(getChkAnchoExacto().isSelected()) {
			exacto = getAnchoExacto();
		}
		RangoAnchoArticuloTenido rango = (RangoAnchoArticuloTenido)definicion.getRango(min, max, exacto);
		if(rango == null) {
			rango = new RangoAnchoArticuloTenido();
			rango.setAnchoExacto(exacto);
			rango.setAnchoMinimo(min);
			rango.setAnchoMaximo(max);
			getDefinicion().getRangos().add(rango);
			rango.setDefinicionPrecio(definicion);
		}
		
		//Grupo
		TipoArticulo ta = getTipoArticulo();
		GrupoTipoArticuloGama grupo = rango.getGrupo(ta);
		if(grupo == null) {
			grupo = new GrupoTipoArticuloGama();
			grupo.setTipoArticulo(ta);
			rango.getGruposGama().add(grupo);
			grupo.setRangoAnchoArticuloTenido(rango);
		}
		//Float precio = getPrecio();
		//GamaColorCliente gcc = (GamaColorCliente) getCmbGama().getSelectedItem();
		PrecioGama pg = grupo.getPrecioGama(gcc);
		if (pg == null) {
			pg = new PrecioGama();
			pg.setGamaCliente(gcc);
			pg.setGamaDefault(gcc.getGamaOriginal());
			pg.setGrupoTipoArticuloGama(grupo);
			pg.setPrecio(precio);
			grupo.getPrecios().add(pg);
		}
		
		getDefinicion().deepOrderBy();

		List<RangoAnchoArticuloTenido> rangosList = new ArrayList<RangoAnchoArticuloTenido>();
		for(RangoAncho r : getDefinicion().getRangos()) {
			rangosList.add((RangoAnchoArticuloTenido)r);
		}
		//agrego a la tabla
		getTablaRango().limpiar();
		getTablaRango().agregarElementos(rangosList);
		
		getTablaRango().selectElement(pg);
		getTablaRango().setTextoBotonGuardar("Agregar");
	}

	@Override
	protected boolean validar() {
		if(validarDatosComunes(true)) {
			return validarGama((GamaColorCliente) getCmbGama().getSelectedItem());
		} else {
			return false;
		}
	}

	private boolean validarGama(GamaColorCliente gamaColorCliente) {
		//Gama
		if(gamaColorCliente == null) {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar una 'Gama'.", "Error");
			return false;
		}
		RangoAnchoArticuloTenido rangoAnchoArticuloTenido = (RangoAnchoArticuloTenido)getDefinicion().getRango(getAnchoInicial(), getAnchoFinal(), getAnchoExacto());
		if(rangoAnchoArticuloTenido != null) {
			GrupoTipoArticuloGama grupo = rangoAnchoArticuloTenido.getGrupo(getTipoArticulo());
			if(grupo != null) {
				PrecioGama pg = grupo.getPrecioGama(gamaColorCliente);
				if(pg != null && (elemSiendoEditado == null  || elemSiendoEditado != pg)) {
					CLJOptionPane.showErrorMessage(this, "Ya existe un precio para la gama " + gamaColorCliente.getNombre(), "Error");
					getTxtPrecio().requestFocus();
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void setModoEdicionExtended(boolean modoEdicion) {
		GuiUtil.setEstadoPanel(createPanelDatosEspecificos(), modoEdicion);
		this.editable = modoEdicion;
	}

	@Override
	protected void limpiarDatosExtended() {
		getCmbGama().setSelectedIndex(-1);
	}

	@Override
	public RangoAnchoArticuloTenido getRangoAnchoFromElemSiendoEditado() {
		if(elemSiendoEditado != null) {
			elemSiendoEditado.getGrupoTipoArticuloGama().getRangoAnchoArticuloTenido();
		}
		return null;
	}

	public JButton getBtnAgregarTodos() {
		if(btnAgregarTodos == null){
			btnAgregarTodos = new JButton("Agregar todas las gamas");
			btnAgregarTodos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for(int i = 0; i < getCmbGama().getItemCount(); i++) {
						GamaColorCliente gcc = (GamaColorCliente) getCmbGama().getItemAt(i);
						if(validarDatosComunes(false)){
							if(validarGama(gcc)) {
								try{
									Float precio = ingresarPrecio(gcc);
									agregarInterno(gcc, precio, true);
								}catch(Exception ex){
									break;
								}
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			});
		}
		return btnAgregarTodos;
	}

	private Float ingresarPrecio(GamaColorCliente gcc) throws Exception {
		boolean okValidez = false;
		String inputValidez = "";
		do {
			if(!okValidez) {
				inputValidez = JOptionPane.showInputDialog(JDialogAgregarModificarDefinicionPreciosTenido.this, "Precio para " + gcc.getNombre() + ": ", "Ingrese el precio", JOptionPane.INFORMATION_MESSAGE);
				if(inputValidez == null){
					break;
				}
				if(inputValidez.trim().length()==0 || !GenericUtils.esNumerico(inputValidez)) {
					CLJOptionPane.showErrorMessage(JDialogAgregarModificarDefinicionPreciosTenido.this, "Ingreso incorrecto", "error");
				} else {
					okValidez = true;
					break;
				}
			}
		} while (!okValidez);
		
		if(!okValidez) {
			throw new Exception();
		}
		Float precio = null;
		try {
			precio = new Float(inputValidez.replace(",", "."));
		} catch (NumberFormatException ex) {
			precio = new Float(0f);
		}
		return NumUtil.redondearDecimales(precio, 2, BigDecimal.ROUND_UP);
	}
}