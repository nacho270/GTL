DELIMITER $$
 DROP PROCEDURE IF EXISTS DEBUG_MIGRAR_DATOS$$
 CREATE PROCEDURE DEBUG_MIGRAR_DATOS()
       BEGIN
               DECLARE done INT DEFAULT FALSE;
               DECLARE TABLA_DE_LA_A VARCHAR(255);
               DECLARE TABLA_EQUIV_DE_LA_A VARCHAR(255);
               DECLARE TABLA_DE_LA_B VARCHAR(255);
               DECLARE COLUMNA_CONST_EN_LA_B VARCHAR(255);

               DECLARE CANT INT;
               DECLARE ID_MP_ELIM INT;

               DECLARE cur_mp CURSOR FOR select A_NOMBRE_TABLA, A_NOMBRE_TABLA_EQUIV from T_MIG_TABLAS WHERE A_NIVEL=1;
               DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

               DELETE FROM T_DEBUG_MIG_CONSTRAINTS;

               OPEN cur_mp;

               loop_mp_rep: LOOP

                     FETCH cur_mp INTO TABLA_DE_LA_A, TABLA_EQUIV_DE_LA_A;
                     IF done THEN
                        LEAVE loop_mp_rep;
                     END IF;

                    BLOCK2: BEGIN
                    DECLARE done_cursor_2 INT DEFAULT FALSE;
                    DECLARE cur_mp_borrar CURSOR FOR select A_TABLA_OWNER_CONST, A_COLUMNA_CONSTRAINT from T_MIG_CONSTRAINTS WHERE A_TABLA_PADRE=TABLA_DE_LA_A AND NOT EXISTS (SELECT 1 FROM T_MIG_TABLAS MT WHERE MT.A_NOMBRE_TABLA=A_TABLA_OWNER_CONST);
                    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_cursor_2 = TRUE;

                    OPEN cur_mp_borrar;
                    loop_internal: LOOP

	                     IF done_cursor_2 THEN
	                        LEAVE loop_internal;
	                     END IF;

		                 FETCH cur_mp_borrar INTO TABLA_DE_LA_B, COLUMNA_CONST_EN_LA_B;
		                 SET @ddlq2 = CONCAT('INSERT INTO T_DEBUG_MIG_CONSTRAINTS (A_TABLA_PADRE, A_TABLA_OWNER_CONST, A_COLUMNA_CONSTRAINT, A_CANT_REGISTROS) SELECT ''',TABLA_DE_LA_A,''',''', TABLA_DE_LA_B,''',''',COLUMNA_CONST_EN_LA_B, ''', COUNT(*) FROM gtltest.', TABLA_DE_LA_B, ' TH LEFT JOIN gtltest.',TABLA_EQUIV_DE_LA_A, ' TP ON TP.P_ID_A=TH.',COLUMNA_CONST_EN_LA_B, ' WHERE TP.P_ID_A IS NULL;');
                     	 PREPARE stmt2 FROM @ddlq2;
                     	 EXECUTE stmt2;
                     	 DEALLOCATE PREPARE stmt2;

                    END LOOP loop_internal;
                    CLOSE cur_mp_borrar;
                    END BLOCK2;

               END LOOP loop_mp_rep;
               CLOSE cur_mp;

       END$$
   DELIMITER ;