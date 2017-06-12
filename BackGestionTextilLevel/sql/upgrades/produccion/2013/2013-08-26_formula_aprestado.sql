ALTER TABLE `gtl`.`t_formula_tenido_cliente` MODIFY COLUMN `F_COLOR_P_ID` INT(11) DEFAULT NULL;
ALTER TABLE `gtl`.`t_formula_estampado_cliente` MODIFY COLUMN `F_COLOR_P_ID` INT(11) DEFAULT NULL;

update t_formula_tenido_cliente set A_NRO_FORMULA=0;
update t_formula_estampado_cliente set A_NRO_FORMULA=0;
update t_formula_aprestado_cliente set A_NRO_FORMULA=0;

update t_formula_tenido_cliente set A_CODIGO_FORMULA='SC' WHERE A_CODIGO_FORMULA IS NULL;
update t_formula_estampado_cliente set A_CODIGO_FORMULA='SC' WHERE A_CODIGO_FORMULA IS NULL;
update t_formula_aprestado_cliente set A_CODIGO_FORMULA='SC' WHERE A_CODIGO_FORMULA IS NULL;
