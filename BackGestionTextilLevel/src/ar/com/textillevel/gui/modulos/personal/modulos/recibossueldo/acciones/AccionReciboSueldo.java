package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.acciones;

import java.math.BigDecimal;
import java.util.List;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLCheckBoxListDialog;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.ReciboSueldoDatosHandler;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera.CabeceraReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera.ModeloCabeceraReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.exception.InvalidStateReciboSueldoException;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.gui.JDialogReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionReciboSueldo extends Accion<InfoReciboSueltoTO> {

	private ReciboSueldoFacadeRemote reciboSueldoFacade;
	private ValeAnticipoFacadeRemote valesFacade;

	public AccionReciboSueldo(){
		setNombre("Recibo de Sueldo");
		setDescripcion("Permite consultar/crear/modificar un recibo de sueldo"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_ver_resumen_legajo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_ver_resumen_legajo_des.png");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean ejecutar(AccionEvent<InfoReciboSueltoTO> e) throws CLException {
		InfoReciboSueltoTO irsto = e.getSelectedElements().get(0);
		ReciboSueldo rs = null;
		if(irsto.getReciboSueldo() == null) {
			ModeloCabeceraReciboSueldo model = ((CabeceraReciboSueldo)e.getSource().getCabecera()).getModel();

			//se buscan los vales aún no descontados y se ofrecen para incluirlos en el recibo de sueldo
			List<ValeAnticipo> vales = getValesFacade().getValesEnEstado(irsto.getLegajo().getId(), EEstadoValeAnticipo.A_DESCONTAR);
			if(!vales.isEmpty()) {
				CLCheckBoxListDialog dialogoSelVales = new CLCheckBoxListDialog(e.getSource().getFrame());
				dialogoSelVales.setValores(vales, true);
				dialogoSelVales.setTitle("Seleccione los vales");
				dialogoSelVales.setVisible(true);
				vales = dialogoSelVales.getValoresSeleccionados();
			}

			rs = new ReciboSueldo();
			rs.setLegajo(irsto.getLegajo());
			BigDecimal valorHora = irsto.getLegajo().getValorHora();
			try {
				new ReciboSueldoDatosHandler(rs, model, valorHora, vales);
			} catch (InvalidStateReciboSueldoException e1) {
				CLJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(e1.getMessage()), "Error");
				return false;
			}
		} else {
			rs = getReciboSueldoFacade().getByIdEager(irsto.getReciboSueldo().getId());
		}
		JDialogReciboSueldo dialog = new JDialogReciboSueldo(e.getSource().getFrame(), rs);
		GuiUtil.centrar(dialog);
		dialog.setVisible(true);
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<InfoReciboSueltoTO> e) {
		return e.getSelectedElements().size()==1;
	}

	private ReciboSueldoFacadeRemote getReciboSueldoFacade() {
		if(reciboSueldoFacade == null) {
			reciboSueldoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ReciboSueldoFacadeRemote.class);
		}
		return reciboSueldoFacade;
	}

	private ValeAnticipoFacadeRemote getValesFacade() {
		if(valesFacade == null) {
			valesFacade = GTLPersonalBeanFactory.getInstance().getBean2(ValeAnticipoFacadeRemote.class);
		}
		return valesFacade;
	}

}