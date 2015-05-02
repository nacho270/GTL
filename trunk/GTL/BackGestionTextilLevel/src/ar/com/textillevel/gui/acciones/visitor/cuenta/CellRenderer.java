package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CellRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = -2160320938562912964L;

	private Map<Integer, String> mapaFilaTexto = new HashMap<Integer, String>();

	public CellRenderer() {

	}

	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		String insert = mapaFilaTexto.get(row) == null ? "" : mapaFilaTexto.get(row);
		setText("<html>" + insert + "</html>");
		return this;
	}

	public void agregarTexto(int fila, String texto) {
		String add = mapaFilaTexto.get(fila);
		if (add == null) {
			add = "";
		}
		add = add.concat(texto);
		mapaFilaTexto.put(fila, add);
	}

	public void clear() {
		mapaFilaTexto.clear();
	}

}
