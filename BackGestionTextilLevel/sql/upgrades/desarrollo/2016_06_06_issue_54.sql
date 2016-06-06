-- EJECUTAR EN LA B
DROP TABLE `t_formula_estampado_cliente`;

CREATE TABLE `t_formula_estampado_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_CODIGO_FORMULA` varchar(255) NOT NULL,
  `A_NRO_FORMULA` int(11) NOT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `A_VERIFICADA` bit(1) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_formula_estampado_cliente';


DROP TABLE `t_formula_tenido_cliente`;

CREATE TABLE `t_formula_tenido_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_CODIGO_FORMULA` varchar(255) NOT NULL,
  `A_NRO_FORMULA` int(11) NOT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  `A_VERIFICADA` bit(1) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_formula_tenido_cliente';


DROP TABLE `t_formula_aprestado_cliente`;

CREATE TABLE `t_formula_aprestado_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_CODIGO_FORMULA` varchar(255) NOT NULL,
  `A_NRO_FORMULA` int(11) NOT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `A_VERIFICADA` bit(1) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_formula_aprestado_cliente';