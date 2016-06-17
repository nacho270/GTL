package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogVisualizarTransicionesODT extends JDialog {

	private static final long serialVersionUID = 1L;

	private PanCabeceraDatosODT panCabecera;
	private PanTablaTransicionODT panelTablaTransicion;
	private JPanel pnlBotones;
	private JButton btnAceptar;
	private JButton btnVerSecuenciaCompleta;
	private JButton btnVerPasosDelSector;

	private OrdenDeTrabajo odt;
	private Frame owner;
	private OrdenDeTrabajoFacadeRemote odtFacade;

	public JDialogVisualizarTransicionesODT(Frame owner, Integer idODT) {
		super(owner);
		this.owner = owner;
		this.odt = getOdtFacade().getByIdEager(idODT);
		setTitle("Detalle de Transiciones de la ODT");
		setModal(true);
		setSize(new Dimension(730, 600));
		setResizable(false);
		construct();
		llenarDatos();
	}

	private void llenarDatos() {
		getPanelTablaTransicion().agregarElementos(createElementosTO(getOdtFacade().getHistoricoTransiciones(odt.getId())));
	}

	private List<TransicionODTTO> createElementosTO(List<TransicionODT> historicoTransiciones) {
		List<TransicionODTTO> transicionesTO = new ArrayList<TransicionODTTO>();
		for(TransicionODT t : historicoTransiciones) {
			List<CambioAvance> cambios = new ArrayList<CambioAvance>(t.getCambiosAvance());
			Collections.sort(cambios, new Comparator<CambioAvance>() {

				public int compare(CambioAvance o1, CambioAvance o2) {
					return o1.getFechaHora().compareTo(o2.getFechaHora());
				}

			});

			for(CambioAvance ca : cambios) {
				TransicionODTTO to = new TransicionODTTO();
				to.setDetalle("En Máquina '" + t.getMaquina().getNombre() + "'");
				to.setEstado(ca.getAvance().getDescripcion());
				to.setFechaHora(ca.getFechaHora());
				to.setUsuario(ca.getUsuario().getUsrName());
				to.setObservaciones(ca.getObservaciones());
				to.setId(ca.getId());
				transicionesTO.add(to);
			}
		}
		return transicionesTO;
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanCabecera(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0.1));
		add(getPanelTablaTransicion(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0.9));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private PanCabeceraDatosODT getPanCabecera() {
		if(panCabecera == null) {
			panCabecera = new PanCabeceraDatosODT(odt);
		}
		return panCabecera;
	}

	private PanTablaTransicionODT getPanelTablaTransicion() {
		if(panelTablaTransicion == null) {
			panelTablaTransicion = new PanTablaTransicionODT();
		}
		return panelTablaTransicion;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnVerSecuenciaCompleta());
			pnlBotones.add(getBtnVerPasosDelSector());
		}
		return pnlBotones;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnAceptar;
	}

	private JButton getBtnVerSecuenciaCompleta() {
		if(btnVerSecuenciaCompleta == null) {
			btnVerSecuenciaCompleta = BossEstilos.createButton("ar/com/textillevel/imagenes/b_verificar_stock.png", "ar/com/textillevel/imagenes/b_verificar_stock_des.png");
			btnVerSecuenciaCompleta.setToolTipText("Ver todas las instrucciones");
			btnVerSecuenciaCompleta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirDialogoVisualizador(odt.getSecuenciaDeTrabajo().getPasos());
				}
			});
		}
		return btnVerSecuenciaCompleta;
	}

	private JButton getBtnVerPasosDelSector() {
		if (btnVerPasosDelSector == null) {
			btnVerPasosDelSector = BossEstilos.createButton("ar/com/textillevel/imagenes/b_abrir.png", "ar/com/textillevel/imagenes/b_abrir_des.png");
			btnVerPasosDelSector.setEnabled(odt.getMaquinaActual() != null);
			if(odt.getMaquinaActual() == null) {
				btnVerPasosDelSector.setToolTipText("Ver instrucciones del sector actual de la ODT.");
			} else {
				btnVerPasosDelSector.setToolTipText("Ver instrucciones del sector '" + odt.getMaquinaActual().getTipoMaquina().getSectorMaquina() + "'.");
			}
			btnVerPasosDelSector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ESectorMaquina sectorMaquinaActual = odt.getMaquinaActual().getTipoMaquina().getSectorMaquina();
					List<PasoSecuenciaODT> pasosDelSectorActual = new ArrayList<PasoSecuenciaODT>();
					for(PasoSecuenciaODT paso : odt.getSecuenciaDeTrabajo().getPasos()){
						if(paso.getSector().getSectorMaquina() == sectorMaquinaActual) {
							pasosDelSectorActual.add(paso);
						}
					}
					if(pasosDelSectorActual.isEmpty()) {
						FWJOptionPane.showInformationMessage(JDialogVisualizarTransicionesODT.this, StringW.wordWrap("No existen pasos definidos dentro de la secuencia de trabajo para el sector '" + sectorMaquinaActual + "' ."), "Atención");
					} else {
						abrirDialogoVisualizador(pasosDelSectorActual);	
					}
				}
			});
		}
		return btnVerPasosDelSector;
	}

	private void abrirDialogoVisualizador(List<PasoSecuenciaODT> pasos){
		JDialogVisualizarPasosSecuenciaODT d = new JDialogVisualizarPasosSecuenciaODT(owner, odt, pasos, true);
		d.setVisible(true);
	}

	private OrdenDeTrabajoFacadeRemote getOdtFacade() {
		if(odtFacade == null) {
			odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class); 
		}
		return odtFacade;
	}

	private class TransicionODTTO {
		
		private Integer id;
		private String detalle;
		private String estado;
		private Timestamp fechaHora;
		private String usuario;
		private String observaciones;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDetalle() {
			return detalle;
		}

		public void setDetalle(String detalle) {
			this.detalle = detalle;
		}

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public Timestamp getFechaHora() {
			return fechaHora;
		}

		public void setFechaHora(Timestamp fechaHora) {
			this.fechaHora = fechaHora;
		}

		public String getUsuario() {
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}

		public String getObservaciones() {
			return observaciones;
		}

		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}

	}

	private class PanTablaTransicionODT extends PanelTabla<TransicionODTTO> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 6;
		private static final int COL_FECHA_HORA = 0;
		private static final int COL_TRANSICION = 1;
		private static final int COL_ESTADO = 2;
		private static final int COL_USUARIO = 3;
		private static final int COL_OBSERVACIONES = 4;
		private static final int COL_OBJ = 5;

		public PanTablaTransicionODT() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
			agregarBotonModificar();
			agregarBoton(getBtnVerSecuenciaCompleta());
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						cargarObservacionesFilaSeleccionada();
					}
				}

			});
			tabla.setStringColumn(COL_TRANSICION, "DETALLE", 150, 150, true);
			tabla.setHeaderAlignment(COL_TRANSICION, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_ESTADO, "ESTADO", 100, 100, true);
			tabla.setHeaderAlignment(COL_ESTADO, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_FECHA_HORA, "FECHA/HORA", 120, 120, true);
			tabla.setHeaderAlignment(COL_FECHA_HORA, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_USUARIO, "USUARIO", 120, 120, true);
			tabla.setHeaderAlignment(COL_FECHA_HORA, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_OBSERVACIONES, "OBSERVACIONES", 150, 150, true);
			tabla.setHeaderAlignment(COL_OBSERVACIONES, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tabla;
		}

		private void cargarObservacionesFilaSeleccionada() {
			int filaSeleccionada = getTabla().getSelectedRow();
			if(filaSeleccionada != -1) {
				TransicionODTTO elemento = getElemento(filaSeleccionada);
				String observaciones = JOptionPane.showInputDialog(JDialogVisualizarTransicionesODT.this, "Observaciones", elemento.getObservaciones());
				if(observaciones!=null){
					getOdtFacade().actualizarObservacionesCambioAvance(elemento.getId(), observaciones);
					elemento.setObservaciones(observaciones);
					getTabla().setValueAt(observaciones, filaSeleccionada, COL_OBSERVACIONES);
				}
			}
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			cargarObservacionesFilaSeleccionada();
		}

		@Override
		protected void agregarElemento(TransicionODTTO elemento) {
			Object[] obj = new Object[CANT_COLS];
			obj[COL_TRANSICION] = elemento.getDetalle();
			obj[COL_FECHA_HORA] = DateUtil.dateToString(elemento.getFechaHora(), DateUtil.SHORT_DATE_WITH_HOUR_SECONDS);
			obj[COL_ESTADO] = elemento.getEstado();
			obj[COL_USUARIO] = elemento.getUsuario();
			obj[COL_OBSERVACIONES] = elemento.getObservaciones();
			obj[COL_OBJ] = elemento;
			getTabla().addRow(obj);
		}

		@Override
		protected TransicionODTTO getElemento(int fila) {
			return (TransicionODTTO)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

}