delete FROM t_tipo_art_tipo_art_asoc;
delete FROM t_articulo where f_tipo_articulo_p_id not in (2,3,5);
-- borrar formulas
delete from t_tipo_articulo where p_id not in (2,3,5);
-- asociar articulos algodon y poliester a ALGOGON-POLIESTER
update t_articulo set f_tipo_articulo_p_id = null;
update t_articulo set f_tipo_articulo_p_id =2 where substring(a_nombre,1,2) = 'AA';
update t_articulo set f_tipo_articulo_p_id =5 where substring(a_nombre,1,3) = 'POL';
update t_articulo set f_tipo_articulo_p_id =3 where substring(a_nombre,1,2) = 'AP';
update t_articulo set f_tipo_articulo_p_id =3 where substring(a_nombre,1,3) = 'PCL';
update t_articulo set f_tipo_articulo_p_id =3 where substring(a_nombre,1,2) = 'PC';
update t_articulo set f_tipo_articulo_p_id =5 where substring(a_nombre,1,6) = 'PONGEE';
update t_articulo set f_tipo_articulo_p_id =5 where substring(a_nombre,1,5) = 'PONGE';
update t_articulo set f_tipo_articulo_p_id =2 where substring(a_nombre,1,3) = 'GAB';
update t_articulo set f_tipo_articulo_p_id =2 where p_id = 24;
update t_articulo set f_tipo_articulo_p_id =2 where p_id = 23;
update t_articulo set f_tipo_articulo_p_id =3 where p_id = 13;
update t_articulo set f_tipo_articulo_p_id =3 where p_id = 21;
update t_articulo set f_tipo_articulo_p_id =3 where p_id in (22, 3, 4, 14);

-- DONE: chequear donde van los siguientes:
/*
TAPICERIA
PERCAL
ART. VS CAMISERIA
TELA PARA GUARDAPOLVO ANCHO 1.60
NIDO DE ABEJA ANCHO 1.60 MTRS
PONGE
GABARDINA
*/

-- TODO: arreglar cosas que no son articulos: devolucion,  descrude y termofijado

delete from t_articulo where p_id in (34, 40);

-- cargar ap 2,45
insert into t_articulo values(75, 'ALGODON POLIESTER 2,45', 'AP 2,45', 1, 2.45, 3);
update t_producto set f_articulo_p_id = 75 where f_articulo_p_id = 74;

-- cargar aa 1,52
insert into t_articulo values(70, 'ALGODON 1,52', 'AA 1,52', 1, 1.52, 2);

-- cargar ap 1,65
insert into t_articulo values(76, 'ALGODON POLIESTER 1,65', 'AP 1,65', 1, 1.65, 3);
update t_producto set f_articulo_p_id = 76 where f_articulo_p_id = 72;

-- cargar ap 2,73
insert into t_articulo values(77, 'ALGODON POLIESTER 2,73', 'AP 2,73', 1, 2.73, 3);
update t_producto set f_articulo_p_id = 77 where f_articulo_p_id = 73;

-- cargar PCL 2,85
insert into t_articulo values(71, 'PERCAL 2,85MTS', 'PCL 2,85', 1, 2.85, 3);


-- borro devolucion repetida
update t_remito_entrada_producto set f_producto_p_id = 1042 where f_producto_p_id = 1043;
delete from t_remito_entrada_producto where f_producto_p_id = 1043;
update t_orden_de_trabajo set f_producto_p_id = 1042 where f_producto_p_id = 1043;
delete from t_producto where p_id in (1043);

-- quito articulos de productos que no los requieren
update t_producto set f_articulo_p_id = null where p_id in (368, 395, 1042); /* 1222 ver que articulo es */

-- cargo anchos faltantes
update t_articulo set a_ancho = 2.4 where p_id = 1;
update t_articulo set a_ancho = 1.5 where p_id = 3;
update t_articulo set a_ancho = 3 where p_id = 4;
update t_articulo set a_ancho = 1.95 where p_id = 5;
update t_articulo set a_ancho = 1.5 where p_id = 6;
update t_articulo set a_ancho = 2.4 where p_id = 7;
update t_articulo set a_ancho = 1.5 where p_id = 8;
update t_articulo set a_ancho = 2.6 where p_id = 10;
update t_articulo set a_ancho = 2.7 where p_id = 11;
update t_articulo set a_ancho = 2.8 where p_id = 12;
update t_articulo set a_ancho = 1.6 where p_id = 13;
update t_articulo set a_ancho = 1.6 where p_id = 14;
update t_articulo set a_ancho = 1.6 where p_id = 15;
update t_articulo set a_ancho = 3 where p_id = 16;
update t_articulo set a_ancho = 1.85 where p_id = 18;
update t_articulo set a_ancho = 1.6 where p_id = 19;
update t_articulo set a_ancho = 2 where p_id = 20;
update t_articulo set a_ancho = 2.6 where p_id = 21;
update t_articulo set a_ancho = 2.10 where p_id = 22;
update t_articulo set a_ancho = 1.6 where p_id = 23;
update t_articulo set a_ancho = 1.6 where p_id = 24;
update t_articulo set a_ancho = 2.1 where p_id = 26;
update t_articulo set a_ancho = 2.5 where p_id = 27;
update t_articulo set a_ancho = 2.8 where p_id = 28;
update t_articulo set a_ancho = 2.05 where p_id = 29;
update t_articulo set a_ancho = 2.5 where p_id = 30;
update t_articulo set a_ancho = 2 where p_id = 31;
update t_articulo set a_ancho = 2.6 where p_id = 32;
update t_articulo set a_ancho = 1.05 where p_id = 33;
update t_articulo set a_ancho = 3 where p_id = 35;
update t_articulo set a_ancho = 3 where p_id = 37;
update t_articulo set a_ancho = 2.5 where p_id = 38;
update t_articulo set a_ancho = 2.7 where p_id = 39;
update t_articulo set a_ancho = 1.6 where p_id = 41;
update t_articulo set a_ancho = 2.8 where p_id = 42;
update t_articulo set a_ancho = 2.9 where p_id = 43;
update t_articulo set a_ancho = 1.5 where p_id = 44;
update t_articulo set a_ancho = 2.1 where p_id = 45;
update t_articulo set a_ancho = 3 where p_id = 46;
update t_articulo set a_ancho = 1.8 where p_id = 47;
update t_articulo set a_ancho = 2.7 where p_id = 48;
update t_articulo set a_ancho = 1.1 where p_id = 49;
update t_articulo set a_ancho = 1.1 where p_id = 50;
update t_articulo set a_ancho = 2.05 where p_id = 51;
update t_articulo set a_ancho = 2.1 where p_id = 52;
update t_articulo set a_ancho = 2.55 where p_id = 53;
update t_articulo set a_ancho = 3 where p_id = 54;
update t_articulo set a_ancho = 2.15 where p_id = 55;
update t_articulo set a_ancho = 1.2 where p_id = 56;
update t_articulo set a_ancho = 1.2 where p_id = 57;
update t_articulo set a_ancho = 1.6 where p_id = 58;
update t_articulo set a_ancho = 1.2 where p_id = 59;
update t_articulo set a_ancho = 2.45 where p_id = 60;
update t_articulo set a_ancho = 2.1 where p_id = 61;
update t_articulo set a_ancho = 1.6 where p_id = 62;
update t_articulo set a_ancho = 1.75 where p_id = 63;
update t_articulo set a_ancho = 2.07 where p_id = 64;
update t_articulo set a_ancho = 2.6 where p_id = 65;
update t_articulo set a_ancho = 1.68 where p_id = 66;
update t_articulo set a_ancho = 1.5 where p_id = 67;
update t_articulo set a_ancho = 1.42 where p_id = 68;
update t_articulo set a_ancho = 1.5 where p_id = 69;

-- agrego gramaje a repasador
update t_articulo set a_gramaje = '0.37' where p_id = 24;

update t_producto set a_descr='DESCRUDE Y TERMOFIJADO', f_articulo_p_id=4 where p_id=395;
-- TODO: HACER REFACTOR CREANDO PROD DESCRUDE



-- querys utiles

--validacion de tipo producto y ancho repetido
SELECT f_tipo_articulo_p_id, a_ancho, count(*)
FROM t_articulo
group by f_tipo_articulo_p_id, a_ancho
having f_tipo_articulo_p_id is not null and a_ancho is not null and count(*) > 1;

select * from t_producto pr where not exists( select 1 from t_articulo art where art.p_id = pr.f_articulo_p_id);
select * from t_remito_entrada_producto where f_producto_p_id in (368, 395, 1042);