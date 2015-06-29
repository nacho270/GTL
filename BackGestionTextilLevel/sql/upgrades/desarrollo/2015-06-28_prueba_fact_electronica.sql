update t_factura f
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura =-f.a_nro_factura,
    rem.a_nro_factura = -f.a_nro_factura;

update t_factura f set f.a_nro_factura = f.a_nro_factura*(-1) where f.a_nro_factura > 0;

update T_PARAM_GENERALES set A_NRO_COMIENZO_FACTURA = 0;
