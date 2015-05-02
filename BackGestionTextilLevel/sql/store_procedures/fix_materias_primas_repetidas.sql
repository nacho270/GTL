DELIMITER $$
 DROP PROCEDURE IF EXISTS FIX_MP_REPETIDAS$$
 CREATE PROCEDURE FIX_MP_REPETIDAS()
       BEGIN
               DECLARE done INT DEFAULT FALSE;
               DECLARE ID_MP INT;
               DECLARE MP_NOMBRE varchar(50);
               DECLARE CANT INT;
               DECLARE ID_MP_ELIM INT;

               DECLARE cur_mp CURSOR FOR select min(p_id) AS id_mp_preservar, count(*) AS cant_repetidos, replace(a_descripcion, ' ', '') from t_materia_prima group by replace(a_descripcion, ' ', '') having count(*) > 1;
               DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

               OPEN cur_mp;

               loop_mp_rep: LOOP

                     FETCH cur_mp INTO ID_MP, CANT, MP_NOMBRE;
                     IF done THEN
                        LEAVE loop_mp_rep;
                     END IF;

                    BLOCK2: BEGIN
                    DECLARE done_cursor_2 INT DEFAULT FALSE;
                    DECLARE cur_mp_borrar CURSOR FOR select p_id from t_materia_prima AS mp where replace(mp.a_descripcion, ' ', '') = MP_NOMBRE and mp.p_id <> ID_MP;
                    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_cursor_2 = TRUE;

                    OPEN cur_mp_borrar;
                    loop_internal: LOOP

                     IF done_cursor_2 THEN
                        LEAVE loop_internal;
                     END IF;

                    FETCH cur_mp_borrar INTO ID_MP_ELIM;

                    update t_anilina_cantidad set f_materia_prima_p_id =ID_MP where f_materia_prima_p_id = ID_MP_ELIM;
                    update t_item_factura_prov set f_materia_prima_p_id =ID_MP where f_materia_prima_p_id = ID_MP_ELIM;
                    update t_precio_materia_prima set f_materia_prima_p_id =ID_MP where f_materia_prima_p_id = ID_MP_ELIM;
                    update t_quimico_cantidad set f_materia_prima_p_id =ID_MP where f_materia_prima_p_id = ID_MP_ELIM;

                    delete from t_materia_prima where p_id = ID_MP_ELIM;

                    END LOOP loop_internal;
                    CLOSE cur_mp_borrar;
                    END BLOCK2;

               END LOOP loop_mp_rep;
               CLOSE cur_mp;

       END$$
   DELIMITER ;
