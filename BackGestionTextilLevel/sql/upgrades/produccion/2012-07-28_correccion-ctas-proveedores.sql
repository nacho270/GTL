update t_cuenta cu set cu.a_saldo = 0 where cu.f_proveedor_p_id is not null;


update t_cuenta cu
  inner join t_factura_proveedor fp on fp.f_prov_p_id =cu.f_proveedor_p_id
set cu.a_saldo = cu.a_saldo - fp.a_monto_total;

update t_cuenta cu
  inner join t_orden_pago op on op.f_prov_p_id  =cu.f_proveedor_p_id
set cu.a_saldo = cu.a_saldo + op.a_monto;


update t_cuenta cu
  inner join t_correccion_factura_prov cp on cp.f_prov_p_id  =cu.f_proveedor_p_id
set cu.a_saldo = cu.a_saldo + cp.a_monto_total
  where cp.DTYPE = 'NC';


update t_cuenta cu
  inner join t_correccion_factura_prov cp on cp.f_prov_p_id  =cu.f_proveedor_p_id
set cu.a_saldo = cu.a_saldo - cp.a_monto_total
  where cp.DTYPE = 'ND';
