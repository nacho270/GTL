update t_movimiento set a_monto = a_monto * -1 where a_monto < 0 and f_not_cred_p_id is not null;
update  t_correccion_factura_prov set a_monto_total = a_monto_total * -1 where DTYPE = 'NC' and a_monto_total < 0;

-- CORRECTOR PROVEEDOR