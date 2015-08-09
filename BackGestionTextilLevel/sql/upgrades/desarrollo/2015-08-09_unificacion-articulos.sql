delete FROM t_tipo_art_tipo_art_asoc;
delete FROM t_articulo where f_tipo_articulo_p_id not in (2,3,5);
-- borrar formulas
delete from t_tipo_articulo where p_id not in (2,3,5);
-- asociar articulos algodon y poliester a ALGOGON-POLIESTER
update t_articulo set f_tipo_articulo_p_id = null;
update t_articulo set f_tipo_articulo_p_id =2 where substring(a_nombre,1,2) = 'AA';
update t_articulo set f_tipo_articulo_p_id =5 where substring(a_nombre,1,3) = 'POL';
update t_articulo set f_tipo_articulo_p_id =3 where substring(a_nombre,1,2) = 'AP';

-- chequear donde van los siguientes:
/*
TAPICERIA
PERCAL
ART. VS CAMISERIA
TELA PARA GUARDAPOLVO ANCHO 1.60
NIDO DE ABEJA ANCHO 1.60 MTRS
PONGE
GABARDINA
*/

-- arreglar cosas que no son articulos: devolucion,  descrude y termofijado

delete from t_articulo where p_id in (34, 40);

-- cargar ap 2,45
update t_producto set f_articulo_p_id = 75 where f_articulo_p_id = 74;

update t_producto set f_articulo_p_id = 69 where f_articulo_p_id = 70;

-- cargar ap 1,65
update t_producto set f_articulo_p_id = 76 where f_articulo_p_id = 72;

-- cargar ap 2,73
update t_producto set f_articulo_p_id = 77 where f_articulo_p_id = 73;


-- querys utiles

select * from t_producto pr where not exists( select 1 from t_articulo art where art.p_id = pr.f_articulo_p_id);

delete from t_producto where p_id in (368, 395, 1042, 1043);

select * from t_remito_entrada_producto where f_producto_p_id in (368, 395, 1042, 1043);