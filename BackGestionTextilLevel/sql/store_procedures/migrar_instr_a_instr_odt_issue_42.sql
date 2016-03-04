DELIMITER $$
 DROP PROCEDURE IF EXISTS MIG_INSTR_TO_INSTR_ODT$$
 CREATE PROCEDURE MIG_INSTR_TO_INSTR_ODT()
       BEGIN
	       
               DECLARE done INT DEFAULT FALSE;
               DECLARE ID_INSTR INT;
               DECLARE ID_INSTR_NEW INT;

               DECLARE CANT INT;

               DECLARE cur_instr CURSOR FOR select P_ID AS id_INS_VIEJO from t_instruccion_procedimiento where disc = 'IPP';
               DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
               
	           -- actualizo las de tipo texto y las borro
               insert into t_instruccion_procedimiento_odt (disc, a_observaciones, a_id_sector, a_velocidad, a_cant_pasadas, a_temperatura,a_especificacion, a_id_tipo_producto_p_id, f_formula_cliente_p_id,f_accion_p_id, f_proc_odt_p_id, f_tipo_articulo_p_id)
			   select 'IPPTODT', a_observaciones, a_id_sector, a_velocidad, a_cant_pasadas, a_temperatura,a_especificacion, a_id_tipo_producto_p_id, f_formula_cliente_p_id,f_accion_p_id, f_proc_odt_p_id, f_tipo_articulo_p_id   from t_instruccion_procedimiento where disc = 'IPPT' and f_proc_odt_p_id is not null;
			   
			   delete from t_instruccion_procedimiento where disc = 'IPPT' and f_proc_odt_p_id is not null;

               -- actualizo las de tipo producto y las borro
			   insert into t_instruccion_procedimiento_odt (disc, a_observaciones, a_id_sector, a_velocidad, a_cant_pasadas, a_temperatura,a_especificacion, a_id_tipo_producto_p_id, f_formula_cliente_p_id,f_accion_p_id, f_proc_odt_p_id, f_tipo_articulo_p_id)
			   select 'IPTPODT', a_observaciones, a_id_sector, a_velocidad, a_cant_pasadas, a_temperatura,a_especificacion, a_id_tipo_producto_p_id, f_formula_cliente_p_id,f_accion_p_id, f_proc_odt_p_id, f_tipo_articulo_p_id   from t_instruccion_procedimiento where disc = 'IPTP' and f_proc_odt_p_id is not null;

			   delete from t_instruccion_procedimiento where disc = 'IPTP' and f_proc_odt_p_id is not null;

			   -- y ahora las de pasadas

               
               OPEN cur_instr;

               loop_mp_rep: LOOP

                    FETCH cur_instr INTO ID_INSTR;
                    IF done THEN
                        LEAVE loop_mp_rep;
                    END IF;

					insert into t_instruccion_procedimiento_odt (disc, a_observaciones, a_id_sector, a_velocidad, a_cant_pasadas, a_temperatura,a_especificacion, a_id_tipo_producto_p_id, f_formula_cliente_p_id,f_accion_p_id, f_proc_odt_p_id, f_tipo_articulo_p_id)
					select 'IPPODT', a_observaciones, a_id_sector, a_velocidad, a_cant_pasadas, a_temperatura,a_especificacion, a_id_tipo_producto_p_id, f_formula_cliente_p_id,f_accion_p_id, f_proc_odt_p_id, f_tipo_articulo_p_id  from t_instruccion_procedimiento where P_ID=ID_INSTR;

					SELECT MAX(P_ID) INTO ID_INSTR_NEW FROM T_INSTRUCCION_PROCEDIMIENTO_ODT WHERE DISC='IPPODT';
					
					-- actualizo las instrucciones padre de las materias primas explotadas
					UPDATE T_MAP_PRIMA_EXPLOTADA SET F_MAT_PRIM_EXP_P_ID = ID_INSTR_NEW WHERE F_MAT_PRIM_EXP_P_ID = ID_INSTR;

               END LOOP loop_mp_rep;
               CLOSE cur_instr;
               
               -- borro las de tipo pasadas
               delete from t_instruccion_procedimiento where disc = 'IPP' and f_proc_odt_p_id is not null;

       END$$
   DELIMITER ;