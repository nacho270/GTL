package ar.com.textillevel.skin;

import javax.swing.Icon;

import ar.clarin.fwjava.templates.main.skin.AbstractSkin;
import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.gui.util.ESkin;

public class SkinModerno extends AbstractSkin {

	public SkinModerno(ESkin eskin) {
		super(eskin.getPath(), new SkinDecoratorModerno(), "Estilo Moderno");
	}

	@Override
	public boolean isSkinLF() {
		return true;
	}

	@Override
	public Icon getPreview() {
		return ImageUtil.loadIcon(SkinModerno.class, "ar/com/textillevel/skin/preview_moderno.png");
	}

}