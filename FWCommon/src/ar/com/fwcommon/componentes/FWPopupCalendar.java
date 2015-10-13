package ar.com.fwcommon.componentes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.util.DecorateUtil;

/**
 * Componente que muestra un calendario en un cuadro de diálogo emergente.
 */
public class FWPopupCalendar extends JDialog implements ActionListener {

	private static final long serialVersionUID = -6045914802334508034L;

	public static Font fuente;
	private Date date;
	private FWCalendar calendar;
	private JButton btnOk;
	private JButton btnCancel;
	private boolean canceled;
	private Date lastDate;
	private Date fechaMinima;
	private Date fechaMaxima;
	private static final String DEFAULT_TITLE = "Calendario";

	/** Método constructor */
	public FWPopupCalendar(Frame owner) {
		this(owner, DEFAULT_TITLE);
	}

	/**
	 * Método constructor.
	 * Construye el calendario y muestra el título especificado por <b>title</b>.
	 * @param title El título del cuadro de diálogo.
	 */
	public FWPopupCalendar(Frame owner, String title) {
		super(owner, title, true);
		this.date = new Date(System.currentTimeMillis());
		construct();
	}

	/**
	 * Método constructor.
	 * Construye el calendario y muestra el título especificado por <b>title</b>.
	 * @param title El título del cuadro de diálogo.
	 * @param fechaMinima
	 * @param fechaMaxima
	 */
	public FWPopupCalendar(Frame owner, String title, Date fechaMinima, Date fechaMaxima) {
		super(owner, title, true);
		this.date = new Date(System.currentTimeMillis());
		setFechaMinima(fechaMinima);
		setFechaMaxima(fechaMaxima);
		construct();
	}

	/**
	 * Método constructor.
	 * Construye el calendario y muestra la fecha especificada por <b>date</b>.
	 * @param date La fecha inicial del calendario.
	 */
	public FWPopupCalendar(Frame owner, Date date) {
		super(owner, DEFAULT_TITLE, true);
		this.date = date;
		construct();
	}


	/**
	 * Método constructor.
	 * Construye el calendario y muestra el título especificado por <b>title</b> y
	 * la fecha especificada por <b>date</b>.
	 * @param title El título del cuadro de diálogo.
	 * @param date La fecha inicial del calendario.
	 */
	public FWPopupCalendar(Frame owner, String title, Date date) {
		super(owner, title, true);
		this.date = date;
		construct();
	}

	//Construye graficamente el componente
	private void construct() {
		//Setea las propiedades del cuadro de diálogo
		setResizable(false);
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent evt) {
		        cancel();
		    }
		});
		//Agrega el calendario y los botones 'Aceptar'/'Cancelar'
		getContentPane().setLayout(new BorderLayout());
		if(getFechaMinima() != null && getFechaMaxima() != null) {
			calendar = new FWCalendar(date, getFechaMinima(), getFechaMaxima()) {
				public void dateSelected() {
					fechaSeleccionada();
				}
			};
		} else {
			calendar = new FWCalendar(date) {
				public void dateSelected() {
					fechaSeleccionada();
				}
			};
		}
		getContentPane().add(calendar, BorderLayout.CENTER);
		JPanel panButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		btnOk = new JButton("Aceptar");
		DecorateUtil.decorateButton(btnOk);
		panButtons.add(btnOk);
		btnOk.addActionListener(this);
		btnCancel = new JButton("Cancelar");
		DecorateUtil.decorateButton(btnCancel);
		panButtons.add(btnCancel);
		btnCancel.addActionListener(this);
		getContentPane().add(panButtons, BorderLayout.SOUTH);
		pack();
		center();
	}

	//Centra el componente en la pantalla
	private void center() {
		int width = ((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2);
		int height = ((Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
		setLocation(width, height);
	}

	//Manejo del evento de los botones 'Aceptar'/'Cancelar'
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == btnOk) {
			if(calendar.getSelectedDay() != -1) {
				calendar.setSelectedDate(calendar.constructDate());
				fechaSeleccionada();
			}
		} else {
			cancel();
		}
	}

	/**
	 * Cierra el diálogo dejando seleccionada la fecha que se encontraba seleccionaba al abrir el diálogo.
	 */
	private void cancel() {
		if(lastDate != null) {
            calendar.setSelectedDate(lastDate);
        }
		canceled = true;
		dispose();	
	}

	/**
	 * Se seleccionó una fecha.
	 */
	private void fechaSeleccionada() {
		canceled = false;
		dispose();
	}

	/**
	 * Devuelve el <b>calendario</b> asociado al cuadro de diálogo.
	 * @return calendar El calendario asociado al cuadro de diálogo.
	 * ar.com.fwcommon.componentes.CLCalendar
	 */
	public FWCalendar getCalendar() {
		return calendar;
	}

	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			lastDate = calendar.getSelectedDate();
		}
		super.setVisible(visible);
	}

	public Date getFechaMaxima() {
		return fechaMaxima;
	}

	public void setFechaMaxima(Date fechaMaxima) {
		this.fechaMaxima = fechaMaxima;
	}

	public Date getFechaMinima() {
		return fechaMinima;
	}

	public void setFechaMinima(Date fechaMinima) {
		this.fechaMinima = fechaMinima;
	}

}