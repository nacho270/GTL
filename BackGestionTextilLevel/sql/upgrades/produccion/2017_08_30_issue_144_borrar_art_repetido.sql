-- Obtengo todas los updates que referencian a T_ARTICULO de la siguiente manera. Hay que cambiar T_ARTICULO.P_ID=44 por T_ARTICULO.P_ID=83 
select concat('update ', table_name,' set ', column_name, ' = 83 WHERE ', column_name, ' = 44;')
from information_schema.KEY_COLUMN_USAGE
where REFERENCED_TABLE_NAME = 'T_ARTICULO' AND REFERENCED_COLUMN_NAME = 'P_ID' and constraint_schema='gtl';

-- [output], quito al de config t_producto_articulo -- Ejecutar en A y en B
update t_cuenta_articulo set F_ARTICULO_P_ID = 83 WHERE F_ARTICULO_P_ID = 44;
update t_grupo_tipo_articulo_base set F_ARTICULO_P_ID = 83 WHERE F_ARTICULO_P_ID = 44;
update t_grupo_tipo_articulo_gama set F_ARTICULO_P_ID = 83 WHERE F_ARTICULO_P_ID = 44;
update t_item_factura set F_ARTICULO_P_ID = 83 WHERE F_ARTICULO_P_ID = 44;
update t_materia_prima set F_ARTICULO_P_ID = 83 WHERE F_ARTICULO_P_ID = 44;
update t_orden_de_trabajo set F_ARTICULO_PARCIAL_P_ID = 83 WHERE F_ARTICULO_PARCIAL_P_ID = 44;
update t_precio_tipo_articulo set F_ARTICULO_P_ID = 83 WHERE F_ARTICULO_P_ID = 44;
update t_remito set F_ARTICULO_STOCK_P_ID = 83 WHERE F_ARTICULO_STOCK_P_ID = 44;

--ahora ajusto las tablas de configuracion. Solo aplica t_producto_articulo ya que t_materia_prima no tiene datos.
create table tmp_prod_arts as
select min(p_id) as pa1, max(p_id) as pa2
from t_producto_articulo
where f_articulo_p_id in (44,83)
group by f_producto_p_id, f_gama_p_id, f_color_p_id, f_dibujo_p_id, f_variante_p_id
having count(*) > 1;

create table tmp_prod_to_update as
select pa1 as pa_to_update, pa2 as pa_final
from tmp_prod_arts
inner join t_producto_articulo pa where pa.p_id = pa1 and pa.f_articulo_p_id=44

union

select pa2 as pa_to_update, pa1 as pa_final
from tmp_prod_arts
inner join t_producto_articulo pa where pa.p_id = pa2 and pa.f_articulo_p_id=44;

-- luego correr esto:

select concat('update ', table_name,' set ', column_name, ' = ',pa_final , ' WHERE ', column_name, ' = ', pa_to_update, ';')
from tmp_prod_to_update, information_schema.KEY_COLUMN_USAGE
where REFERENCED_TABLE_NAME = 'T_PRODUCTO_ARTICULO' AND REFERENCED_COLUMN_NAME = 'P_ID' and constraint_schema='gtl';


-- [output], ejecutar en A y en B
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2378 WHERE F_PRODUCTO_ARTICULO_P_ID = 347;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2378 WHERE F_PRODUCTO_ARTICULO_P_ID = 347;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2378 WHERE F_PRODUCTO_ARTICULO_P_ID = 347;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2234 WHERE F_PRODUCTO_ARTICULO_P_ID = 738;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2234 WHERE F_PRODUCTO_ARTICULO_P_ID = 738;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2234 WHERE F_PRODUCTO_ARTICULO_P_ID = 738;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2247 WHERE F_PRODUCTO_ARTICULO_P_ID = 770;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2247 WHERE F_PRODUCTO_ARTICULO_P_ID = 770;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2247 WHERE F_PRODUCTO_ARTICULO_P_ID = 770;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2198 WHERE F_PRODUCTO_ARTICULO_P_ID = 1009;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2198 WHERE F_PRODUCTO_ARTICULO_P_ID = 1009;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2198 WHERE F_PRODUCTO_ARTICULO_P_ID = 1009;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2377 WHERE F_PRODUCTO_ARTICULO_P_ID = 1076;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2377 WHERE F_PRODUCTO_ARTICULO_P_ID = 1076;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2377 WHERE F_PRODUCTO_ARTICULO_P_ID = 1076;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2259 WHERE F_PRODUCTO_ARTICULO_P_ID = 1096;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2259 WHERE F_PRODUCTO_ARTICULO_P_ID = 1096;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2259 WHERE F_PRODUCTO_ARTICULO_P_ID = 1096;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2267 WHERE F_PRODUCTO_ARTICULO_P_ID = 1162;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2267 WHERE F_PRODUCTO_ARTICULO_P_ID = 1162;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2267 WHERE F_PRODUCTO_ARTICULO_P_ID = 1162;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2338 WHERE F_PRODUCTO_ARTICULO_P_ID = 1287;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2338 WHERE F_PRODUCTO_ARTICULO_P_ID = 1287;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2338 WHERE F_PRODUCTO_ARTICULO_P_ID = 1287;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2380 WHERE F_PRODUCTO_ARTICULO_P_ID = 2327;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2380 WHERE F_PRODUCTO_ARTICULO_P_ID = 2327;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2380 WHERE F_PRODUCTO_ARTICULO_P_ID = 2327;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2289 WHERE F_PRODUCTO_ARTICULO_P_ID = 2092;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2289 WHERE F_PRODUCTO_ARTICULO_P_ID = 2092;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2289 WHERE F_PRODUCTO_ARTICULO_P_ID = 2092;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2264 WHERE F_PRODUCTO_ARTICULO_P_ID = 2125;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2264 WHERE F_PRODUCTO_ARTICULO_P_ID = 2125;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2264 WHERE F_PRODUCTO_ARTICULO_P_ID = 2125;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2217 WHERE F_PRODUCTO_ARTICULO_P_ID = 2199;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2217 WHERE F_PRODUCTO_ARTICULO_P_ID = 2199;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2217 WHERE F_PRODUCTO_ARTICULO_P_ID = 2199;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 620 WHERE F_PRODUCTO_ARTICULO_P_ID = 636;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 620 WHERE F_PRODUCTO_ARTICULO_P_ID = 636;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 620 WHERE F_PRODUCTO_ARTICULO_P_ID = 636;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2322 WHERE F_PRODUCTO_ARTICULO_P_ID = 2329;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2322 WHERE F_PRODUCTO_ARTICULO_P_ID = 2329;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2322 WHERE F_PRODUCTO_ARTICULO_P_ID = 2329;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2340 WHERE F_PRODUCTO_ARTICULO_P_ID = 2356;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2340 WHERE F_PRODUCTO_ARTICULO_P_ID = 2356;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2340 WHERE F_PRODUCTO_ARTICULO_P_ID = 2356;
update t_item_factura set F_PRODUCTO_ARTICULO_P_ID = 2312 WHERE F_PRODUCTO_ARTICULO_P_ID = 2333;
update t_orden_de_trabajo set F_PRODUCTO_ARTICULO_P_ID = 2312 WHERE F_PRODUCTO_ARTICULO_P_ID = 2333;
update t_remito_entrada_producto_art_asoc set F_PRODUCTO_ARTICULO_P_ID = 2312 WHERE F_PRODUCTO_ARTICULO_P_ID = 2333;

-- [sólo en A] borro los t_producto_articulo repetidos y finalmente el t_articulo.p_id=83
delete from t_producto_articulo where p_id in (select pa_to_update from tmp_prod_to_update);
update t_producto_articulo set f_articulo_p_id = 83 where f_articulo_p_id= 44; -- son los que no estaban repetidos
delete from t_articulo where p_id=44;

-- [sólo en A] drop tablas temporales
drop table tmp_prod_arts;
drop table tmp_prod_to_update;
