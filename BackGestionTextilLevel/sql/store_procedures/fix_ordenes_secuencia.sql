DELIMITER $$
 DROP PROCEDURE IF EXISTS FIX_ORDEN_SECUENCIA$$
 CREATE PROCEDURE FIX_ORDEN_SECUENCIA()
       BEGIN
               DECLARE done INT DEFAULT FALSE;
               DECLARE ID_SEQ INT;
               DECLARE ID_SEQ2 INT;

               DECLARE cur_mp CURSOR FOR select P_ID AS id_mp_preservar from T_PASO_SECEUNCIA;
               DECLARE cur_mp2 CURSOR FOR select P_ID AS id_mp_preservar2 from T_PASO_SECEUNCIA_ODT;
               DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

               OPEN cur_mp;
               loop_mp_rep: LOOP

                     FETCH cur_mp INTO ID_SEQ;
                     IF done THEN
                        LEAVE loop_mp_rep;
                     END IF;

                    update T_PASO_SECEUNCIA PPSS
					inner join (
							SELECT @rownum:=@rownum + 1 as row_number, ps2.p_id as iddd
							FROM T_PASO_SECEUNCIA ps2, (SELECT @rownum := -1) r
							WHERE ps2.f_secuencia_p_id = ID_SEQ
					) t
					set PPSS.A_ORDEN = t.row_number
					where t.iddd = PPSS.p_id;
                     
               END LOOP loop_mp_rep;
               CLOSE cur_mp;

               SET done = FALSE;
               
               OPEN cur_mp2;
               loop_mp_rep2: LOOP

                     FETCH cur_mp2 INTO ID_SEQ2;
                     IF done THEN
                        LEAVE loop_mp_rep2;
                     END IF;

                    update T_PASO_SECEUNCIA_ODT PPSS
					inner join (
							SELECT @rownum:=@rownum + 1 as row_number, ps2.p_id as iddd
							FROM T_PASO_SECEUNCIA_ODT ps2, (SELECT @rownum := -1) r
							WHERE ps2.f_secuencia_p_id = ID_SEQ2
					) t
					set PPSS.A_ORDEN = t.row_number
					where t.iddd = PPSS.p_id;
                     
               END LOOP loop_mp_rep2;
               CLOSE cur_mp2;

       END$$
   DELIMITER ;
