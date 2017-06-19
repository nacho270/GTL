-- creo movimientos

insert into t_movimiento (tipo, a_monto, a_fecha_hora, a_descripcion, f_cuenta_p_id)
select 'MD', 0, remito.a_fecha_emision, CONCAT(DATE_FORMAT(remito.a_fecha_emision, "%d/%m/%Y"),
  ' - DEVOLUCION - RTO ', remito.a_nro_remito), c.p_id
from (
  select r.* from t_remito r
    inner join t_remito_salida_odt rsodt on rsodt.F_REMITO_SALIDA_P_ID = r.P_ID
    inner join t_orden_de_trabajo odt on odt.p_id = f_odt_p_id
    inner join t_producto_articulo pa on pa.p_id = odt.F_PROducto_articulo_p_id
    inner join t_producto p on p.p_id = pa.f_producto_p_id
  where p.tipo in ('REPROCESOSC', 'DEVOLUCION')
  group by r.p_id
) as remito
inner join t_cuenta c on c.f_cliente_p_id = remito.F_CLIENTE_P_ID;

-- pongo las ODT en facturada

update t_orden_de_trabajo odt
    inner join t_producto_articulo pa on pa.p_id = odt.F_PROducto_articulo_p_id
    inner join t_producto p on p.p_id = pa.f_producto_p_id
set a_id_estado = 7
where p.tipo in ('REPROCESOSC', 'DEVOLUCION');
