package main.statusbar;

public abstract class ComponenteStatusBarRenderer<C extends ComponenteStatusBar<?>> {

	private C componente;
	
	public ComponenteStatusBarRenderer(C componente) {
		this.componente = componente;
	}
	
	protected abstract void render();
	
	protected C getComponente(){
		return this.componente;
	}
}
