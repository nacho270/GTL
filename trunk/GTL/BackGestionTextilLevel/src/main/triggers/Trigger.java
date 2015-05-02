package main.triggers;

public abstract class Trigger {
	
	public Trigger(){}
	
	public abstract boolean esValido();
	
	public abstract void execute();
}
