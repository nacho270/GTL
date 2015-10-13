package ar.com.fwcommon.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import ar.com.fwcommon.templates.main.AbstractMainTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.ImageUtil;
/**
 * Componente que representa la ventana <b>Acerca De...</b> de una aplicación.
 * Ej.:
 * CLAboutScreen aboutScreen = new CLAboutScreen("C:/imagen.png", "Mi Aplicación", "Versión 1.0");
 * aboutScreen.setVisible(true);
 */
public class FWAboutScreen extends JWindow {

	private static final long serialVersionUID = -3578238552941824542L;

	private Icon imagen;
    private JLabel labelImagen;
    private JPanel panelTexto;
    private JLabel labelTexto;
    private JLabel labelVersion;
    private JLabel labelJavaVersion;
    private String texto;
    private String version;
    private Font font;

    /**
     * Método constructor.
     * @param imagen La imágen que mostrará la ventana.
     */
    public FWAboutScreen(String imagen) {
    	super(AbstractMainTemplate.getFrameInstance());
        this.imagen = ImageUtil.loadIcon(imagen);
        construct();
    }

    /**
     * Método constructor.
     * @param imagen La imágen que mostrará la ventana.
     * @param texto El texto que mostrará la ventana.
     * @param version El nro. de versión de la aplicación.
     */
    public FWAboutScreen(String imagen, String texto, String version) {
    	super(AbstractMainTemplate.getFrameInstance());
    	if(imagen != null) {
    		this.imagen = ImageUtil.loadIcon(imagen);
    	}
		this.texto = texto;
		this.version = version;
		construct();
    }

	//Construye el componente
    private void construct() {
		JPanel cp = new JPanel();
		setContentPane(cp);
		cp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		cp.setLayout(new BorderLayout());
		try {
			labelImagen = new JLabel(imagen);
		} catch(Exception e) {
			e.printStackTrace();
		}
		cp.add(labelImagen, BorderLayout.CENTER);

        panelTexto = new JPanel(new BorderLayout(0, 0));
        cp.add(panelTexto, BorderLayout.SOUTH);
		//Text
		labelTexto = new JLabel(texto);
		labelTexto.setHorizontalAlignment(JLabel.CENTER);
		panelTexto.add(labelTexto, BorderLayout.NORTH);

		//Version
		labelVersion = new JLabel(version);
		labelVersion.setHorizontalAlignment(JLabel.CENTER);
		panelTexto.add(labelVersion, BorderLayout.CENTER);

		addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                FWAboutScreen.this.dispose();
            }
		});
        labelJavaVersion = new JLabel("");
        labelJavaVersion.setHorizontalAlignment(JLabel.CENTER);
        panelTexto.add(labelJavaVersion, BorderLayout.SOUTH);
		pack();
		GuiUtil.centrar(this);
    }

	/**
	 * Retorna la <b>imágen</b> de la ventana.
	 * @return image
	 */
	public Icon getImagen() {
		return imagen;
	}

	/**
	 * Retorna el <b>texto</b> de la ventana.
	 * @return text
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Retorna la <b>versión</b> de la aplicación mostrada en la ventana.
	 * @return version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Setea la <b>imágen</b> de la ventana.
	 * @param imagen
	 */
	public void setImage(String imagen) {
		this.imagen = ImageUtil.loadIcon(imagen);
        labelImagen.setIcon(this.imagen);
	}

	/**
	 * Setea el <b>texto</b> de la ventana.
	 * @param texto
	 */
	public void setTexto(String texto) {
		this.texto = texto;
        labelTexto.setText(texto);
	}

	/**
	 * Setea la <b>versión</b> de la aplicación en la ventana.
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
        labelVersion.setText(version);
	}

	/**
	 * Devuelve la tipografía seteada en la ventana.
	 * @return fuente
	 */
	public Font getFont() {
	    return font;
	}

	/**
	 * Setea la tipografía de la ventana.
	 * @param fuente
	 */
	public void setFont(Font font) {
	    this.font = font;
	    labelTexto.setFont(font);
	    labelVersion.setFont(font);
        labelJavaVersion.setFont(font);
	}

    /**
     * Agrega al texto la versión de Java. 
     */
    public void displayJavaVersion() {
        labelJavaVersion.setText("Java " + System.getProperty("java.version"));
        pack();
    }

}