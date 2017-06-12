DELIMITER $$
 DROP PROCEDURE IF EXISTS RENAME_TABLAS$$
 CREATE PROCEDURE RENAME_TABLAS()
       BEGIN
               DECLARE done INT DEFAULT FALSE;
               DECLARE TABLA_RENAME varchar(255);
               DECLARE cur_rename CURSOR FOR SELECT A_NOMBRE_TABLA FROM T_MIG_TABLAS;
               -- DECLARE cur_rename CURSOR FOR SELECT distinct M1.TABLE_NAME FROM information_schema.KEY_COLUMN_USAGE M1 WHERE M1.CONSTRAINT_SCHEMA='gtltest' AND M1.TABLE_SCHEMA='gtltest' AND upper(M1.TABLE_NAME) like '%_OLD%';
               DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

               OPEN cur_rename;

               loop_rename: LOOP

                     FETCH cur_rename INTO TABLA_RENAME;
                     IF done THEN
                        LEAVE loop_rename;
                     END IF;

                     SET @ddlq2 = CONCAT ('RENAME TABLE gtltest.',TABLA_RENAME,' TO gtltest.', TABLA_RENAME, '_OLD;');
                     --  SET @ddlq2 = CONCAT ('RENAME TABLE gtltest.',TABLA_RENAME,' TO gtltest.',REPLACE(upper(TABLA_RENAME), '_OLD', ''));

                     PREPARE stmt2 FROM @ddlq2;
                     EXECUTE stmt2;
                     DEALLOCATE PREPARE stmt2;

               END LOOP loop_rename;
               CLOSE cur_rename;

       END$$
   DELIMITER ;