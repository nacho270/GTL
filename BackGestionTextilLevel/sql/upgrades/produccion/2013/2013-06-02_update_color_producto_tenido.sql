select count(*) from t_producto where tipo='TENIDO' and f_color_p_id is null; 

-- update teniendo en cuenta la gama
update t_producto p
inner join T_GAMA gc on gc.p_id = p.F_GAMA_P_ID
inner join T_COLOR c on c.f_gama_p_id = gc.p_id
set p.f_color_p_id = c.p_id
where p.tipo='TENIDO' and
      TRIM(c.A_NOMBRE) = TRIM(SUBSTR(p.A_DESCR,instr(p.A_DESCR, '-')+1,LENGTH(p.A_DESCR) - LENGTH(SUBSTRING_INDEX(p.A_DESCR, '-', -1))- instr(p.A_DESCR, '-')-2));

-- sin tener en cuenta la gama
update t_producto p, T_COLOR c
set p.f_color_p_id = c.p_id
where p.tipo='TENIDO' and
      TRIM(c.A_NOMBRE) = TRIM(SUBSTR(p.A_DESCR,instr(p.A_DESCR, '-')+1,LENGTH(p.A_DESCR) - LENGTH(SUBSTRING_INDEX(p.A_DESCR, '-', -1))- instr(p.A_DESCR, '-')-2));
 
