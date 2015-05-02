update t_orden_de_trabajo set A_ID_ESTADO = 0;

INSERT INTO t_tipo_maquina VALUES
 (1,'SECTOR COSIDO',1,1),
 (2,'SECTOR HUMEDO',2,2),
 (3,'SECTOR SECO',3,3),
 (4,'SECTOR ESTAMPADO',4,4),
 (5,'SECTOR TERMINADO',5,5);

insert into t_color_cilindro (A_NRO_CILINDRO, F_COLOR_P_ID, F_VARIANTE_P_ID) select -1, F_COLOR_P_ID, F_VARIANTE_P_ID FROM t_color_variante;
insert into t_color_cilindro (F_COLOR_P_ID, F_VARIANTE_P_ID) select F_COLOR_FONDO_P_ID, P_ID FROM T_VARIANTE;

ALTER TABLE `gtl`.`t_formula_tenido_cliente` MODIFY COLUMN `F_COLOR_P_ID` INT(11) DEFAULT NULL;
ALTER TABLE `gtl`.`t_formula_estampado_cliente` MODIFY COLUMN `F_COLOR_P_ID` INT(11) DEFAULT NULL;

ALTER TABLE t_formula_tenido_cliente MODIFY COLUMN F_CLIENTE_P_ID INT(11) DEFAULT NULL;

update t_formula_tenido_cliente set A_NRO_FORMULA=0;
update t_formula_estampado_cliente set A_NRO_FORMULA=0;
update t_formula_aprestado_cliente set A_NRO_FORMULA=0;

update t_formula_tenido_cliente set A_CODIGO_FORMULA='SC' WHERE A_CODIGO_FORMULA IS NULL;
update t_formula_estampado_cliente set A_CODIGO_FORMULA='SC' WHERE A_CODIGO_FORMULA IS NULL;
update t_formula_aprestado_cliente set A_CODIGO_FORMULA='SC' WHERE A_CODIGO_FORMULA IS NULL;

ALTER TABLE T_QUIMICO_CANTIDAD MODIFY COLUMN F_INSTRUCCIONP_ID INT(11) DEFAULT NULL;

ALTER TABLE t_pigmento_cantidad DROP FOREIGN KEY FKDE135AB37042E151;

ALTER TABLE t_pigmento_cantidad ADD CONSTRAINT FKDE135AB37042E151 FOREIGN KEY FKDE135AB37042E151 (F_FORMULA_P_ID) REFERENCES t_formula_estampado_cliente (P_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

INSERT INTO T_MODULO VALUES
 (103,'main.acciones.odt.VerEstadoProduccionAction','ODT - Ver Estado Producción',0,0),
 (104,'ar.com.textillevel.gui.modulos.odt.gui.GuiABMTipoMaquina','ODT - Administrar Tipos de Máquina',0,0),
 (106,'ar.com.textillevel.gui.modulos.odt.gui.tenido.VerAdministrarFormulasAction','ODT - Administrar Fórmulas de Teñido por Cliente',0,0),
 (107,'ar.com.textillevel.gui.modulos.odt.gui.secuencias.GuiABMSecuencias','ODT - Administrar Secuencias de Trabajo',0,0)
 ;
 
update t_articulo set f_tipo_articulo_p_id = 2 where a_nombre like 'AP %';
update t_articulo set f_tipo_articulo_p_id = 3 where a_nombre like 'AA %';
update t_articulo set f_tipo_articulo_p_id = 4 where a_nombre like 'PCL %';
update t_articulo set f_tipo_articulo_p_id = 5 where a_nombre like 'POL %';
update t_articulo set f_tipo_articulo_p_id = 6 where a_nombre like 'TAP %';

update t_articulo set f_tipo_articulo_p_id = 3 where f_tipo_articulo_p_id in (4,6); -- TAP/PERCAL => AP

delete from t_quimico_cantidad;
delete from t_anilina_cantidad;
delete from t_tenido_tipo_articulo;
delete from t_formula_tenido_cliente;
delete from t_maquina_tipo_articulo_asoc;
delete from t_procedimiento_instruccion_asoc;
delete from t_map_prima_explotada;
delete from t_instruccion_procedimiento ;
delete from t_paso_seceuncia;
delete from t_procedimiento_tipo_articulo;
delete from t_tipo_ani_tipo_art_asoc;
delete from t_tipo_articulo where p_id in (4,6);
delete from t_procedimiento_tipo_articulo;
delete from t_accion_prodecimiento;
update t_materia_prima set TIPO = 'MPVARIOS' WHERE P_ID = 150;
delete from t_paso_seceuncia_odt;
delete from T_SECUENCIA_ODT;
delete from T_SECUENCIA_TIPO_PRODUCTO;

insert into T_TIPO_ANI_TIPO_ART_ASOC values (2, 3);
insert into T_TIPO_ANI_TIPO_ART_ASOC values (2, 5);
insert into T_TIPO_ANI_TIPO_ART_ASOC values (1, 2);
insert into T_TIPO_ANI_TIPO_ART_ASOC values (1, 3);
insert into T_TIPO_ANI_TIPO_ART_ASOC values (3, 2);
insert into T_TIPO_ANI_TIPO_ART_ASOC values (3, 3);


