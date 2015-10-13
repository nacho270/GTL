package ar.com.textillevel.gui.acciones.odt;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.MaquinaFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.TipoMaquinaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarMaquina extends JDialog{

	private static final long serialVersionUID = 3641824246688379203L;

	public static final byte MODO_SIGUIENTE = 0;
	public static final byte MODO_ANTERIOR = 1;
	public static final byte MODO_SOLO_MAQUINA = 2;
	
	private final byte modoActual;
	private byte ordenAComparar;
	private Maquina maquinaElegida;
	private Integer idTipoMaquina;
	private Integer idMaquina;
	
	private JComboBox cmbTipoMaquina;
	private JComboBox cmbMaquinas;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private TipoMaquinaFacadeRemote tipoMaquinaFacade;
	private MaquinaFacadeRemote maquinaFacade;
	
	private boolean acepto;
	
	public JDialogSeleccionarMaquina(Frame padre, byte ordenAComparar, byte modo){
		super(padre);
		this.modoActual = modo;
		this.ordenAComparar = ordenAComparar;
		this.acepto = false;
		setUpComponentes();
		setUpScreen();
	}

	public JDialogSeleccionarMaquina(Dialog padre, Integer idTipoMaquina, Integer idMaquina){
		super(padre);
		this.modoActual = MODO_SOLO_MAQUINA;
		this.acepto = false;
		this.idTipoMaquina = idTipoMaquina;
		this.idMaquina = idMaquina;
		setUpComponentes();
		setUpScreen();
	}
	
	private void setUpScreen() {
		setTitle("Selección de maquina");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(300, 170));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		JPanel panelCentro = new JPanel(new GridBagLayout());
		panelCentro.add(new JLabel("Tipo de máquina"),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getCmbTipoMaquina(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		panelCentro.add(new JLabel("Máquina"),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getCmbMaquinas(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		add(panelCentro,BorderLayout.CENTER);
		
		JPanel panelbotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelbotones.add(getBtnAceptar());
		panelbotones.add(getBtnCancelar());
		add(panelbotones,BorderLayout.SOUTH);
	}

	public Maquina getMaquinaElegida() {
		return maquinaElegida;
	}

	public void setMaquinaElegida(Maquina maquinaElegida) {
		this.maquinaElegida = maquinaElegida;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public JComboBox getCmbTipoMaquina() {
		if(cmbTipoMaquina == null){
			cmbTipoMaquina = new JComboBox();
			List<TipoMaquina> tiposMaquina = null;
			if(this.modoActual!=MODO_SOLO_MAQUINA){
				tiposMaquina = getTipoMaquinaFacade().getAllOrderByOrden();
				CollectionUtils.filter(tiposMaquina, new Predicate() {
					public boolean evaluate(Object arg0) {
						TipoMaquina tm = (TipoMaquina)arg0;
						return (modoActual == MODO_SIGUIENTE && tm.getOrden()>ordenAComparar)||
								(modoActual == MODO_ANTERIOR && tm.getOrden()<ordenAComparar);
					}
				});
			}else{
				tiposMaquina = getTipoMaquinaFacade().getAllByIdTipo(getIdTipoMaquina());
				cmbTipoMaquina.setEnabled(false);
			}
			GuiUtil.llenarComboWithoutListener(cmbTipoMaquina, tiposMaquina, true);
			cmbTipoMaquina.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					getCmbMaquinas().removeAllItems();
					TipoMaquina tm = (TipoMaquina)getCmbTipoMaquina().getSelectedItem();
					if(tm!=null){
						GuiUtil.llenarCombo(getCmbMaquinas(),getMaquinaFacade().getAllByTipo(tm), true);
					}
				}
			});
		}
		return cmbTipoMaquina;
	}

	public JComboBox getCmbMaquinas() {
		if(cmbMaquinas == null){
			cmbMaquinas = new JComboBox();
			TipoMaquina tm = (TipoMaquina)getCmbTipoMaquina().getSelectedItem();
			if(tm!=null){
				List<Maquina> maquinas = getMaquinaFacade().getAllByTipo(tm);
				CollectionUtils.filter(maquinas, new Predicate() {
					public boolean evaluate(Object arg0) {
						if(idMaquina == null){
							return true;
						}
						return !idMaquina.equals(((Maquina)arg0).getId());
					}
				});
				GuiUtil.llenarCombo(cmbMaquinas,maquinas , true);	
			}
		}
		return cmbMaquinas;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Maquina maquinaElegida = (Maquina)getCmbMaquinas().getSelectedItem();
					if(maquinaElegida!=null){
						setMaquinaElegida(maquinaElegida);
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public TipoMaquinaFacadeRemote getTipoMaquinaFacade() {
		if(tipoMaquinaFacade == null){
			tipoMaquinaFacade = GTLBeanFactory.getInstance().getBean2(TipoMaquinaFacadeRemote.class);
		}
		return tipoMaquinaFacade;
	}

	public MaquinaFacadeRemote getMaquinaFacade() {
		if(maquinaFacade == null){
			maquinaFacade = GTLBeanFactory.getInstance().getBean2(MaquinaFacadeRemote.class);
		}
		return maquinaFacade;
	}

	public Integer getIdTipoMaquina() {
		return idTipoMaquina;
	}

	public void setIdTipoMaquina(Integer idTipoMaquina) {
		this.idTipoMaquina = idTipoMaquina;
	}

	public Integer getIdMaquina() {
		return idMaquina;
	}

	public void setIdMaquina(Integer idMaquina) {
		this.idMaquina = idMaquina;
	}
}
