package ar.com.textillevel.gui.acciones.stock;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.MovimientoStockFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogRealizarBajaStock extends JDialog {

	private static final long serialVersionUID = -1465497132508152012L;

	private static final int CANT_COLS_TABLA_PRECIO_MATERIA_PRIMA = 4;
	private static final int COL_DESCRIPCION = 0;
	private static final int COL_STOCK_ACTUAL = 1;
	private static final int COL_CANTIDAD_BAJA = 2;
	private static final int COL_OBJ = 3;
	
	private static final int CANT_COLS_TABLA_PROVEEDORES = 2;
	private static final int COL_NOMBRE_PROVEEDOR = 0;
	private static final int COL_OBJ_PROVEEDOR = 1;
	
	private PanelTablaBajaStock panelTablaBajaStock;
	private PanelTablaProveedor panelTablaProveedor;
	private JButton btnAceptar;
	private JButton btnSalir;
	
	private JPanel panelSur;
	private JPanel panelCentro;
	
	private List<Proveedor> proveedoresElegidos;
	private List<PrecioMateriaPrima> preciosMateriaPrimaBuscados;
	
	private PrecioMateriaPrimaFacadeRemote precioMateriaPrimaFacade;

	public JDialogRealizarBajaStock(Frame padre) {
		super(padre);
		this.proveedoresElegidos = new ArrayList<Proveedor>();
		this.preciosMateriaPrimaBuscados = new ArrayList<PrecioMateriaPrima>();
		setUpComponentes();
		setUpScreen();
	}
	
	private void setUpScreen() {
		setTitle("Realizar baja de stock");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(600, 700));
		setResizable(false);
		setModal(true);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelSur() {
		if(panelSur == null){
			panelSur = new JPanel();
			panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSalir());
			
		}
		return panelSur;
	}
	
	private JPanel getPanelCentro() {
		if(panelCentro == null){
			panelCentro = new JPanel();
			panelCentro.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 15, 15));
			panelCentro.add(getPanelTablaProveedor());
			panelCentro.add(getPanelTablaBajaStock());
		}
		return panelCentro;
	}

	private void guardar(){
		if(validar()){
			for(int i = 0; i< getPanelTablaBajaStock().getTabla().getRowCount();i++){
				Object valor = getPanelTablaBajaStock().getTabla().getValueAt(i, COL_CANTIDAD_BAJA);
				if(valor!=null){
					String strValor = String.valueOf(valor);
					if(strValor.trim().length()>0){
						Double cantBaja = Double.valueOf(strValor.replace(',', '.'));
						getPrecioMateriaPrimaFacade().actualizarStockPrecioMateriaPrima(new BigDecimal(cantBaja).multiply(new BigDecimal(-1d)), getPanelTablaBajaStock().getElemento(i).getId());
						GTLBeanFactory.getInstance().getBean2(MovimientoStockFacadeRemote.class).crearMovimientoResta( getPanelTablaBajaStock().getElemento(i), new BigDecimal(cantBaja), null);
					}
				}
			}
			FWJOptionPane.showInformationMessage(this, "La operación fue realizada con éxito", "Información");
		}
		getPanelTablaBajaStock().getTabla().removeAllRows();
		getPreciosMateriaPrimaBuscados().clear();
		buscarPreciosMateriaPrima();
	}

	private boolean validar() {
		for(int i = 0; i< getPanelTablaBajaStock().getTabla().getRowCount();i++){
			Object valor = getPanelTablaBajaStock().getTabla().getValueAt(i, COL_CANTIDAD_BAJA);
			if(valor!=null){
				String strValor = String.valueOf(valor);
				if(strValor.trim().length()>0){
					Double cantBaja = Double.valueOf(strValor.replace(',', '.'));
					Double stockActual = (Double)getPanelTablaBajaStock().getTabla().getValueAt(i, COL_STOCK_ACTUAL);
					if(cantBaja>stockActual){
						FWJOptionPane.showErrorMessage(this, "La cantidad a bajar mayor al stock en la fila " + (i+1), "Error");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void buscarPreciosMateriaPrima(){
		for(Proveedor p : getProveedoresElegidos()){
			List<PrecioMateriaPrima> lista = getPrecioMateriaPrimaFacade().getAllWithStockByProveedorOrderByMateriaPrima(p.getId());
			if(lista != null && !lista.isEmpty()){
				for(PrecioMateriaPrima pm : lista){
					if(!getPreciosMateriaPrimaBuscados().contains(pm)){
						getPreciosMateriaPrimaBuscados().add(pm);
					}
				}
			}
		}
		
		for(PrecioMateriaPrima pm : getPreciosMateriaPrimaBuscados()){
			if(!estaPrecioMateriaPrimaEnTabla(pm)){
				getPanelTablaBajaStock().agregarElemento(pm);
			}
		}
	}
	
	private boolean estaPrecioMateriaPrimaEnTabla(PrecioMateriaPrima pm){
		int rowCount = getPanelTablaBajaStock().getTabla().getRowCount();
		if(rowCount==0) return false;
		for(int i = 0; i<rowCount;i++){
			if(pm.equals(getPanelTablaBajaStock().getElemento(i))){
				return true;
			}
		}
		return false;
	}
	
	private void salir(){
		if(FWJOptionPane.showQuestionMessage(this, "Desea salir?", "Pregunta") == FWJOptionPane.YES_OPTION){
			dispose();
		}
	}

	private class PanelTablaBajaStock extends PanelTablaSinBotones<PrecioMateriaPrima>{

		private static final long serialVersionUID = -3950044407129994917L;
		
		public PanelTablaBajaStock() {
			setPreferredSize(new Dimension(570, 350));
		}


		@Override
		protected void agregarElemento(PrecioMateriaPrima elemento) {
			Object[] row = new Object[CANT_COLS_TABLA_PRECIO_MATERIA_PRIMA];
			row[COL_DESCRIPCION] = elemento.getPreciosProveedor().getProveedor().getNombreCorto() + " - " + elemento.getMateriaPrima().getDescripcion() + " - " + elemento.getAlias();
			row[COL_STOCK_ACTUAL] = elemento.getStockActual().doubleValue();
			row[COL_CANTIDAD_BAJA] = null;
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TABLA_PRECIO_MATERIA_PRIMA);
			tabla.setStringColumn(COL_DESCRIPCION, "Descripción", 330, 330, true);
			tabla.setFloatColumn(COL_STOCK_ACTUAL, "Stock actual", 90, true);
			tabla.setFloatColumn(COL_CANTIDAD_BAJA, "Cantidad a descontar", 120, false);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setAlignment(COL_DESCRIPCION, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_STOCK_ACTUAL, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_CANTIDAD_BAJA, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DESCRIPCION, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_STOCK_ACTUAL, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_CANTIDAD_BAJA, FWJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected PrecioMateriaPrima getElemento(int fila) {
			return (PrecioMateriaPrima)getTabla().getValueAt(fila, COL_OBJ) ;
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}
	
	private class PanelTablaProveedor extends PanelTabla<Proveedor> {

		private static final long serialVersionUID = -1203617593598895156L;

		public PanelTablaProveedor() {
			setPreferredSize(new Dimension(430, 180));
		}

		@Override
		protected void agregarElemento(Proveedor elemento) {
			Object[] row = new Object[CANT_COLS_TABLA_PROVEEDORES];
			row[COL_NOMBRE_PROVEEDOR] = elemento.getRazonSocial();
			row[COL_OBJ_PROVEEDOR] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TABLA_PROVEEDORES){

				private static final long serialVersionUID = 2174629054586617241L;
				
				@Override
				public void cellEdited(int cell, int row) {
					
				}
			};
			tabla.setStringColumn(COL_NOMBRE_PROVEEDOR, "Proveedor", 350, 350, true);
			tabla.setStringColumn(COL_OBJ_PROVEEDOR, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setAlignment(COL_NOMBRE_PROVEEDOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NOMBRE_PROVEEDOR, FWJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected Proveedor getElemento(int fila) {
			return (Proveedor)getTabla().getValueAt(fila, COL_OBJ_PROVEEDOR);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(null);
			GuiUtil.centrar(dialogSeleccionarProveedor);
			dialogSeleccionarProveedor.setVisible(true);
			Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
			if (proveedorElegido != null) {
				if(!getProveedoresElegidos().contains(proveedorElegido)){
					getProveedoresElegidos().add(proveedorElegido);
				}
				getTabla().removeAllRows();
				for(Proveedor p : getProveedoresElegidos()){
					agregarElemento(p);
				}
				buscarPreciosMateriaPrima();
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			Proveedor prov = getElemento(getTabla().getSelectedRow());
			getProveedoresElegidos().remove(prov);
			getTabla().removeRow(getTabla().getSelectedRow());
			List<Integer> listaBorrar = new ArrayList<Integer>();
			for(int i = 0; i<getPanelTablaBajaStock().getTabla().getRowCount();i++){
				if(i>=getPanelTablaBajaStock().getTabla().getRowCount()){
					break;
				}
				PrecioMateriaPrima pm = getPanelTablaBajaStock().getElemento(i);
				if(pm.getPreciosProveedor().getProveedor().equals(prov)){
					getPreciosMateriaPrimaBuscados().remove(pm);
					listaBorrar.add(i);
				}
			}
			int[] rows = new int[listaBorrar.size()];
			for(int i = 0; i<listaBorrar.size();i++){
				rows[i] = listaBorrar.get(i);
			}
			getPanelTablaBajaStock().getTabla().removeRows(rows);
			return false;
		}
	}
	
	private PanelTablaProveedor getPanelTablaProveedor() {
		if(panelTablaProveedor == null){
			panelTablaProveedor = new PanelTablaProveedor();
		}
		return panelTablaProveedor;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.setMnemonic(KeyEvent.VK_A);
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guardar();
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Salir");
			btnSalir.setMnemonic(KeyEvent.VK_S);
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}
	
	private PanelTablaBajaStock getPanelTablaBajaStock() {
		if(panelTablaBajaStock == null){
			panelTablaBajaStock = new PanelTablaBajaStock();
		}
		return panelTablaBajaStock;
	}

	
	public List<Proveedor> getProveedoresElegidos() {
		return proveedoresElegidos;
	}

	
	public void setProveedoresElegidos(List<Proveedor> proveedoresElegidos) {
		this.proveedoresElegidos = proveedoresElegidos;
	}
	
	private PrecioMateriaPrimaFacadeRemote getPrecioMateriaPrimaFacade() {
		if(precioMateriaPrimaFacade == null){
			precioMateriaPrimaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		}
		return precioMateriaPrimaFacade;
	}

	
	public List<PrecioMateriaPrima> getPreciosMateriaPrimaBuscados() {
		return preciosMateriaPrimaBuscados;
	}

	
	public void setPreciosMateriaPrimaBuscados(List<PrecioMateriaPrima> preciosMateriaPrimaBuscados) {
		this.preciosMateriaPrimaBuscados = preciosMateriaPrimaBuscados;
	}
}
