-- DAR DE ALTA CON EL ABM UN TIPO DE ARTICULO 'ALGODÓN POLIESTER' -> 'AP'
select * from t_articulo where a_nombre like 'AP %';
update t_articulo set f_tipo_articulo_p_id = 1 where a_nombre like 'AP %';

-- DAR DE ALTA CON EL ABM UN TIPO DE ARTICULO 'ALGODÓN 100%' -> 'AA'
select * from t_articulo where a_nombre like 'AA %';
update t_articulo set f_tipo_articulo_p_id = 2 where a_nombre like 'AA %';

-- DAR DE ALTA CON EL ABM UN TIPO DE ARTICULO 'PERCAL' -> 'PCL'
select * from t_articulo where a_nombre like 'PCL %';
update t_articulo set f_tipo_articulo_p_id = 3 where a_nombre like 'PCL %';

-- DAR DE ALTA CON EL ABM UN TIPO DE ARTICULO 'POLIESTER' -> 'POL'
select * from t_articulo where a_nombre like 'POL %';
update t_articulo set f_tipo_articulo_p_id = 4 where a_nombre like 'POL %';

-- DAR DE ALTA CON EL ABM UN TIPO DE ARTICULO 'TAPICERIA' -> 'TAP'
select * from t_articulo where a_nombre like 'TAP %';
update t_articulo set f_tipo_articulo_p_id = 5 where a_nombre like 'TAP %';

select * from t_tipo_articulo;
select * from t_articulo where f_tipo_articulo_p_id is null order by a_nombre;


-- FIX ARTICULOS COMPUESTOS

update t_articulo set f_tipo_articulo_p_id = 1 where f_tipo_articulo_p_id in (3,5); -- TAP/PERCAL => AP

delete from t_quimico_cantidad;
delete from t_instruccion_procedimiento ;
delete from t_procedimiento_tipo_articulo;
delete from  t_maquina_tipo_articulo_asoc where f_tipo_articulo_p_id in (3,5);
delete from t_tipo_articulo where p_id in (3,5);
