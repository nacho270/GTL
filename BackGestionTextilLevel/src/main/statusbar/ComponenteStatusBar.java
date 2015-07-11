package main.statusbar;

import javax.swing.JPanel;

public abstract class ComponenteStatusBar<R extends ComponenteStatusBarRenderer<?>> extends JPanel implements Runnable {

	private static final long serialVersionUID = -3160162158639355907L;

	private ConfiguracionComponenteStatusBar configuracion;
	private R renderer;
	
	public ComponenteStatusBar() {
		this(new ConfiguracionComponenteStatusBar(false, 0l));
	}

	public ComponenteStatusBar(ConfiguracionComponenteStatusBar configuracion) {
		this.configuracion = configuracion;
		this.renderer = createRenderer();
		new Thread(this).start();
	}
	
	public void run() {
		if(configuracion.isRepetible()){
			while(true){
				try{
					ejecutar();
					getRenderer().render();
					Thread.sleep(configuracion.getTiempoRepeticion());
				}catch(Exception e){
					e.printStackTrace();
					break;
				}
			}
		}else{
			ejecutar();
			getRenderer().render();
		}
	}
	
	protected abstract void ejecutar();
	
	protected abstract R createRenderer();

	private R getRenderer() {
		return renderer;
	}
	
}
