drop table T_PRECIO_PRODUCTO;
drop table t_lista_de_precios;

update t_modulo set a_class = 'ar.com.textillevel.gui.modulos.abm.listaprecios.GuiABMListaDePrecios' where P_ID = 7;