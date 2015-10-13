package ar.com.textillevel.modulos.personal.utils;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.BeanFactoryRemoteAbstract;
import ar.com.textillevel.facade.api.remote.QuincenaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.AFJPFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.AntiFichadaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.AntiguedadFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ArtFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.AsignacionFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.CategoriaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConceptoReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionPresentismoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVHCategoriaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ContratoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ContribucionFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.DiaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpresaSegurosFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.FichadaLegajoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.MesFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.NacionalidadFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ObraSocialFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ParametrosGeneralesPersonalFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.PuestoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SancionFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.VacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAtencionFacadeRemote;

public class GTLPersonalBeanFactory extends BeanFactoryRemoteAbstract {

	private static GTLPersonalBeanFactory instance;
	
	protected GTLPersonalBeanFactory() throws FWException {
		super("GTL");
		addJndiName(ArtFacadeRemote.class);
		addJndiName(EmpresaSegurosFacadeRemote.class);
		addJndiName(EmpleadoFacadeRemote.class);
		addJndiName(CalendarioAnualFeriadoFacadeRemote.class);
		addJndiName(NacionalidadFacadeRemote.class);
		addJndiName(AFJPFacadeRemote.class);
		addJndiName(SindicatoFacadeRemote.class);
		addJndiName(PuestoFacadeRemote.class);
		addJndiName(CategoriaFacadeRemote.class);
		addJndiName(DiaFacadeRemote.class);
		addJndiName(ObraSocialFacadeRemote.class);
		addJndiName(SancionFacadeRemote.class);
		addJndiName(ValeAtencionFacadeRemote.class);
		addJndiName(ContratoFacadeRemote.class);
		addJndiName(FichadaLegajoFacadeRemote.class);
		addJndiName(ConfiguracionVHCategoriaFacadeRemote.class);
		addJndiName(ContribucionFacadeRemote.class);
		addJndiName(AntiguedadFacadeRemote.class);
		addJndiName(ValeAnticipoFacadeRemote.class);
		addJndiName(ConceptoReciboSueldoFacadeRemote.class);
		addJndiName(AsignacionFacadeRemote.class);
		addJndiName(ParametrosGeneralesPersonalFacadeRemote.class);
		addJndiName(MesFacadeRemote.class);
		addJndiName(ReciboSueldoFacadeRemote.class);
		addJndiName(AntiFichadaFacadeRemote.class);
		addJndiName(ConfiguracionPresentismoFacadeRemote.class);
		addJndiName(ConfiguracionVacacionesFacadeRemote.class);
		addJndiName(VacacionesFacadeRemote.class);
		addJndiName(QuincenaFacadeRemote.class);
	}

	public static GTLPersonalBeanFactory getInstance() {
		if(instance == null) {
			try {
				instance = new GTLPersonalBeanFactory();
			} catch (FWException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
