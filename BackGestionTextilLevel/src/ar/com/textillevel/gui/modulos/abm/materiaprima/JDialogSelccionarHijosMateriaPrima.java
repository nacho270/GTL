package ar.com.textillevel.gui.modulos.abm.materiaprima;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSelccionarHijosMateriaPrima extends JDialog{

	private static final long serialVersionUID = -1182859205318727998L;
	
	private JButton btnCancelar;
	private JButton btnAceptar;
	private CLCheckBoxList<MateriaPrima> listaMps;

	private boolean acepto;
	private MateriaPrima materiaPrima;
	
	private List<MateriaPrima> materiasPrimasSeleccionadas;
	
	public JDialogSelccionarHijosMateriaPrima(Frame padre, MateriaPrima materiaPrimaActual) {
		super(padre);
		this.materiaPrima = materiaPrimaActual;
		this.materiasPrimasSeleccionadas = new ArrayList<MateriaPrima>(materiaPrimaActual.getMpHijas());
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(250, 400));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(new JScrollPane(getListaMps(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		add(panelSur, BorderLayout.SOUTH);		
	}

	public CLCheckBoxList<MateriaPrima> getListaMps() {
		if (listaMps == null) {
			listaMps = new CLCheckBoxList<MateriaPrima>() {

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
			List<MateriaPrima> mps = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class).getAllOrderByTipos(true, materiaPrima.getTipo());
			Object[] materiasPrimas = new Object[mps.size() - 1]; // para no incluir el actual
			int i=0;
			for(MateriaPrima m : mps) {
				if(m.getId().intValue() != materiaPrima.getId().intValue()) {
					materiasPrimas[i++] = m;
				}
			}
			getListaMps().setValues(materiasPrimas);
			for(MateriaPrima mp : materiaPrima.getMpHijas()) {
				getListaMps().setSelectedValue(mp, false);
			}
		}
		return listaMps;
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
		if(CLJOptionPane.showQuestionMessage(this, "Va a salir sin guardar. Esta seguro?", "Pregunta") == CLJOptionPane.YES_OPTION) {
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
}
