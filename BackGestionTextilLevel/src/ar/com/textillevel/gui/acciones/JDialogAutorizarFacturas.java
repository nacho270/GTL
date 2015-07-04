package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.taglibs.string.util.StringW;

import net.sf.jasperreports.engine.JRException;
import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionfactura.ImpresionFacturaHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAutorizarFacturas extends JDialog {

	private static final long serialVersionUID = 7943905510082333400L;

	private PanelTablaFacturas panelTablaFacturas;
	private JButton btnAceptar;
	private DocumentoContableFacadeRemote docFacade;
	
	public JDialogAutorizarFacturas(Frame owner) {
		super(owner);
		setUpComponentes();
		setUpScreen();
		llenarTabla();
	}

	private void llenarTabla() {
		getPanelTablaFacturas().getTabla().removeAllRows();
		List<DocumentoContableCliente> documentosContablesSinCAE = getDocFacade().getDocumentosContablesSinCAE();
		getPanelTablaFacturas().agregarElementos(documentosContablesSinCAE);
	}

	private void setUpScreen(){
		setTitle("Visualizador de facturas sin autorización AFIP");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(700, 200));
		setResizable(false);
		GuiUtil.centrar(this);
		setModal(true);
	}
	
	private void setUpComponentes() {
		add(getPanelTablaFacturas(), BorderLayout.CENTER);
		JPanel pSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pSur.add(getBtnAceptar());
		add(pSur, BorderLayout.SOUTH);
	}

	public PanelTablaFacturas getPanelTablaFacturas() {
		if (panelTablaFacturas == null) {
			panelTablaFacturas = new PanelTablaFacturas();
		}
		return panelTablaFacturas;
	}

	public JButton getBtnAceptar() {
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
	
	private class PanelTablaFacturas extends PanelTabla<DocumentoContableCliente> {

		private static final long serialVersionUID = -7073521178403678770L;

		private static final int CANT_COLS = 6;
		private static final int COL_TIPO = 0;
		private static final int COL_NUMERO = 1;
		private static final int COL_FECHA = 2;
		private static final int COL_CLIENTE = 3;
		private static final int COL_IMPORTE = 4;
		private static final int COL_OBJ = 5;
		
		private JButton btnAutorizar;
		
		public PanelTablaFacturas() {
			agregarBoton(getBtnAutorizar());
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_TIPO, "Documento", 100, 100, true);
			tabla.setIntColumn(COL_NUMERO, "Número", 60, true);
			tabla.setDateColumn(COL_FECHA, "Fecha", 70, true);
			tabla.setStringColumn(COL_CLIENTE, "Cliente", 240, 240, true);
			tabla.setStringColumn(COL_IMPORTE, "Importe", 100, 100, true);
			tabla.setStringColumn(COL_OBJ,"",0);
			tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				
				public void valueChanged(ListSelectionEvent e) {
					getBtnAutorizar().setEnabled(getTabla().getSelectedRowCount() > 0);
					
				}
			});
			
			return tabla;
		}

		@Override
		protected void agregarElemento(DocumentoContableCliente elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_TIPO] = elemento.getTipoDocumento().toString().replaceAll("_", " ");
			row[COL_NUMERO] = elemento.getNroFactura();
			row[COL_FECHA] = DateUtil.dateToString(elemento.getFechaEmision(), DateUtil.SHORT_DATE);
			row[COL_CLIENTE] = elemento.getCliente().getNroCliente() + " - " + elemento.getCliente().getRazonSocial();
			row[COL_IMPORTE] = GenericUtils.getDecimalFormat().format(elemento.getMontoTotal().doubleValue());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected DocumentoContableCliente getElemento(int fila) {
			return (DocumentoContableCliente) getTabla().getValueAt(fila, COL_OBJ);
		}

		protected String validarElemento(int fila) {
			return null;
		}

		public JButton getBtnAutorizar() {
			if(btnAutorizar == null){
				btnAutorizar = new JButton("Autorizar");
				btnAutorizar.setEnabled(false);
				btnAutorizar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int[] selectedRows = getTabla().getSelectedRows();
						if(selectedRows[0] != 0) {
							CLJOptionPane.showErrorMessage(JDialogAutorizarFacturas.this, "Debe seleccionar la primer factura", "Error");
							return;
						}
						
						for(int i = 0; i<selectedRows.length;i++){
							if(selectedRows[i] != i) {
								CLJOptionPane.showErrorMessage(JDialogAutorizarFacturas.this, "Debe seleccionar facturas consecutivas", "Error");
								return;
							}
						}
						
						try {
							for(int i : selectedRows) {
								DocumentoContableCliente docAut = getDocFacade().autorizarDocumentoContableAFIP(getElemento(i));
								if(CLJOptionPane.showQuestionMessage(JDialogAutorizarFacturas.this, "El documento ha sido autorizado con exito.\nDesea imprimir el documento?", "Pregunta") == CLJOptionPane.YES_OPTION){
									imprimir(docAut);
								}
							}
							llenarTabla();
						} catch (ValidacionExceptionSinRollback e1) {
							CLJOptionPane.showErrorMessage(JDialogAutorizarFacturas.this, e1.getMensajeError(), "Error");
						} catch (ValidacionException e1) {
							CLJOptionPane.showErrorMessage(JDialogAutorizarFacturas.this, e1.getMensajeError(), "Error");
						}
					}

				});
			}
			return btnAutorizar;
		}
	}

	private void imprimir(DocumentoContableCliente docAut) {
		boolean ok = false;
		do {
			String input = JOptionPane.showInputDialog(JDialogAutorizarFacturas.this, "Ingrese la cantidad de copias: ", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				break;
			}
			if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
				CLJOptionPane.showErrorMessage(JDialogAutorizarFacturas.this, "Ingreso incorrecto", "error");
			} else {
				ok = true;
				try{
					ImpresionFacturaHandler ifHandler = new ImpresionFacturaHandler(docAut, input);
					ifHandler.imprimir();
				}catch(CLException cle){
					BossError.gestionarError(cle);
				}catch(JRException jre){
					jre.printStackTrace();
					CLJOptionPane.showErrorMessage(JDialogAutorizarFacturas.this, "Se ha producido un error al imprimir.", "Error");
				} catch (ValidacionException e) {
					CLJOptionPane.showErrorMessage(JDialogAutorizarFacturas.this, StringW.wordWrap(e.getMensajeError()), "Error");
				}
			}
		} while (!ok);
	}

	public DocumentoContableFacadeRemote getDocFacade() {
		if(docFacade == null){
			docFacade = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class);
		}
		return docFacade;
	}
}
