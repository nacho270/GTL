DELIMITER $$
 DROP PROCEDURE IF EXISTS FIX_FECHAS_ULT_SECTOR_ODT$$
 CREATE PROCEDURE FIX_FECHAS_ULT_SECTOR_ODT()
       BEGIN
               DECLARE done INT DEFAULT FALSE;
               DECLARE ID_TR INT;
			   DECLARE ID_ODT INT;
			   DECLARE ID_CA TINYINT;
			   DECLARE ID_CA_ANT TINYINT;
			   DECLARE FA DATETIME;
         DECLARE cur_mp CURSOR FOR select max(p_id) AS id_mp_preservar from t_transicion_odt group by f_odt_p_id;
         DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

                -- cambios de avance creados por transicion innecesario "en oficina"
			    CREATE TABLE `gtl`.`tmp_cambios_estado_borrar` (
			    `P_ID` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  				PRIMARY KEY (`P_ID`)
			    )ENGINE = InnoDB;

				insert into tmp_cambios_estado_borrar
				select ca.p_id
				from t_cambio_avance_odt ca
				where ca.a_id_avance=3 and not exists (select ca2.p_id from t_cambio_avance_odt ca2 where ca2.f_transicion_p_id=ca.f_transicion_p_id and ca.p_id != ca2.p_id);

			    -- transiciones que se insertaron al quedar la ODT como facturada
	            CREATE TABLE `gtl`.`tmp_trans_borrar` (
			    `P_ID` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  				PRIMARY KEY (`P_ID`)
			    )ENGINE = InnoDB;

			    insert into tmp_trans_borrar
				select tr.p_id
				from t_transicion_odt tr
				left join t_cambio_avance_odt ca on ca.f_transicion_p_id = tr.p_id
				where tr.f_maquina_p_id is null and ca.p_id is null;

				insert into tmp_trans_borrar
				select ca.f_transicion_p_id
				from t_cambio_avance_odt ca
				where ca.a_id_avance=3 and not exists (select ca2.p_id from t_cambio_avance_odt ca2 where ca2.f_transicion_p_id=ca.f_transicion_p_id and ca.p_id != ca2.p_id);

				-- borro transiciones y cambios de estados innecesarios
			   delete from t_cambio_avance_odt where p_id in (select p_id from tmp_cambios_estado_borrar);
			   delete from t_transicion_odt where p_id in (select p_id from tmp_trans_borrar);


               OPEN cur_mp;
               loop_mp_rep: LOOP

                     FETCH cur_mp INTO ID_TR;
                     IF done THEN
                        LEAVE loop_mp_rep;
                     END IF;

                    -- obtengo el id de la ODT
                    SELECT F_ODT_P_ID INTO ID_ODT FROM T_TRANSICION_ODT WHERE P_ID = ID_TR;

                    SET ID_CA_ANT = 4;

					BLOCK2: BEGIN
                    DECLARE done_cursor_2 INT DEFAULT FALSE;
                    DECLARE cur_mp_borrar CURSOR FOR select ca.a_id_avance, ca.a_fecha_hora from t_cambio_avance_odt ca where ca.f_transicion_p_id = ID_TR order by ca.a_fecha_hora desc, ca.a_id_avance desc ;
                    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_cursor_2 = TRUE;

                    OPEN cur_mp_borrar;
                    loop_internal: LOOP

                    IF done_cursor_2 THEN
                        LEAVE loop_internal;
                    END IF;
                    
                    FETCH cur_mp_borrar INTO ID_CA, FA;
                    
					IF ID_CA >= ID_CA_ANT THEN
                      SET done_cursor_2 = TRUE;
                    END IF;

                    IF ID_CA=3 and ID_CA<ID_CA_ANT THEN
                    	UPDATE T_ORDEN_DE_TRABAJO SET A_FECHA_FINALIZADO_ULT_SECTOR = FA WHERE P_ID = ID_ODT;
                    	SET ID_CA_ANT=3;
                    END IF;

                    IF (ID_CA=2 and ID_CA<ID_CA_ANT) THEN
                    	UPDATE T_ORDEN_DE_TRABAJO SET A_FECHA_EN_PROCESO_ULT_SECTOR = FA WHERE P_ID = ID_ODT;
                    	SET ID_CA_ANT=2;
                    END IF;

                    IF (ID_CA=1 and ID_CA<ID_CA_ANT) THEN
                    	UPDATE T_ORDEN_DE_TRABAJO SET A_FECHA_POR_COMENZAR_ULT_SECTOR = FA WHERE P_ID = ID_ODT;
                    	SET ID_CA_ANT=1;
                    END IF;


                    END LOOP loop_internal;
                    CLOSE cur_mp_borrar;
					END BLOCK2;

               END LOOP loop_mp_rep;
               CLOSE cur_mp;

       END$$
   DELIMITER ;