-- borrado de productos sin cargo mal cargados

update t_orden_de_trabajo set f_producto_p_id = 368 where f_producto_p_id in (712,713);
update t_remito_entrada_producto set f_producto_p_id = 368 where f_producto_p_id in (712,713);
delete from t_producto where p_id in (712, 713);

-- shift de facturas corrimiento de a 1

update t_movimiento m
inner join t_factura f on f.p_id = m.f_factura_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, f.a_nro_factura, f.a_nro_factura-1)
where f.a_nro_factura between 3891 and 3913;

update t_remito r
inner join t_factura f on f.p_id = r.f_factura_p_id
set r.a_nro_factura = f.a_nro_factura - 1
where f.a_nro_factura between 3891 and 3913;

update t_factura
set a_nro_factura = a_nro_factura - 1
where a_nro_factura between 3891 and 3913;


-- shift de facturas corrimiento de a 2

update t_movimiento m
inner join t_factura f on f.p_id = m.f_factura_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, f.a_nro_factura, f.a_nro_factura-2)
where f.a_nro_factura >= 3915;

update t_remito r
inner join t_factura f on f.p_id = r.f_factura_p_id
set r.a_nro_factura = f.a_nro_factura - 2
where f.a_nro_factura >= 3915;

update t_factura
set a_nro_factura = a_nro_factura - 2
where a_nro_factura >= 3915;


-- shift de ND corrimiento de a 1

update t_movimiento m
inner join t_correccion_factura f on f.p_id = m.f_nota_debito_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, f.a_nro_correccion, f.a_nro_correccion-1)
where f.a_nro_correccion = 3895;


update t_correccion_factura
set a_nro_correccion = a_nro_correccion - 1
where a_nro_correccion = 3895;


-- shift de ND corrimiento de a 2

update t_movimiento m
inner join t_correccion_factura f on f.p_id = m.f_nota_debito_p_id
set m.a_descripcion = REPLACE(m.a_descripcion, f.a_nro_correccion, f.a_nro_correccion-2)
where f.a_nro_correccion >=3929;


update t_correccion_factura
set a_nro_correccion = a_nro_correccion - 2
where a_nro_correccion >=3929;

-- nota anulada
update t_correccion_factura set a_anulada = true where a_nro_correccion = 3735;