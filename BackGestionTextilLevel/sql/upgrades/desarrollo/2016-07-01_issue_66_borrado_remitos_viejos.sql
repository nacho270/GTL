-- para detectar los remitos a borrar!!
select re.*
from t_remito re
left join t_orden_de_trabajo odt on odt.f_remito_p_id = re.p_id
left join T_REMITO_SALIDA_ODT asoc on asoc.f_odt_p_id = odt.p_id
where re.tipo='ENT' and (odt.p_id is null OR (odt.p_id is not null and odt.f_secuencia_p_id is null and asoc.F_ODT_P_ID is null)) and
      not exists (
        select 1
        from t_pieza_remito pr
          left join t_pieza_remito pr2 on pr2.f_pieza_padre_p_id = pr.p_id
          inner join t_remito sal on sal.p_id = pr2.f_remito_p_id and sal.tipo='SAL'
        where pr.f_remito_p_id = re.p_id
);


-- creo tabla con los registros a borrar

-- (1) RE que no tienen o tienen ODT pero sin RS (esto se puede correr en la A y en la B).
create table ru_remito_borrar
select re.p_id
from t_remito re
left join t_orden_de_trabajo odt on odt.f_remito_p_id = re.p_id
left join T_REMITO_SALIDA_ODT asoc on asoc.f_odt_p_id = odt.p_id
where re.tipo='ENT' and (odt.p_id is null OR (odt.p_id is not null and odt.f_secuencia_p_id is null and asoc.F_ODT_P_ID is null)) and
      not exists (
        select 1
        from t_pieza_remito pr
          left join t_pieza_remito pr2 on pr2.f_pieza_padre_p_id = pr.p_id
          inner join t_remito sal on sal.p_id = pr2.f_remito_p_id and sal.tipo='SAL'
        where pr.f_remito_p_id = re.p_id
);


create table ru_odt_borrar
    select odt.p_id
    from ru_remito_borrar re
      left join t_orden_de_trabajo odt on odt.f_remito_p_id = re.p_id
where odt.F_SECUENCIA_P_ID is null;


create table ru_pieza_odt_borrar
select pdt.p_id
from t_pieza_odt pdt
inner join ru_odt_borrar odt where odt.p_id = pdt.f_odt_p_id;

create table ru_pieza_remito_borrar
select pr.p_id
from t_pieza_remito pr
inner join ru_remito_borrar rem where rem.p_id = pr.f_remito_p_id;


create table ru_transicion_borrar
select t.p_id
from t_transicion_odt t
inner join ru_odt_borrar odt on odt.p_id = t.f_odt_p_id;

create table ru_cambioavance_borrar
select c.p_id
from T_CAMBIO_AVANCE_ODT c
inner join ru_transicion_borrar t on t.p_id = c.F_TRANSICION_P_ID;

-- comienzan los delets/updates!!

delete pp from t_pieza_odt pp inner join ru_pieza_odt_borrar ru on ru.p_id = pp.p_id;
delete pr from t_pieza_remito pr inner join ru_pieza_remito_borrar ru on ru.p_id = pr.p_id;

delete ca from t_cambio_avance_odt ca inner join ru_cambioavance_borrar ru on ru.p_id = ca.p_id;
delete t from t_transicion_odt t inner join ru_transicion_borrar ru on ru.p_id = t.p_id;

delete odt from t_orden_de_trabajo odt inner join ru_odt_borrar ru on ru.p_id = odt.p_id;

update t_remito rs set rs.f_remito_entrada_p_id=null where f_remito_entrada_p_id is not null;

delete asoc from t_remito_entrada_producto_art_asoc asoc inner join ru_remito_borrar re on re.p_id = asoc.f_remito_entrada_p_id;

update T_CUENTA_ARTICULO cta
inner join T_MOVIMIENTO_CTA_ART mov on mov.F_CUENTA_P_ID = cta.p_id
inner join ru_remito_borrar ru on ru.p_id = mov.f_remito_entrada_p_id
set cta.a_cantidad = cta.a_cantidad - mov.a_cantidad;

delete mov
from T_MOVIMIENTO_CTA_ART mov
inner join ru_remito_borrar ru on ru.p_id = mov.f_remito_entrada_p_id;

delete re from t_remito re inner join ru_remito_borrar ru on ru.p_id = re.p_id;


-- (2) SOLO EN LA B, BORRAR RE que TENGAN RS PERO NO FACTURADOS NI ANULADOS
drop table ru_remito_borrar;

create table ru_remito_borrar
select re.p_id
from t_remito re
inner join t_orden_de_trabajo odt on odt.f_remito_p_id = re.p_id
inner join T_REMITO_SALIDA_ODT asoc on asoc.f_odt_p_id = odt.p_id
inner join t_remito rs on rs.p_id = asoc.F_REMITO_SALIDA_P_ID
where rs.f_factura_p_id is null and (rs.a_anulado is null or rs.a_anulado=0);

drop table ru_odt_borrar;
create table ru_odt_borrar
    select odt.p_id
    from ru_remito_borrar re
      left join t_orden_de_trabajo odt on odt.f_remito_p_id = re.p_id
where odt.F_SECUENCIA_P_ID is null;

drop table ru_pieza_odt_borrar;
create table ru_pieza_odt_borrar
select pdt.p_id
from t_pieza_odt pdt
inner join ru_odt_borrar odt where odt.p_id = pdt.f_odt_p_id;

create table ru_rem_sal_borrar
    select asoc.F_REMITO_SALIDA_P_ID
    from ru_odt_borrar odt
      left join T_REMITO_SALIDA_ODT asoc on asoc.f_odt_p_id = odt.p_id;

create table ru_pieza_remito_sal_borrar
select pr.p_id
from t_pieza_remito pr
inner join ru_rem_sal_borrar ru where ru.F_REMITO_SALIDA_P_ID = pr.f_remito_p_id;


drop table ru_pieza_remito_borrar;
create table ru_pieza_remito_borrar
select pr.p_id
from t_pieza_remito pr
inner join ru_remito_borrar rem where rem.p_id = pr.f_remito_p_id;

drop table ru_transicion_borrar;
create table ru_transicion_borrar
select t.p_id
from t_transicion_odt t
inner join ru_odt_borrar odt on odt.p_id = t.f_odt_p_id;

drop table ru_cambioavance_borrar;
create table ru_cambioavance_borrar
select c.p_id
from T_CAMBIO_AVANCE_ODT c
inner join ru_transicion_borrar t on t.p_id = c.F_TRANSICION_P_ID;

-- comienzan los delets/updates!!

delete asoc from T_PIEZA_REM_SAL_PIEZA_ODT asoc inner join ru_pieza_remito_sal_borrar ru on asoc.F_PIEZA_REM_SAL_P_ID = ru.p_id;
delete pp from t_pieza_remito pp inner join ru_pieza_remito_sal_borrar ru on ru.p_id = pp.p_id;

delete rs from t_remito_salida_odt rs inner join ru_rem_sal_borrar ru on ru.F_REMITO_SALIDA_P_ID = rs.F_REMITO_SALIDA_P_ID;

delete rs from t_remito rs inner join ru_rem_sal_borrar ru on ru.F_REMITO_SALIDA_P_ID = rs.p_id;


delete pp from t_pieza_odt pp inner join ru_pieza_odt_borrar ru on ru.p_id = pp.p_id;
delete pr from t_pieza_remito pr inner join ru_pieza_remito_borrar ru on ru.p_id = pr.p_id;

delete ca from t_cambio_avance_odt ca inner join ru_cambioavance_borrar ru on ru.p_id = ca.p_id;
delete t from t_transicion_odt t inner join ru_transicion_borrar ru on ru.p_id = t.p_id;

delete odt from t_orden_de_trabajo odt inner join ru_odt_borrar ru on ru.p_id = odt.p_id;

update t_remito rs set rs.f_remito_entrada_p_id=null where f_remito_entrada_p_id is not null;

delete asoc from t_remito_entrada_producto_art_asoc asoc inner join ru_remito_borrar re on re.p_id = asoc.f_remito_entrada_p_id;

update T_CUENTA_ARTICULO cta
inner join T_MOVIMIENTO_CTA_ART mov on mov.F_CUENTA_P_ID = cta.p_id
inner join ru_remito_borrar ru on ru.p_id = mov.f_remito_entrada_p_id
set cta.a_cantidad = cta.a_cantidad - mov.a_cantidad;

delete mov
from T_MOVIMIENTO_CTA_ART mov
inner join ru_remito_borrar ru on ru.p_id = mov.f_remito_entrada_p_id;

delete re from t_remito re inner join ru_remito_borrar ru on ru.p_id = re.p_id;


-- drop de las tablas

drop table ru_pieza_odt_borrar;
drop table ru_pieza_remito_borrar;
drop table ru_cambioavance_borrar;
drop table ru_transicion_borrar;
drop table ru_odt_borrar;
drop table ru_remito_borrar;


