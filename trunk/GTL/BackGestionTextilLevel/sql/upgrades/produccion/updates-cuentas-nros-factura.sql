update t_remito set f_cliente_p_id = null where p_id in (18, 30, 34, 46, 89);

update t_remito set f_factura_p_id=null where p_id=30;

/*
delete from t_movimiento where p_id = 5702;
delete from t_item_factura where f_factura_p_id = 11;
delete from t_factura where p_id = 11;
*/

-- ARREGLAR CLIENTE YABAR Y STAMATI

/*
delete from t_movimiento where p_id = 5475
delete from t_item_factura where f_factura_p_id = 83;
delete from t_factura where p_id = 83;
*/

-- AREGLAR BONETTI


update t_remito set a_nro_factura = 1769 where p_id = 111;
update t_remito set a_nro_factura = 1799 where p_id = 187;
update t_remito set a_nro_factura = 1878 where p_id = 337;
update t_remito set a_nro_factura = 1879 where p_id = 338;
update t_remito set a_nro_factura = 1882 where p_id = 340;
update t_remito set a_nro_factura = 1883 where p_id = 341;
update t_remito set a_nro_factura = 1884 where p_id = 342;
update t_remito set a_nro_factura = 1885 where p_id = 343;
update t_remito set a_nro_factura = 1886 where p_id = 344;
update t_remito set a_nro_factura = 1887 where p_id = 345;
update t_remito set a_nro_factura = 1888 where p_id = 346;
update t_factura set f_remito_salida_p_id = 346 where p_id = 168;

update t_remito set a_nro_factura = 2939 where p_id = 2401;
update t_factura set a_nro_factura = 2939 where p_id = 1180;

update t_remito set a_nro_factura = 2940 where p_id = 2403;
update t_factura set a_nro_factura = 2940 where p_id = 1181;

update t_remito set a_nro_factura = 2941 where p_id = 2405;
update t_factura set a_nro_factura = 2941 where p_id = 1182;


update t_factura fact
    inner join t_movimiento mov on mov.f_factura_p_id = fact.p_id
    inner join t_cuenta cta on cta.p_id = mov.f_cuenta_p_id
set fact.f_cliente_p_id = cta.f_cliente_p_id
where fact.p_id in (
12,
152,
153,
154,
155,
156,
157,
158,
265,
269,
287,
1434,
1446,
1451
);


update t_factura set f_cliente_p_id=41 where p_id=1445;
update t_factura set f_cliente_p_id=70 where p_id=1431;
update t_factura set f_cliente_p_id=92 where p_id=1429;
update t_factura set f_cliente_p_id=92 where p_id=1432;

/*ERRAMUSPE*/

update t_factura set a_nro_factura=2989 where a_nro_factura=2990;
update t_remito set a_nro_factura=2989 where p_id=2516;

update t_factura set a_nro_factura=2988 where p_id=1229;
update t_remito set a_nro_factura=2988 where f_factura_p_id=1229;

update t_factura set a_nro_factura=2977 where p_id=1217;
update t_remito set a_nro_factura=2977 where f_factura_p_id=1217;


update t_factura set a_nro_factura=2964 where p_id=1204;
update t_remito set a_nro_factura=2964 where f_factura_p_id=1204;


update t_factura set a_nro_factura=2950 where p_id=1191;
update t_remito set a_nro_factura=2950 where f_factura_p_id=1191;

update t_factura set a_nro_factura=2948 where p_id=1189;
update t_remito set a_nro_factura=2948 where f_factura_p_id=1189;

update t_factura set a_nro_factura=2943 where p_id=1184;
update t_remito set a_nro_factura=2943 where f_factura_p_id=1184;


update t_factura set a_nro_factura=2784 where p_id=1029;
update t_remito set a_nro_factura=2784 where f_factura_p_id=1029;

update t_factura set a_nro_factura=2783 where p_id=1026;
update t_remito set a_nro_factura=2783 where f_factura_p_id=1026;

update t_factura set a_nro_factura=2749 where p_id=995;
update t_remito set a_nro_factura=2749 where f_factura_p_id=995;

update t_factura set f_cliente_p_id=41 where p_id=270;

update t_factura set f_cliente_p_id=92 where p_id=263;

update t_factura set f_cliente_p_id=41 where p_id=162;

/*PRUSKI*/

update t_factura set f_cliente_p_id=5 where p_id=1453;

/*FRANCO*/

update t_factura set f_cliente_p_id=5 where p_id = 43;
update t_remito set f_cliente_p_id=26 where p_id=30;
update t_factura set f_cliente_p_id=26 where p_id = 174;
update t_factura set f_cliente_p_id=46 where p_id = 165;

/*MACATEX*/
update t_factura f
inner join t_remito r on r.p_id = f.f_remito_salida_p_id
set f.f_cliente_p_id=r.f_cliente_p_id
where f.p_id in (61,64,164,171,175,1433,1437,1441);

update t_factura f
inner join t_remito r on r.p_id = f.f_remito_salida_p_id
set f.f_cliente_p_id=41
where f.p_id in (166,1180,1181,1439);

update t_factura set a_id_estado_factura=3 where p_id = 252;
update t_remito set a_anulado=1 where p_id=511;

update t_factura set f_cliente_p_id=81 where p_id = 173;

/*BONNETI*/
update t_factura set f_cliente_p_id=7 where p_id = 1182;

/*BIANCHI*/
update t_factura set f_cliente_p_id=5 where p_id = 857;

/*BOTINI*/
update t_factura set f_cliente_p_id=4 where p_id = 264;
update t_factura set f_cliente_p_id=4 where p_id = 1440;

/*STAMATI*/
update t_factura set f_cliente_p_id=63 where p_id = 167;
update t_factura set f_cliente_p_id=5 where p_id = 163;
update t_factura set f_cliente_p_id=81 where p_id = 176;

/*MAYFRATEX*/
update t_factura set f_cliente_p_id=5 where p_id=389;
update t_factura set f_cliente_p_id=40 where p_id = 168;

/*RONEU*/
update t_factura set f_cliente_p_id=18 where p_id=169;

/*DALESANDRO*/
update t_factura set f_cliente_p_id=143 where p_id=1459;
update t_factura set f_cliente_p_id=18 where p_id = 1457;

/*TEXTIL DOSS*/
update t_remito set f_cliente_p_id = 77 where p_id = 18;

/*PEDROZA*/
update t_factura set f_cliente_p_id=113 where p_id = 387;



/*FIX DESFASAJE FACTURAS Y CORRECCIONES*/

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura - 1,
    rem.a_nro_factura = f.a_nro_factura - 1,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, f.a_nro_factura -1)
where f.a_nro_factura in (3628);


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura - 2,
    rem.a_nro_factura = f.a_nro_factura - 2,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, f.a_nro_factura -2)
where f.a_nro_factura in (3665,3666,3669,3670,3672,3673,3678,3681,3682,3683,3686,3687,3688,3689, 3690);


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura - 4,
    rem.a_nro_factura = f.a_nro_factura - 4,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, f.a_nro_factura -4)
where f.a_nro_factura in (3705,3713,3714, 3718,3719,3720);


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = CONVERT(TRIM(REPLACE(REPLACE(mov.a_observaciones, 'FC', ''), 'fc', '')), UNSIGNED),
    rem.a_nro_factura = CONVERT(TRIM(REPLACE(REPLACE(mov.a_observaciones, 'FC', ''), 'fc', '')), UNSIGNED),
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, TRIM(REPLACE(REPLACE(mov.a_observaciones, 'FC', ''), 'fc', '')))
where mov.a_observaciones like 'FC%';

update t_correccion_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_correccion = f.a_nro_correccion - 2,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_correccion, f.a_nro_correccion -2)
where f.a_nro_correccion in (3684, 3685);

update t_correccion_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_correccion = f.a_nro_correccion - 4,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_correccion, f.a_nro_correccion -4)
where f.a_nro_correccion in (3716, 3717);


update t_correccion_factura f
inner join t_movimiento mov on mov.f_nota_credito_p_id = f.p_id
set f.a_nro_correccion = f.a_nro_correccion - 2,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_correccion, f.a_nro_correccion -2)
where f.a_nro_correccion in (3691);


/*FIX 2 FACTURAS MAS*/

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 3646,
    rem.a_nro_factura = 3646,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3546, 3646)
where f.p_id = 1800; -- 3546;


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 3648,
    rem.a_nro_factura = 3648,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3548, 3648)
where f.p_id = 1802; -- 3546;