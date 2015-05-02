update t_remito set a_fecha_emision = '2011-06-30' where p_id = 1518;
update t_remito set a_fecha_emision = '2012-07-31' where p_id = 3939;

update t_orden_de_trabajo set a_fecha_odt = (select a_fecha_emision from t_remito where p_id = f_remito_p_id);
update t_orden_de_trabajo set A_ID_ESTADO = 0;

-- hasta acá queda correctamente seteado [yyyy][mm]
update t_orden_de_trabajo odt, t_remito re
set odt.a_codigo=CONCAT(DATE_FORMAT(re.A_FECHA_EMISION, '%Y%m'), LPAD(DATE_FORMAT(re.A_FECHA_EMISION, '%d'), 2, '0'))
where re.p_id =odt.f_remito_p_id;

-- y hasta acá queda [yyyy][mm][nro_odt] pero los nros que son < a 10 quedan con un solo dígito
update t_orden_de_trabajo odt2, (SELECT odtr.p_id,CONCAT(SUBSTRING(odtr.A_codigo,1, 4),LPAD(SUBSTRING(odtr.A_codigo,5, 2),2,'0'),(SELECT COUNT(*) FROM t_orden_de_trabajo odtb  WHERE SUBSTRING(odtr.A_codigo,1, 4) = SUBSTRING(odtb.A_codigo,1, 4)  AND SUBSTRING(odtr.A_codigo,5, 2) = SUBSTRING(odtb.A_codigo,5, 2) AND odtb.p_id < odtr.p_id) + 1) as cododt FROM t_orden_de_trabajo AS odtr) odtnew
set odt2.a_codigo=odtnew.cododt
where odt2.p_id = odtnew.p_id;

-- se agrega un '0' a aquellos que tienen un nroodt < 10
update t_orden_de_trabajo o set o.a_codigo = CONCAT(SUBSTRING(o.A_codigo, 1, 6), LPAD(SUBSTRING(o.A_codigo,7, length(o.A_codigo)), 2, '0')) where length(SUBSTRING(o.A_codigo,7, length(o.A_codigo))) = 1;

-- creo un índice unique
ALTER TABLE `gtl`.`t_orden_de_trabajo` ADD UNIQUE INDEX `IX_UNIQUE_CODIGO`(`A_CODIGO`);


-- CAMBIOS TRANSICION

ALTER TABLE `gtl`.`t_transicion_odt` MODIFY COLUMN `F_TIPO_MAQUINA_P_ID` INTEGER DEFAULT NULL,  MODIFY COLUMN `F_MAQUINA_P_ID` INTEGER DEFAULT NULL;