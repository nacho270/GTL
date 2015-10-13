package ar.com.fwcommon.util;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
public class DecorateUtil {

    public static void decorateButton(JButton button) {
        button.setMargin(new Insets(2, 5, 2, 5));
        button.setFocusPainted(false);
    }

    public static void decorateButton(JButton button, String enabledIcon, String disabledIcon) {
        button.setContentAreaFilled(false);
        button.setBorder(null);
        Icon icon = ImageUtil.loadIcon(enabledIcon);
        button.setIcon(icon);
        button.setDisabledIcon(ImageUtil.loadIcon(disabledIcon));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }

    public static void decorateButton(JButton button, String icon) {
        decorateButton(button, icon, icon);
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        decorateButton(button);
        return button;
    }

    public static JButton createButton(String enabledIcon, String disabledIcon) {
        JButton button = new JButton();
        decorateButton(button, enabledIcon, disabledIcon);
        return button;
    }

    public static void decorateCheckBox(JCheckBox checkBox, boolean selected) {
        checkBox.setFocusPainted(false);
        checkBox.setSelected(selected);
    }

    public static void decorateCheckBox(JCheckBox checkBox) {
        decorateCheckBox(checkBox, false);
    }

    public static void decorateRadioButton(JRadioButton radioButton, boolean selected) {
        radioButton.setFocusPainted(false);
        radioButton.setSelected(selected);
    }

    public static void decorateRadioButton(JRadioButton radioButton) {
        decorateRadioButton(radioButton, false);
    }

}