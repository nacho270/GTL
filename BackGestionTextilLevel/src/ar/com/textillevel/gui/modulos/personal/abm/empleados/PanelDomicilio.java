package ar.com.textillevel.gui.modulos.personal.abm.empleados;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanDatosDireccion;
import ar.com.textillevel.gui.util.panels.PanDatosTelefono;
import ar.com.textillevel.gui.util.panels.PanDireccionEvent;
import ar.com.textillevel.gui.util.panels.PanDireccionEventListener;
import ar.com.textillevel.util.GTLBeanFactory;

public class PanelDomicilio extends JPanel {

	private static final long serialVersionUID = 3743224195605865157L;

	private PanDatosDireccion panelDireccion;
	private PanDatosTelefono panelTelefono;
	
	private List<InfoLocalidad> infoLocalidadList;
	private InfoLocalidadFacadeRemote infoLocalidadRemote;

	private final Frame frame;
	
	public PanelDomicilio(Frame padre){
		this.frame = padre;
		setLayout(new GridBagLayout());
		add(getPanelDireccion(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
		add(getPanelTelefono(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 1, 0));
		setBorder(BorderFactory.createTitledBorder("Domicilio"));
		getPanelDireccion().setDireccion(null, getInfoLocalidadList());
	}
	
	public boolean validar(){
		String validacionDireccion = getPanelDireccion().validar();
		if(validacionDireccion!=null){
			FWJOptionPane.showErrorMessage(this, validacionDireccion, "Error");
			return false;
		}
		String validacionTelefono = getPanelTelefono().validar();
		if(validacionTelefono !=null){
			FWJOptionPane.showErrorMessage(this, validacionTelefono, "Error");
			return false;
		}
		if(!StringUtil.isNullOrEmptyString(getPanelTelefono().getValorCodigoArea())){
			Integer codigoAreaLocalidad = getPanelDireccion().getInfoDireccion().getLocalidad().getCodigoArea();
			Integer codigoAreaIngresado = Integer.valueOf(getPanelTelefono().getValorCodigoArea());
			if(!codigoAreaIngresado.equals(codigoAreaLocalidad)){
				if(FWJOptionPane.showQuestionMessage(this, "El código de área ingresado para la localidad es incorrecto. Esto se modificará automáticamente. Desea continuar?", "Pregunta")==FWJOptionPane.NO_OPTION){
					return false;
				}else{
					getPanelTelefono().setValorCodigoArea(String.valueOf(codigoAreaLocalidad));
				}
			}
		}
		return true;
	}
	
	public PanDatosDireccion getPanelDireccion() {
		if(panelDireccion == null){
			panelDireccion = new PanDatosDireccion(frame, "Dirección");
			panelDireccion.addPanDireccionListener(new PanDireccionEventListener() {

				public void newInfoLocalidadAdded(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					infoLocalidadList.clear();
					infoLocalidadList.addAll(getInfoLocalidadRemote().getAllInfoLocalidad());
					InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
					infoLocalidadOTRO.setId(PanDatosDireccion.OTRO);
					infoLocalidadOTRO.setNombreLocalidad("OTRO");
					infoLocalidadList.add(infoLocalidadOTRO);
					getPanelDireccion().seleccionarLocalidad(infoLocalidadSelected);
				}

				public void newInfoLocalidadSelected(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					getPanelTelefono().setValorCodigoArea(String.valueOf(infoLocalidadSelected.getCodigoArea()));
					updateUI();
				}
			});
		}
		return panelDireccion;
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
	
	public PanDatosTelefono getPanelTelefono() {
		if(panelTelefono == null){
			panelTelefono = new PanDatosTelefono("Teléfono", "-");
		}
		return panelTelefono;
	}
}
