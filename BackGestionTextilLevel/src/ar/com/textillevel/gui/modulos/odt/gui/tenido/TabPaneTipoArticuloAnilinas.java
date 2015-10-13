package ar.com.textillevel.gui.modulos.odt.gui.tenido;

import java.awt.Dialog;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Reactivo;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaReactivoCantidad;
import ar.com.textillevel.gui.modulos.odt.gui.PanelTablaMateriaPrimaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.ReactivoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.AnilinaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.TenidoTipoArticulo;
import ar.com.textillevel.util.GTLBeanFactory;

public class TabPaneTipoArticuloAnilinas extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private Dialog owner;
	private FormulaTenidoCliente formula;
	private List<PanelFormulablesTenido> panelesFormulables;
	private boolean modoConsulta;
	private MateriaPrimaFacadeRemote materiaPrimaFacade;

	public TabPaneTipoArticuloAnilinas(Dialog owner, FormulaTenidoCliente formula, boolean modoConsulta) {
		super();
		this.owner = owner;
		this.modoConsulta = modoConsulta;
		this.formula = formula;
		this.panelesFormulables = new ArrayList<PanelFormulablesTenido>();
		construct();
	}

	private void construct() {
		if(formula != null) {
			rebuildTabs(formula.getTenidosComponentes());
		}
	}

	public void rebuildTabs(List<TenidoTipoArticulo> tenidosComponentes) {
		int tabCount = getTabCount();
		int borrados = 0;
		for(int i = 0; i < tabCount; i++) {
			removeTabAt(i-borrados);
			borrados ++;
		}
		panelesFormulables.clear();
		for(TenidoTipoArticulo tta : tenidosComponentes) {
			PanelFormulablesTenido panel = new PanelFormulablesTenido(tta.getTipoArticulo());
			panel.setModoConsulta(modoConsulta);
			panel.setElementos(tta.getAnilinasCantidad(), tta.getReactivosCantidad());
			panelesFormulables.add(panel);
			add(tta.getTipoArticulo().getNombre(), panel);
		}
	}

	public String validar() {
		for(PanelFormulablesTenido p : panelesFormulables) {
			String msg = p.validarElementos();
			if(!StringUtil.isNullOrEmpty(msg)) {
				return msg;
			}
		}
		return null;
	}

	public FormulaTenidoCliente setearDatosEnFormula() {
		if(formula != null) {
			for(TenidoTipoArticulo tta : formula.getTenidosComponentes()) {
				PanelFormulablesTenido panelTta = null;
				for(PanelFormulablesTenido panel : panelesFormulables) {
					if(panel.getTipoArticulo().equals(tta.getTipoArticulo())) {
						panelTta = panel;
						break;
					}
				}
				tta.getAnilinasCantidad().clear();
				tta.getAnilinasCantidad().addAll(panelTta.getAnilinasCantidad());
				tta.getReactivosCantidad().clear();
				tta.getReactivosCantidad().addAll(panelTta.getReactivosCantidad());

			}
		}
		return formula;
	}

	private MateriaPrimaFacadeRemote getMateriaPrimaFacade() {
		if(materiaPrimaFacade == null) {
			materiaPrimaFacade = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class);
		}
		return materiaPrimaFacade;
	}

	private class PanelFormulablesTenido extends JTabbedPane {

		private static final long serialVersionUID = 5989776257116030073L;

		private PanTablaAnilinaCantidad panAnilina;
		private PanTablaReactivoCantidad panReactivos;
		private TipoArticulo tipoArticulo;

		public PanelFormulablesTenido(TipoArticulo tipoArticulo) {
			this.tipoArticulo = tipoArticulo;
			panAnilina = new PanTablaAnilinaCantidad(owner, "Anilina", getMateriaPrimaFacade().getAllAnilinasByTipoArticulo(tipoArticulo));
			add("Anilina", panAnilina);
			panReactivos = new PanTablaReactivoCantidad(owner, "Reactivo", getMateriaPrimaFacade().getAllByClase(Reactivo.class));
			add("Reactivo", panReactivos);
		}

		public List<ReactivoCantidad> getReactivosCantidad() {
			return panReactivos.getElementos();
		}

		public List<AnilinaCantidad> getAnilinasCantidad() {
			return panAnilina.getElementos();
		}

		public TipoArticulo getTipoArticulo() {
			return tipoArticulo;
		}

		public String validarElementos() {
			String msg = panAnilina.validarElementos();
			if(StringUtil.isNullOrEmpty(msg)) {
				msg = panReactivos.validarElementos();
			}
			return msg;
		}

		public void setElementos(List<AnilinaCantidad> anilinasCantidad, List<ReactivoCantidad> reactivosCantidad) {
			panAnilina.agregarElementos(anilinasCantidad);
			panReactivos.agregarElementos(reactivosCantidad);
		}

		public void setModoConsulta(boolean modoConsulta) {
			panAnilina.setModoConsulta(modoConsulta);
			panReactivos.setModoConsulta(modoConsulta);
		}

	}
	
	private class PanTablaAnilinaCantidad extends PanelTablaMateriaPrimaCantidad<Anilina, AnilinaCantidad> {

		private static final long serialVersionUID = -5278662193977929538L;

		public PanTablaAnilinaCantidad(Dialog owner, String descripcionTipoMateriaPrima, List<Anilina> allFormulablesPosibles) {
			super(owner, descripcionTipoMateriaPrima, allFormulablesPosibles);
		}

		@Override
		public AnilinaCantidad createMateriaPrimaCantidad() {
			AnilinaCantidad anilinaCantidad = new AnilinaCantidad();
			anilinaCantidad.setCantidad(0f);
			return anilinaCantidad;
		}

		@Override
		public String createLabelTipoMateriaPrima() {
			return "Anilina";
		}
	
	}

}