package main.statusbar;

public class CompoTest extends ComponenteStatusBar<CompoTestRenderer> {

	private static final long serialVersionUID = -8728406558632359940L;
	
	private int con = 100;
	
	public CompoTest(ConfiguracionComponenteStatusBar configuracion) {
		super(configuracion);
	}

	@Override
	protected void ejecutar() {
		this.con--;
	}
	
	public int getCon(){
		return this.con;
	}

	@Override
	protected CompoTestRenderer createRenderer() {
		return new CompoTestRenderer(this);
	}
}
