package ar.com.textillevel.gui.util.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

public class JFileChooserImage extends JFileChooser {
	private static final long serialVersionUID = -3165305075727358207L;
	private static File lastDirectory = null;

	public JFileChooserImage() {
		super();
		setAccessory(new LabelAccessory(this));
		setCurrentDirectory(lastDirectory);
	}

	@Override
	public int showDialog(Component parent, String approveButtonText)
			throws HeadlessException {
		int answ = super.showDialog(parent, approveButtonText);
		if (answ == JFileChooser.APPROVE_OPTION) {
			setLastDirectory(this);
		}
		return answ;
	}

	@Override
	public int showOpenDialog(Component parent) throws HeadlessException {
		int answ = super.showOpenDialog(parent);
		if (answ == JFileChooser.APPROVE_OPTION) {
			setLastDirectory(this);
		}
		return answ;
	}

	@Override
	public int showSaveDialog(Component parent) throws HeadlessException {
		int answ = super.showSaveDialog(parent);
		if (answ == JFileChooser.APPROVE_OPTION) {
			setLastDirectory(this);
		}
		return answ;

	}

	public void addFilter(String filterName, String[] extensions) {
		FileFilter filter = new ExtensionFileFilter(filterName, extensions);
		addChoosableFileFilter(filter);
	}

	private class ExtensionFileFilter extends FileFilter {
		String description;

		String extensions[];

		@SuppressWarnings("unused")
		public ExtensionFileFilter(String description, String extension) {
			this(description, new String[] { extension });
		}

		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0] + "{" + extensions.length
						+ "}";
			} else {
				this.description = description;
			}
			this.extensions = extensions.clone();
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length()
							- extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}

	}

	private class LabelAccessory extends JLabel implements
			PropertyChangeListener {
		private static final long serialVersionUID = -6585588928773654200L;
		private static final int PREFERRED_WIDTH = 125;
		private static final int PREFERRED_HEIGHT = 100;

		public LabelAccessory(JFileChooser chooser) {
			setVerticalAlignment(JLabel.CENTER);
			setHorizontalAlignment(JLabel.CENTER);
			chooser.addPropertyChangeListener(this);
			setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		}

		public void propertyChange(PropertyChangeEvent changeEvent) {
			String changeName = changeEvent.getPropertyName();
			if (changeName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
				File file = (File) changeEvent.getNewValue();
				if (file != null) {
					ImageIcon icon = new ImageIcon(file.getPath());
					if (icon.getIconWidth() > PREFERRED_WIDTH) {
						icon = new ImageIcon(icon.getImage().getScaledInstance(
								PREFERRED_WIDTH, -1, Image.SCALE_DEFAULT));
						if (icon.getIconHeight() > PREFERRED_HEIGHT) {
							icon = new ImageIcon(icon.getImage()
									.getScaledInstance(-1, PREFERRED_HEIGHT,
											Image.SCALE_DEFAULT));
						}
					}
					setIcon(icon);
				}
			}

		}
	}

	private static void setLastDirectory(JFileChooser chooser) {
		if (chooser.getCurrentDirectory() != null)
			lastDirectory = chooser.getCurrentDirectory();
	}
}
