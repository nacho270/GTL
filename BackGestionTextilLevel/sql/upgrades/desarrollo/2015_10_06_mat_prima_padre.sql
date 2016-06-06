update t_materia_prima set f_mp_padre=null where p_id=F_MP_PADRE;

-- detecta las que van en el IN de abajo
select mph.p_id
from t_materia_prima mph
inner join t_materia_prima mpadre on mpadre.p_id = mph.f_mp_padre and mpadre.f_mp_padre is not null;

update t_materia_prima mm
inner join t_materia_prima mp on mp.p_id = mm.F_MP_PADRE
set mm.F_MP_PADRE = mp.F_MP_PADRE
where mm.p_id in (463,1232,1235,2402,5256);

update t_anilina_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_item_factura_prov p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_pigmento_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_precio_materia_prima p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_quimico_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_reactivo_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;

-- borro las MPs HIJAS
delete from t_materia_prima where f_mp_padre is not null;
