update t_movimiento m
inner join t_factura f on f.p_id = m.f_factura_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, f.a_nro_factura, f.a_nro_factura-2)
where f.a_nro_factura >= 3289;

update t_remito r
inner join t_factura f on f.p_id = r.f_factura_p_id
set r.a_nro_factura = f.a_nro_factura - 2
where f.a_nro_factura >= 3289;

update t_factura
set a_nro_factura = a_nro_factura - 2
where a_nro_factura >= 3289;
SELECT * FROM t_remito t;



/*ND*/

update t_movimiento m
inner join t_correccion_factura c on c.p_id = m.f_nota_debito_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, c.a_nro_correccion, c.a_nro_correccion-2)
where c.a_nro_correccion >= 3289 and c.TIPO = 'ND';

update t_correccion_factura c
set c.a_nro_correccion = c.a_nro_correccion-2
where c.a_nro_correccion >= 3289 and c.TIPO = 'ND';








/* SEGUNDA ETAPA */

update t_movimiento m
inner join t_factura f on f.p_id = m.f_factura_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, f.a_nro_factura, f.a_nro_factura-1)
where f.a_nro_factura between 3394 and 3396;

update t_remito r
inner join t_factura f on f.p_id = r.f_factura_p_id
set r.a_nro_factura = f.a_nro_factura - 1
where f.a_nro_factura between 3394 and 3396;

update t_factura
set a_nro_factura = a_nro_factura - 1
where a_nro_factura between 3394 and 3396;
SELECT * FROM t_remito t;



/*ND*/

update t_movimiento m
inner join t_correccion_factura c on c.p_id = m.f_nota_debito_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, c.a_nro_correccion, c.a_nro_correccion-1)
where c.a_nro_correccion between 3394 and 3396 and c.TIPO = 'ND';

update t_correccion_factura c
set c.a_nro_correccion = c.a_nro_correccion-1
where c.a_nro_correccion between 3394 and 3396 and c.TIPO = 'ND';







/* TERCERA ETAPA */


update t_movimiento m
inner join t_factura f on f.p_id = m.f_factura_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, f.a_nro_factura, f.a_nro_factura-2)
where f.a_nro_factura between 3398 and 3400;

update t_remito r
inner join t_factura f on f.p_id = r.f_factura_p_id
set r.a_nro_factura = f.a_nro_factura - 2
where f.a_nro_factura between 3398 and 3400;

update t_factura
set a_nro_factura = a_nro_factura - 2
where a_nro_factura between 3398 and 3400;
SELECT * FROM t_remito t;



/*ND*/

update t_movimiento m
inner join t_correccion_factura c on c.p_id = m.f_nota_debito_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, c.a_nro_correccion, c.a_nro_correccion-2)
where c.a_nro_correccion between 3398 and 3400 and c.TIPO = 'ND';

update t_correccion_factura c
set c.a_nro_correccion = c.a_nro_correccion-2
where c.a_nro_correccion between 3398 and 3400 and c.TIPO = 'ND';


/* CUARTA ETAPA */


/*NC*/

update t_movimiento m
inner join t_correccion_factura c on c.p_id = m.f_nota_credito_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, c.a_nro_correccion, c.a_nro_correccion-4)
where c.a_nro_correccion = 3403 and c.TIPO = 'NC';

update t_correccion_factura c
set c.a_nro_correccion = c.a_nro_correccion-4
where c.a_nro_correccion = 3403 and c.TIPO = 'NC';
