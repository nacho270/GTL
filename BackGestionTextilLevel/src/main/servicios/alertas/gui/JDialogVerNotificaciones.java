package main.servicios.alertas.gui;

import java.awt.Color;
import java.awt.Component;
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
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.servicios.alertas.AccionNotificacion;
import main.servicios.alertas.ETipoNotificacionUI;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.NotificacionUsuarioFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

import com.google.common.collect.Maps;

public class JDialogVerNotificaciones extends JDialog {

	private static final long serialVersionUID = 6882152212322989005L;

	private static final Color COLOR_DESELECCIONADO = Color.WHITE;
	private static final Color COLOR_SELECCIONADO = Color.CYAN;

	private static final Map<Integer, Color> COLOR_MAP = Maps.newHashMap();
	
	private static final int CANT_COLS = 3;
	private static final int COL_LEIDA = 0;
	private static final int COL_TEXTO = 1;
	private static final int COL_OBJ = 2;
	
	private final List<NotificacionUsuario> notificaciones;
	private NotificacionUsuarioFacadeRemote notificacionFacade = GTLBeanFactory.getInstance().getBean2(NotificacionUsuarioFacadeRemote.class);
	
	private FWJTable tablaNotificaciones;
	private JPanel panelBotones;
	private JButton btnAceptar;
	
	public JDialogVerNotificaciones(Frame owner, List<NotificacionUsuario> notificaciones) {
		super(owner);
		this.notificaciones = notificaciones;
		setUpScreen();
		setUpComponentes();
		llenarTabla();
		crearBotones();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void crearBotones() {
		int y = 0;
		for (final NotificacionUsuario nc : notificaciones) {
			int x = 0;
			ETipoNotificacionUI tipoNotificacionUI = ETipoNotificacionUI.by(nc.getTipo());
			for (final AccionNotificacion an : tipoNotificacionUI.getAcciones()) {
				final JButton boton = new JButton(new AbstractAction(an.getTitulo()) {

					private static final long serialVersionUID = 2368144073667152535L;

					@Override
					public void actionPerformed(ActionEvent e) {
						boolean accionRealizada = an.ejecutar(nc.getIdRelacionado());
						notificacionFacade.marcarComoLeida(nc);
						if (accionRealizada) {
							notificacionFacade.marcarComoLeidaATodosLosUsuarios(nc);
						}
						for (NotificacionUsuario noti : notificaciones) {
							if (noti.getId().equals(nc.getId())) {
								noti.setLeida(true);
							}
						}
						llenarTabla();
					}
				});
				
				boton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						int y = Integer.valueOf(boton.getActionCommand());
						COLOR_MAP.put(y, COLOR_SELECCIONADO);
						((DefaultTableModel) getTablaNotificaciones().getModel()).fireTableRowsUpdated(y, y);
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						int y = Integer.valueOf(boton.getActionCommand());
						COLOR_MAP.put(y, COLOR_DESELECCIONADO);
						((DefaultTableModel) getTablaNotificaciones().getModel()).fireTableRowsUpdated(y, y);
					}
				});
				
				Dimension preferredSize = boton.getPreferredSize();
				preferredSize.height = getTablaNotificaciones().getRowHeight() - 1;
				boton.setPreferredSize(preferredSize);
				boton.setActionCommand(String.valueOf(y));
				getPanelBotones().add(boton, GenericUtils.createGridBagConstraints(x++, y, //
						GridBagConstraints.CENTER, GridBagConstraints.NONE, //
						new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			}
			y++;
		}
	}

	private void llenarTabla() {
		getTablaNotificaciones().removeAllRows();
		COLOR_MAP.clear();
		int i = 0;
		for(final NotificacionUsuario nc : notificaciones) {
			getTablaNotificaciones().addRow(new Object[]{nc.getLeida(), nc.getTexto() , nc});
			COLOR_MAP.put(i, COLOR_DESELECCIONADO);
			((DefaultTableModel) getTablaNotificaciones().getModel()).fireTableRowsUpdated(i, i);
		}
	}

	private void setUpComponentes() {
		setLayout(new GridBagLayout());
		JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pnlSur.add(getBtnAceptar());

		JScrollPane jspTabla = new JScrollPane(getTablaNotificaciones());
		JScrollPane jspBotones = new JScrollPane(getPanelBotones());

		jspBotones.getVerticalScrollBar().setModel(jspTabla.getVerticalScrollBar().getModel());
		
		add(jspTabla, GenericUtils.createGridBagConstraints(0, 0, //
				GridBagConstraints.WEST, GridBagConstraints.BOTH, //
				new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		add(jspBotones, GenericUtils.createGridBagConstraints(1, 0, //
				GridBagConstraints.EAST, GridBagConstraints.BOTH, //
				new Insets(5, 5, 5, 5), 1, 1, 0.2, 1));
		add(pnlSur, GenericUtils.createGridBagConstraints(0, 1, //
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, //
				new Insets(5, 5, 5, 5), 2, 1, 0, 0));
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Notificaciones");
		setSize(new Dimension(630,400));
		setResizable(true);
		GuiUtil.centrarEnFramePadre(this);
		setModal(true);
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}
	
	public FWJTable getTablaNotificaciones() {
		if (tablaNotificaciones == null) {
			tablaNotificaciones = new FWJTable(0, CANT_COLS);
			tablaNotificaciones.setCheckColumn(COL_LEIDA, "Leida", 50, true);
			tablaNotificaciones.setStringColumn(COL_TEXTO, "Notificación", 414, 414, true);
//			tablaNotificaciones.setStringColumn(COL_ACCIONES, "Acciones", 280, 280, true);
			tablaNotificaciones.setStringColumn(COL_OBJ, "", 0);
			tablaNotificaciones.setAllowHidingColumns(false);
			tablaNotificaciones.setAllowSorting(false);
			tablaNotificaciones.setReorderingAllowed(false);
			tablaNotificaciones.setHeaderAlignment(COL_LEIDA, FWJTable.CENTER_ALIGN);
//			tablaNotificaciones.setHeaderAlignment(COL_ACCIONES, FWJTable.CENTER_ALIGN);
			tablaNotificaciones.setHeaderAlignment(COL_TEXTO, FWJTable.CENTER_ALIGN);

			tablaNotificaciones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				
				private static final long serialVersionUID = 3567985007339292748L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		            final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		            c.setBackground(COLOR_MAP.get(row));
		            return c;				
				}
			});
			
//			TableColumn columnaAcciones = tablaNotificaciones.getColumnModel().getColumn(2);
////			tablaNotificaciones.setRowHeight(renderer.getTableCellRendererComponent(tablaNotificaciones, null, true, true, 0, 0).getPreferredSize().height);
//			tablaNotificaciones.setRowHeight(30);
//			
//			columnaAcciones.setCellRenderer(new PanelAccionesRenderer());
//			//columnaAcciones.setCellEditor(new PanelAccionesEditor());
//			columnaAcciones.setCellEditor(new PanelAccionesEditor());
		}
		return tablaNotificaciones;
	}

	public JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 1));
			// groncho pad
			for (int i = 0; i <13; i++) {
				panelBotones.add(new JLabel(""));
			}
		}
		return panelBotones;
	}

//	private class PanelAccionesRenderer extends DefaultTableCellRenderer {
//
//		private static final long serialVersionUID = 5174960496977385318L;
//
//		private PanelAccionesNotificacion pnlAcciones;
//
//        public PanelAccionesRenderer() {
//			pnlAcciones = new PanelAccionesNotificacion();
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            pnlAcciones.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
//            pnlAcciones.updateButtons(value);
//            return pnlAcciones;
//        }
//    }
//	
//	private class PanelAccionesNotificacion extends JPanel {
//
//		private static final long serialVersionUID = 7186052076003686901L;
//
//		private List<JButton> buttons = Lists.newArrayList();
//		
//		public PanelAccionesNotificacion() {
//            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
//            setOpaque(true);
//        }
//    
//		@SuppressWarnings("unchecked")
//		protected void updateButtons(Object value) {
//			if (value != null && value instanceof ArrayList) {
//				removeAll();
//				buttons.clear();
//				List<JButton> botones = (ArrayList<JButton>) value;
//				for(final JButton btn : botones) {
//					btn.setFocusable(false);
//					btn.setRolloverEnabled(false);
//					add(btn);
//					buttons.add(btn);
//				}
//			}
//		}
//	}
//
//	private class PanelAccionesEditor extends AbstractCellEditor implements TableCellEditor {
//
//		private static final long serialVersionUID = -5861419769325532452L;
//		
//		private final PanelAccionesNotificacion panel = new PanelAccionesNotificacion();
//		private Object o;
//
//		@Override
//		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//			panel.setBackground(table.getSelectionBackground());
//			panel.updateButtons(value);
//			o = value;
//			return panel;
//		}
//
//		@Override
//		public Object getCellEditorValue() {
//			return o;
//		}
//	}
}
