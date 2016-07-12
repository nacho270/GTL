select mov.a_monto, cfp.a_monto_total, icr.a_importe, icr.a_importe*0.21
from T_CORRECCION_FACTURA_PROV cfp
inner join T_ITEM_CORRECC_FACT_PROV icf on icf.F_FACTURA_PROV_P_ID = cfp.p_id
inner join t_movimiento mov on mov.F_NOT_DEB_PROV_P_ID = cfp.p_id
inner join T_ITEM_CORRECC_FACT_PROV icr on icr.F_FACTURA_PROV_P_ID = cfp.p_id
where cfp.dtype='ND' and icr.tipo='ICR'and icf.tipo='ICCH' and  icr.a_importe > 0;

-- ejecutar en A y en B

update t_movimiento m
  inner join T_CORRECCION_FACTURA_PROV cfp on m.F_NOT_DEB_PROV_P_ID = cfp.p_id
  inner join T_ITEM_CORRECC_FACT_PROV icf on icf.F_FACTURA_PROV_P_ID = cfp.p_id
  inner join T_ITEM_CORRECC_FACT_PROV icr on icr.F_FACTURA_PROV_P_ID = cfp.p_id
set m.a_monto = m.a_monto - icr.a_importe*0.21
where cfp.dtype='ND' and icr.tipo='ICR'and icf.tipo='ICCH' and  icr.a_importe > 0;

update T_CORRECCION_FACTURA_PROV cfp
  inner join T_ITEM_CORRECC_FACT_PROV icf on icf.F_FACTURA_PROV_P_ID = cfp.p_id
  inner join T_ITEM_CORRECC_FACT_PROV icr on icr.F_FACTURA_PROV_P_ID = cfp.p_id
set cfp.a_monto_total = a_monto_total + icr.a_importe*0.21,cfp.A_MONTO_FALTANTE_X_PAGAR = icr.a_importe*0.21
where cfp.dtype='ND' and icr.tipo='ICR'and icf.tipo='ICCH' and  icr.a_importe > 0;


update t_cuenta cta
  inner join T_CORRECCION_FACTURA_PROV cfp on cfp.p_id = cta.F_PROVEEDOR_P_ID
  inner join T_ITEM_CORRECC_FACT_PROV icf on icf.F_FACTURA_PROV_P_ID = cfp.p_id
  inner join T_ITEM_CORRECC_FACT_PROV icr on icr.F_FACTURA_PROV_P_ID = cfp.p_id
set cta.a_saldo = cta.a_saldo + icr.a_importe*0.21
where cfp.dtype='ND' and icr.tipo='ICR'and icf.tipo='ICCH' and  icr.a_importe > 0;

