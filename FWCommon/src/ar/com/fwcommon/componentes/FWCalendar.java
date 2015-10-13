package ar.com.fwcommon.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.DecorateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.ImageUtil;

/**
 * Componente que muestra un calendario en pantalla. Extiende de JPanel.
 */
public class FWCalendar extends JPanel implements MouseListener, KeyListener, FocusListener, ItemListener {

	private static final long serialVersionUID = 4997810948781011115L;

	private static final String[] MONTHS = new String[] {
		"Enero",
		"Febrero",
		"Marzo",
		"Abril",
		"Mayo",
		"Junio",
		"Julio",
		"Agosto",
		"Septiembre",
		"Octubre",
		"Noviembre",
		"Diciembre"
	};

	private static final String[] DAYS = new String[] {
		"Dom",
		"Lun",
		"Mar",
		"Mié",
		"Jue",
		"Vie",
		"Sáb"
	};

	private GregorianCalendar calendar;
	private Date selectedDate;
	private Date minDate;
	private Date maxDate;
	private JLabel[][] days;
	private int offset;
	private int lastDay;
	private JLabel day;
	private FocusablePanel daysGrid;
	private JComboBox cmbMonth;
	private JComboBox cmbYear;
    private JButton btnBack;
    private JButton btnForward;
    private Color foregroundLastDaySelected = null;
    private int mesOffset = 0;
    private boolean update = true;
	private static final int FIRST_YEAR = 1900;
	private static final int LAST_YEAR = 2100;
    private static final int DAYS_GRID_WIDTH = 250;
	private static final int DAYS_GRID_HEIGHT = 165;
	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    private static final String DEFAULT_ICON_BTN_FORWARD = "ar/com/fwcommon/imagenes/b_cal_der.png";
    private static final String DEFAULT_ICON_BTN_BACK = "ar/com/fwcommon/imagenes/b_cal_izq.png";
    public static String forwardIcon;
    public static String backIcon;
	public static Color daysForeground = (Color)UIManager.get("List.foreground");
	public static Color daysGridBackground = Color.WHITE; //(Color)UIManager.get("List.background");
	public static Color weekDaysForeground = (Color)UIManager.get("List.foreground");
	public static Color selectedDayForeground = (Color)UIManager.get("List.selectionForeground");
	public static Color selectedDayBackground = (Color)UIManager.get("List.selectionBackground");

	/** Método constructor */
	public FWCalendar() {
		construct();
		select();
	}

	/**
	 * Método constructor.
	 * @param date La fecha inicial del calendario.
	 */
	public FWCalendar(Date date) {
		construct();
		select(date);
	}

	/**
	 * Método constructor.
	 * @param date La fecha inicial del calendario.
	 * @param minDate La fecha mínima del calendario.
	 * @param maxDate La fecha máxima del calendario.
	 */
	public FWCalendar(Date date, Date minDate, Date maxDate) {
		setMinDate(minDate);
		setMaxDate(maxDate);
		construct();
		select(date);
	}

	//Construye gráficamente el componente
	private void construct() {
		setLayout(new BorderLayout());
		calendar = DateUtil.getGregorianCalendar();

		cmbMonth = new JComboBox(getMonths());
		cmbMonth.addItemListener(this);

		cmbYear = new JComboBox();
		for(int i = getFirstYear(); i <= getLastYear(); i++)
			cmbYear.addItem(Integer.toString(i));
		cmbYear.addItemListener(this);

		days = new JLabel[7][7];
		for(int i = 0; i < 7; i++) {
			days[0][i] = new JLabel(DAYS[i], JLabel.CENTER);
			days[0][i].setForeground(weekDaysForeground);
		}
		for(int i = 1; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				days[i][j] = new JLabel(" ", JLabel.CENTER);
				days[i][j].setBackground(selectedDayBackground);
				days[i][j].addMouseListener(this);
			}
		}

		JPanel monthYear = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        monthYear.add(getBtnBack());
		monthYear.add(cmbMonth);
        monthYear.add(getBtnForward());
		monthYear.add(cmbYear);
		add(monthYear, BorderLayout.NORTH);

		daysGrid = new FocusablePanel(new GridLayout(7, 7, 5, 0));
		daysGrid.addFocusListener(this);
		daysGrid.addKeyListener(this);
		for(int i = 0; i < 7; i++)
			for(int j = 0; j < 7; j++)
				daysGrid.add(days[i][j]);
		daysGrid.setPreferredSize(new Dimension(DAYS_GRID_WIDTH, DAYS_GRID_HEIGHT));
		daysGrid.setBackground(daysGridBackground);
		daysGrid.setBorder(BorderFactory.createLoweredBevelBorder());
		JPanel daysPanel = new JPanel();
		daysPanel.add(daysGrid);
		add(daysPanel, BorderLayout.CENTER);
	}

	/**
	 * Setea el día seleccionado. 
	 * El día es especificado como el número de día en el mes a setear.
	 * @param newDay
	 */
	private void setSelected(int newDay) {
		setSelected(days[(newDay + offset - 1) / 7 + 1][(newDay + offset - 1) % 7]);
	}

	/**
	 * Setea el día seleccionado.
	 * @param newDay
	 */
	private void setSelected(JLabel newDay) {
		if(day != null) {
			day.setForeground(foregroundLastDaySelected);
			day.setOpaque(false);
			day.repaint();
			day.setBorder(EMPTY_BORDER);
		}
        foregroundLastDaySelected = newDay.getForeground();
		day = newDay;
		day.setForeground(selectedDayForeground);
		day.setOpaque(true);
		day.repaint();
	}

	/**
	 * Pone en <b>negrita</b> el día de la fecha pasada por parámetro.
	 * @param date La fecha cuyo día se pondrá en negrita.
	 */
	public void setDayBold(Date date) {
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		JLabel label = days[(day + offset - 1) / 7 + 1][(day + offset - 1) % 7];
		label.setFont(label.getFont().deriveFont(Font.BOLD));
	}

    /** Pone a todos los días con la fuente default */
    public void setAllDaysDefaultFont() {
        for(int i = 1; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                JLabel lblDay = days[i][j];
                lblDay.setFont(lblDay.getFont().deriveFont(Font.PLAIN));
            }
        }
    }

    /** Pone a todos los días con el color default */
    public void setAllDaysDefaultColor() {
        for(int i = 1; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                JLabel lblDay = days[i][j];
                lblDay.setForeground(daysForeground);
            }
        }
    }

    /**
	 * Cambia de <b>color</b> el día de la fecha pasada por parámetro.
	 * @param date La fecha cuyo día será cambiado de color.
	 * @param color El nuevo color.
	 */
	public void setDayColor(Date date, Color color) {
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		days[(day + offset - 1) / 7 + 1][(day + offset - 1) % 7].setForeground(color);
	}

    /**
     * Cambia de <b>color</b> un conjunto de días
     * @param dates La lista de días a cambiar el color
     * @param color El nuevo color
     */
    public void setDaysColor(Date[] dates, Color color) {
        for(Date day : dates) {
            setDayColor(day, color);
        }
    }
    
    /**
	 * Pone en <b>negrita</b> los días correspondientes a las fechas del array.
	 * @param dates Las fechas cuyos días serán puestos en negrita.
	 */
	public void setDaysBold(Date[] dates) {
		for(int i = 0; i < dates.length; i++)
			setDayBold(dates[i]);
	}

	/**
	 * Devuelve el <b>día</b> seleccionado en el calendario.
	 * @return Devuelve <b>-1</b> si no hay ningún día seleccionado. 
	 */
	public int getSelectedDay() {
		if(day == null)
			return -1;
		try {
			return Integer.parseInt(day.getText());
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Devuelve el <b>mes</b> seleccionado en el calendario.
	 * @return Devuelve <b>-1</b> si no hay ningún mes seleccionado. 
	 */
	public int getSelectedMonth() {
		if(cmbMonth == null)
			return -1;
		return cmbMonth.getSelectedIndex() + 1 + mesOffset;
	}

	/**
	 * Devuelve el <b>año</b> seleccionado en el calendario.
	 * @return Devuelve <b>-1</b> si no hay ningún año seleccionado. 
	 */
	public int getSelectedYear() {
		if(cmbYear == null)
			return -1;
		return Integer.parseInt(cmbYear.getSelectedItem().toString());
	}

	/** Actualiza el calendario */
	private void update() {
		int day = getSelectedDay();
		for(int i = 0; i < 7; i++) {
			days[1][i].setText(" ");
			days[5][i].setText(" ");
			days[6][i].setText(" ");
		}
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MONTH, cmbMonth.getSelectedIndex() + mesOffset + Calendar.JANUARY);
		calendar.set(Calendar.YEAR, cmbYear.getSelectedIndex() + getFirstYear());

		offset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
		lastDay = calendar.getActualMaximum(Calendar.DATE);
		for(int i = 0; i < lastDay; i++) {
			days[(i + offset) / 7 + 1][(i + offset) % 7].setEnabled(true);
			days[(i + offset) / 7 + 1][(i + offset) % 7].removeMouseListener(this);
			days[(i + offset) / 7 + 1][(i + offset) % 7].addMouseListener(this);
			days[(i + offset) / 7 + 1][(i + offset) % 7].setText(String.valueOf(i + 1));
			if(getSelectedYear() != -1 && getSelectedMonth() != -1) {
				Date aux = DateUtil.getFecha(getSelectedYear(), getSelectedMonth(), i+1);
				if(aux.before(getMinDate()) || aux.after(getMaxDate())) {
					days[(i + offset) / 7 + 1][(i + offset) % 7].setEnabled(false);
					days[(i + offset) / 7 + 1][(i + offset) % 7].removeMouseListener(this);
				}
			}
		}
		if(day != -1) {
			Date aux = DateUtil.getFecha(getSelectedYear(), getSelectedMonth(), day);
			if(aux.before(getMinDate()))
				//Estoy en mes año desde y day es < que el dia desde
				day = DateUtil.getDia(getMinDate());
			else if(aux.after(getMaxDate()))
				//Estoy en mes año hasta y day es > que el dia hasta
				day = DateUtil.getDia(getMaxDate());
			else if(day > lastDay)
				day = lastDay;
			setSelected(day);
		}
		setAllDaysDefaultFont();
        postUpdate();
	}

    /** Método a sobrescribir si se desea hacer algo cuando cambia la fecha del calendario */
    public void postUpdate() {
    }

    /**
	 * Devuelve la <b>fecha seleccionada</b> en el calendario.
	 * @return selectedDate La fecha seleccionada.
	 */
	public Date getSelectedDate() {
		return selectedDate;
	}

	/**
	 * Setea la <b>fecha seleccionada</b> en el calendario.
	 * @param selectedDate La fecha a seleccionar.
	 */
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
		select(selectedDate);
	}

	/**
	 * Método llamado en el evento de doble click en un día determinado.
	 * El método es vacío para ser sobreescrito.
	 */
	public void dateSelected() {
	}

	public void mouseClicked(MouseEvent evt) {
		JLabel day = (JLabel)evt.getSource();
		if(!day.getText().equals(" "))
			setSelected(day);
		daysGrid.requestFocus();
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mousePressed(MouseEvent evt) {
	}

	public void mouseReleased(MouseEvent evt) {
		if(evt.getClickCount() == 2)
			if(day != null) {
				if(getSelectedDay() != -1)
					selectedDate = constructDate();
				dateSelected();
			}
	}

	/**
	 * Concatena y formatea la fecha seleccionada en el calendario, con el formato
	 * 'YYYY-MM-DD'.
	 * @return newDate La fecha concatenada y formateada.
	 */
	public Date constructDate() {
		Date newDate = DateUtil.stringToDate((getSelectedYear() + "-" + getSelectedMonth() + "-" + getSelectedDay()) , DateUtil.DEFAULT_DATE);
		return newDate;
	}

	/**
	 * Selecciona la fecha pasada por parámetro en el calendario.
	 * @param date La fecha a seleccionar.
	 * @return La nueva fecha seleccionada.
	 */
	private Date select(Date date) {
		if(date.before(getMinDate()))
			date = getMinDate();
		if(date.after(getMaxDate()))
			date = getMaxDate();
		calendar.setTime(date);
		int _day = calendar.get(Calendar.DATE);
		int _month = calendar.get(Calendar.MONTH);
		int _year = calendar.get(Calendar.YEAR);

		cmbYear.setSelectedIndex(_year - getFirstYear());
		cmbMonth.setSelectedIndex(_month - mesOffset - Calendar.JANUARY);
		setSelected(_day);
		calendar.set(Calendar.DATE, getSelectedDay());
		calendar.set(Calendar.MONTH, cmbMonth.getSelectedIndex() + mesOffset + Calendar.JANUARY);
		calendar.set(Calendar.YEAR, cmbYear.getSelectedIndex() + getFirstYear());
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Selecciona la fecha de hoy en el calendario.
	 * @return La nueva fecha seleccionada.
	 */
	public Date select() {
		return select(new Date(System.currentTimeMillis()));
	}

	public void keyPressed(KeyEvent evt) {
		int day = getSelectedDay();
		Date aux = DateUtil.getFecha(getSelectedYear(), getSelectedMonth(), day);
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_LEFT:
			if(day > 1 && aux.after(getMinDate()))
				setSelected(day - 1);
			break;

			case KeyEvent.VK_RIGHT:
			if(day < lastDay && aux.before(getMaxDate()))
				setSelected(day + 1);
			break;

			case KeyEvent.VK_UP:
				aux = DateUtil.getFecha(getSelectedYear(), getSelectedMonth(), day - 6);
			if(day > 7 && aux.after(getMinDate()))
				setSelected(day - 7);
			break;

			case KeyEvent.VK_DOWN:
				aux = DateUtil.getFecha(getSelectedYear(), getSelectedMonth(), day + 6);
			if(day <= lastDay - 7 && aux.before(getMaxDate()))
				setSelected(day + 7);
			break;
		}
	}

	public void keyReleased(KeyEvent evt) {
	}
    
	public void keyTyped(KeyEvent evt) {
	}

	public void focusGained(FocusEvent evt) {
		setSelected(day);
	}

	public void focusLost(FocusEvent evt) {
		setSelected(day);
	}

	public void itemStateChanged(ItemEvent evt) {
		if(evt.getSource() == cmbYear) {
			int mesSeleccionado = getSelectedMonth();
			mesOffset = 0;
			cmbMonth.removeItemListener(this);
			if(getSelectedYear() == DateUtil.getAnio(getMinDate())) {
				cmbMonth.removeAllItems();
				mesOffset = DateUtil.getMes(getMinDate());
				for(int i = mesOffset; i < 12; i++) {
					cmbMonth.addItem(MONTHS[i]);
				}
			} else if(getSelectedYear() == DateUtil.getAnio(getMaxDate())) {
				cmbMonth.removeAllItems();
				for(int i = 0; i <= DateUtil.getMes(getMaxDate()); i++) {
					cmbMonth.addItem(MONTHS[i]);
				}
			} else {
				cmbMonth.removeAllItems();
				for(int i = 0; i < 12; i++) {
					cmbMonth.addItem(MONTHS[i]);
				}				
			}
			//Setear el mes seleccionado
			if(mesSeleccionado != -1) {
				if(mesOffset > 0) {
					if(mesSeleccionado - 1 < mesOffset) {
						mesSeleccionado = 0; 
					} else {
						mesSeleccionado -= (mesOffset+1); 
					}
				} else {
					if(mesSeleccionado > cmbMonth.getItemCount()) {
						mesSeleccionado = cmbMonth.getItemCount() - 1;
					} else {
						mesSeleccionado--;
					}
				}
				cmbMonth.addItemListener(this);
				cmbMonth.setSelectedIndex(mesSeleccionado);
			}
		}
		if(update)
			update();
	}

	static class FocusablePanel extends JPanel {
		public FocusablePanel(LayoutManager layout) {
			super(layout);
		}
	}

	/**
	 * Retorna el combobox de meses del componente calendario.
	 * @return cmbMonth
	 */
	public JComboBox getComboBoxMonth() {
		return cmbMonth;
	}

	/**
	 * Retorna el combobox de años del componente calendario.
	 * @return cmbYear
	 */
	public JComboBox getComboBoxYear() {
		return cmbYear;
	}

    /**
     * Devuelve el icono del botón <b>Back</b>.
     * @return iconBackward
     */
    public String getBackIcon() {
        return backIcon;
    }

    /**
     * Setea el icono del botón <b>Back</b>.
     * @param backIcon
     */
    public void setBackIcon(String backIcon) {
        FWCalendar.backIcon = backIcon;
        Icon icon = ImageUtil.loadIcon(backIcon);
        btnBack.setIcon(icon);
        btnBack.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }

    /**
     * Devuelve el icono del botón <b>Forward</b>.
     * @return iconForward
     */
    public String getForwardIcon() {
        return forwardIcon;
    }

    /**
     * Setea el icono del botón <b>Forward</b>.
     * @param forwardIcon
     */
    public void setForwardIcon(String forwardIcon) {
        FWCalendar.forwardIcon = forwardIcon;
        Icon icon = ImageUtil.loadIcon(forwardIcon);
        btnForward.setIcon(icon);
        btnForward.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }

    public void habilitar(boolean enabled) {
    	GuiUtil.setEstadoPanel(this, enabled);
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
		    	if(enabled) {
		    		days[i][j].addMouseListener(this);
		    	} else {
		    		days[i][j].removeMouseListener(this);
		    	}
			}
		}
    }

    //Construye el botón 'Atrás' de los Meses
    private JButton getBtnBack() {
        if(btnBack == null) {
            btnBack = new JButton();
            String icon = (backIcon == null ? DEFAULT_ICON_BTN_BACK : backIcon);
            DecorateUtil.decorateButton(btnBack, icon);
            btnBack.setFocusable(false);
            btnBack.addActionListener(new BtnBackActionListener());
        }
        return btnBack;
    }

    //Construye el botón 'Adelante' de los Meses
    private JButton getBtnForward() {
        if(btnForward == null) {
            btnForward = new JButton();
            String icon = (forwardIcon == null ? DEFAULT_ICON_BTN_FORWARD : forwardIcon);
            DecorateUtil.decorateButton(btnForward, icon);
            btnForward.setFocusable(false);
            btnForward.addActionListener(new BtnForwardActionListener());
        }
        return btnForward;
    }

    class BtnBackActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            int selectedMonth = cmbMonth.getSelectedIndex();
            if(selectedMonth == 0) {
                int selectedYear = cmbYear.getSelectedIndex();
                if(selectedYear != 0) {
                	update = false;
                    cmbYear.setSelectedIndex(selectedYear - 1);
                    update = true;
                    selectedMonth = cmbMonth.getItemCount();
                } else {
                	selectedMonth++;
                }
            }
            cmbMonth.setSelectedIndex(selectedMonth - 1);
        }
    }

    class BtnForwardActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            int selectedMonth = cmbMonth.getSelectedIndex();
            if(selectedMonth == cmbMonth.getItemCount() - 1) {
                int selectedYear = cmbYear.getSelectedIndex();
                if(selectedYear != (getLastYear() - getFirstYear())) {
                	selectedMonth = -1;
                	update = false;
                    cmbYear.setSelectedIndex(selectedYear + 1);
                    update = true;
                } else {
                	selectedMonth--;
                }
            }
            cmbMonth.setSelectedIndex(selectedMonth + 1);
        }
    }

	private Date getMaxDate() {
		if(maxDate == null)
			maxDate = DateUtil.getFecha(LAST_YEAR, 12, 31);
		return maxDate;
	}

	private void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	private Date getMinDate() {
		if(minDate == null)
			minDate = DateUtil.getFecha(FIRST_YEAR, 1, 1);
		return minDate;
	}

	private void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	private int getFirstYear() {
		return DateUtil.getAnio(getMinDate());
	}
	
	private int getLastYear() {
		return DateUtil.getAnio(getMaxDate());
	}

	private String[] getMonths() {
		return MONTHS;
	}

}