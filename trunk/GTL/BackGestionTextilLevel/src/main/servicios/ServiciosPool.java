package main.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ServiciosPool {
	
	private static final Set<Timer> timers = new HashSet<Timer>();
	
	public static void launch(List<Servicio> servicios){
		for(final Servicio serv : servicios){
			ServiceConfig conf = serv.getClass().getAnnotation(ServiceConfig.class);
			if(conf==null){
				throw new RuntimeException("Servicio sin configurar " + serv.getClass().getCanonicalName());
			}
			
			int ejecucionCada = conf.ejecutarCada();
			if(ejecucionCada == 0){
				serv.execute();
			}else{
				TimerTask tt = new TimerTask() {
					@Override
					public void run() {
						serv.execute();
					}
				};
				Timer timer = new Timer();
				timer.schedule(tt, 0, ejecucionCada);
				timers.add(timer);
			}
		}
	}
	
	public static void stopAll(){
		for(Timer t : timers){
			t.cancel();
		}
	}
}
