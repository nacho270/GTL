package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.ToStringComparator;

@SuppressWarnings("unused")
public class TestDobleCombo extends JFrame {

	private static final long serialVersionUID = 4786574211230032006L;

	private List<Maquina> maquinas;
	private Tabla panelTabla;
	
	public TestDobleCombo() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(getPanelTabla(),BorderLayout.CENTER);
		pack();
	}

	private class Maquina {
		private String nombre;
		private List<Proceso> procesos;
		
		public Maquina() {
			procesos = new ArrayList<TestDobleCombo.Proceso>();
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
		
		
		public String toString(){
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
		
		public String toString(){
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

	private class MaquinasCellRenderer extends JComboBox  implements TableCellRenderer{

		private static final long serialVersionUID = 518223823900522222L;

		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			removeAllItems();
			for(Maquina posicion : getMaquinas())
				addItem(posicion);
			return this;
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
		
		public void addItemListener(ItemListener l) {
			listenerList.add(ItemListener.class, l);
		}

		public void removeItemListener(ItemListener l) {
			listenerList.remove(ItemListener.class, l);
		}

		protected final void fireItemListener(final ItemEvent e) {
			final ItemListener[] listeners = listenerList.getListeners(ItemListener.class);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for(int i = 0; i < listeners.length; i++) {
						try {
							listeners[i].itemStateChanged(e);
						} catch(RuntimeException e) {
							BossError.gestionarError(e);
						}
					}
				}
			});
		}
	}
	
//	private class ProcesosCellEditor extends AbstractCellEditor implements TableCellEditor{
//
//		private static final long serialVersionUID = 6341624019843675075L;
//
//		
//		public Object getCellEditorValue() {
//			return null;
//		}
//
//		
//		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//			return null;
//		}
//	}
	
	private class Tabla extends PanelTabla<Renglon>{

		private static final long serialVersionUID = -7030812202700359519L;

		private static final int CANT_COLS = 3;
		private static final int COL_MAQUINA = 0;
		private static final int COL_PROCESO = 1;
		private static final int COL_OBJ = 2;
		
		
		protected FWJTable construirTabla() {
			final MaquinasCellEditor maquinaCellEditor = new MaquinasCellEditor();
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setComboColumn(COL_MAQUINA, "Maquinas", new JComboBox(),100,false);
			tabla.setComboColumn(COL_PROCESO, "Proceso", new JComboBox(),100,false);
			tabla.setStringColumn(COL_OBJ, "", 0);
//			tabla.getColumnModel().getColumn(COL_MAQUINA).setCellRenderer(new MaquinasCellRenderer());
			tabla.getColumnModel().getColumn(COL_MAQUINA).setCellEditor(maquinaCellEditor);
//			tabla.getColumnModel().getColumn(COL_PROCESO).setCellEditor(new ProcesosCellEditor());
			
			return tabla;
		}

		
		protected void agregarElemento(Renglon elemento) {
			
		}

		
		protected Renglon getElemento(int fila) {
			return null;
		}

		
		protected String validarElemento(int fila) {
			return null;
		}
	}

	public List<Maquina> getMaquinas() {
		if(maquinas == null){
			maquinas = new ArrayList<Maquina>();
			for (int i = 0; i < 5; i++) {
				Maquina m = new Maquina();
				m.setNombre("Maquina "+(i+1));
				for(int j = 0;j<3;j++){
					Proceso p = new Proceso();
					p.setNombre("Proceso " + (j+1)+" - Maquina " +(i+1));
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
		if(panelTabla == null){
			panelTabla = new Tabla();
		}
		return panelTabla;
	}

	public static void main(String[] args) {
		new TestDobleCombo().setVisible(true);
	}
}
