package ar.com.fwcommon.templates.modulo.gui.filtros;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtro;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroLista;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroListaOpciones;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroNumerico;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroOpcion;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRangoFechas;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroTexto;

public class GuiFiltroFactory {

	public GuiFiltroFactory() {
		super();
	}

	@SuppressWarnings("unchecked")
	public static <T> GuiFiltro<T, ?> createGuiFiltro(ModuloTemplate<T, ?> owner, Filtro<T, ?> filtro) {
		if (filtro instanceof FiltroListaOpciones){
			return new GuiFiltroCmb(owner, (FiltroListaOpciones<T, ?>)filtro);
		} else if (filtro instanceof FiltroOpcion) {
			return new GuiFiltroChk(owner, (FiltroOpcion<T>)filtro);
		} else if (filtro instanceof FiltroLista) {
			return new GuiFiltroChkList(owner, (FiltroLista<T, Object>)filtro);
		} else if (filtro instanceof FiltroTexto){
			return new GuiFiltroTexto(owner, (FiltroTexto<T>)filtro);
		} else if (filtro instanceof FiltroNumerico){
			return new GuiFiltroNumerico(owner, (FiltroNumerico<T>)filtro);			
		} else if(filtro instanceof FiltroRangoFechas) {
			return new GuiFiltroRangoFechas(owner, (FiltroRangoFechas<T, ?>)filtro);
		}
		throw new IllegalArgumentException("Tipo de filtro no conocido");
	}
}
