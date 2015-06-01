package ar.com.textillevel.gui.modulos.chat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextArea;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.gui.modulos.chat.client.ChatClient;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.chat.mensajes.MensajeChat;
import ar.com.textillevel.modulos.chat.mensajes.MensajeMessageChat;

public class ChatWindow extends JFrame {

	private static final long serialVersionUID = 6440436221524120525L;

	private static ChatWindow instance;

	private final Map<String, TabConversacion> conversacionesAbiertas;

	private JList listaContactos;
	private JPanel panelStatus;
//	private JButton btnConectarDesconectar;
	private JTabbedPane panelTabsChat;

	private ImageIcon imgEstadoConexion;
	private JLabel lblEstadoConexion;

	public static ChatWindow getInstance() {
		if (instance == null) {
			instance = new ChatWindow();
		}
		return instance;
	}

	private ChatWindow() {
		conversacionesAbiertas = new HashMap<String, TabConversacion>();
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrar(this);
		updateConnectionStatus();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Chat GTL");
		setSize(new Dimension(1024, 768));
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}

	private void setUpComponentes() {
		add(getPanelTabsChat(),BorderLayout.CENTER);
		JScrollPane jsp = new JScrollPane(getListaContactos(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(200, 768));
		add(jsp,BorderLayout.EAST);
		add(getPanelStatus(),BorderLayout.SOUTH);
	}

	public void agregarListaUsuarios(List<String> usuarios) {
		for(String u: usuarios){
			agregarUsuario(u);
		}
	}
	
	public void agregarMensaje(MensajeChat mensaje) {
		String nick = mensaje.getNick();
		TabConversacion tabConversacion = getConversacionesAbiertas().get(nick);
		if(tabConversacion == null){
			tabConversacion = new TabConversacion(nick);
			conversacionesAbiertas.put(nick, tabConversacion);
		}
		tabConversacion.getTxtAreaConversacion().append(nick+": " + ((MensajeMessageChat)mensaje).getMsg()+"\n");
		int i=0;
		for(i = 0;i<getPanelTabsChat().getTabCount();i++){
			TabConversacion tab = (TabConversacion)getPanelTabsChat().getComponent(i);
			if(tab.getUsuario().equalsIgnoreCase(nick)){
				getPanelTabsChat().setComponentAt(i, tabConversacion);
				return;
			}
		}
		getPanelTabsChat().addTab(mensaje.getNick(), tabConversacion);
	}
	
	public void agregarUsuario(String nick){
		DefaultListModel defaultListModel = (DefaultListModel)getListaContactos().getModel();
		if(defaultListModel.contains(nick)){
			return;
		}
		defaultListModel.addElement(nick);
	}
	
	public void quitarUsuario(MensajeChat msg) {
		getConversacionesAbiertas().remove(msg.getNick());
		((DefaultListModel)getListaContactos().getModel()).removeElement(msg.getNick());
		for(int i =0; i<getPanelTabsChat().getComponentCount();i++){
			TabConversacion tab = (TabConversacion)getPanelTabsChat().getComponentAt(i);
			if(tab.getUsuario().equalsIgnoreCase(msg.getNick())){
				getPanelTabsChat().remove(i);
				return;
			}
		}
	}

	public void mostrarVentana() {
		if(!isVisible()){
			setVisible(true);
		}
	}
	
	public void mostrarVentana(String nick) {
		if(!isVisible()){
			for(int i =0; i<getPanelTabsChat().getComponentCount();i++){
				TabConversacion tab = (TabConversacion)getPanelTabsChat().getComponentAt(i);
				if(tab.getUsuario().equalsIgnoreCase(nick)){
					getPanelTabsChat().setSelectedIndex(i);
					break;
				}
			}
			setVisible(true);
		}
	}

	private JList getListaContactos() {
		if (listaContactos == null) {
			listaContactos = new JList(new DefaultListModel());
			listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			Font font = listaContactos.getFont();
			listaContactos.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
			listaContactos.setForeground(Color.RED);
			listaContactos.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2){
						String nick =(String)((DefaultListModel)getListaContactos().getModel()).get(getListaContactos().getSelectedIndex());
						if(nick.equals(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName())){
							return;
						}
						if(conversacionesAbiertas.get(nick)==null){
							TabConversacion tab = new TabConversacion(nick);
							conversacionesAbiertas.put(nick, tab);
							getConversacionesAbiertas().put(nick, tab);
							getPanelTabsChat().addTab(tab.getUsuario(), tab);
							getPanelTabsChat().setSelectedComponent(tab);
							tab.getTxtInput().requestFocus();
						}
					}
				}
			});
		}
		return listaContactos;
	}

	private JPanel getPanelStatus() {
		if (panelStatus == null) {
			panelStatus = new JPanel();
			panelStatus.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			panelStatus.add(new JLabel("Estado: "));
			imgEstadoConexion = (ImageIcon) ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/connecting.png");
			panelStatus.add(new JLabel(imgEstadoConexion));
			getLblEstadoConexion().setText("CONECTANDO");
			panelStatus.add(getLblEstadoConexion());
			panelStatus.setBorder(BorderFactory.createLineBorder(Color.RED));
		}
		return panelStatus;
	}

//	private JButton getBtnConectarDesconectar() {
//		if(btnConectarDesconectar == null){
//			btnConectarDesconectar = new JButton();
//			if(ChatClient.getInstance().isConnected()){
//				btnConectarDesconectar.setText("Desconectar");
//			}else{
//				btnConectarDesconectar.setText("Conectar");
//			}
//			btnConectarDesconectar.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if(ChatClient.getInstance().isConnected()){
//						ChatClient.getInstance().desconectar();
//						btnConectarDesconectar.setText("Conectar");
//					}else{
//						try {
//							ChatClient.getInstance().conectar();
//							btnConectarDesconectar.setText("Desconectar");
//						} catch (UnknownHostException uhe) {
//							CLJOptionPane.showErrorMessage(ChatWindow.this, "No se ha podido conectar a " + System.getProperty("textillevel.chat.server.ip") +":" +System.getProperty("textillevel.chat.server.port")+". Host desconocido", "Error");
//						} catch (IOException ioe) {
//							CLJOptionPane.showErrorMessage(ChatWindow.this, "Ha ocurrido un error al intentar conectarse : " + System.getProperty("textillevel.chat.server.ip") +":" +System.getProperty("textillevel.chat.server.port")+".", "Error");
//						} catch (Exception ex){
//							CLJOptionPane.showErrorMessage(ChatWindow.this,"Error desconocido al iniciar el cliente de Chat.","Error");
//							ex.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//		return btnConectarDesconectar;
//	}

	private class TabConversacion extends JPanel {

		private static final long serialVersionUID = -4435001806498846749L;

		private static final int MAX_LENGTH_MENSAJE = 500;

		private CLJTextArea txtAreaConversacion;
		private CLJTextField txtInput;
		private JButton btnEnviar;
		private String usuario;

		public TabConversacion(String usuario) {
			setUsuario(usuario);
			construct();
		}

		private void construct() {
			JScrollPane jsp = new JScrollPane(getTxtAreaConversacion(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setPreferredSize(new Dimension(800, 600));
			add(jsp, BorderLayout.CENTER);
			add(getPanelSur(), BorderLayout.SOUTH);
		}

		private JPanel getPanelSur() {
			JPanel panelSur = new JPanel();
			panelSur.setLayout(new GridBagLayout());
			panelSur.add(getTxtInput(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
			panelSur.add(getBtnEnviar(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 1));
			return panelSur;
		}

		public CLJTextArea getTxtAreaConversacion() {
			if (txtAreaConversacion == null) {
				txtAreaConversacion = new CLJTextArea();
				txtAreaConversacion.setEditable(false);
				txtAreaConversacion.setLineWrap(true);
			}
			return txtAreaConversacion;
		}

		public CLJTextField getTxtInput() {
			if (txtInput == null) {
				txtInput = new CLJTextField(MAX_LENGTH_MENSAJE);
				txtInput.setPreferredSize(new Dimension(680, 20));
				txtInput.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						getBtnEnviar().doClick();
					}
				});
			}
			return txtInput;
		}

		public JButton getBtnEnviar() {
			if (btnEnviar == null) {
				btnEnviar = new JButton("Enviar", ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/star.png"));
				btnEnviar.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						try {
							String selectedValue = ((TabConversacion)getPanelTabsChat().getSelectedComponent()).getUsuario();
							ChatClient.getInstance().enviarMensaje(selectedValue, getTxtInput().getText().trim());
							getTxtAreaConversacion().append(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName()+": " + getTxtInput().getText().trim()+"\n");
							getTxtAreaConversacion().setCaretPosition(getTxtAreaConversacion().getText().length());
							getTxtInput().setText("");
						} catch (IOException e1) {
							CLJOptionPane.showErrorMessage(ChatWindow.this, "Se ha producido un error al enviar el mensaje", "Error");
							e1.printStackTrace();
						}
					}
				});
			}
			return btnEnviar;
		}

		public String getUsuario() {
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
	}

	private Map<String, TabConversacion> getConversacionesAbiertas() {
		return conversacionesAbiertas;
	}

	private JTabbedPane getPanelTabsChat() {
		if (panelTabsChat == null) {
			panelTabsChat = new JTabbedPane(JTabbedPane.TOP);
		}
		return panelTabsChat;
	}

	private JLabel getLblEstadoConexion() {
		if(lblEstadoConexion == null){
			lblEstadoConexion = new JLabel();
		}
		return lblEstadoConexion;
	}
	
	private void updateConnectionStatus(){
		if(ChatClient.getInstance().isConnected()){
			lblEstadoConexion.setText("CONECTADO");
			imgEstadoConexion = (ImageIcon) ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/ok.png");
		}else{
			lblEstadoConexion.setText("DESCONECTADO");
			imgEstadoConexion = (ImageIcon) ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/error.png");
		}
	}
}
