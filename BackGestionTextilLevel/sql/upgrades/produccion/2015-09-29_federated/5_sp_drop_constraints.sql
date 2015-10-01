DELIMITER $$
 DROP PROCEDURE IF EXISTS DROP_CONST$$
 CREATE PROCEDURE DROP_CONST()
       BEGIN
               DECLARE done INT DEFAULT FALSE;
               DECLARE TABLA_OWNER_CONST varchar(255);
               DECLARE CONSTRAINT_NAME varchar(255);
               DECLARE cur_drop_const CURSOR FOR select A_TABLA_OWNER_CONST, A_CONSTRAINT_NAME from T_MIG_CONSTRAINTS;
               DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

               -- LOOP DROP CONSTRAINTS
			   OPEN cur_drop_const;

               loop_tab: LOOP

                     FETCH cur_drop_const INTO TABLA_OWNER_CONST, CONSTRAINT_NAME;
                     IF done THEN
                        LEAVE loop_tab;
                     END IF;

                     SET @ddlq = CONCAT ('ALTER TABLE gtltest.',TABLA_OWNER_CONST,' DROP FOREIGN KEY ', CONSTRAINT_NAME, ' ;');
                     PREPARE stmt FROM @ddlq;
                     EXECUTE stmt;
                     DEALLOCATE PREPARE stmt;

               END LOOP loop_tab;
               CLOSE cur_drop_const;

       END$$
   DELIMITER ;