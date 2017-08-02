-- ejecutar en A
ALTER TABLE `gtl`.`t_articulo` DROP COLUMN `A_GRAMAJE`;

-- en B, recrear la tabla ya que es FEDERATED

show create table t_articulo;
drop table t_articulo;
CREATE TABLE `t_articulo` (
  `P_ID` int(11) NOT NULL,
  `A_DESC` varchar(255) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_ANCHO` decimal(19,2) DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED DEFAULT CHARSET=utf8 CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_articulo';
