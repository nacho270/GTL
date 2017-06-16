-- actualizo los movimientos de las ODPs repetidas

-- check cantidades

select a_descripcion, replace(a_descripcion, x.odp_orden, x.odp_pid)
from  t_movimiento m
inner join (
	select op.a_nro_orden as odp_orden, max(op.p_id) as odp_pid
	from t_orden_pago_pers op
	inner join (
	  select a_nro_orden as nop
	  from t_orden_pago_pers
	  group by a_nro_orden
	  having count(*) > 1
	) x on x.nop = op.a_nro_orden
	inner join t_persona pp on pp.p_id = op.f_pers_p_id
	group by op.a_nro_orden
) x on m.f_odp_pers_p_id = x.odp_pid;


-- update
update t_movimiento m
inner join (
	select op.a_nro_orden as odp_orden, max(op.p_id) as odp_pid
	from t_orden_pago_pers op
	inner join (
	  select a_nro_orden as nop
	  from t_orden_pago_pers
	  group by a_nro_orden
	  having count(*) > 1
	) x on x.nop = op.a_nro_orden
	inner join t_persona pp on pp.p_id = op.f_pers_p_id
	group by op.a_nro_orden
) x on m.f_odp_pers_p_id = x.odp_pid
set a_descripcion = replace(a_descripcion, x.odp_orden, x.odp_pid);

-- actualizo ODPs 

-- check cantidades
select op.a_nro_orden as odp_orden, max(op.p_id) as odp_pid
from t_orden_pago_pers op
inner join (
  select a_nro_orden as nop
  from t_orden_pago_pers
  group by a_nro_orden
  having count(*) > 1
) x on x.nop = op.a_nro_orden
inner join t_persona pp on pp.p_id = op.f_pers_p_id
group by op.a_nro_orden;


-- update
update t_orden_pago_pers odp2
inner join (
	select op.a_nro_orden as odp_orden, max(op.p_id) as odp_pid
	from t_orden_pago_pers op
	inner join (
	  select a_nro_orden as nop
	  from t_orden_pago_pers
	  group by a_nro_orden
	  having count(*) > 1
	) x on x.nop = op.a_nro_orden
	inner join t_persona pp on pp.p_id = op.f_pers_p_id
	group by op.a_nro_orden
) x on odp2.p_id = x.odp_pid
set odp2.a_nro_orden = x.odp_pid;

-- ejecutar en A y en B cuando se hayan resueltos los conflictos de unicidad con A_NRO_ORDEN 
ALTER TABLE `t_orden_pago_pers` ADD UNIQUE INDEX `IX_ODP_UNIQUE_NRO_ORDEN`(`A_NRO_ORDEN`);
