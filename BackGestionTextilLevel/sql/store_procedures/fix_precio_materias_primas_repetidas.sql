-- USE information_schema;
-- SELECT * FROM KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_NAME = 't_precio_materia_prima' AND REFERENCED_COLUMN_NAME = 'P_ID' AND TABLE_SCHEMA='gtl';

DELIMITER $$
 DROP PROCEDURE IF EXISTS FIX_PMP_REPETIDAS$$
 CREATE PROCEDURE FIX_PMP_REPETIDAS()
       BEGIN
               DECLARE done INT DEFAULT FALSE;
               DECLARE ID_MP INT;

               DECLARE CANT INT;
               DECLARE ID_MP_ELIM INT;

               DECLARE cur_mp CURSOR FOR select min(p_id) AS id_mp_preservar from t_precio_materia_prima group by f_materia_prima_p_id having count(*) > 1;
               DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

               OPEN cur_mp;

               loop_mp_rep: LOOP

                     FETCH cur_mp INTO ID_MP;
                     IF done THEN
                        LEAVE loop_mp_rep;
                     END IF;

                    BLOCK2: BEGIN
                    DECLARE done_cursor_2 INT DEFAULT FALSE;
                    DECLARE cur_mp_borrar CURSOR FOR select mp.p_id from t_precio_materia_prima AS mp inner join t_precio_materia_prima AS mp2 on mp2.f_materia_prima_p_id = mp.f_materia_prima_p_id where mp2.p_id = ID_MP AND mp.p_id <> ID_MP;
                    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_cursor_2 = TRUE;

                    OPEN cur_mp_borrar;
                    loop_internal: LOOP

                     IF done_cursor_2 THEN
                        LEAVE loop_internal;
                     END IF;

                    FETCH cur_mp_borrar INTO ID_MP_ELIM;

                    update t_item_correcc_fact_prov set F_PRECIO_MAT_PRIMA_P_ID =ID_MP where F_PRECIO_MAT_PRIMA_P_ID = ID_MP_ELIM;
                    update t_item_factura set F_PRECIO_MP_P_ID =ID_MP where F_PRECIO_MP_P_ID = ID_MP_ELIM;
                    update t_item_factura_prov set F_PRECIO_MAT_PRIMA_P_ID =ID_MP where F_PRECIO_MAT_PRIMA_P_ID = ID_MP_ELIM;
                    update t_item_remito_salida_prov set F_PRECIO_MP_P_ID =ID_MP where F_PRECIO_MP_P_ID = ID_MP_ELIM;
                    update t_movs_stock set F_PRECIO_MP_P_ID =ID_MP where F_PRECIO_MP_P_ID = ID_MP_ELIM;
                    update t_pieza_remito set F_PMP_DESC_STOCK_P_ID =ID_MP where F_PMP_DESC_STOCK_P_ID = ID_MP_ELIM;
                    update t_pieza_remito_entrada_prov set F_PRECIO_MP_P_ID =ID_MP where F_PRECIO_MP_P_ID = ID_MP_ELIM;
                    update t_rel_cont_prec_mat_prima set F_PRECIO_MAT_PRIMA_P_ID =ID_MP where F_PRECIO_MAT_PRIMA_P_ID = ID_MP_ELIM;
                    update t_remito set F_PMP_P_ID =ID_MP where F_PMP_P_ID = ID_MP_ELIM;

                    delete from t_precio_materia_prima where p_id = ID_MP_ELIM;

                    END LOOP loop_internal;
                    CLOSE cur_mp_borrar;
                    END BLOCK2;

               END LOOP loop_mp_rep;
               CLOSE cur_mp;

       END$$
   DELIMITER ;
