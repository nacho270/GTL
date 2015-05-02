package ar.com.textillevel.gui.modulos.personal.abm.empleados;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.com.textillevel.entidades.gente.InfoDireccion;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;
import ar.com.textillevel.util.GTLBeanFactory;

public class PanelDomicilioCompleto extends JPanel{

	private static final long serialVersionUID = 5946598740293668410L;

	private PanelDomicilio panelDomicilio;
	private CLJNumericTextField txtNumero;
	private CLJTextField txtPiso;
	private CLJTextField txtDto;
	
	private final Frame owner;
	
	private List<InfoLocalidad> infoLocalidadList;
	private InfoLocalidadFacadeRemote infoLocalidadRemote;
	
	public PanelDomicilioCompleto(Frame padre){
		this.owner = padre;
		this.setLayout(new GridBagLayout());
		add(getPanelDomicilio(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
	}
	
	public boolean validar(){
		boolean domicilioOk =getPanelDomicilio().validar();
		if(!domicilioOk){
			return false;
		}
		if(getTxtNumero().getValueWithNull()==null){
			CLJOptionPane.showErrorMessage(this, "Debe indicar el número del domicilio", "Error");
			getTxtNumero().requestFocus();
			return false;
		}
		return true;
	}
	
	public void setDatos(Domicilio domicilio){
		InfoDireccion infoDir = new InfoDireccion();
		infoDir.setDireccion(domicilio.getCalle());
		infoDir.setLocalidad(domicilio.getInfoLocalidad());
		getPanelDomicilio().getPanelDireccion().setDireccion(infoDir, getInfoLocalidadList());
		getTxtDto().setText(domicilio.getDepartamento());
		getTxtNumero().setValue(domicilio.getNumero().longValue());
		getTxtPiso().setText(domicilio.getPiso());
		getPanelDomicilio().getPanelTelefono().setFullDatos(domicilio.getTelefono());
	}
	
	public PanelDomicilio getPanelDomicilio() {
		if(panelDomicilio == null){
			panelDomicilio = new PanelDomicilio(owner);
			GridBagConstraints gc = new GridBagConstraints();
			
			gc.gridx = 0;
			gc.gridy = 1;
			gc.insets = new Insets(5, 10, 5, 0);
			gc.anchor = GridBagConstraints.WEST;
			panelDomicilio.getPanelDireccion().add(new JLabel("NRO: "), gc);
			
			gc.gridx = 1;
			gc.gridy = 1;
			gc.insets = new Insets(5, 0, 5, 0);
			gc.anchor = GridBagConstraints.WEST;
			panelDomicilio.getPanelDireccion().add(getTxtNumero(), gc);
			
			gc.gridx = 2;
			gc.gridy = 1;
			gc.insets = new Insets(5, 10, 5, 0);
			gc.anchor = GridBagConstraints.WEST;
			panelDomicilio.getPanelDireccion().add(new JLabel("PISO: "), gc);
			
			gc.gridx = 3;
			gc.gridy = 1;
			gc.insets = new Insets(5, 0, 5, 0);
			gc.anchor = GridBagConstraints.WEST;
			panelDomicilio.getPanelDireccion().add(getTxtPiso(), gc);
			
			gc.gridx = 4;
			gc.gridy = 1;
			gc.insets = new Insets(5, 0, 5, 0);
			gc.anchor = GridBagConstraints.WEST;
			panelDomicilio.getPanelDireccion().add(new JLabel("DTO: "), gc);
			
			gc.gridx = 5;
			gc.gridy = 1;
			gc.insets = new Insets(5, 0, 5, 0);
			gc.anchor = GridBagConstraints.WEST;
			panelDomicilio.getPanelDireccion().add(getTxtDto(), gc);
		}
		return panelDomicilio;
	}
	
	public CLJNumericTextField getTxtNumero() {
		if(txtNumero == null){
			txtNumero = new CLJNumericTextField();
			txtNumero.setColumns(5);
		}
		return txtNumero;
	}

	public CLJTextField getTxtPiso() {
		if(txtPiso == null){
			txtPiso = new CLJTextField();
			txtPiso.setPreferredSize(new Dimension(50, 20));
		}
		return txtPiso;
	}
	
	public CLJTextField getTxtDto() {
		if(txtDto == null){
			txtDto = new CLJTextField();
			txtDto.setPreferredSize(new Dimension(50, 20));
		}
		return txtDto;
	}
	
	private InfoLocalidadFacadeRemote getInfoLocalidadRemote() {
		if(infoLocalidadRemote == null) {
			infoLocalidadRemote = GTLBeanFactory.getInstance().getBean2(InfoLocalidadFacadeRemote.class);
		}
		return infoLocalidadRemote;
	}

	private List<InfoLocalidad> getInfoLocalidadList() {
		if(infoLocalidadList == null) {
			infoLocalidadList = getInfoLocalidadRemote().getAllInfoLocalidad();
			InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
			infoLocalidadOTRO.setId(-1);
			infoLocalidadOTRO.setNombreLocalidad("OTRO");
			infoLocalidadList.add(infoLocalidadOTRO);
		}
		return infoLocalidadList;
	}
}
