package ar.com.textillevel.gui.util.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.util.NumUtil;
import ar.clarin.fwjava.util.StringUtil;

/**
 * Componente para mostrar un n�mero tel�fonico manteniendo separado
 * el c�digo de area y el n�mero.  
 */
public class PanDatosTelefono extends JPanel {

	private static final long serialVersionUID = 2731301716903059350L;

	private CLJNumericTextField txtCodArea;
	private CLJNumericTextField txtNumero;
	private final String separator;
	private final String title;

	/**
	 * M�todo Constructor.
	 * @param title El titulo del n�mero tel�fonico (Celular, Tel�fono, Fax, etc.)
	 * @param separator El separador a utilizar al juntar c�digo de area y n�mero
	 */
	public PanDatosTelefono(String title, String separator) {
		this.title = title;
		this.separator = separator;
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 0, 5);
		add(new JLabel("COD. AREA:"), gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(getTxtCodArea(), gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 5, 0, 0);
		add(new JLabel("NUMERO:"), gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 0.7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(getTxtNumero(), gbc);
		setBorder(BorderFactory.createTitledBorder(title));
	}

	private CLJNumericTextField getTxtNumero() {
		if(txtNumero == null) {
			txtNumero = new CLJNumericTextField();
			txtNumero.setPreferredSize(new Dimension(50, 20));
		}
		return txtNumero;
	}

	private CLJNumericTextField getTxtCodArea() {
		if(txtCodArea == null) {
			txtCodArea = new CLJNumericTextField();
			txtCodArea.setPreferredSize(new Dimension(20, 20));
		}
		return txtCodArea;
	}

	/**
	 * Setea en el componente los datos de c�digo de area y el n�mero 
	 * los cuales se encuentran en <code>datosStr</code> separados por 
	 * <code>separator</code>.     
	 * @param datosStr
	 */
	public void setFullDatos(String datosStr) {
		if(!StringUtil.isNullOrEmpty(datosStr)) {
			String[] codigoYNumero = datosStr.split(separator);
			String codAreaStr = codigoYNumero[0];
			if(NumUtil.esNumerico(codAreaStr)) {
				getTxtCodArea().setValue(Integer.valueOf(codAreaStr).longValue());
			}
			String numeroStr = codigoYNumero[1];
			if(NumUtil.esNumerico(numeroStr)) {
				getTxtNumero().setValue(Integer.valueOf(numeroStr).longValue());
			}
		}else{
			getTxtCodArea().setValue(null);
			getTxtNumero().setValue(null);
		}
	}

	/**
	 * Permite setear el campo codigo de area.
	 * @param codArea
	 */
	public void setCodArea(Integer codArea) {
		if(codArea != null) {
			getTxtCodArea().setValue(codArea.longValue());
		}
	}

	/**
	 * Devuelve el n�mero de telefono en base a los campos c�digo de area y n�mero
	 * concatenados con <code>separator</code>.  
	 * @return El n�mero de telefono en base a los campos c�digo de area y n�mero
	 * concatenados con <code>separator</code>
	 */
	public String getDatos() {
		if(!StringUtil.isNullOrEmpty(getTxtCodArea().getText()) && !StringUtil.isNullOrEmpty(getTxtNumero().getText())) {
			return getTxtCodArea().getValue() + separator + getTxtNumero().getValue();
		} else {
			return null;
		}
	}
	
	/**
	 * Setea la cantidad maxima de caracteres permitidos
	 * @param longitud
	 */
	
	public void setMaxLongTelefono(Integer longitud){
		getTxtNumero().setMaxLength(longitud);
	}

	/**
	 * Limpia los componentes c�digo de area y n�mero. 
	 */
	public void limpiarDatos() {
		getTxtCodArea().setText("");
		getTxtNumero().setText("");
	}

	public String getValorTelefono(){
		return getTxtNumero().getText();
	}
	
	public void setValorTelefono(String valor){
		getTxtNumero().setText(valor);
	}
	
	public String getValorCodigoArea(){
		return getTxtCodArea().getText();
	}
	
	public void setValorCodigoArea(String valor){
		getTxtCodArea().setText(valor);
	}
	
	/**
	 * Valida que ambos campos est�n completos o incompletos. Caso contrario devuelve el mensaje de error.
	 * @return Un mensaje indicando el campo que hay que completar 
	 */
	public String validar() {
		if(StringUtil.isNullOrEmpty(getTxtCodArea().getText()) && !StringUtil.isNullOrEmpty(getTxtNumero().getText())) {
			return "Falta completar el c�digo de area del campo '" + title + "'";
		}
//		if(!StringUtil.isNullOrEmpty(getTxtCodArea().getText()) && StringUtil.isNullOrEmpty(getTxtNumero().getText())) {
//			return "Falta completar el n�mero del campo '" + title + "'";
//		}
		return null;
	}
}