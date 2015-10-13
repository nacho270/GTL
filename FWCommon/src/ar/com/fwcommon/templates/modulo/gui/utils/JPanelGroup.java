package ar.com.fwcommon.templates.modulo.gui.utils;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class JPanelGroup extends JPanel {
	private static final long serialVersionUID = 3346650594194324022L;
	private static final int DEFAULT_HORIZONTAL_GAP = 5;
	private static final int DEFAULT_VERTICAL_GAP = 5;
	private GridLayout layout;
	private int rows = 1;
	
	public JPanelGroup() {
		super();
		layout = new GridLayout();
		layout.setHgap(DEFAULT_HORIZONTAL_GAP);
		layout.setVgap(DEFAULT_VERTICAL_GAP);
		this.setLayout(layout);
	}

	public GridLayout getLayout() {
		return layout;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.layout.setRows(rows);
		this.layout.setColumns((this.getComponentCount()+rows-1)/rows);
		this.validate();
	}
}
