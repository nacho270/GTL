update t_anilina_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_item_factura_prov p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_pigmento_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_precio_materia_prima p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_quimico_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;
update t_reactivo_cantidad p inner join t_materia_prima mp on mp.p_id = p.f_materia_prima_p_id set p.f_materia_prima_p_id = mp.F_MP_PADRE where mp.F_MP_PADRE is not null;


