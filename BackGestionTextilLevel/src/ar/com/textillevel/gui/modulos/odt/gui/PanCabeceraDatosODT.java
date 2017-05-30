package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.gui.acciones.RemitoEntradaLinkeableLabel;
import ar.com.textillevel.gui.acciones.odt.componentes.ODTLinkeableLabel;
import ar.com.textillevel.gui.modulos.odt.util.ODTDatosMostradoHelper;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class PanCabeceraDatosODT extends JPanel {

	private static final long serialVersionUID = 1L;

	
	private FWJTextField txtProducto;
	
	private FWJTextField txtTotalMetros;
	private FWJTextField txtTotalKilos;
	private FWJTextField txtGramaje;
	private RemitoEntradaLinkeableLabel remitoLinkeableLabel;
	
	private FWJTextField txtCliente;
	private FWJTextField txtTotalPiezas;
	private FWJTextField txtArticulo;
	private FWJTextField txtColor;

	private FWJTextField txtTarima;
	private FWJTextField txtMaquina;
	private FWJTextField txtAnchoCrudo;
	private FWJTextField txtAnchoFinal;
	
	private OrdenDeTrabajo odt;
	private RemitoEntrada remitoEntrada;
	private ODTDatosMostradoHelper odtDatosHelper;

	public PanCabeceraDatosODT(OrdenDeTrabajo odt) {
		super();
		this.odt = odt;
		this.remitoEntrada = odt.getRemito();
		this.odtDatosHelper = new ODTDatosMostradoHelper(odt);
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());
		add(createLabelTitulo(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 8, 1, 1, 0));

		add(new JLabel(" PRODUCTO: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
		add(getTxtProducto(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 5, 1, 0, 0));

		add(new JLabel(" METROS:"), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTotalMetros(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(new JLabel(" KILOS:"), GenericUtils.createGridBagConstraints(2, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTotalKilos(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(new JLabel(" GRAMAJE:"), GenericUtils.createGridBagConstraints(4, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtGramaje(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(new JLabel(" REMITO:"), GenericUtils.createGridBagConstraints(6, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getRemitoLinkeableLabel(), GenericUtils.createGridBagConstraints(7, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

		add(new JLabel(" CLIENTE:"), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtCliente(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(new JLabel(" PIEZAS:"), GenericUtils.createGridBagConstraints(2, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTotalPiezas(), GenericUtils.createGridBagConstraints(3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(new JLabel(" ARTICULO:"), GenericUtils.createGridBagConstraints(4, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtArticulo(), GenericUtils.createGridBagConstraints(5, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.3, 0));
		add(new JLabel(" COLOR:"), GenericUtils.createGridBagConstraints(6, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtColor(), GenericUtils.createGridBagConstraints(7, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.3, 0));

		add(new JLabel(" TARIMA:"), GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTarima(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.3, 0));
		add(new JLabel(" MAQUINA:"), GenericUtils.createGridBagConstraints(2, 4,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtMaquina(), GenericUtils.createGridBagConstraints(3, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.3, 0));
		add(new JLabel(" ANCHO CRUDO:"), GenericUtils.createGridBagConstraints(4, 4,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtAnchoCrudo(), GenericUtils.createGridBagConstraints(5, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(new JLabel(" ANCHO FINAL:"), GenericUtils.createGridBagConstraints(6, 4,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtAnchoFinal(), GenericUtils.createGridBagConstraints(7, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JLabel createLabelTitulo() {
		ODTLinkeableLabel lblTitlo = new ODTLinkeableLabel(odt, "ORDEN DE TRABAJO N° ");
		Font fuente = lblTitlo.getFont();
		Font fuenteNueva = new Font(fuente.getFontName(), Font.BOLD, 25);
		lblTitlo.setFont(fuenteNueva);
		lblTitlo.setHorizontalAlignment(JLabel.CENTER);
		lblTitlo.setForeground(Color.RED.darker());
		return lblTitlo;
	}

	private FWJTextField getTxtProducto() {
		if(txtProducto == null) {
			txtProducto = new FWJTextField();
			txtProducto.setText(odt.getIProductoParaODT().toString());
			txtProducto.setEditable(false);
		}
		return txtProducto;
	}

	private RemitoEntradaLinkeableLabel getRemitoLinkeableLabel() {
		if(remitoLinkeableLabel == null) {
			this.remitoLinkeableLabel = new RemitoEntradaLinkeableLabel();
			this.remitoLinkeableLabel.setRemito(odt.getRemito());
		}
		return remitoLinkeableLabel;
	}

	private FWJTextField getTxtTotalMetros() {
		if(txtTotalMetros == null) {
			txtTotalMetros = new FWJTextField();
			if(remitoEntrada.getId() != null) {
				txtTotalMetros.setText(GenericUtils.getDecimalFormat().format(odt.getRemito().getTotalMetros().doubleValue()));
			}
			txtTotalMetros.setEnabled(false);
		}
		return txtTotalMetros;
	}

	private FWJTextField getTxtTotalKilos() {
		if(txtTotalKilos == null) {
			txtTotalKilos = new FWJTextField();
			if(remitoEntrada.getId() != null) {
				txtTotalKilos.setText(GenericUtils.getDecimalFormat().format(remitoEntrada.getPesoTotal().doubleValue()));
			}
			txtTotalKilos.setEnabled(false);
		}
		return txtTotalKilos;
	}

	private FWJTextField getTxtGramaje() {
		if(txtGramaje == null) {
			txtGramaje = new FWJTextField();
			txtGramaje.setText(odtDatosHelper.getDescGramaje());
			txtGramaje.setEnabled(false);
		}
		return txtGramaje;
	}

	private FWJTextField getTxtCliente() {
		if(txtCliente == null) {
			txtCliente = new FWJTextField();
			txtCliente.setText(odtDatosHelper.getDescCliente());
			txtCliente.setEnabled(false);
		}
		return txtCliente;
	}

	private FWJTextField getTxtTotalPiezas() {
		if(txtTotalPiezas == null) {
			txtTotalPiezas = new FWJTextField();
			txtTotalPiezas.setText(String.valueOf(odt.getPiezas().size()));
			txtTotalPiezas.setEnabled(false);
		}
		return txtTotalPiezas;
	}

	private FWJTextField getTxtArticulo() {
		if(txtArticulo == null) {
			txtArticulo = new FWJTextField();
			txtArticulo.setText(odtDatosHelper.getDescArticulo());
			txtArticulo.setEnabled(false);
		}
		return txtArticulo;
	}
	
	private FWJTextField getTxtColor() {
		if(txtColor == null) {
			txtColor = new FWJTextField();
			txtColor.setText(odtDatosHelper.getDescColor());
			txtColor.setEnabled(false);
		}
		return txtColor;
	}

	private FWJTextField getTxtTarima() {
		if(txtTarima == null) {
			txtTarima = new FWJTextField();
			txtTarima.setText(odtDatosHelper.getDescTarima());
			txtTarima.setEnabled(false);
		}
		return txtTarima;
	}

	private FWJTextField getTxtMaquina() {
		if(txtMaquina == null) {
			txtMaquina = new FWJTextField();
			txtMaquina.setText(null);
			txtMaquina.setEnabled(false);
		}
		return txtMaquina;
	}

	private FWJTextField getTxtAnchoCrudo() {
		if(txtAnchoCrudo == null) {
			txtAnchoCrudo = new FWJTextField();
			txtAnchoCrudo.setText(odtDatosHelper.getDescAnchoCrudo());
			txtAnchoCrudo.setEnabled(false);
		}
		return txtAnchoCrudo;
	}

	private FWJTextField getTxtAnchoFinal() {
		if(txtAnchoFinal == null) {
			txtAnchoFinal = new FWJTextField();
			txtAnchoFinal.setText(odtDatosHelper.getDescAnchoFinal());
			txtAnchoFinal.setEnabled(false);
		}
		return txtAnchoFinal;
	}

}