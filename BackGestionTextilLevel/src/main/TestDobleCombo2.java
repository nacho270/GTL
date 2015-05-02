package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.ToStringComparator;

@SuppressWarnings("unused")
public class TestDobleCombo2 extends JFrame {

	private static final long serialVersionUID = 4786574211230032006L;
	Map<String,TableCellEditor> editors = new HashMap<String, TableCellEditor>();
	private List<Maquina> maquinas;
	private Tabla panelTabla;

	public TestDobleCombo2(boolean ponerDatos) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		preapreEditors();
		add(getPanelTabla(), BorderLayout.CENTER);
		pack();
		if(ponerDatos){
			agregarRenglones();
		}
	}

	private void agregarRenglones() {
		Random rand = new Random();
		for(int i = 0;i<5;i++){
			Renglon r = new Renglon();
			Maquina maquina = getMaquinas().get(rand.nextInt(getMaquinas().size()));
			r.setMaquina(maquina);
			r.setProceso(maquina.getProcesos().get((rand.nextInt(maquina.getProcesos().size()))));
			getPanelTabla().agregarElemento(r);
		}
	}

	private void preapreEditors() {
		for(Maquina m : getMaquinas()){
			JComboBox cmb = new JComboBox(m.getProcesos().toArray());
			DefaultCellEditor dce1 = new DefaultCellEditor( cmb );
	        editors.put(m.getNombre(), dce1 );
		}
		JComboBox cmb = new JComboBox(getMaquinas().toArray());
		DefaultCellEditor dce1 = new DefaultCellEditor( cmb );
		editors.put("maquinas", dce1);
	}

	private class Tabla extends PanelTabla<Renglon> {

		private static final long serialVersionUID = -7030812202700359519L;

		private static final int CANT_COLS = 3;
		private static final int COL_MAQUINA = 0;
		private static final int COL_PROCESO = 1;
		private static final int COL_OBJ = 2;

		
		protected CLJTable construirTabla() {
			final MaquinasCellEditor maquinaCellEditor = new MaquinasCellEditor();
			CLJTable tabla = new CLJTable(0, CANT_COLS){
				private static final long serialVersionUID = -4298540110741639756L;
				
				public TableCellEditor getCellEditor(int row, int column) {
					int modelColumn = convertColumnIndexToModel( column );
	                if (modelColumn == 0)
	                	return editors.get("maquinas");
	                else
	                	return editors.get(((Maquina)getValueAt(row, 0)).getNombre());
				}
			};
			tabla.setComboColumn(COL_MAQUINA, "Maquinas", new JComboBox(),100,false);
			tabla.setStringColumn(COL_PROCESO, "Proceso", 100,100,false);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.getColumnModel().getColumn(COL_MAQUINA).setCellEditor(maquinaCellEditor);
			return tabla;
		}

		
		protected void agregarElemento(Renglon elemento) {
			getTabla().addRow(new Object[]{elemento.getMaquina(),elemento.getProceso()});
		}

		
		protected Renglon getElemento(int fila) {
			return null;
		}

		
		protected String validarElemento(int fila) {
			return null;
		}
	}

	public List<Maquina> getMaquinas() {
		if (maquinas == null) {
			maquinas = new ArrayList<Maquina>();
			for (int i = 0; i < 5; i++) {
				Maquina m = new Maquina();
				m.setNombre("Maquina " + (i + 1));
				for (int j = 0; j < 3; j++) {
					Proceso p = new Proceso();
					p.setNombre("Proceso " + (j + 1) + " - Maquina " + (i + 1));
					m.getProcesos().add(p);
				}
				maquinas.add(m);
			}
		}
		return maquinas;
	}

	public void setMaquinas(List<Maquina> maquinas) {
		this.maquinas = maquinas;
	}

	public Tabla getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new Tabla();
		}
		return panelTabla;
	}

	public static void main(String[] args) {
		new TestDobleCombo2(true).setVisible(true);
	}

	private class Maquina {
		private String nombre;
		private List<Proceso> procesos;

		public Maquina() {
			procesos = new ArrayList<TestDobleCombo2.Proceso>();
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public List<Proceso> getProcesos() {
			return procesos;
		}

		public void setProcesos(List<Proceso> procesos) {
			this.procesos = procesos;
		}

		
		public String toString() {
			return nombre;
		}
	}

	private class Proceso {
		private String nombre;

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		
		public String toString() {
			return nombre;
		}
	}

	private class Renglon {
		private Maquina maquina;
		private Proceso proceso;

		public Maquina getMaquina() {
			return maquina;
		}

		public void setMaquina(Maquina maquina) {
			this.maquina = maquina;
		}

		public Proceso getProceso() {
			return proceso;
		}

		public void setProceso(Proceso proceso) {
			this.proceso = proceso;
		}
	}

	private class MaquinasCellEditor extends AbstractCellEditor implements TableCellEditor{

		private static final long serialVersionUID = 7358306301990913834L;

		private final JComboBox comboBox;
		
		public MaquinasCellEditor() {
			this.comboBox = new JComboBox();
			GuiUtil.llenarCombo(this.comboBox, getMaquinas(), true);
			
			this.comboBox.addFocusListener(new FocusListener() {

				public void focusLost(FocusEvent evt) {
					MaquinasCellEditor.this.stopCellEditing();
				}

				public void focusGained(FocusEvent evt) {
				}
			});
		}

		
		public Object getCellEditorValue() {
			return comboBox.getSelectedItem();
		}

		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			comboBox.removeAllItems();
//			if(value != null) {
//				comboBox.addItem(value);
//			}
			Collections.sort(getMaquinas(), new ToStringComparator<Maquina>());
			for(Maquina posicion : getMaquinas())
				comboBox.addItem(posicion);
			if(value == null) {
				comboBox.setSelectedItem(null);
			}
			return comboBox;
		}
	}
}
