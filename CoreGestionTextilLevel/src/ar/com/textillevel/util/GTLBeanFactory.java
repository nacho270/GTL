package ar.com.textillevel.util;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.BeanFactoryRemoteAbstract;
import ar.com.textillevel.facade.api.remote.AgendaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ContenedorMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaPersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorrectorCuentasClientesFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorrectorCuentasProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.CuentaArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.CuentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.facade.api.remote.EntregaReingresoDocumentosFacadeRemote;
import ar.com.textillevel.facade.api.remote.EventoFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaPersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ImpuestoItemProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ModuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.MovimientoStockFacadeRemote;
import ar.com.textillevel.facade.api.remote.NotaDebitoFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDeDepositoFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoPersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.PerfilFacadeRemote;
import ar.com.textillevel.facade.api.remote.PersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProvinciaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.facade.api.remote.RelacionContenedorMatPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaDibujoFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ServicioFacadeRemote;
import ar.com.textillevel.facade.api.remote.TarimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoAnilinaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.modulos.alertas.facade.api.remote.AlertaFacadeRemote;
import ar.com.textillevel.modulos.alertas.facade.api.remote.TipoAlertaFacadeRemote;
import ar.com.textillevel.modulos.fe.facade.api.remote.ConfiguracionFacturaElectronicaFacadeRemote;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.ConfiguracionNotificacionFacadeRemote;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.NotificacionUsuarioFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.AccionProcedimientoFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.FormulaClienteFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.InstruccionProcedimientoFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.MaquinaFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.SecuenciaTipoProductoFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.TipoMaquinaFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.TransicionODTFacadeRemote;
import ar.com.textillevel.modulos.terminal.facade.api.remote.ModuloTerminalFacadeRemote;
import ar.com.textillevel.modulos.terminal.facade.api.remote.TerminalFacadeRemote;

public class GTLBeanFactory extends BeanFactoryRemoteAbstract {

	private static GTLBeanFactory instance;
	
	protected GTLBeanFactory() throws FWException {
		super("GTL");
		addJndiName(ClienteFacadeRemote.class);
		addJndiName(ProveedorFacadeRemote.class);
		addJndiName(AgendaFacadeRemote.class);
		addJndiName(MateriaPrimaFacadeRemote.class);
		addJndiName(InfoLocalidadFacadeRemote.class);
		addJndiName(PersonaFacadeRemote.class);
		addJndiName(RubroPersonaFacadeRemote.class);
		addJndiName(ColorFacadeRemote.class);
		addJndiName(ArticuloFacadeRemote.class);
		addJndiName(GamaColorFacadeRemote.class);
		addJndiName(ProductoFacadeRemote.class);
		addJndiName(DibujoEstampadoFacadeRemote.class);
		addJndiName(ListaDePreciosFacadeRemote.class);
		addJndiName(BancoFacadeRemote.class);
		addJndiName(RemitoSalidaFacadeRemote.class);
		addJndiName(ParametrosGeneralesFacadeRemote.class);
		addJndiName(CondicionDeVentaFacadeRemote.class);
		addJndiName(FacturaFacadeRemote.class);
		addJndiName(RemitoEntradaFacadeRemote.class);
		addJndiName(ChequeFacadeRemote.class);
		addJndiName(ReciboFacadeRemote.class);
		addJndiName(CuentaFacadeRemote.class);
		addJndiName(CorreccionFacadeRemote.class);
		addJndiName(NotaDebitoFacadeRemote.class);
		addJndiName(PerfilFacadeRemote.class);
		addJndiName(UsuarioSistemaFacadeRemote.class);
		addJndiName(ModuloFacadeRemote.class);
		addJndiName(OrdenDeTrabajoFacadeRemote.class);
		addJndiName(EventoFacadeRemote.class);
		addJndiName(TipoAnilinaFacadeRemote.class);
		addJndiName(ListaDePreciosProveedorFacadeRemote.class);
		addJndiName(RemitoEntradaProveedorFacadeRemote.class);
		addJndiName(PrecioMateriaPrimaFacadeRemote.class);
		addJndiName(MovimientoStockFacadeRemote.class);		
		addJndiName(ImpuestoItemProveedorFacadeRemote.class);
		addJndiName(FacturaProveedorFacadeRemote.class);
		addJndiName(OrdenDePagoFacadeRemote.class);
		addJndiName(ContenedorMateriaPrimaFacadeRemote.class);
		addJndiName(RelacionContenedorMatPrimaFacadeRemote.class);
		addJndiName(CorreccionFacturaProveedorFacadeRemote.class);
		addJndiName(OrdenDeDepositoFacadeRemote.class);
		addJndiName(OrdenDePagoPersonaFacadeRemote.class);
		addJndiName(CuentaArticuloFacadeRemote.class);
		addJndiName(FacturaPersonaFacadeRemote.class);
		addJndiName(ServicioFacadeRemote.class);
		addJndiName(ProvinciaFacadeRemote.class);
		addJndiName(CorrectorCuentasClientesFacadeRemote.class);
		addJndiName(CorrectorCuentasProveedorFacadeRemote.class);
		addJndiName(TipoMaquinaFacadeRemote.class);
		addJndiName(MaquinaFacadeRemote.class);
		addJndiName(TarimaFacadeRemote.class);
		addJndiName(TipoArticuloFacadeRemote.class);
		addJndiName(AccionProcedimientoFacadeRemote.class);
		addJndiName(FormulaClienteFacadeRemote.class);
		addJndiName(SecuenciaTipoProductoFacadeRemote.class);
		addJndiName(InstruccionProcedimientoFacadeRemote.class);
		addJndiName(TipoAlertaFacadeRemote.class);
		addJndiName(AlertaFacadeRemote.class);
		addJndiName(ConfiguracionFacturaElectronicaFacadeRemote.class);
		addJndiName(DocumentoContableFacadeRemote.class);
		addJndiName(GamaColorClienteFacadeRemote.class);
		addJndiName(ProductoArticuloFacadeRemote.class);
		addJndiName(TransicionODTFacadeRemote.class);
		addJndiName(TerminalFacadeRemote.class);
		addJndiName(ModuloTerminalFacadeRemote.class);
		addJndiName(EntregaReingresoDocumentosFacadeRemote.class);
		addJndiName(NotificacionUsuarioFacadeRemote.class);
		addJndiName(ConfiguracionNotificacionFacadeRemote.class);
		addJndiName(CorreccionFacturaPersonaFacadeRemote.class);
		addJndiName(RemitoEntradaDibujoFacadeRemote.class);
	}

	public static GTLBeanFactory getInstance() {
		if(instance == null) {
			try {
				instance = new GTLBeanFactory();
			} catch (FWException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
