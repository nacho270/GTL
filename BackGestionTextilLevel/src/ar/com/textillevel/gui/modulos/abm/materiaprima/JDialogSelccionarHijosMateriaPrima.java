package ar.com.textillevel.gui.modulos.abm.materiaprima;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSelccionarHijosMateriaPrima extends JDialog{

	private static final long serialVersionUID = -1182859205318727998L;
	
	private FWJTextField txtBusqueda;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private FWCheckBoxList<MateriaPrima> listaMps;

	private boolean acepto;
	private MateriaPrima materiaPrima;
	
	private List<MateriaPrima> mps;
	private List<MateriaPrima> materiasPrimasSeleccionadas;
	
	public JDialogSelccionarHijosMateriaPrima(Frame padre, MateriaPrima materiaPrimaActual) {
		super(padre);
		this.materiaPrima = materiaPrimaActual;
		this.materiasPrimasSeleccionadas = new ArrayList<MateriaPrima>(materiaPrimaActual.getMpHijas());
		boolean yaTieneHijas = !materiaPrima.getMpHijas().isEmpty();
		this.mps = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class).
				getAllOrderByTipos(yaTieneHijas, materiaPrima.getTipo());
		
		this.mps.remove(materiaPrimaActual);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Seleccionar hijos para " + materiaPrima.getDescripcion());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		setModal(true);
		setSize(new Dimension(350, 430));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelNorte.add(new JLabel("Buscar: "));
		panelNorte.add(getTxtBusqueda());
		add(panelNorte, BorderLayout.NORTH);

		add(new JScrollPane(getListaMps(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		add(panelSur, BorderLayout.SOUTH);
	}

	public FWCheckBoxList<MateriaPrima> getListaMps() {
		if (listaMps == null) {
			listaMps = new FWCheckBoxList<MateriaPrima>() {

				private static final long serialVersionUID = 8161545033580410261L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					MateriaPrima mp = (MateriaPrima) item;
					if(seleccionado) {
						materiasPrimasSeleccionadas.add(mp);
					} else {
						materiasPrimasSeleccionadas.remove(mp);
					}
				}
			};
			llenarListaCheckboxes(null);
		}
		return listaMps;
	}

	private void llenarListaCheckboxes(final String filter) {
		if (getListaMps().getValueIsAdjusting()) return;
		boolean yaTieneHijas = !materiaPrima.getMpHijas().isEmpty();
		List<MateriaPrima> mps2 = new ArrayList<MateriaPrima>(this.mps);
		if(!yaTieneHijas) {
			CollectionUtils.filter(mps2, new Predicate() {
				public boolean evaluate(Object arg0) {
					MateriaPrima mp = (MateriaPrima) arg0;
					boolean noEsLaQueEdito = mp.getId().intValue() != materiaPrima.getId().intValue();
					if (StringUtil.isNullOrEmpty(filter)) {
						return noEsLaQueEdito && mp.getMpHijas().isEmpty();
					}
					return noEsLaQueEdito && mp.getDescripcion().toUpperCase().contains(filter.toUpperCase());
				}
			});
		}
		getListaMps().setValues(mps2.toArray());
		for(MateriaPrima mp : materiaPrima.getMpHijas()) {
			getListaMps().setSelectedValue(mp, false);
		}
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public List<MateriaPrima> getMateriasPrimasSeleccionadas() {
		return materiasPrimasSeleccionadas;
	}

	private void salir() {
		if(FWJOptionPane.showQuestionMessage(this, "Va a salir sin guardar. Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}
	
	public JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public FWJTextField getTxtBusqueda() {
		if (txtBusqueda == null) {
			txtBusqueda = new FWJTextField();
			txtBusqueda.setPreferredSize(new Dimension(170, 20));
			txtBusqueda.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					llenarListaCheckboxes(txtBusqueda.getText());
				}
			});
		}
		return txtBusqueda;
	}
}
