drop table T_GRUPO_TIPO_ARTICULO_BASE;
drop table T_GRUPO_TIPO_ARTICULO_GAMA;
drop table T_PRECIO_TIPO_ARTICULO;

CREATE TABLE `t_grupo_tipo_articulo_base` (
  `P_ID` int(11) NOT NULL AUTO_INCREMENT,
  `F_RANGO_P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  `F_ARTICULO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED DEFAULT CHARSET=utf8 CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_grupo_tipo_articulo_base';

CREATE TABLE `t_grupo_tipo_articulo_gama` (
  `P_ID` int(11) NOT NULL AUTO_INCREMENT,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  `F_RANGO_P_ID` int(11) NOT NULL,
  `F_ARTICULO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED DEFAULT CHARSET=utf8 CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_grupo_tipo_articulo_gama';

CREATE TABLE `t_precio_tipo_articulo` (
  `P_ID` int(11) NOT NULL AUTO_INCREMENT,
  `A_PRECIO` float NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  `F_RANGO_P_ID` int(11) NOT NULL,
  `F_ARTICULO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED DEFAULT CHARSET=utf8 CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_precio_tipo_articulo';
