package ar.com.fwcommon.templates.modulo.gui.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import com.l2fprod.common.swing.JCollapsiblePane;

/**
 * Panel que permite contraerse y expandirse utilizando el botón que tiene en el
 * borde.<br>
 * <b>No se deben agregar componentes directamente a este panel, ni tampoco
 * cambiar el borde</b>. Para agregar componentes debe hacerse lo siguiente:
 * <pre>
 * JHideablePanel panel = new JHideablePanel();
 * panel.getContentPane().add(new JLabel("Mi Label"));
 * ...
 * </pre>
 * <p>
 * Requiere <b><code>l2fprod-common-all.jar</code></b>. Disponible en <a
 * href="http://common.l2fprod.com/">http://common.l2fprod.com/</a>
 * 
 */
public class JHideablePanel extends JPanel {

	private static final long serialVersionUID = -8166539361197111475L;
	private String title = "";
	private JPanel contentPane;
	private JCollapsiblePane collapsible;
	private CollapseButtonBorder buttonBorder;

	public JHideablePanel() {
		super();
		contentPane = new JPanel();
		construct();
	}

	public JHideablePanel(LayoutManager layout) {
		super();
		contentPane = new JPanel(layout);
		construct();
	}

	private void construct() {
		super.setLayout(new BorderLayout());
		this.buttonBorder = new CollapseButtonBorder(BorderFactory.createTitledBorder(getTitle()));
		this.setBorder(buttonBorder);
		this.collapsible = new JCollapsiblePane();
		super.add(collapsible, BorderLayout.CENTER);
		this.collapsible.getContentPane().add(contentPane);
		this.buttonBorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				collapsible.setCollapsed(!collapsible.isCollapsed());
			}
		});
	}

	/**
	 * Usar <code>{@link #getContentPane()}.add(Component, int)</code>
	 */
	@Deprecated
	@Override
	public Component add(Component comp, int index) {
		return super.add(comp, index);
	}

	/**
	 * Usar <code>{@link #getContentPane()}.add(Component, Object, int)</code>
	 */
	@Deprecated
	@Override
	public void add(Component comp, Object constraints, int index) {
		super.add(comp, constraints, index);
	}

	/**
	 * Usar <code>{@link #getContentPane()}.add(Component, Object)</code>
	 */
	@Deprecated
	@Override
	public void add(Component comp, Object constraints) {
		super.add(comp, constraints);
	}

	/**
	 * Usar <code>{@link #getContentPane()}.add(Component)</code>
	 */
	@Deprecated
	@Override
	public Component add(Component comp) {
		return super.add(comp);
	}

	/**
	 * Usar <code>{@link #getContentPane()}.add(String, Component)</code>
	 */
	@Deprecated
	@Override
	public Component add(String name, Component comp) {
		return super.add(name, comp);
	}

	/**
	 * Devuelve el panel al que hay que agregarle los componentes
	 * 
	 * @return Panel al que hay que agregarle los componentes
	 */
	public JPanel getContentPane() {
		return contentPane;
	}

	/**
	 * Establece si el panel se encuentra expandido o contraido
	 * 
	 * @param collapsed <code>true</code> si se encuentra contraido.
	 *            <code>false</code> si se encuentra expandido
	 */
	public void setCollapsed(boolean collapsed) {
		if(collapsible.isCollapsed() != collapsed) {
			buttonBorder.getIcon().setUp(collapsed);
			collapsible.setCollapsed(collapsed);
		}
	}

	/**
	 * Devuelve si el panel se encuentra expandido o contraido
	 * 
	 * @return <code>true</code> si se encuentra contraido.
	 *            <code>false</code> si se encuentra expandido
	 */
	public boolean isCollapsed() {
		return collapsible.isCollapsed();
	}

	/**
	 * Devuelve el titulo del {@link TitledBorder}
	 * @return Titulo del Borde
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Establece el título del {@link TitledBorder}
	 * @param title Titulo del Borde
	 */
	public void setTitle(String title) {
		this.title = title;
		buttonBorder.setBorder(BorderFactory.createTitledBorder(getTitle()));
		repaint();
	}

}