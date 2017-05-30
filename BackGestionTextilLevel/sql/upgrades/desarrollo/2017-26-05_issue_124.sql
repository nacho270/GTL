-- ejecutar en A y en B
select count(*)
from t_remito re2
where exists (select 1 from t_orden_de_trabajo odt where odt.f_remito_p_id = re2.p_id) and re2.tipo='ENT';


update t_remito re
inner join (
  select re2.p_id AS REID
  from t_remito re2
  where exists (select 1 from t_orden_de_trabajo odt where odt.f_remito_p_id = re2.p_id)
) Z on Z.REID = re.p_id
set re.A_ID_SITUACION_ODT = 0
where re.tipo='ENT';

select count(*)
from t_remito re2
where not exists (select 1 from t_orden_de_trabajo odt where odt.f_remito_p_id = re2.p_id) and re2.tipo='ENT';


update t_remito re
inner join (
  select re2.p_id AS REID
  from t_remito re2
  where not exists (select 1 from t_orden_de_trabajo odt where odt.f_remito_p_id = re2.p_id)

) Z on Z.REID = re.p_id
set re.A_ID_SITUACION_ODT = 2
where re.tipo='ENT';