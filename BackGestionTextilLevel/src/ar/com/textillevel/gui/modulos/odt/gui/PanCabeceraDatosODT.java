package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJTextField;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.gui.acciones.RemitoEntradaLinkeableLabel;
import ar.com.textillevel.gui.modulos.odt.util.ODTDatosMostradoHelper;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.ODTCodigoHelper;

public class PanCabeceraDatosODT extends JPanel {

	private static final long serialVersionUID = 1L;

	
	private CLJTextField txtProducto;
	
	private CLJTextField txtTotalMetros;
	private CLJTextField txtTotalKilos;
	private CLJTextField txtGramaje;
	private RemitoEntradaLinkeableLabel remitoLinkeableLabel;
	
	private CLJTextField txtCliente;
	private CLJTextField txtTotalPiezas;
	private CLJTextField txtArticulo;
	private CLJTextField txtColor;

	private CLJTextField txtTarima;
	private CLJTextField txtMaquina;
	private CLJTextField txtAnchoCrudo;
	private CLJTextField txtAnchoFinal;
	
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
		JLabel lblTitlo = new JLabel("ORDEN DE TRABAJO N° " + ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
		Font fuente = lblTitlo.getFont();
		Font fuenteNueva = new Font(fuente.getFontName(), Font.BOLD, 25);
		lblTitlo.setFont(fuenteNueva);
		lblTitlo.setHorizontalAlignment(JLabel.CENTER);
		lblTitlo.setForeground(Color.RED.darker());
		return lblTitlo;
	}

	private CLJTextField getTxtProducto() {
		if(txtProducto == null) {
			txtProducto = new CLJTextField();
			txtProducto.setText(odt.getProducto().toString());
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

	private CLJTextField getTxtTotalMetros() {
		if(txtTotalMetros == null) {
			txtTotalMetros = new CLJTextField();
			if(remitoEntrada.getId() != null) {
				txtTotalMetros.setText(GenericUtils.getDecimalFormat().format(odt.getTotalMetros().doubleValue()));
			}
			txtTotalMetros.setEnabled(false);
		}
		return txtTotalMetros;
	}

	private CLJTextField getTxtTotalKilos() {
		if(txtTotalKilos == null) {
			txtTotalKilos = new CLJTextField();
			if(remitoEntrada.getId() != null) {
				txtTotalKilos.setText(GenericUtils.getDecimalFormat().format(remitoEntrada.getPesoTotal().doubleValue()));
			}
			txtTotalKilos.setEnabled(false);
		}
		return txtTotalKilos;
	}

	private CLJTextField getTxtGramaje() {
		if(txtGramaje == null) {
			txtGramaje = new CLJTextField();
			txtGramaje.setText(odtDatosHelper.getDescGramaje());
			txtGramaje.setEnabled(false);
		}
		return txtGramaje;
	}

	private CLJTextField getTxtCliente() {
		if(txtCliente == null) {
			txtCliente = new CLJTextField();
			txtCliente.setText(odtDatosHelper.getDescCliente());
			txtCliente.setEnabled(false);
		}
		return txtCliente;
	}

	private CLJTextField getTxtTotalPiezas() {
		if(txtTotalPiezas == null) {
			txtTotalPiezas = new CLJTextField();
			txtTotalPiezas.setText(String.valueOf(odt.getPiezas().size()));
			txtTotalPiezas.setEnabled(false);
		}
		return txtTotalPiezas;
	}

	private CLJTextField getTxtArticulo() {
		if(txtArticulo == null) {
			txtArticulo = new CLJTextField();
			txtArticulo.setText(odtDatosHelper.getDescArticulo());
			txtArticulo.setEnabled(false);
		}
		return txtArticulo;
	}
	
	private CLJTextField getTxtColor() {
		if(txtColor == null) {
			txtColor = new CLJTextField();
			txtColor.setText(odtDatosHelper.getDescColor());
			txtColor.setEnabled(false);
		}
		return txtColor;
	}

	private CLJTextField getTxtTarima() {
		if(txtTarima == null) {
			txtTarima = new CLJTextField();
			txtTarima.setText(odtDatosHelper.getDescTarima());
			txtTarima.setEnabled(false);
		}
		return txtTarima;
	}

	private CLJTextField getTxtMaquina() {
		if(txtMaquina == null) {
			txtMaquina = new CLJTextField();
			txtMaquina.setText(null);
			txtMaquina.setEnabled(false);
		}
		return txtMaquina;
	}

	private CLJTextField getTxtAnchoCrudo() {
		if(txtAnchoCrudo == null) {
			txtAnchoCrudo = new CLJTextField();
			txtAnchoCrudo.setText(odtDatosHelper.getDescAnchoCrudo());
			txtAnchoCrudo.setEnabled(false);
		}
		return txtAnchoCrudo;
	}

	private CLJTextField getTxtAnchoFinal() {
		if(txtAnchoFinal == null) {
			txtAnchoFinal = new CLJTextField();
			txtAnchoFinal.setText(odtDatosHelper.getDescAnchoFinal());
			txtAnchoFinal.setEnabled(false);
		}
		return txtAnchoFinal;
	}

}