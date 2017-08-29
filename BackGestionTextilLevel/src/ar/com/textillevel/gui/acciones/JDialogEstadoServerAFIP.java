package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.ImageUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.fe.cliente.dto.CbteTipo;
import ar.com.textillevel.modulos.fe.cliente.dto.DocTipo;
import ar.com.textillevel.modulos.fe.cliente.dto.IvaTipo;
import ar.com.textillevel.modulos.fe.cliente.dto.Moneda;
import ar.com.textillevel.modulos.fe.cliente.responses.CbteTipoResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.DocTipoResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.FECompConsultaResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.IvaTipoResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.MonedaResponse;
import ar.com.textillevel.modulos.fe.to.EstadoAFIPWrapper;
import ar.com.textillevel.modulos.fe.to.EstadoServidorAFIP;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class JDialogEstadoServerAFIP extends JDialog {

	private static final long serialVersionUID = 5177698652696680802L;

	private JTabbedPane tabbedPane;

	private JButton btnAceptar;
	private EstadoServidorAFIP estadoAFIP;

	public JDialogEstadoServerAFIP(Frame owner, EstadoServidorAFIP estadoAfip) {
		super(owner);
		this.estadoAFIP = estadoAfip;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("AFIP");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(360, 280));
		setResizable(true);
		GuiUtil.centrar(this);
		setModal(true);
	}

	private void setUpComponentes() {
		add(getTabbedPane(), BorderLayout.CENTER);
		JPanel pSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pSur.add(getBtnAceptar());
		add(pSur, BorderLayout.SOUTH);
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("Estado de servicios", new PanelEstadoAFIP());
			if (GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()) {
				tabbedPane.addTab("Ejecucion de servicios", new PanelEjecutarServicioAFIP());
			}
		}
		return tabbedPane;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	private EstadoServidorAFIP getEstadoAFIP() {
		return estadoAFIP;
	}

	private class PanelEstadoAFIP extends JPanel {

		private static final long serialVersionUID = 8098584068046463171L;

		private PanelEstadoServicioAFIP panelEstadoAppServer;
		private PanelEstadoServicioAFIP panelEstadoAuthServer;
		private PanelEstadoServicioAFIP panelEstadoDBServer;
		private PanelEstadoServicioAFIP panelPruebaAuth;
		private JLabel lblUltimaFCAutorizada;
		private JLabel lblUltimaNDAutorizada;
		private JLabel lblUltimaNCAutorizada;

		public PanelEstadoAFIP() {
			setLayout(new BorderLayout());
			JPanel pCentro = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
			pCentro.add(getPanelEstadoAppServer());
			pCentro.add(getPanelEstadoAuthServer());
			pCentro.add(getPanelEstadoDBServer());
			pCentro.add(getPanelPruebaAuth());

			JPanel pnlDocs = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
			pnlDocs.add(getLblUltimaFCAutorizada());
			pnlDocs.add(getLblUltimaNDAutorizada());
			pnlDocs.add(getLblUltimaNCAutorizada());

			pCentro.add(pnlDocs);

			add(pCentro, BorderLayout.CENTER);
		}

		private PanelEstadoServicioAFIP getPanelEstadoAppServer() {
			if (panelEstadoAppServer == null) {
				panelEstadoAppServer = new PanelEstadoServicioAFIP(getEstadoAFIP().getAppServer());
			}
			return panelEstadoAppServer;
		}

		private PanelEstadoServicioAFIP getPanelEstadoAuthServer() {
			if (panelEstadoAuthServer == null) {
				panelEstadoAuthServer = new PanelEstadoServicioAFIP(getEstadoAFIP().getAuthServer());
			}
			return panelEstadoAuthServer;
		}

		private PanelEstadoServicioAFIP getPanelEstadoDBServer() {
			if (panelEstadoDBServer == null) {
				panelEstadoDBServer = new PanelEstadoServicioAFIP(getEstadoAFIP().getDbServer());
			}
			return panelEstadoDBServer;
		}

		private PanelEstadoServicioAFIP getPanelPruebaAuth() {
			if (panelPruebaAuth == null) {
				panelPruebaAuth = new PanelEstadoServicioAFIP(getEstadoAFIP().getPruebaAutenticacion());
			}
			return panelPruebaAuth;
		}

		private JLabel getLblUltimaFCAutorizada() {
			if (lblUltimaFCAutorizada == null) {
				lblUltimaFCAutorizada = crearLblUltimoDocumentoAutorizado("Última FC \"A\" autorizada: " + getEstadoAFIP().getUltimaFacturaAutorizada());
			}
			return lblUltimaFCAutorizada;
		}

		private JLabel getLblUltimaNDAutorizada() {
			if (lblUltimaNDAutorizada == null) {
				lblUltimaNDAutorizada = crearLblUltimoDocumentoAutorizado("Última ND \"A\" autorizada: " + getEstadoAFIP().getUltimaNDAutorizada());
			}
			return lblUltimaNDAutorizada;
		}

		private JLabel getLblUltimaNCAutorizada() {
			if (lblUltimaNCAutorizada == null) {
				lblUltimaNCAutorizada = crearLblUltimoDocumentoAutorizado("Última NC \"A\" autorizada: " + getEstadoAFIP().getUltimaNCAutorizada());
			}
			return lblUltimaNCAutorizada;
		}

		private JLabel crearLblUltimoDocumentoAutorizado(String text) {
			JLabel lbl = new JLabel(text);
			Font font = lbl.getFont();
			lbl.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
			return lbl;
		}

		private class PanelEstadoServicioAFIP extends JPanel {

			private static final long serialVersionUID = -1697432342219657805L;

			public PanelEstadoServicioAFIP(EstadoAFIPWrapper estado) {
				setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
				JLabel lblNombre = new JLabel(estado.getNombre());
				Font font = lblNombre.getFont();
				lblNombre.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
				add(lblNombre);
				JLabel lblEstado = new JLabel();
				if (estado.isOK()) {
					lblEstado.setIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/ok.png"));
				} else {
					lblEstado.setIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/delete.png"));
				}
				add(lblEstado);
				setPreferredSize(new Dimension(150, 60));
			}
		}
	}

	private static class PanelEjecutarServicioAFIP extends JPanel {

		private static final long serialVersionUID = 4790766642401045961L;

		private JComboBox cmbServicios;
		private JButton btnEjecutar;
		private JPanel panelResultados;

		public PanelEjecutarServicioAFIP() {
			JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
			panelNorte.add(new JLabel("Servicio: "));
			panelNorte.add(getCmbServicios());
			panelNorte.add(getBtnEjecutar());
			add(panelNorte, BorderLayout.NORTH);
			panelResultados = new JPanel();
			add(panelResultados, BorderLayout.CENTER);
		}

		public JComboBox getCmbServicios() {
			if (cmbServicios == null) {
				cmbServicios = new JComboBox();
				GuiUtil.llenarCombo(cmbServicios, Arrays.asList(EServicioAFIP.values()), true);
			}
			return cmbServicios;
		}

		public JButton getBtnEjecutar() {
			if (btnEjecutar == null) {
				btnEjecutar = new JButton("Ejecutar");
				btnEjecutar.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						EServicioAFIP servicio = (EServicioAFIP) getCmbServicios().getSelectedItem();
						try {
							panelResultados.removeAll();
							servicio.ejecutar(panelResultados);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
			return btnEjecutar;
		}

		private enum EServicioAFIP {
			TIPOS_DOC("Ver tipos de documento", new WrapperServicio<DocTipoResponse>() {

				@Override
				protected DocTipoResponse internalEjecutar() throws RemoteException {
					DocTipoResponse tiposDoc = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class).getTiposDoc();
					if (tiposDoc == null || tiposDoc.getResultGet() == null) {
						return null;
					}
					return tiposDoc;
				}

				@Override
				protected AFIPRenderer<DocTipoResponse> getRenderer() {
					return new TablaRenderer<DocTipoResponse>() {

						@Override
						protected void agregarRegistros(FWJTable tabla, DocTipoResponse object) {
							for (DocTipo tipo : object.getResultGet()) {
								tabla.addRow(new Object[] { tipo.getId(), tipo.getDesc() });
							}
						}

					};
				}
			}), //
			TIPOS_IVA("Ver tipos de IVA", new WrapperServicio<IvaTipoResponse>() {

				@Override
				protected IvaTipoResponse internalEjecutar() throws RemoteException {
					IvaTipoResponse tiposIVA = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class).getTiposIVA();
					if (tiposIVA == null || tiposIVA.getResultGet() == null) {
						return null;
					}
					return tiposIVA;
				}

				@Override
				protected AFIPRenderer<IvaTipoResponse> getRenderer() {
					return new TablaRenderer<IvaTipoResponse>() {

						@Override
						protected void agregarRegistros(FWJTable tabla, IvaTipoResponse object) {
							for (IvaTipo tipo : object.getResultGet()) {
								tabla.addRow(new Object[] { tipo.getId(), tipo.getDesc() });
							}
						}
					};
				}
			}), //
			TIPOS_MONEDA("Ver tipos de moneda", new WrapperServicio<MonedaResponse>() {

				@Override
				protected MonedaResponse internalEjecutar() throws RemoteException {
					MonedaResponse tiposMoneda = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class).getTiposMoneda();
					if (tiposMoneda == null || tiposMoneda.getResultGet() == null) {
						return null;
					}
					return tiposMoneda;
				}

				@Override
				protected AFIPRenderer<MonedaResponse> getRenderer() {
					return new TablaRenderer<MonedaResponse>() {

						@Override
						protected void agregarRegistros(FWJTable tabla, MonedaResponse object) {
							for (Moneda tipo : object.getResultGet()) {
								tabla.addRow(new Object[] { tipo.getId(), tipo.getDesc() });
							}
						}
					};
				}
			}), //
			TIPOS_COMPROBANTE("Ver tipos de comprobantes", new WrapperServicio<CbteTipoResponse>() {

				@Override
				protected CbteTipoResponse internalEjecutar() throws RemoteException {
					CbteTipoResponse tiposComprobante = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class).getTiposComprobante();
					if (tiposComprobante == null || tiposComprobante.getResultGet() == null) {
						return null;
					}
					return tiposComprobante;
				}

				@Override
				protected AFIPRenderer<CbteTipoResponse> getRenderer() {
					return new TablaRenderer<CbteTipoResponse>() {

						@Override
						protected void agregarRegistros(FWJTable tabla, CbteTipoResponse object) {
							for (CbteTipo tipo : object.getResultGet()) {
								tabla.addRow(new Object[] { tipo.getId(), tipo.getDesc() });
							}
						}
					};
				}
			}), //
			CONSULTAR_COMPROBANTE("Consultar comprobante", new WrapperServicio<FECompConsultaResponse>() {

				@Override
				protected FECompConsultaResponse internalEjecutar() throws RemoteException {
					FWJTextField txtNroComprobante = new FWJTextField(7);
					JComboBox cmbTipoComprobante = new JComboBox();
					GuiUtil.llenarCombo(cmbTipoComprobante, Arrays.asList(ETipoDocumento.getTiposDocumentoConAFIP()), true);

					JPanel panel = new JPanel(new GridLayout(0,1));
					panel.add(new JLabel("Tipo doc.:"));
					panel.add(cmbTipoComprobante);
					panel.add(new JLabel("Nro. Doc.:"));
					panel.add(txtNroComprobante);

					boolean ok = false;
					do {
						int result = JOptionPane.showConfirmDialog(null, panel, "Ingrese tipo y nro de documento:", JOptionPane.OK_CANCEL_OPTION);
						if (result != JOptionPane.OK_OPTION) {
							return new FECompConsultaResponse();
						}
						if (txtNroComprobante.getText().trim().length()==0 || !GenericUtils.esNumerico(txtNroComprobante.getText())) {
							FWJOptionPane.showErrorMessage(null, "Ingreso incorrecto", "error");
						} else {
							ok = true;
						}
					} while (!ok);
					return GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class).consultarDatosDocumentoIngresado(((ETipoDocumento)cmbTipoComprobante.getSelectedItem()).getIdTipoDocAFIP(), Integer.valueOf(txtNroComprobante.getText()));
				}

				@Override
				protected AFIPRenderer<FECompConsultaResponse> getRenderer() {
					return new TablaRenderer<FECompConsultaResponse>() {

						@Override
						protected void agregarRegistros(FWJTable tabla, FECompConsultaResponse object) {
							if (object.getResultGet() == null) {
								if (object.getErrors()!= null && object.getErrors().length > 0) {
									tabla.addRow(new Object[] { "Error", object.getErrors()[0] });
								}
								return;
							}
							tabla.addRow(new Object[] { "Nro.", object.getResultGet().getDocNro() });
							tabla.addRow(new Object[] { "CAE", object.getResultGet().getCodAutorizacion() });
							tabla.addRow(new Object[] { "Vencimiento", object.getResultGet().getFchVto() });
						}
						
						protected String getPrimerHeader() {
							return "Dato";
						};
						
						protected String getSegundoHeader() {
							return "Valor";
						};
					};
				}
			});

			private <T> EServicioAFIP(String nombre, WrapperServicio<T> wrapper) {
				this.nombre = nombre;
				this.wrapper = wrapper;
			}

			public void ejecutar(JPanel panelResultado) throws RemoteException {
				wrapper.ejecutar(panelResultado);
			}

			private String nombre;
			private WrapperServicio<?> wrapper;

			@Override
			public String toString() {
				return nombre;
			}
		}

		private static abstract class TablaRenderer<T> extends AFIPRenderer<T> {

			@Override
			public void render(T object, JPanel panelResultado) {
				FWJTable tabla = new FWJTable(0, 2);
				tabla.setStringColumn(0, getPrimerHeader(), 30, 30, true);
				tabla.setStringColumn(1, getSegundoHeader(), 140, 140, true);
				tabla.setHeaderAlignment(0, FWJTable.CENTER_ALIGN);
				tabla.setHeaderAlignment(1, FWJTable.CENTER_ALIGN);
				agregarRegistros(tabla, object);
				agregarTabla(tabla, panelResultado);
			}

			protected String getPrimerHeader() {
				return "ID";
			}
			
			protected String getSegundoHeader() {
				return "Tipo";
			}

			protected abstract void agregarRegistros(FWJTable tabla, T object);

			public void agregarTabla(FWJTable tabla, JPanel panelResultado) {
				JScrollPane jsp = new JScrollPane(tabla, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jsp.setSize(new Dimension(200, 130));
				jsp.setPreferredSize(new Dimension(200, 130));
				panelResultado.add(jsp, BorderLayout.CENTER);
				panelResultado.invalidate();
				panelResultado.repaint();
				panelResultado.updateUI();
			}
		}

		private static abstract class WrapperServicio<T> {

			public void ejecutar(JPanel panelResultado) throws RemoteException {
				T resultado = internalEjecutar();
				if (resultado == null) {
					FWJOptionPane.showErrorMessage(null, "Ha ocurrudo un error", "Error");
					return;
				}
				getRenderer().render(resultado, panelResultado);
			}

			protected abstract AFIPRenderer<T> getRenderer();

			protected abstract T internalEjecutar() throws RemoteException;
		}

		private static abstract class AFIPRenderer<T> {
			public abstract void render(T object, JPanel panelResultado);
		}
	}
}
