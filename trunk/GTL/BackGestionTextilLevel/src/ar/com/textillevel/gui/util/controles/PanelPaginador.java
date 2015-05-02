package ar.com.textillevel.gui.util.controles;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.util.GuiUtil;

@SuppressWarnings("serial")
public class PanelPaginador extends JPanel
{
	private JButton btnNext;
	private JButton btnPrev;
	private CLJNumericTextField txtPagina;
	private CLJNumericTextField txtPaginaTotal;
	private int rowsPageSize = 30;
	private int rowsCount;
	private int pageIndex = 1;
	
	public PanelPaginador()
	{
		super();
		construct();
		setVisible(true);
		sincronizarBotones();
	}
	
	private void construct() 
	{
		JPanel panPagina = new JPanel(new FlowLayout());
		panPagina.add(getBtnPrev());
		panPagina.add(getTxtPagina());
		panPagina.add(new JLabel("de:"));
		panPagina.add(getTxtPaginaTotal());
		panPagina.add(getBtnNext());
		add(panPagina);
	}
	
	private JButton getBtnPrev()
	{
		if (btnPrev == null)
		{
			btnPrev = new JButton("<<");
			btnPrev.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent evt) 
				{
					if (pageIndex > 1)
						pageIndex--;
					getTxtPagina().setText(String.valueOf(pageIndex));
					sincronizarBotones();
					fireActionPerformed();
				}
			});
		}
		return btnPrev;		
	}
	
	private JButton getBtnNext()
	{
		if (btnNext == null)
		{
			btnNext = new JButton(">>");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) 
				{
					if (pageIndex < getPageCount())
						pageIndex++;			
					getTxtPagina().setText(String.valueOf(pageIndex));
					sincronizarBotones();
					fireActionPerformed();
				}
			});					
		}
		return btnNext;		
	}
	
	private CLJNumericTextField getTxtPagina()
	{
		if (txtPagina == null)
		{
			txtPagina = new CLJNumericTextField();
			txtPagina.setColumns(8);
			txtPagina.setPreferredSize(new Dimension(10,20));
			txtPagina.setText(String.valueOf(pageIndex));
			txtPagina.addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent ev)
				{
					int codigo=ev.getKeyCode();
				    if(codigo==KeyEvent.VK_ENTER)
				    {
				    	if(txtPagina.getText() == null || txtPagina.getText().trim().length() == 0) {
				    		GuiUtil.showTooltipText((JComponent)ev.getSource(), "Debe ingresar un número de página entre 1 y " + getPageCount());
				    		return;
				    	}
				    	int iPage = Integer.parseInt(txtPagina.getText());
				    	if ((iPage > getPageCount()) || (iPage <= 0))
				    		GuiUtil.showTooltipText((JComponent)ev.getSource(), "El número de página debe estar entre 1 y " + getPageCount());
				    	else
				    	{
				    		pageIndex = iPage;
				    		sincronizarBotones();
				    		fireActionPerformed();
				    	}
				    }
				}
			});
		}
		return txtPagina;
	}
	
	private CLJNumericTextField getTxtPaginaTotal()
	{
		if (txtPaginaTotal == null)
		{
			txtPaginaTotal = new CLJNumericTextField();
			txtPaginaTotal.setColumns(8);
			txtPaginaTotal.setText("1");
			txtPaginaTotal.setEnabled(false);
		}
		return txtPaginaTotal;
	}

	private void sincronizarBotones() {
		getBtnNext().setEnabled(pageIndex < getPageCount());
		getBtnPrev().setEnabled(pageIndex > 1);
	}

	public int getRowsCount() 
	{
		return rowsCount;	
	}

	public void setRowsCount(int rowsCount) 
	{
		this.rowsCount = rowsCount;
		getTxtPaginaTotal().setText(String.valueOf(getPageCount()));
		sincronizarBotones();		
	}
	
	public int getRowsPageSize() 
	{
		return rowsPageSize;
	}

	public void setRowsPageSize(int rowsPageSize) 
	{
		this.rowsPageSize = rowsPageSize;
	}
	
	public Integer getPageCount()
	{
		int pageCount = 0;
		if ((rowsCount % rowsPageSize) != 0)
			pageCount = (int)(rowsCount /rowsPageSize )+1;
		else
			pageCount = (int)(rowsCount /rowsPageSize );
		if (pageCount == 0) pageCount++;
		
		return pageCount;
	}

	public int getPageIndex() 
	{
		return pageIndex;
	}
	
    public void addActionListener(ActionListener l) 
    {
        listenerList.add(ActionListener.class, l);
    }
    
    protected void fireActionPerformed() 
    {
        ActionEvent e = null;
        e = new ActionEvent(PanelPaginador.this, ActionEvent.ACTION_PERFORMED, "", 0, 0);
        for(ActionListener actionListener : listenerList.getListeners(ActionListener.class)) {
        	actionListener.actionPerformed(e);         
        }

    }

	public void setPageIndex(int i) {
		this.pageIndex = i;
		getTxtPagina().setText(String.valueOf(i));
	}

}
