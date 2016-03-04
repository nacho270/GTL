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

