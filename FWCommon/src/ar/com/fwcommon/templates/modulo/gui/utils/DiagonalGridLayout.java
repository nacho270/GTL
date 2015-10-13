package ar.com.fwcommon.templates.modulo.gui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 *
 */
public class DiagonalGridLayout implements LayoutManager2, Serializable {
	private static final long serialVersionUID = -3723846173971173532L;
	private int rows;
	private int[] cols;
	private Dimension minimum;
	private Dimension preferred;
	private Dimension maximum;
	
	public DiagonalGridLayout() {
		super();
		setRows(1);
	}

	public DiagonalGridLayout(int rows) {
		super();
		setRows(rows);
	}

	/**
	 * Devuelve la cantidad de filas en las que se distribuirán los componentes
	 * @return Cantidad de filas en las que se distribuirán los componentes
	 */
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.cols = new int[rows];
	}

	/* (non-Javadoc)
	 * java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
	 */
	public void addLayoutComponent(Component comp, Object constraints) {}

	/* (non-Javadoc)
	 * java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
	 */
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	/* (non-Javadoc)
	 * java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
	 */
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	/* (non-Javadoc)
	 * java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
	 */
	public synchronized void invalidateLayout(Container target) {
		this.minimum = null;
		this.preferred = null;
		this.maximum = null;		
	}

	/* (non-Javadoc)
	 * java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
	 */
	public void addLayoutComponent(String name, Component comp) {}

	/* (non-Javadoc)
	 * java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	public synchronized void layoutContainer(Container parent) {
		Component[] components = parent.getComponents();
		Rectangle[] positions = new Rectangle[components.length];
		minimum = new Dimension();
		preferred = new Dimension();
		maximum = new Dimension();
		Arrays.fill(cols, 0);
		for (int i=0; i<components.length; i++) {
			Component comp = components[i];
			int cant = 1;
			if (comp instanceof JPanelGroup) {
				cant = ((JPanelGroup)comp).getComponentCount();
			}
			boolean placed = false;
			for (int startRow = rows-1; startRow>=0 && !placed; startRow--) {
				for (int height = Math.min(cant, rows-startRow); height > 0 && !placed; height--) {
					//Verifico que el cuadrado tenga a lo sumo un espacio vacio
					if (cant%height == 0 || cant%height == height-1) {
						int width = (cant+height-1)/height;
						//Verifico la regla de la diagonal
						if (startRow == 0 || cols[startRow]+width <= cols[startRow-1]) {
							//Verifico que no me quede ningún hueco fuera de un grupo
							boolean allEquals = true;
							for (int j=startRow+1; j<startRow+height && allEquals; j++)
								allEquals = cols[startRow] == cols[j];
							if (allEquals) {
								//YES!!!!!!!
								int col = cols[startRow];
								if (comp instanceof JPanelGroup) ((JPanelGroup)comp).setRows(height);
								positions[i] = new Rectangle(col, startRow, width, height);

								minimum.height = Math.max(minimum.height, Math.abs(comp.getMinimumSize().height/height));
								minimum.width = Math.max(minimum.width, Math.abs(comp.getMinimumSize().width/width));
								preferred.height = Math.max(preferred.height, Math.abs(comp.getPreferredSize().height/height));
								preferred.width = Math.max(preferred.width, Math.abs(comp.getPreferredSize().width/width));
								maximum.height = Math.max(maximum.height, Math.abs(comp.getMaximumSize().height/height));
								maximum.width = Math.max(maximum.width, Math.abs(comp.getMaximumSize().width/width));

								//Actualizo las cosas
								for (int j=startRow; j<startRow+height; j++)
									cols[j] += width;

								placed = true;
							}
						}
					}
				}
			}
			if (!placed) System.err.println("No se pudo colocar el componente");
		}

		minimum.width  += 5;
		minimum.height += 1;
		preferred.width  += 5;
		preferred.height += 1;
		maximum.width  += 5;
		maximum.height += 1;
		
		for (int i=0; i<components.length; i++) {
			Component comp = components[i];
			Rectangle rect = positions[i];
			rect.x *= preferred.width;
			rect.width *= preferred.width;
			rect.y *= preferred.height;
			rect.height *= preferred.height;
			comp.setBounds(rect);
		}
		
		minimum.width  *= cols[0];
		minimum.height *= rows;
		preferred.width  *= cols[0];
		preferred.height *= rows;
		maximum.width  *= cols[0];
		maximum.height *= rows;
	}

	/* (non-Javadoc)
	 * java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	public Dimension minimumLayoutSize(Container parent) {
		while (minimum == null) {
			layoutContainer(parent);
		}
		return minimum;
	}

	/* (non-Javadoc)
	 * java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	public Dimension preferredLayoutSize(Container parent) {
		while (preferred == null) {
			layoutContainer(parent);
		}
		return preferred;
	}

	/* (non-Javadoc)
	 * java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 */
	public Dimension maximumLayoutSize(Container target) {
		while (maximum == null) {
			layoutContainer(target);
		}
		return maximum;
	}
	
	/* (non-Javadoc)
	 * java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(Component comp) {}

}
