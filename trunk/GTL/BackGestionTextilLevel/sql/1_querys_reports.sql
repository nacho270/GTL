select art.a_nombre, it.a_canitdad, (case it.a_unidad when 1 then 'MTS' when 2 then 'UD' when 3 then 'KG' end),
	it.a_descripcion, it.a_precio_unitario, it.a_importe
from t_item_factura it left join t_producto prod on prod.p_id = it.f_producto_p_id
left join t_articulo art on art.p_id = prod.f_articulo_p_id
where it.f_factura_p_id = 3