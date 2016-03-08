-- CORRER EN LA A
grant all on gtl.t_terminacion_fraccionado to 'root'@'192.168.1.13' identified by 's4l3m';

-- CORRER EN LA B

INSERT INTO T_MODULO VALUES (102,'ar.com.textillevel.gui.modulos.odt.acciones.ModuloODT','ODT - Administrar ODTs',0);

RENAME TABLE gtltest.t_accion_procedimiento TO gtltest.t_accion_procedimiento_old;

CREATE TABLE  `t_accion_prodecimiento` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_ID_SECTOR` tinyint(4) NOT NULL,
  PRIMARY KEY  (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_accion_prodecimiento';

drop table t_procedimiento_odt;

RENAME TABLE gtltest.t_procedimiento_odt_old TO gtltest.t_procedimiento_odt;

RENAME TABLE gtltest.t_terminacion_fraccionado TO gtltest.t_terminacion_fraccionado_old; 

CREATE TABLE `t_terminacion_fraccionado` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_terminacion_fraccionado';


-- los anteriores ya se ejecutaron en PROD, ejecutar desde acá...

ALTER TABLE `gtl`.`t_map_prima_explotada` DROP FOREIGN KEY `FKB775F9987382370D`;
ALTER TABLE `gtl`.`t_instruccion_procedimiento` DROP FOREIGN KEY `FK3DA937A940906E2F`;

EJECUTAR JBOSS CON LA NUEVA VERSION

INSTALAR migrar_instr_a_instr_odt_issue_42.sql

CALL MIG_INSTR_TO_INSTR_ODT();

ALTER TABLE `gtl`.`t_instruccion_procedimiento` DROP COLUMN `F_PROC_ODT_P_ID`;



-- esto lo corrimos en la B
DROP TABLE IF EXISTS `gtltest`.`t_formula_tenido_explotada`;
CREATE TABLE  `gtltest`.`t_formula_tenido_explotada` (
  `P_ID` int(11) NOT NULL,
  `F_FORMULA_CLIENTE_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `gtltest`.`t_formula_estampado_explotada`;
CREATE TABLE  `gtltest`.`t_formula_estampado_explotada` (
  `P_ID` int(11) NOT NULL,
  `F_FORMULA_CLIENTE_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `gtltest`.`t_map_prima_explotada`;
CREATE TABLE  `gtltest`.`t_map_prima_explotada` (
  `P_ID` int(11) NOT NULL AUTO_INCREMENT,
  `A_CANTIDAD_EXPLOTADA` float DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  `F_MAT_PRIMA_CANT_P_ID` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXP_P_ID` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXPL_ANILINA` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXPL_PIGMENTOS` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXPL_QUIMICOS` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`),
  KEY `FKB775F998963ED4E4` (`F_MAT_PRIM_EXPL_PIGMENTOS`),
  KEY `FKB775F99866614658` (`F_MAT_PRIM_EXPL_QUIMICOS`),
  KEY `FKB775F998765297FF` (`F_MAT_PRIM_EXPL_ANILINA`),
  KEY `FKB775F9987382370D` (`F_MAT_PRIM_EXP_P_ID`),
  CONSTRAINT `FKB775F99866614658` FOREIGN KEY (`F_MAT_PRIM_EXPL_QUIMICOS`) REFERENCES `t_formula_estampado_explotada` (`P_ID`),
  CONSTRAINT `FKB775F998765297FF` FOREIGN KEY (`F_MAT_PRIM_EXPL_ANILINA`) REFERENCES `t_formula_tenido_explotada` (`P_ID`),
  CONSTRAINT `FKB775F998963ED4E4` FOREIGN KEY (`F_MAT_PRIM_EXPL_PIGMENTOS`) REFERENCES `t_formula_estampado_explotada` (`P_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `gtltest`.`t_instruccion_procedimiento_odt`;
CREATE TABLE  `gtltest`.`t_instruccion_procedimiento_odt` (
  `DISC` varchar(31) NOT NULL,
  `P_ID` int(11) NOT NULL AUTO_INCREMENT,
  `A_OBSERVACIONES` varchar(255) DEFAULT NULL,
  `A_ID_SECTOR` tinyint(4) NOT NULL,
  `A_VELOCIDAD` float DEFAULT NULL,
  `A_CANT_PASADAS` int(11) DEFAULT NULL,
  `A_TEMPERATURA` float DEFAULT NULL,
  `A_ESPECIFICACION` varchar(255) DEFAULT NULL,
  `A_ID_TIPO_PRODUCTO_P_ID` int(11) DEFAULT NULL,
  `F_ACCION_P_ID` int(11) DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  `F_FORMULA_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `F_PROC_ODT_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`),
  KEY `FK51AF9B8940906E2F` (`F_PROC_ODT_P_ID`),
  -- KEY `FK51AF9B89F5E7990D` (`F_TIPO_ARTICULO_P_ID`),
  CONSTRAINT `FK51AF9B8940906E2F` FOREIGN KEY (`F_PROC_ODT_P_ID`) REFERENCES `t_procedimiento_odt` (`P_ID`)
  -- CONSTRAINT `FK51AF9B89F5E7990D` FOREIGN KEY (`F_TIPO_ARTICULO_P_ID`) REFERENCES `t_tipo_articulo` (`P_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;