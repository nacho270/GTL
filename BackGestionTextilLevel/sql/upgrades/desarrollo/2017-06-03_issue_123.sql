-- ejecutar en A y en B
insert into t_codigo_odt(a_codigo)
select a_codigo from t_orden_de_trabajo;
