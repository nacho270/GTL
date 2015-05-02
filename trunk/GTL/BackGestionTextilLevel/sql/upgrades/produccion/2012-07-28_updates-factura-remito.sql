update t_remito remito
    inner join t_factura fact on remito.p_id = fact.f_remito_salida_p_id
set remito.f_factura_p_id = fact.p_id
where TIPO = 'SAL';

/*
update t_remito remito
    inner join t_factura fact on remito.a_nro_factura = fact.a_nro_factura
set remito.f_factura_p_id = fact.p_id
where TIPO = 'SAL';
*/

update t_factura fact
    inner join t_remito rem on rem.f_factura_p_id = fact.p_id
set fact.f_cliente_p_id = rem.f_cliente_p_id
where fact.f_cliente_p_id is null and rem.TIPO = 'SAL';