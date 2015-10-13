package ar.com.fwcommon.boss;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import ar.com.fwcommon.util.DateUtil;

public class BossIdiomas {

	public static final String FWCOMMON = "fwcommon";
	private static Map<String, BossIdiomas> instances = new HashMap<String, BossIdiomas>();
	private Locale locale;
	private ResourceBundle messageBundle;
	private String resourceBundle;

	private BossIdiomas(String resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public static BossIdiomas getInstance(String resourceBundle) {
		if(instances.get(resourceBundle) == null) {
			instances.put(resourceBundle, new BossIdiomas(resourceBundle));
		}
		return instances.get(resourceBundle);
	}

	private SimpleDateFormat dateFormatter = null;

	/**
	 * @param fecha
	 * @return fecha Corta, en el locale que corresponda
	 */
	public String formatDate(java.util.Date fecha) {
		if(dateFormatter == null) {
			if(isSpanish())
				//dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
				dateFormatter = DateUtil.getSimpleDateFormat("dd/MM/yyyy");
			else
				//dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
				dateFormatter = DateUtil.getSimpleDateFormat("MM/dd/yyyy");
		}
		return dateFormatter.format(fecha);
	}

	private boolean isSpanish() {
		return getLocale().getLanguage().equals("") || getLocale().getLanguage().equals("es");
	}

	private Locale getLocale() {
		if(locale == null) {
//			locale = new Locale("es", "","");//esto no funciona
			locale = new Locale("", "", "");//Ojo!! Si quiero el lenguaje default (español,backdt.properties) tengo que usar esto. 
//			locale = new Locale("en", "US");
		}
		return locale;
	}

	private ResourceBundle getMessageBundle() {
		if(messageBundle == null) {
			messageBundle = ResourceBundle.getBundle(resourceBundle, getLocale());
		}
		return messageBundle;
	}


	public String getString(String key) {
		return getMessageBundle().getString(key);
	}

	public String getString(String key, Object[] messageArguments) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(getLocale());
		formatter.applyPattern(getMessageBundle().getString(key));
		return formatter.format(messageArguments);
	}

}