package main.servicios.alertas;

import java.util.List;

import main.GTLGlobalCache;
import main.servicios.ServiceConfig;
import main.servicios.Servicio;
import main.servicios.alertas.gui.GUIServicioAlertas;
import ar.com.textillevel.modulos.alertas.entidades.Alerta;
import ar.com.textillevel.modulos.alertas.facade.api.remote.AlertaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

@ServiceConfig(ejecutarCada = 120000)
public class ServicioAlertas extends Servicio {

	@Override
	public void execute() {
		if (GUIServicioAlertas.getInstance() == null) {
			GUIServicioAlertas.getInstance(true);
		}
		List<Alerta> alertasVigentes = GTLBeanFactory.getInstance().getBean2(AlertaFacadeRemote.class).getAlertasVigentesByPerfil(GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil());
		if (alertasVigentes != null && !alertasVigentes.isEmpty()) {
			GUIServicioAlertas.getInstance().addAlertas(alertasVigentes);
			if (!GUIServicioAlertas.getInstance().isVisible()) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						GUIServicioAlertas.getInstance().setVisible(true);
					}
				});
			}
		}
	}
}
