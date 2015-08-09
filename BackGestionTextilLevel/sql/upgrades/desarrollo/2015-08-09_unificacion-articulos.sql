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