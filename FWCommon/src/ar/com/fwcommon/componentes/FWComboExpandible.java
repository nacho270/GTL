package ar.com.fwcommon.componentes;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;

public class FWComboExpandible extends JComboBox {

	public FWComboExpandible() {
	}

	public FWComboExpandible(final Object items[]) {
		super(items);
	}

	public FWComboExpandible(Vector items) {
		super(items);
	}

	public FWComboExpandible(ComboBoxModel aModel) {
		super(aModel);
	}

	private boolean layingOut = false;

	public void doLayout() {
		try {
			layingOut = true;
			super.doLayout();
		} finally {
			layingOut = false;
		}
	}

	public Dimension getSize() {
		Dimension dim = super.getSize();
		if (!layingOut) {
			Font defaultFont = UIManager.getDefaults().getFont("TextPane.font");
			int defaultSize = defaultFont.getSize();
			int maxLen = Integer.MIN_VALUE;
			for (int i = 0; i < getItemCount(); i++) {
				int itemLen = getItemAt(i).toString().length();
				if (itemLen >= maxLen) {
					maxLen = itemLen;
				}
			}
			dim.width = Math.max(Math.min(800, defaultSize * maxLen / 2), getPreferredSize().width);
		}
		return dim;
	}
}