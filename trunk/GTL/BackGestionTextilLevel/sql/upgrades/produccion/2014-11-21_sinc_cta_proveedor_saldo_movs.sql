/* Query para chequear la cantidad de registros para updatear */
select count(*) from (
  select cta.f_proveedor_p_id, sum(mov.a_monto),cta.a_saldo
  from t_movimiento mov
  inner join t_cuenta cta on cta.p_id = mov.f_cuenta_p_id
  where cta.f_proveedor_p_id is not null
  group by cta.f_proveedor_p_id
  having sum(mov.a_monto) != cta.a_saldo
) X;

/* update que sincroniza las cuentas de los proveedores que estaban mal */
update t_cuenta cta2
inner join  (
  select cta.p_id as CTA, sum(mov.a_monto) AS MONTO,cta.a_saldo
  from t_movimiento mov
  inner join t_cuenta cta on cta.p_id = mov.f_cuenta_p_id
  where cta.f_proveedor_p_id is not null
  group by cta.p_id
  having sum(mov.a_monto) != cta.a_saldo
) X on X.CTA = cta2.p_id
set cta2.a_saldo=X.MONTO;

/* LIMPIO CHEQUES VENCIDOS VIEJOS */
update t_cheque set a_id_estado_cheque = 9, a_prov_salida='.'
where a_id_estado_cheque in (1,2) and a_fecha_deposito < '2014-01-01'
order by a_fecha_deposito desc;

/* SINCRONIZACION DE CUENTAS DE CLIENTES CON MOVIMIENTOS */
update t_cuenta cta2
inner join  (
  select cta.p_id as CTA, sum(mov.a_monto) AS MONTO,cta.a_saldo
  from t_movimiento mov
  inner join t_cuenta cta on cta.p_id = mov.f_cuenta_p_id
  where cta.f_cliente_p_id is not null
  group by cta.p_id
  having sum(mov.a_monto) != cta.a_saldo
) X on X.CTA = cta2.p_id
set cta2.a_saldo=-X.MONTO;

/* BORRO REGISTOS HUERFANOS */
delete from t_pago_recibo where f_recibo_p_id is null;
delete from t_forma_pago where f_recibo_p_id is null;

/* CORRIJO CHEQUE, HACER NOTA DE DEBITO */
update t_cheque set a_id_estado_cheque = 2 where a_numero_interno_cheque = 7192;

/* CORRIJO CHEQUE DEBERIA ESTAR EN CARTERA */
update t_cheque set a_id_estado_cheque = 2 where a_numero_interno_cheque = 7747;