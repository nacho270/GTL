package ar.com.textillevel.gui.modulos.chat.notifications;

import java.awt.Dimension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ar.com.textillevel.gui.modulos.chat.notifications.notifier.EnumNotificaciones;
import ar.com.textillevel.gui.modulos.chat.notifications.notifier.NotificationType;
import ar.com.textillevel.gui.modulos.chat.notifications.notifier.NotifierDialog;
import ar.com.textillevel.gui.util.GenericUtils;

public class NotificationHelper {

	public static void showNotification(final String titulo, final String cuerpo, final EnumNotificaciones notifiacion) {
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				try{
					Display display = new Display();
					Shell shell = new Shell(display,SWT.ON_TOP|SWT.NO_TRIM);
					shell.setText("Parent shell");
					shell.setSize(0, 0);
					shell.setLayout(new FillLayout());
			
					/*Button tester = new Button(shell, SWT.PUSH);
					tester.setText("Push me!");
					tester.addListener(SWT.Selection, new Listener() {
			
						public void handleEvent(Event event) {
							int max = NotificationType.values().length;
							Random r = new Random();
							int toUse = r.nextInt(max);
							String msg = "Diego";
							NotifierDialog.notify("Nuevo mensaje de: " + msg, "Hola papaaa", NotificationType.CONNECTED);
						}
			
					});*/
					shell.open();
					NotificationType noti = null;
					for(NotificationType nt : NotificationType.values()){
						if(nt.toString().equals(notifiacion.toString())){
							noti = nt;
						}
					}
					if(noti!=null){
						NotifierDialog.notify(titulo, cuerpo,noti);
						//shell.setVisible(false);
						Dimension dimensionPantalla = GenericUtils.getDimensionPantalla();
						shell.setLocation((int)dimensionPantalla.getHeight(), (int)dimensionPantalla.getWidth());
						while (!shell.isDisposed()) {
							if (!display.readAndDispatch()){
								display.sleep();
							}
						}
						display.dispose();
					}
				}catch(Exception e){
					//aca no paso nada
					e.printStackTrace();
					return;
				}
			}
		});
		t.start();
	}
}
