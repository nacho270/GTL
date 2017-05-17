-- revisar las siguientes facturas:

1)  factura anulada pero tiene movimiento => ¿qué hacemos?

select *
from t_factura f
inner join t_movimiento m on m.f_factura_p_id = f.p_id
where f.a_id_estado_factura=3;

update t_factura set a_id_estado_factura=2 where p_id=3510;



2) la factura dice que es de un cliente pero figura para otro => ¿qué hacemos?

select f.*
from t_factura f
inner join t_movimiento m on m.f_factura_p_id = f.p_id
inner join t_cuenta cta on cta.p_id = m.f_cuenta_p_id
where f.f_cliente_p_id != cta.f_cliente_p_id;

update t_factura set f_cliente_p_id=115 where p_id=1451;


-- revisar las cuentas

select cl.a_razon_social, cl.a_nrocliente, sum(X.DEB),sum(X.HAB), cast((sum(X.HAB)-sum(X.DEB)) as decimal(19,2)) AS CALC, cta.a_saldo as REG_CTA, abs(abs(cta.a_saldo) - abs(cast((sum(X.HAB)-sum(X.DEB)) as decimal(19,2))) ) as DIFF
from (

  select f_cliente_p_id as CLI, sum(m.a_monto) AS DEB, 0 as HAB
  from t_factura f
  inner join t_movimiento m on m.f_factura_p_id = f.p_id
  where A_ID_ESTADO_FACTURA != 3
  group by f_cliente_p_id

  union

  select f_cliente_p_id as CLI, 0, (-1)*sum(m.a_monto) AS HAB
  from t_factura f
  inner join t_movimiento m on m.f_nota_credito_p_id = f.p_id
  group by f_cliente_p_id

  union

  select f_cliente_p_id as CLI, sum(m.a_monto) AS DEB, 0 as HAB
  from t_factura f
  inner join t_movimiento m on m.f_nota_debito_p_id = f.p_id
  group by f_cliente_p_id

  union

  select f_cliente_p_id as CLI, 0 as DEB, sum(r.a_monto) as HAB from t_recibo r inner join t_movimiento mov on mov.f_recibo_p_id = r.p_id where r.A_ID_ESTADO_RECIBO != 3 group by r.f_cliente_p_id
) X
inner join t_cuenta cta on cta.f_cliente_p_id = X.CLI
inner join t_cliente cl on cl.p_id = X.CLI
group by cl.a_razon_social, cl.a_nrocliente
having abs(abs(cta.a_saldo) - abs(cast((sum(X.HAB)-sum(X.DEB)) as decimal(19,2)))) > 0;


-- update

update t_cuenta cta2
inner join (
	select cta.p_id as CTA_ID, cast((sum(X.HAB)-sum(X.DEB)) as decimal(19,2)) AS CALC, cta.a_saldo as REG_CTA
	from (

	  select f_cliente_p_id as CLI, sum(m.a_monto) AS DEB, 0 as HAB
	  from t_factura f
	  inner join t_movimiento m on m.f_factura_p_id = f.p_id
	  where A_ID_ESTADO_FACTURA != 3
	  group by f_cliente_p_id

	  union

	  select f_cliente_p_id as CLI, 0, (-1)*sum(m.a_monto) AS HAB
	  from t_factura f
	  inner join t_movimiento m on m.f_nota_credito_p_id = f.p_id
	  group by f_cliente_p_id

	  union

	  select f_cliente_p_id as CLI, sum(m.a_monto) AS DEB, 0 as HAB
	  from t_factura f
	  inner join t_movimiento m on m.f_nota_debito_p_id = f.p_id
	  group by f_cliente_p_id

	  union

	  select f_cliente_p_id as CLI, 0 as DEB, sum(r.a_monto) as HAB from t_recibo r inner join t_movimiento mov on mov.f_recibo_p_id = r.p_id where r.A_ID_ESTADO_RECIBO != 3 group by r.f_cliente_p_id
	) X
	inner join t_cuenta cta on cta.f_cliente_p_id = X.CLI
	inner join t_cliente cl on cl.p_id = X.CLI
	group by cta.p_id
	having abs(abs(cta.a_saldo) - abs(cast((sum(X.HAB)-sum(X.DEB)) as decimal(19,2)))) > 0
) Z on Z.CTA_ID = cta2.p_id
set cta2.a_saldo = Z.CALC;