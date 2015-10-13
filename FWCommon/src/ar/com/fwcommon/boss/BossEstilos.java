package ar.com.fwcommon.boss;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;

import ar.com.fwcommon.templates.main.skin.AbstractSkin;
import ar.com.fwcommon.util.DecorateUtil;
import ar.com.fwcommon.util.FileUtil;
import ar.com.fwcommon.util.ImageUtil;
import ar.com.fwcommon.util.MiscUtil;

public class BossEstilos {

	private static AbstractSkin[] skins;
	private static AbstractSkin defaultSkin;
	private static Font defaultFont = new Font("Dialog", Font.PLAIN, 10);
	private static Font secondaryFont = new Font("Dialog", Font.PLAIN, 10);

    public static AbstractSkin[] getSkins() {
    	return BossEstilos.skins;
    }

    public static void setSkins(AbstractSkin[] skins) {
    	BossEstilos.skins = skins;
    }

    public static AbstractSkin getDefaultSkin() {
    	return BossEstilos.defaultSkin;
    }

    public static void setDefaultSkin(AbstractSkin defaultSkin) {
    	BossEstilos.defaultSkin = defaultSkin;
    }

    public static Font getDefaultFont() {
        return BossEstilos.defaultFont;
    }

    public static void setDefaultFont(Font defaultFont) {
        BossEstilos.defaultFont = defaultFont;
    }

    public static void setDefaultFont(String defaultFontFile) throws FontFormatException, IOException {
    	BossEstilos.defaultFont = MiscUtil.crearFont(defaultFontFile, MiscUtil.TRUETYPE_FONT, MiscUtil.PLAIN_FONT_STYLE, MiscUtil.isMacOS() ? 9.0f : 10.0f);
    }

    public static Font getSecondaryFont() {
        return BossEstilos.secondaryFont;
    }

    public static void setSecondaryFont(Font secondaryFont) {
        BossEstilos.secondaryFont = secondaryFont;
    }

    public static void setSecondaryFont(String secondaryFontFile) throws FontFormatException, IOException {
		BossEstilos.secondaryFont = MiscUtil.crearFont(secondaryFontFile, MiscUtil.TRUETYPE_FONT, MiscUtil.PLAIN_FONT_STYLE, MiscUtil.isMacOS() ? 8.0f : 10.0f);
    }

    public static void ajustarFuenteComponentes() {
		BossEstilos.defaultFont.deriveFont(BossEstilos.defaultFont.getSize() - 1);
		BossEstilos.secondaryFont.deriveFont(BossEstilos.secondaryFont.getSize() - 2);
    	ajustarFuenteComponente("FormattedTextField", -2);
    	ajustarFuenteComponente("ComboBox", -2);
    }

    private static void ajustarFuenteComponente(String componente, int ajuste) {
    	componente += ".font";
    	UIDefaults uidefaults = UIManager.getDefaults();
    	Font fuenteOriginal = (Font)uidefaults.get(componente);
    	float tam = fuenteOriginal.getSize() + ajuste;
    	Font fuenteNueva = fuenteOriginal.deriveFont(tam); 
    	uidefaults.put(componente, fuenteNueva);
    }

    public static void decorateButton(JButton button) {
        DecorateUtil.decorateButton(button);
    }

    public static void decorateButton(JButton button, String enabledIcon, String disabledIcon) {
        DecorateUtil.decorateButton(button, enabledIcon, disabledIcon);
    }

    public static void decorateButton(JButton button, String icon) {
        DecorateUtil.decorateButton(button, icon, icon);
    }

    public static JButton createButton(String text) {
        return DecorateUtil.createButton(text);
    }

    public static JButton createButton(String enabledIcon, String disabledIcon) {
        return DecorateUtil.createButton(enabledIcon, disabledIcon);
    }

    public static void decorateCheckBox(JCheckBox checkBox, boolean selected) {
        DecorateUtil.decorateCheckBox(checkBox, selected);
    }

    public static void decorateCheckBox(JCheckBox checkBox) {
        DecorateUtil.decorateCheckBox(checkBox, false);
    }

    public static void decorateRadioButton(JRadioButton radioButton, boolean selected) {
        DecorateUtil.decorateRadioButton(radioButton, selected);
    }

    public static void decorateRadioButton(JRadioButton radioButton) {
        DecorateUtil.decorateRadioButton(radioButton, false);
    }

	/**
	 * Agrega un <b>tooltip</b> con formato HTML al componente.
	 * El formato es:
	 * <b>Título</b>
	 * Una descripción.
	 * <i>Una nota</i>
	 * @param c El objeto JComponent que mostrará el tooltip.
	 * @param titulo El <b>título</b> del tooltip.
	 * @param descripcion La <b>descripción<b> del tooltip.
	 * @param nota La <b>nota</b> del tooltip.
	 */
	public static void setHTMLTooltip(JComponent c, String titulo, String descripcion, String nota) {
	    c.setToolTipText("<HTML><B>" + titulo + "</B><BR>" + descripcion + "<BR><I>" + nota + "</I></HTML>");
	}

	/**
	 * Agrega un tooltip con una imágen y un texto a un componente.
	 * @param c El objeto JComponent que mostrará el tooltip.
	 * @param tipText El texto del tooltip.
	 * @param image La imágen del tooltip.
	 */
	public static void setHTMLTooltip(JComponent c, String tipText, String image) {
	    URL url = FileUtil.getResource(image);
	    c.setToolTipText("<HTML><IMG SRC=\"" + url.toString() + "\" HSPACE=\"5\">" + tipText + "</HTML>");
	}

	public static void setCursor(Component c, String icon) {
	    Image img = ((ImageIcon)ImageUtil.loadIcon(icon)).getImage();
	    Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0, 0), null);
	    c.setCursor(cursor);
	}

    public static JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
	    return label;
	}

    public static JLabel createLabel(String text, Border border) {
		JLabel label = new JLabel(text);
		label.setBorder(border);
	    return label;
	}

    public static JLabel createLabel(String text, int horizontalAlignment) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(horizontalAlignment);
	    return label;
	}

    public static JLabel createLabelFor(String text, Component c) {
        JLabel label = createLabel(text);
        label.setLabelFor(c);
        return label;
    }

    public static JCheckBox createCheckBox(String text) {
    	return createCheckBox(text, false);
    }

    public static JCheckBox createCheckBox(String text, boolean selected) {
    	JCheckBox checkBox = new JCheckBox(text, selected);
    	checkBox.setFocusPainted(false);
    	return checkBox;
    }

    public static JRadioButton createRadioButton(String text) {
    	return createRadioButton(text, false);
    }

    public static JRadioButton createRadioButton(String text, boolean selected) {
    	JRadioButton radioButton = new JRadioButton(text, selected);
    	radioButton.setFocusPainted(false);
    	return radioButton;
    }

}