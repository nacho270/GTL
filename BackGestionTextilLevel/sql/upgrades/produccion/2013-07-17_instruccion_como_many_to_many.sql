INSERT INTO T_PROCEDIMIENTO_INSTRUCCION_ASOC (F_PROCEDIMIENTO_P_ID, F_INSTRUCCION_P_ID) SELECT F_PROCEDIMIENTO_P_ID, P_ID FROM T_INSTRUCCION_PROCEDIMIENTO;
ALTER TABLE t_instruccion_procedimiento MODIFY COLUMN F_PROCEDIMIENTO_P_ID INT(11) DEFAULT NULL; -- no me deja tirar el drop de la columna.

update t_instruccion_procedimiento ip
	inner join t_procedimiento_instruccion_asoc pias on pias.F_INSTRUCCION_P_ID = ip.p_id
	inner join t_procedimiento_tipo_articulo pta on pta.P_ID = pias.F_PROCEDIMIENTO_P_ID
	inner join t_proceso_tipo_maquina proc on pta.F_PROCESO_P_ID = proc.P_ID
	inner join t_tipo_maquina tm on tm.P_ID = proc.F_TIPO_MAQUINA_P_ID
set ip.A_ID_SECTOR = tm.P_ID;


ALTER TABLE t_formula_tenido_cliente MODIFY COLUMN F_CLIENTE_P_ID INT(11) DEFAULT NULL;

update T_PROCESO_TIPO_MAQUINA set A_REQUIERE_MUESTRA = 0;

update T_ANILINA_CANTIDAD set f_materia_prima_p_id=f_anilina_p_id;
update T_QUIMICO_CANTIDAD set f_materia_prima_p_id=f_quimico_p_id;
update T_PIGMENTO_CANTIDAD set f_materia_prima_p_id=f_anilina_p_id;

ALTER TABLE t_anilina_cantidad DROP FOREIGN KEY FK1090F6E0B140E814;
ALTER TABLE t_anilina_cantidad DROP INDEX FK1090F6E0B140E814;
ALTER TABLE t_anilina_cantidad DROP COLUMN F_ANILINA_P_ID;

ALTER TABLE t_quimico_cantidad DROP FOREIGN KEY FK8362447FB83544D6;
ALTER TABLE t_quimico_cantidad DROP INDEX FK8362447FB83544D6;
ALTER TABLE t_quimico_cantidad DROP COLUMN F_QUIMICO_P_ID;

ALTER TABLE t_pigmento_cantidad DROP FOREIGN KEY FKDE135AB379EA6645;
ALTER TABLE t_pigmento_cantidad DROP INDEX FKDE135AB379EA6645;
ALTER TABLE t_pigmento_cantidad DROP COLUMN F_ANILINA_P_ID;

ALTER TABLE T_QUIMICO_CANTIDAD MODIFY COLUMN F_INSTRUCCIONP_ID INT(11) DEFAULT NULL;

ALTER TABLE t_pigmento_cantidad DROP FOREIGN KEY FKDE135AB37042E151;

ALTER TABLE t_pigmento_cantidad ADD CONSTRAINT FKDE135AB37042E151 FOREIGN KEY FKDE135AB37042E151 (F_FORMULA_P_ID) REFERENCES t_formula_estampado_cliente (P_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;


