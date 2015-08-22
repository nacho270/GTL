drop table T_PRECIO_PRODUCTO;
drop table t_lista_de_precios;

update t_modulo set a_class = 'ar.com.textillevel.gui.modulos.abm.listaprecios.GuiABMListaDePrecios' where P_ID = 7;
update t_param_generales set A_CARGA_MINIMA_COLOR = 500.00, A_CARGA_MINIMA_ESTAMPADO = 2000.00;