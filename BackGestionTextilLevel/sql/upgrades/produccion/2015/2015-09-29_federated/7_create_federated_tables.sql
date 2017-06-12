CREATE TABLE `t_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_RAZON_SOCIAL` varchar(50) NOT NULL,
  `A_TELEFONO` varchar(20) DEFAULT NULL,
  `A_CELULAR` varchar(20) DEFAULT NULL,
  `A_CUIT` varchar(50) DEFAULT NULL,
  `A_LOCALIDAD` varchar(100) DEFAULT NULL,
  `A_CONTACTO` varchar(50) DEFAULT NULL,
  `A_OBSERVACIONES` varchar(1000) DEFAULT NULL,
  `A_NROCLIENTE` int(11) DEFAULT NULL,
  `A_EMAIL` varchar(100) DEFAULT NULL,
  `A_SKYPE` varchar(50) DEFAULT NULL,
  `F_INFO_DIR_FISC_P_ID` int(11) DEFAULT NULL,
  `F_INFO_DIR_REAL_P_ID` int(11) DEFAULT NULL,
  `A_FAX` varchar(255) DEFAULT NULL,
  `A_ID_POSICION_IVA` int(11) DEFAULT NULL,
  `F_COND_VENTA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_cliente';


CREATE TABLE `t_condicion_venta` (
  `TIPO` varchar(31) NOT NULL,
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_DIAS_INICIALES` int(11) NOT NULL DEFAULT '0',
  `A_DIAS_FINALES` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_condicion_venta';

CREATE TABLE `t_direccion` (
  `P_ID` int(11) NOT NULL,
  `A_DIRECCION` varchar(255) NOT NULL,
  `F_INFO_LOCALIDAD_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_direccion';

CREATE TABLE `t_info_localidad` (
  `P_ID` int(11) NOT NULL,
  `A_COD_POSTAL` int(11) DEFAULT NULL,
  `A_NOMBRE_LOCALIDAD` varchar(255) DEFAULT NULL,
  `A_COD_AREA` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_info_localidad';

CREATE TABLE `t_dibujo` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_IMAGEN` blob,
  `A_NRO_DIBUJO` int(11) DEFAULT NULL,
  `A_ANCHO_CILINDRO` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_dibujo';


CREATE TABLE `t_variante` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `F_VARIANTE_P_ID` int(11) DEFAULT NULL,
  `F_COLOR_FONDO_P_ID` int(11) DEFAULT NULL,
  `A_IMAGEN` blob,
  `A_IMAGEN_ORIG` blob,
  `A_PORC_COBERTURA` float DEFAULT NULL,
  `F_GAMA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_variante';

CREATE TABLE `t_color_cilindro` (
  `P_ID` int(11) NOT NULL,
  `A_NRO_CILINDRO` int(11) DEFAULT NULL,
  `A_METROS_POR_COLOR` float DEFAULT NULL,
  `A_KILOS_POR_COLOR` float DEFAULT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  `F_VARIANTE_P_ID` int(11) NOT NULL,
  `A_X_COORD_DIBUJO` int(11) DEFAULT NULL,
  `A_Y_COORD_DIBUJO` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_color_cilindro';

CREATE TABLE `t_lista_de_precios` (
  `P_ID` int(11) NOT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_lista_de_precios';

CREATE TABLE `t_version_lista_precio` (
  `P_ID` int(11) NOT NULL,
  `A_INICIO_VALIDEZ` date NOT NULL,
  `F_LISTA_PRECIO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_version_lista_precio';

CREATE TABLE `t_definicion_precio_lista` (
  `P_ID` int(11) NOT NULL,
  `A_ID_TIPO_PRODUCTO` int(11) NOT NULL,
  `F_VERSION_LISTA_PRECIO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_definicion_precio_lista';


CREATE TABLE `t_rango_ancho_articulo` (
  `DTYPE` varchar(31) NOT NULL,
  `P_ID` int(11) NOT NULL,
  `A_ANCHO_EXACTO` float DEFAULT NULL,
  `A_ANCHO_MINIMO` float DEFAULT NULL,
  `A_ANCHO_MAXIMO` float DEFAULT NULL,
  `F_DEFINICION_PRECIO_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_rango_ancho_articulo';


CREATE TABLE `t_grupo_tipo_articulo_base` (
  `P_ID` int(11) NOT NULL,
  `F_RANGO_P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_grupo_tipo_articulo_base';

CREATE TABLE `t_precio_base` (
  `P_ID` int(11) NOT NULL,
  `F_GRUPO_P_ID` int(11) NOT NULL,
  `F_GAMA_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_precio_base';

CREATE TABLE `t_rango_cantidad_colores` (
  `P_ID` int(11) NOT NULL,
  `A_MAXIMO` int(11) NOT NULL,
  `A_MINIMO` int(11) NOT NULL,
  `F_PRECIO_BASE_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_rango_cantidad_colores';


CREATE TABLE `t_rango_cobertura_estampado` (
  `P_ID` int(11) NOT NULL,
  `A_MAXIMO` int(11) NOT NULL,
  `A_PRECIO` float NOT NULL,
  `A_MINIMO` int(11) NOT NULL,
  `F_RANGO_CANT_COLORES_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_rango_cobertura_estampado';

CREATE TABLE `t_grupo_tipo_articulo_gama` (
  `P_ID` int(11) NOT NULL,
  `F_RANGO_P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_grupo_tipo_articulo_gama';

CREATE TABLE `t_precio_gama` (
  `P_ID` int(11) NOT NULL,
  `A_PRECIO` float NOT NULL,
  `F_GRUPO_P_ID` int(11) NOT NULL,
  `F_GAMA_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `F_GAMA_DEFAULT_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_precio_gama';

CREATE TABLE `t_gama_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `F_CLIENTE_P_ID` int(11) NOT NULL,
  `F_GAMA_ORIGINAL_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_gama_cliente';

CREATE TABLE `t_gama_cliente_color_asoc` (
  `F_GAMA_CLIENTE_P_ID` int(11) NOT NULL,
  `F_COLOR_P_ID` int(11) NOT NULL
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_gama_cliente_color_asoc';

CREATE TABLE `t_cotizacion` (
  `P_ID` int(11) NOT NULL,
  `A_NUMERO` int(11) NOT NULL,
  `A_FECHA_INICIO` date NOT NULL,
  `A_VALIDEZ` int(11) NOT NULL,
  `F_VERSION_LISTA_PRECIOS_P_ID` int(11) NOT NULL,
  `F_CLIENTE_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_cotizacion';

CREATE TABLE `t_gama` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_gama';

CREATE TABLE `t_precio_tipo_articulo` (
  `P_ID` int(11) NOT NULL,
  `A_PRECIO` float NOT NULL,
  `F_RANGO_P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_precio_tipo_articulo';

CREATE TABLE `t_formula_aprestado_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_CODIGO_FORMULA` varchar(255) NOT NULL,
  `A_NRO_FORMULA` int(11) NOT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_formula_aprestado_cliente';

CREATE TABLE `t_quimico_cantidad` (
  `P_ID` int(11) NOT NULL,
  `A_CANTIDAD` float NOT NULL,
  `A_UNIDAD` int(11) DEFAULT NULL,
  `F_MATERIA_PRIMA_P_ID` int(11) NOT NULL,
  `F_INSTRUCCIONP_ID` int(11) DEFAULT NULL,
  `F_FORMULA_P_ID` int(11) DEFAULT NULL,
  `F_FORMULA_APRESTADO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_quimico_cantidad';

CREATE TABLE `t_formula_estampado_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_CODIGO_FORMULA` varchar(255) NOT NULL,
  `A_NRO_FORMULA` int(11) NOT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_formula_estampado_cliente';

CREATE TABLE `t_pigmento_cantidad` (
  `P_ID` int(11) NOT NULL,
  `A_CANTIDAD` float NOT NULL,
  `A_UNIDAD` int(11) DEFAULT NULL,
  `F_MATERIA_PRIMA_P_ID` int(11) NOT NULL,
  `F_FORMULA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_pigmento_cantidad';

CREATE TABLE `t_reactivo_cantidad` (
  `P_ID` int(11) NOT NULL,
  `A_CANTIDAD` float NOT NULL,
  `A_UNIDAD` int(11) DEFAULT NULL,
  `F_MATERIA_PRIMA_P_ID` int(11) NOT NULL,
  `F_TENIDO_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  `F_FORMULA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_reactivo_cantidad';

CREATE TABLE `t_formula_tenido_cliente` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_CODIGO_FORMULA` varchar(255) NOT NULL,
  `A_NRO_FORMULA` int(11) NOT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_formula_tenido_cliente';

CREATE TABLE `t_tenido_tipo_articulo` (
  `P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  `F_FORMULA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_tenido_tipo_articulo';

CREATE TABLE `t_anilina_cantidad` (
  `P_ID` int(11) NOT NULL,
  `A_CANTIDAD` float NOT NULL,
  `A_UNIDAD` int(11) DEFAULT NULL,
  `F_MATERIA_PRIMA_P_ID` int(11) NOT NULL,
  `F_TENIDO_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_anilina_cantidad';

CREATE TABLE `t_color` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `F_GAMA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_color';

CREATE TABLE `t_maquina` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `F_TIPO_MAQUINA_P_ID` int(11) NOT NULL,
  `DISC` varchar(31) DEFAULT NULL,
  `A_POTENCIA` float DEFAULT NULL,
  `A_ANCHO_MIN` float DEFAULT NULL,
  `A_ANCHO_MAX` float DEFAULT NULL,
  `A_CANT_CAMPOS` int(11) DEFAULT NULL,
  `A_CANT_COLORES` int(11) DEFAULT NULL,
  `A_TEMP_MIN` float DEFAULT NULL,
  `A_TEMP_MAX` float DEFAULT NULL,
  `A_TEMP_PROM` float DEFAULT NULL,
  `A_VEL_MIN` float DEFAULT NULL,
  `A_VEL_MAX` float DEFAULT NULL,
  `A_VEL_PROM` float DEFAULT NULL,
  `A_ALTA_TEMPERATURA` bit(1) DEFAULT NULL,
  `A_CAP_CARGA_MIN` float DEFAULT NULL,
  `A_CAP_CARGA_MAX` float DEFAULT NULL,
  `A_DIAM_TEJIDO_MAX` float DEFAULT NULL,
  `A_CANT_LITROS_MIN` float DEFAULT NULL,
  `A_CANT_LITROS_MAX` float DEFAULT NULL,
  `A_CANT_LITROS_PROM` float DEFAULT NULL,
  `A_DIAM_CILINDRO_MAX` float DEFAULT NULL,
  `A_CANT_RODILLOS` int(11) DEFAULT NULL,
  `A_CANT_PASADAS_MIN` int(11) DEFAULT NULL,
  `A_CANT_PASADAS_MAX` int(11) DEFAULT NULL,
  `A_PRESION_TRABAJO_MAX` float DEFAULT NULL,
  `F_TERM_FRACCIONADO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_maquina';

CREATE TABLE `t_maquina_tipo_articulo_asoc` (
  `F_MAQUINA_P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_maquina_tipo_articulo_asoc';

CREATE TABLE `t_tipo_maquina` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_ORDEN` tinyint(4) NOT NULL,
  `A_ID_SECTOR` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_tipo_maquina';

CREATE TABLE `t_proceso_tipo_maquina` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_ORDEN` int(11) DEFAULT NULL,
  `F_TIPO_MAQUINA_P_ID` int(11) NOT NULL,
  `A_REQUIERE_MUESTRA` bit(1) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_proceso_tipo_maquina';

CREATE TABLE `t_terminacion_fraccionado` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_terminacion_fraccionado';

CREATE TABLE `t_procedimiento_tipo_articulo` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  `F_PROCESO_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_procedimiento_tipo_articulo';

CREATE TABLE `t_procedimiento_instruccion_asoc` (
  `F_PROCEDIMIENTO_P_ID` int(11) NOT NULL,
  `F_INSTRUCCION_P_ID` int(11) NOT NULL
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_procedimiento_instruccion_asoc';

CREATE TABLE `t_instruccion_procedimiento` (
  `DISC` varchar(31) NOT NULL,
  `P_ID` int(11) NOT NULL,
  `A_OBSERVACIONES` varchar(255) DEFAULT NULL,
  `A_ID_SECTOR` tinyint(4) NOT NULL,
  `A_VELOCIDAD` float DEFAULT NULL,
  `A_CANT_PASADAS` int(11) DEFAULT NULL,
  `A_TEMPERATURA` float DEFAULT NULL,
  `A_ESPECIFICACION` varchar(255) DEFAULT NULL,
  `A_ID_TIPO_PRODUCTO_P_ID` int(11) DEFAULT NULL,
  `F_FORMULA_CLIENTE_P_ID` int(11) DEFAULT NULL,
  `F_ACCION_P_ID` int(11) DEFAULT NULL,
  `F_PROC_ODT_P_ID` int(11) DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  `F_PROCESO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
    ) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_instruccion_procedimiento';

CREATE TABLE `t_procedimiento_odt` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_procedimiento_odt';

CREATE TABLE `t_secuencia_tipo_producto` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) DEFAULT NULL,
  `A_ID_TIPO_PRODUCTO` int(11) NOT NULL,
  `F_CLIENTE_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_secuencia_tipo_producto';

CREATE TABLE `t_paso_seceuncia` (
  `P_ID` int(11) NOT NULL,
  `A_OBSERVACIONES` varchar(255) DEFAULT NULL,
  `F_PROCESO_P_ID` int(11) NOT NULL,
  `F_SECTOR_P_ID` int(11) NOT NULL,
  `F_PROCEDIMIENTO_P_ID` int(11) NOT NULL,
  `F_SECUENCIA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
    ) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_paso_seceuncia';

CREATE TABLE `t_proveedores` (
  `P_ID` int(11) NOT NULL,
  `A_RAZON_SOCIAL` varchar(255) NOT NULL,
  `A_TELEFONO` varchar(255) NOT NULL,
  `A_CELULAR` varchar(255) DEFAULT NULL,
  `A_CUIT` varchar(255) NOT NULL,
  `A_CONTACTO` varchar(255) DEFAULT NULL,
  `A_OBSERVACIONES` varchar(1000) DEFAULT NULL,
  `A_NOMBRE_CORTO` varchar(255) DEFAULT NULL,
  `F_INFO_DIR_FISC_P_ID` int(11) DEFAULT NULL,
  `F_INFO_DIR_REAL_P_ID` int(11) DEFAULT NULL,
  `A_EMAIL` varchar(100) DEFAULT NULL,
  `A_SKYPE` varchar(50) DEFAULT NULL,
  `A_FAX` varchar(255) DEFAULT NULL,
  `F_RUBRO_P_ID` int(11) DEFAULT NULL,
  `A_SITIO_WEB` varchar(255) DEFAULT NULL,
  `A_ID_POSICION_IVA` int(11) DEFAULT NULL,
  `A_NRO_ING_BRUTOS` varchar(255) DEFAULT NULL,
  `F_COND_VENTA_P_ID` int(11) DEFAULT NULL,
  `F_PROVINCIA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
      ) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_proveedores';

CREATE TABLE `t_provincia` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_provincia';

CREATE TABLE `t_servicio` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `F_PROVEEDOR_P_ID` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_servicio';

CREATE TABLE `t_articulo` (
  `P_ID` int(11) NOT NULL,
  `A_DESC` varchar(255) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_GRAMAJE` decimal(19,2) DEFAULT NULL,
  `A_ANCHO` decimal(19,2) DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_articulo';

CREATE TABLE `t_tipo_articulo` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_SIGLA` varchar(255) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_tipo_articulo';

CREATE TABLE `t_tipo_art_tipo_art_asoc` (
  `P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_PADRE_P_ID` int(11) NOT NULL
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_tipo_art_tipo_art_asoc';

CREATE TABLE `t_persona` (
  `P_ID` int(11) NOT NULL,
  `A_TELEFONO` varchar(20) DEFAULT NULL,
  `A_CELULAR` varchar(20) DEFAULT NULL,
  `A_CONTACTO` varchar(50) DEFAULT NULL,
  `A_OBSERVACIONES` varchar(1000) DEFAULT NULL,
  `A_EMAIL` varchar(100) DEFAULT NULL,
  `A_NOMBRES` varchar(100) NOT NULL,
  `A_APELLIDO` varchar(100) NOT NULL,
  `F_RUBRO_P_ID` int(11) NOT NULL,
  `F_INFO_DIRECCION_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_persona';

CREATE TABLE `t_rubro` (
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) DEFAULT NULL,
  `A_TIPO_RUBRO` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_rubro';

CREATE TABLE `t_materia_prima` (
  `TIPO` varchar(31) NOT NULL,
  `P_ID` int(11) NOT NULL,
  `A_DESCRIPCION` varchar(50) NOT NULL,
  `A_UNIDAD` int(11) NOT NULL,
  `A_OBSERVACIONES` varchar(3000) DEFAULT NULL,
  `A_CONCENTRACION` decimal(19,2) DEFAULT NULL,
  `A_COLOR_INDEX` int(11) DEFAULT NULL,
  `A_HEXA_COLOR` varchar(255) DEFAULT NULL,
  `F_TIPO_ANILINA_P_ID` int(11) DEFAULT NULL,
  `F_ARTICULO_P_ID` int(11) DEFAULT NULL,
  `A_ANCHO_CABEZAL` decimal(19,2) DEFAULT NULL,
  `A_ANCHO_CILINDRO` decimal(19,2) DEFAULT NULL,
  `A_MESH_CILINDRO` decimal(19,2) DEFAULT NULL,
  `A_DIAMETRO_CILINDRO` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_materia_prima';

CREATE TABLE `t_tipo_anilina` (
  `P_ID` int(11) NOT NULL,
  `A_DESCRIPCION` varchar(255) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_tipo_anilina';

CREATE TABLE `t_tipo_ani_tipo_art_asoc` (
  `F_TIPO_ANILINA_P_ID` int(11) NOT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) NOT NULL
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_tipo_ani_tipo_art_asoc';

CREATE TABLE `t_lista_precios_prov` (
  `P_ID` int(11) NOT NULL,
  `F_PROVEEDOR_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_lista_precios_prov';

CREATE TABLE `t_precio_materia_prima` (
  `P_ID` int(11) NOT NULL,
  `A_ALIAS` varchar(255) NOT NULL,
  `A_FECHA_ULT_MODIF` datetime NOT NULL,
  `A_PRECIO` decimal(19,2) NOT NULL,
  `F_MATERIA_PRIMA_P_ID` int(11) DEFAULT NULL,
  `F_LISTA_DE_PRECIOS_P_ID` int(11) DEFAULT NULL,
  `A_TIPO_MONEDA_ID` int(11) DEFAULT NULL,
  `A_SIGLAS` varchar(255) DEFAULT NULL,
  `A_STOCK_ACTUAL` decimal(19,2) DEFAULT NULL,
  `A_PUNTO_PEDIDO` decimal(19,2) DEFAULT NULL,
  `A_VERSION` int(11) DEFAULT NULL,
  `A_STOCK_INICIAL_DISP` decimal(19,2) DEFAULT NULL,
  `A_STOCK_INICIAL` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_precio_materia_prima';

CREATE TABLE `t_contenedor_mat_prima` (
  `TIPO` varchar(31) NOT NULL,
  `P_ID` int(11) NOT NULL,
  `A_NOMBRE` varchar(255) NOT NULL,
  `A_CAPACIDAD` decimal(19,2) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_contenedor_mat_prima';

CREATE TABLE `t_rel_cont_prec_mat_prima` (
  `P_ID` int(11) NOT NULL,
  `A_VERSION` int(11) DEFAULT NULL,
  `A_STOCK_ACTUAL` decimal(19,2) NOT NULL,
  `F_PRECIO_MAT_PRIMA_P_ID` int(11) DEFAULT NULL,
  `F_CONT_MAT_PRIMA_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_rel_cont_prec_mat_prima';

CREATE TABLE `t_producto` (
  `TIPO` varchar(31) NOT NULL,
  `P_ID` int(11) NOT NULL,
  `A_DESCR` varchar(255) NOT NULL,
  `F_GAMA_P_ID` int(11) DEFAULT NULL,
  `F_DIBUJO_P_ID` int(11) DEFAULT NULL,
  `F_ARTICULO_P_ID` int(11) DEFAULT NULL,
  `F_VARIANTE_P_ID` int(11) DEFAULT NULL,
  `F_COLOR_P_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_producto';

CREATE TABLE `t_map_prima_explotada` (
  `P_ID` int(11) NOT NULL,
  `A_CANTIDAD_EXPLOTADA` float DEFAULT NULL,
  `F_TIPO_ARTICULO_P_ID` int(11) DEFAULT NULL,
  `F_MAT_PRIMA_CANT_P_ID` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXP_P_ID` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXPL_ANILINA` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXPL_PIGMENTOS` int(11) DEFAULT NULL,
  `F_MAT_PRIM_EXPL_QUIMICOS` int(11) DEFAULT NULL,
  PRIMARY KEY (`P_ID`)
  ) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@IP_SERVER_A:3306/gtl/t_map_prima_explotada';
  
  
  
