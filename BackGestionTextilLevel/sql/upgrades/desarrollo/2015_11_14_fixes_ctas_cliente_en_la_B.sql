-- BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB

-- BORRO CUENTAS DUPLICADAS
delete from t_cuenta where p_id=212;  -- cliente 202
-- cliente 106
update t_movimiento set f_cuenta_p_id= 67 where f_cuenta_p_id=55;
delete from t_cuenta where p_id=55; 
delete from t_cuenta where p_id=215;   -- cliente 140
delete from t_cuenta where p_id=220;   -- cliente 151

select cl.a_razon_social, cl2.a_razon_social, ccc.a_razon_social, f.*
from t_movimiento m
 inner join t_factura f on m.f_nota_credito_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
where cl2.a_nrocliente != cl.a_nrocliente;

update  t_movimiento m
 inner join t_factura f on m.f_nota_credito_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
 inner join t_cuenta cta on cta.f_cliente_p_id = ccc.p_id
set m.f_cuenta_p_id = cta.p_id

where cl2.a_nrocliente != cl.a_nrocliente;

select cl.a_razon_social, cl2.a_razon_social, ccc.a_razon_social, f.*
from t_movimiento m
 inner join t_factura f on m.f_nota_debito_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
where cl2.a_nrocliente != cl.a_nrocliente;

update  t_movimiento m
 inner join t_factura f on m.f_nota_debito_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
 inner join t_cuenta cta on cta.f_cliente_p_id = ccc.p_id
 set m.f_cuenta_p_id = cta.p_id
where cl2.a_nrocliente != cl.a_nrocliente;


select cl.a_razon_social, cl2.a_razon_social, ccc.a_razon_social, f.*
from t_movimiento m
 inner join t_factura f on m.f_factura_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
where cl2.a_nrocliente != cl.a_nrocliente;

update  t_movimiento m
 inner join t_factura f on m.f_factura_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
 inner join t_cuenta cta on cta.f_cliente_p_id = ccc.p_id
 set m.f_cuenta_p_id = cta.p_id
where cl2.a_nrocliente != cl.a_nrocliente;



select cl.a_razon_social, cl2.a_razon_social, ccc.a_razon_social, f.*
from t_movimiento m
 inner join t_recibo f on m.f_recibo_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_recibo_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
where cl2.a_nrocliente != cl.a_nrocliente;

update  t_movimiento m
 inner join t_recibo f on m.f_recibo_p_id = f.p_id
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_recibo_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
 inner join t_cuenta cta on cta.f_cliente_p_id = ccc.p_id
 set m.f_cuenta_p_id = cta.p_id
where cl2.a_nrocliente != cl.a_nrocliente;


select f.* from t_factura f
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act
 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
where cl2.a_nrocliente != cl.a_nrocliente;

update t_factura f
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_factura_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act
 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
set f.f_cliente_p_id = ccc.p_id
where cl2.a_nrocliente != cl.a_nrocliente;



select cl.a_razon_social, cl2.a_razon_social, ccc.a_razon_social, f.*
from t_recibo f
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_recibo_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
where cl2.a_nrocliente != cl.a_nrocliente;


update t_recibo f
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_recibo_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
set f.f_cliente_p_id = ccc.p_id
where cl2.a_nrocliente != cl.a_nrocliente;


select cl.a_razon_social, cl2.a_razon_social, ccc.a_razon_social, f.*
from t_remito f
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_remito_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
where cl2.a_nrocliente != cl.a_nrocliente;


update t_remito f
 inner join t_cliente cl on cl.p_id = f.f_cliente_p_id
 inner join t_remito_fix ff on ff.p_id = f.p_id
 inner join t_cliente_old cl2 on cl2.p_id = ff.f_cliente_p_id
 inner join (
      select 106 as cli_ant, 16 as cli_act
      union
      select 151 as cli_ant, 72 as cli_act
      union
      select 156 as cli_ant, 72 as cli_act
      union
      select 157 as cli_ant, 94 as cli_act

 ) Z on Z.cli_ant = cl.a_nrocliente
 inner join t_cliente ccc on ccc.a_nrocliente = Z.cli_act
set f.f_cliente_p_id = ccc.p_id
where cl2.a_nrocliente != cl.a_nrocliente;

-- EJECUTAR CORRECTOR PARA 106, 16, 151, 72, 156, 157, 94, 202