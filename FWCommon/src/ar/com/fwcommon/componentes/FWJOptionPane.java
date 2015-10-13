package ar.com.fwcommon.componentes;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ar.com.fwcommon.boss.BossEstilos;
/**
 * Componente utilizado para mostrar mensajes emergentes en pantalla, como mensajes de
 * error, de advertencia, de información o de pregunta al usuario.
 */
public class FWJOptionPane {

	public static Font fuente;
	//Return value from class method if YES is chosen
	public static final int YES_OPTION = 0;
	//Return value from class method if NO is chosen
	public static final int NO_OPTION = 1;
	//Return value from class method if CANCEL is chosen
	public static final int CANCEL_OPTION = 2;
	//Return value form class method if OK is chosen
	public static final int OK_OPTION = 0;
	//Return value from class method if CLOSED is chosen
	public static final int CLOSED_OPTION = -1;

	//Types
	public static final int ERROR_MSG = JOptionPane.ERROR_MESSAGE;
	public static final int INFO_MSG = JOptionPane.INFORMATION_MESSAGE;
	public static final int WARNING_MSG = JOptionPane.WARNING_MESSAGE;
	public static final int QUESTION_MSG = JOptionPane.QUESTION_MESSAGE;

	/**
	 * Muestra un mensaje de <b>error</b> en un cuadro de diálogo.
	 * @param owner La ventana dueña del cuadro de diálogo.
	 * @param message El texto del mensaje.
	 * @param title El título del mensaje.
	 */
	public static void showErrorMessage(Component owner, String message, String title) {
		show(owner, title, message, JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION);
	}

	/**
	 * Muestra un mensaje de <b>advertencia</b> en un cuadro de diálogo.
	 * @param owner La ventana dueña del cuadro de diálogo.
	 * @param message El texto del mensaje.
	 * @param title El título del mensaje.
	 */
	public static void showWarningMessage(Component owner, String message, String title) {
		show(owner, title, message, JOptionPane.WARNING_MESSAGE, JOptionPane.CLOSED_OPTION);
	}

	/**
	 * Muestra un mensaje de <b>pregunta al usuario</b> en un cuadro de diálogo.
	 * Devuelve la respuesta seleccionada por el usuario.
	 * @param owner La ventana dueña del cuadro de diálogo.
	 * @param message El texto del mensaje.
	 * @param title El título del mensaje.
	 * @return CLJOptionPane.YES_OPTION/CLJOptionPane.NO_OPTION/
	 *         CLJOptionPane.CANCEL_OPTION/CLJOptionPane.OK_OPTION/
	 *         CLJOptionPane.CLOSED_OPTION
	 */
	public static int showQuestionMessage(Component owner, String message, String title) {
		return show(owner, title, message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
	}

	/**
	 * Muestra un mensaje de <b>pregunta al usuario</b> en un cuadro de diálogo con opciones SI/NO/CANCELAR
	 * Devuelve la respuesta seleccionada por el usuario.
	 * @param owner La ventana dueña del cuadro de diálogo.
	 * @param message El texto del mensaje.
	 * @param title El título del mensaje.
	 * @return CLJOptionPane.YES_OPTION/CLJOptionPane.NO_OPTION/
	 *         CLJOptionPane.CANCEL_OPTION/CLJOptionPane.OK_OPTION/
	 *         CLJOptionPane.CLOSED_OPTION
	 */
	public static int showQuestionMessageWithCancelOption(Component owner, String message, String title) {
		return show(owner, title, message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
	}

	
	/**
	 * Muestra un mensaje de <b>información</b> en un cuadro de diálogo.
	 * @param owner La ventana dueña del cuadro de diálogo.
	 * @param message El texto del mensaje.
	 * @param title El título del mensaje.
	 */
	public static void showInformationMessage(Component owner, String message, String title) {
		show(owner, title, message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.CLOSED_OPTION);
	}

	private static int show(Component owner, String title, String message, int messageType, int optionType) {
		JOptionPane pane = new JOptionPane(message, messageType, optionType);
		decorate(pane);
		JDialog dialog = pane.createDialog(JOptionPane.getFrameForComponent(owner), title);
		dialog.removeNotify();
		dialog.addWindowListener(new DialogListener(pane));
		dialog.pack();
		dialog.setVisible(true);
		if(pane.getValue() == null) {
			return -1;
		}
		return ((Integer)pane.getValue()).intValue();
	}

	private static void decorate(JComponent component) {
		Component[] components = component.getComponents();
		for(Component c : components) {
			if(c instanceof JPanel) {
				decorate((JComponent)c);
			} else {
				if(c instanceof JLabel) {
					c.setFont(BossEstilos.getSecondaryFont());
				} else if(c instanceof JButton) {
					JButton b = (JButton)c;
					b.setMargin(new Insets(4, 10, 4, 10));
					b.setFocusPainted(false);
				}
			}
		}
	}

	static class DialogListener extends WindowAdapter {
		JOptionPane pane;

		public DialogListener(JOptionPane pane) {
			this.pane = pane;
		}

		public void windowClosing(WindowEvent evt) {
			if(pane.getMessageType() == JOptionPane.QUESTION_MESSAGE) {
				pane.setValue(JOptionPane.NO_OPTION);
			}
		}
	}

}