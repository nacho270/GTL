-- detecto los artículos potencialmente repetidos

select a_ancho, a_gramaje, f_tipo_articulo_p_id, count(*)
from t_articulo
group by a_ancho, a_gramaje, f_tipo_articulo_p_id
having count(*) > 1;

-- selecciono el par de artículos repetidos, ejemplo

select * from t_articulo where a_gramaje=0.4 and f_tipo_articulo_p_id=3 and a_ancho=3;

-- detecto las tablas y proyecto los querys armados

select CONCAT('SELECT * FROM ',TABLE_NAME,' WHERE ', COLUMN_NAME, '= 54;')
from information_schema.KEY_COLUMN_USAGE
where REFERENCED_TABLE_NAME = 'T_ARTICULO' AND REFERENCED_COLUMN_NAME = 'P_ID' AND CONSTRAINT_SCHEMA='gtl';


-- me fijo cuáles tienen datos y updateo/borro al final de acuerdo a eso

update t_grupo_tipo_articulo_gama set F_ARTICULO_P_ID = 37 where F_ARTICULO_P_ID = 54;
update t_producto_articulo set F_ARTICULO_P_ID = 37 where F_ARTICULO_P_ID = 54;
delete from t_articulo where p_id=54;