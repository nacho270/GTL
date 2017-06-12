update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2792,
    rem.a_nro_factura = 2792,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2791, 2792)
where f.p_id = 1035;


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2793,
    rem.a_nro_factura = 2793,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2792, 2793)
where f.p_id = 1036;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2794,
    rem.a_nro_factura = 2794,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2793, 2794)
where f.p_id = 1037;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2795,
    rem.a_nro_factura = 2795,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2794, 2795)
where f.p_id = 1038;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2796,
    rem.a_nro_factura = 2796,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2795, 2796)
where f.p_id = 1039;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2797,
    rem.a_nro_factura = 2797,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2796, 2797)
where f.p_id = 1040;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2798,
    rem.a_nro_factura = 2798,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2797, 2798)
where f.p_id = 1041;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2799,
    rem.a_nro_factura = 2799,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2798, 2799)
where f.p_id = 1042;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2800,
    rem.a_nro_factura = 2800,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2799, 2800)
where f.p_id = 1043;

update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2801,
    rem.a_nro_factura = 2801,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2800, 2801)
where f.p_id = 1044;


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura + 1,
    rem.a_nro_factura = f.a_nro_factura + 1,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, f.a_nro_factura + 1)
where f.p_id = 1045 OR (f.a_nro_factura between 2802 and 2805);

update t_factura f
set f.a_nro_factura = f.a_nro_factura + 1
where f.p_id = 1047;


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura - 1,
    rem.a_nro_factura = f.a_nro_factura - 1,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, f.a_nro_factura - 1)
where f.p_id = 1222 OR (f.a_nro_factura between 2952 and 2981);


update t_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_factura = 3005,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3008, 3005)
where f.p_id = 2716;

update t_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_factura = 3007,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3010, 3007)
where f.p_id = 2717;

update t_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_factura = 3017,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3020, 3017)
where f.p_id = 2718;

update t_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_factura = 3025,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3028, 3025)
where f.p_id = 2719;

update t_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_factura = 3261,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3260, 3261)
where f.p_id = 2729;

update t_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_factura = 3628,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 3629, 3628)
where f.p_id = 2749;


update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = 2990,
    rem.a_nro_factura = 2990,
    mov.a_descripcion = REPLACE(mov.a_descripcion, 2991, 2990)
where f.p_id = 1231;

-- correr desde la 2756 hasta la 2781
update t_factura f
inner join t_movimiento mov on mov.f_factura_p_id = f.p_id
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura + 1,
    rem.a_nro_factura = f.a_nro_factura + 1,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, f.a_nro_factura + 1)
where (f.a_nro_factura between 2756 and 2781);

update t_factura f
inner join t_movimiento mov on mov.f_nota_debito_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura + 1,
    mov.a_descripcion = REPLACE(mov.a_descripcion, f.a_nro_factura, f.a_nro_factura +1)
where f.p_id in (2709, 2710,2711);


ALTER TABLE `gtl`.`t_factura` ADD UNIQUE INDEX `IX_UNIQUE_NRO_FC`(`A_NRO_FACTURA`);

-- ------------------------------------------------------------------------------------------------------
