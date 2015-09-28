DELIMITER $$
 DROP PROCEDURE IF EXISTS MIGRAR_DATOS$$
 CREATE PROCEDURE MIGRAR_DATOS()
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

               OPEN cur_mp;

               loop_mp_rep: LOOP

                     FETCH cur_mp INTO TABLA_DE_LA_A, TABLA_EQUIV_DE_LA_A;
                     IF done THEN
                        LEAVE loop_mp_rep;
                     END IF;

                    BLOCK2: BEGIN
                    DECLARE done_cursor_2 INT DEFAULT FALSE;
                    DECLARE cur_mp_borrar CURSOR FOR select A_TABLA_OWNER_CONST, A_COLUMNA_CONSTRAINT from T_MIG_CONSTRAINTS WHERE A_TABLA_PADRE=TABLA_DE_LA_A;
                    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_cursor_2 = TRUE;

                    OPEN cur_mp_borrar;
                    loop_internal: LOOP

	                     IF done_cursor_2 THEN
	                        LEAVE loop_internal;
	                     END IF;

		                 FETCH cur_mp_borrar INTO TABLA_DE_LA_B, COLUMNA_CONST_EN_LA_B;

		                 --UPDATE TABLA_DE_LA_B INNER JOIN TABLA_EQUIV_DE_LA_A EQUIV ON COLUMNA_CONST_EN_LA_B = EQUIV.P_ID SET COLUMNA_CONST_EN_LA_B = EQUIV.P_ID_A;
		                 SET @ddlq2 = CONCAT ('UPDATE TABLE gtltest.',TABLA_DE_LA_B,' INNER JOIN ', TABLA_EQUIV_DE_LA_A, ' EQUIV ON ', COLUMNA_CONST_EN_LA_B, ' = EQUIV.P_ID SET ', COLUMNA_CONST_EN_LA_B, ' = EQUIV.P_ID_A;');
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