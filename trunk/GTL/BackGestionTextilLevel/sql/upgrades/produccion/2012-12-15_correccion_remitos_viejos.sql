update t_remito r
inner join t_tmp_facturas_viejas tmp on r.p_id = tmp.p_id
set r.f_factura_p_id = tmp.f_factura_p_id;

update t_factura set f_cliente_p_id = 5 where p_id = 159;
update t_factura set f_cliente_p_id = 52 where p_id = 160;
update t_factura set f_cliente_p_id = 41 where p_id = 49;
update t_factura set f_cliente_p_id = 41 where p_id = 87;

update t_remito r
inner join t_factura f on f.p_id = r.f_Factura_p_id
set r.f_cliente_p_id = f.f_cliente_p_id
where r.f_factura_p_id is not null and r.f_cliente_p_id is null;

update t_factura set a_id_estado_factura=3 where p_id = 170;
update t_remito set a_anulado=1 where p_id=350;
update t_factura set f_cliente_p_id = 38 where a_nro_factura=1889;

update t_factura set a_id_estado_factura=3 where p_id = 252;
update t_remito set a_anulado=1 where p_id=511;



update t_remito r
inner join t_factura f on r.p_id = f.f_remito_salida_p_id
set r.a_nro_factura = f.a_nro_factura
where f.a_nro_factura != r.a_nro_factura;

update t_remito r
inner join t_factura f on r.p_id = f.f_remito_salida_p_id
set r.f_factura_p_id = f.p_id
where r.f_factura_p_id != f.p_id;

update t_factura set f_cliente_p_id = 8 where a_nro_factura=3283;

-- Correr corrector de cuentas en los clientes:
/*
'KALEJMAN ELENA RUTH'
'BANCHERO'
'DINAY S.R.L'
'MACATEX S.A'
'RODOLFO RENE BIANCHI'
'MARIQUEL S.R.L.'
'TEJEDURIA STAMATI S.A'
'MAYFRATEX S.A'
'DALESSANDRO MOLINA DIEGO DAVID'
'YABAR FABIO GABRIEL'
'FRANCO E HIJOS S.R.L'
'SUCESION DE SILVIO H. ERRAMUSPE'
'HORACIO BOTTINI'
'RONEU'
'TEJIDOS DE ARTE SORPRESA S.H.'
'RAUL PRUSKI'
*/







