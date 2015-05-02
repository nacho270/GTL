update t_correccion_factura_prov set a_monto_total = a_monto_total * -1 where dtype = 'NC' and a_monto_total <0;
update t_movimiento set a_monto = a_monto * -1 where F_NOT_CRED_P_ID is not null and a_monto < 0;

-- CORREGIR
/*
'DALGAR S.A.'
'MORAN HNOS. S.R.L.'
'SEIPAC S.A.'
'ARANIL S.A.'
'ORG. DE SERVICIOS DIRECTO EMPRESARIOS'
'HILADO S.A. - TN & PLATEX -'
'PINTURERIAS REX S.A. '
*/