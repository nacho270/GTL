-- toca una tabla federated => actualizo la estructura de la tabla en la B
show create table t_dibujo;

drop table t_dibujo;

CREATE TABLE `t_dibujo` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_IMAGEN` blob,
  `A_NRO_DIBUJO` int(11) DEFAULT NULL,
  `A_ANCHO_CILINDRO` decimal(19,2) DEFAULT NULL,
  `A_ID_ESTADO` int(11) NOT NULL,
  `A_FACTURA_P_ID` int(11),
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `F_REMITO_SALIDA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED DEFAULT CHARSET=utf8 CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_dibujo';

select * from t_dibujo;
