package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
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
				FWJOptionPane.showWarningMessage(this, "El cliente no cuenta con gamas definidas. Debe ingresarlas.", "Advertencia");
				JDialogAgregarModificarGamaColorCliente d = new JDialogAgregarModificarGamaColorCliente(this, getCliente());
				d.setVisible(true);
				if (d.isAcepto()) {
					gamas = getGamaClienteFacade().getByCliente(getCliente().getId());
				} else {
					FWJOptionPane.showWarningMessage(this, StringW.wordWrap("No se puede dar de alta la lista de precios para teñido si tener definidas las gamas cliente."), "Advertencia");
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
		getCmbArticulo().setSelectedItem(grupoTipoArticuloBase.getArticulo());

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
		Articulo art = getArticulo();
		GrupoTipoArticuloGama grupo = rango.getPrecioArticulo(ta, art, GrupoTipoArticuloGama.class);
		if(grupo == null || (grupo.getArticulo() == null && art != null)) {
			grupo = new GrupoTipoArticuloGama();
			grupo.setTipoArticulo(ta);
			grupo.setArticulo(art);
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
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar una 'Gama'.", "Error");
			return false;
		}
		RangoAnchoArticuloTenido rangoAnchoArticuloTenido = (RangoAnchoArticuloTenido)getDefinicion().getRango(getAnchoInicial(), getAnchoFinal(), getAnchoExacto());
		if(rangoAnchoArticuloTenido != null) {
			Articulo art = getArticulo();
			GrupoTipoArticuloGama grupo = rangoAnchoArticuloTenido.getPrecioArticulo(getTipoArticulo(),art, GrupoTipoArticuloGama.class);
			if(grupo != null && ( (grupo.getArticulo() == null && art == null) || (grupo.getArticulo() != null && art != null)) ) {
				PrecioGama pg = grupo.getPrecioGama(gamaColorCliente);
				if(pg != null && (elemSiendoEditado == null  || !elemSiendoEditado.equals(pg))) {
					FWJOptionPane.showErrorMessage(this, "Ya existe un precio para la gama " + gamaColorCliente.getNombre(), "Error");
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
								}catch(SaltarException ex){
									continue;
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

	private Float ingresarPrecio(GamaColorCliente gcc) throws SaltarException, Exception {
		boolean okValidez = false;
		String inputPrecio = "";
		do {
			if(!okValidez) {
				//inputPrecio = JOptionPane.showInputDialog(JDialogAgregarModificarDefinicionPreciosTenido.this, "Precio para " + gcc.getNombre() + ": ", "Ingrese el precio", JOptionPane.INFORMATION_MESSAGE);
				JDialogInputPrecio dialogInput = new JDialogInputPrecio("Precio para " + gcc.getNombre() + ": ");
				dialogInput.setVisible(true);
				inputPrecio = dialogInput.getPrecioInput();
				if(inputPrecio == null){
					break;
				}
				if(inputPrecio.trim().length()==0 || !GenericUtils.esNumerico(inputPrecio)) {
					FWJOptionPane.showErrorMessage(JDialogAgregarModificarDefinicionPreciosTenido.this, "Ingreso incorrecto", "error");
				} else {
					boolean okPrecio = validarPrecio(inputPrecio);
					if (okPrecio) {
						okValidez = true;
						break;
					}
				}
			}
		} while (!okValidez);
		
		if(!okValidez) {
			throw new Exception();
		}
		try {
			return new Float(GenericUtils.getDecimalFormat().format(new Float(inputPrecio.replace(",", "."))).replace(",", "."));
		} catch (NumberFormatException ex) {
			return new Float(0f);
		}
	}
	
	public class SaltarException extends Exception {

		private static final long serialVersionUID = 5248117900571514497L;

	}

	private class JDialogInputPrecio extends JDialog {

		private static final long serialVersionUID = 1L;

		private JButton btnAceptar;
		private JButton btnCancelar;
		private JButton btnSaltar;
		private FWJTextField txtNumberInput;
		private JPanel pnlBotones;
		private String precioInput;
		private String txtIngreso;
		private boolean throwException = false;

		public JDialogInputPrecio(String txtIngreso) {
			super(JDialogAgregarModificarDefinicionPreciosTenido.this);
			this.txtIngreso = txtIngreso;
			setAcepto(false);
			setUpComponentes();
			setUpScreen();
		}

		private void setUpScreen() {
			setTitle("Ingrese el precio");
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setSize(new Dimension(350, 92));
			setResizable(false);
			setModal(true);
			GuiUtil.centrar(this);
		}

		private void setUpComponentes() {
			add(getPanelCarga(), BorderLayout.CENTER);
			add(getPanelBotones(), BorderLayout.SOUTH);
		}

		private JPanel getPanelCarga() {
			JPanel pnl = new JPanel();
			pnl.setLayout(new GridBagLayout());
			pnl.add(new JLabel(txtIngreso), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			pnl.add(getTxtNumberInput(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			return pnl;
		}

		private JPanel getPanelBotones() {
			if (pnlBotones == null) {
				pnlBotones = new JPanel();
				pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
				pnlBotones.add(getBtnAceptar());
				pnlBotones.add(getBtnCancelar());
				pnlBotones.add(getBtnSaltar());
			}
			return pnlBotones;
		}

		private JButton getBtnAceptar() {
			if (btnAceptar == null) {
				btnAceptar = new JButton("Aceptar");
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						precioInput = getTxtNumberInput().getText();
						dispose();
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
						precioInput = null;
						dispose();
					}
				});
			}
			return btnCancelar;
		}

		public JButton getBtnSaltar() {
			if (btnSaltar == null) {
				btnSaltar = new JButton("Saltar");
				btnSaltar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						precioInput = null;
						throwException = true;
						dispose();
					}
				});
			}
			return btnSaltar;
		}

		private FWJTextField getTxtNumberInput() {
			if (txtNumberInput == null) {
				txtNumberInput = new FWJTextField();
				txtNumberInput.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							getBtnAceptar().doClick();
						}
					}

				});
			}
			return txtNumberInput;
		}

		public String getPrecioInput() throws SaltarException {
			if (throwException) {
				throw new SaltarException();
			}
			return precioInput;
		}
	}
}