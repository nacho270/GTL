package ar.com.fwcommon.templates.main.menu.decorator;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import ar.com.fwcommon.util.ImageUtil;

public class MenuDecorator {

	private Map<String, String> table;
	public static final String BLANK_ICON = "ar/com/fwcommon/templates/main/menu/decorator/blank_icon.png";

	public MenuDecorator() {
		table = new HashMap<String, String>();
	}

	public void putIcono(String key, String iconPath) {
		table.put(key, iconPath);
	}

	public Icon getIcono(String key) {
		String iconPath = table.get(key);
		return iconPath == null ? ImageUtil.loadIcon(BLANK_ICON) : ImageUtil.loadIcon(iconPath);
	}

}