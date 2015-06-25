-- MySQL Administrator dump 1.4
-- 
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema gtl
--

CREATE DATABASE IF NOT EXISTS gtl;
USE gtl;

--
-- Definition of table 'T_MODULO'
--

DROP TABLE IF EXISTS T_MODULO;
CREATE TABLE 'T_MODULO' (
  'P_ID' int(11) NOT NULL,
  'A_CLASS' varchar(255) default NULL,
  'A_NOMBRE' varchar(255) default NULL,
  PRIMARY KEY  ('P_ID')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'T_MODULO'
--

/*!40000 ALTER TABLE 'T_MODULO' DISABLE KEYS */;
INSERT INTO T_MODULO VALUES 
 (1,'ar.com.textillevel.gui.modulos.cheques.ModuloCheques','Administrar Cheques',0),
 (2,'ar.com.textillevel.gui.modulos.abm.gente.GuiABMCliente','Administrar Clientes',0),
 (3,'ar.com.textillevel.gui.modulos.abm.gente.GuiABMProveedor','Administrar Proveedores',0),
 (4,'ar.com.textillevel.gui.modulos.abm.gente.GuiABMPersona','Administrar Personas',0),
 (5,'ar.com.textillevel.gui.modulos.agenda.ModuloAgenda','Agenda',0),
 (6,'ar.com.textillevel.gui.modulos.abm.GuiABMProductos','Administrar Productos',0),
 (7,'ar.com.textillevel.gui.modulos.abm.GuiABMListaDePrecios','Administrar Listas de Precios',0),
 (8,'ar.com.textillevel.gui.modulos.abm.GuiABMArticulos','Administrar Artículos',0),
 (10,'ar.com.textillevel.gui.modulos.abm.gente.GuiABMInfoLocalidad','Administrar Localidades',0),
 (11,'ar.com.textillevel.gui.modulos.abm.gente.GuiABMRubro','Administrar Rubros',0),
 (12,'ar.com.textillevel.gui.modulos.abm.GuiABMColor','Administrar Colores',0),
 (13,'ar.com.textillevel.gui.modulos.abm.GuiABMGamas','Administrar Gamas de colores',0),
 (14,'ar.com.textillevel.gui.modulos.abm.GuiABMDibujoEstampado','Administrar Dibujos de Estampados',0),
 (15,'ar.com.textillevel.gui.modulos.abm.GuiABMBancos','Administrar Bancos',0),
 (16,'ar.com.textillevel.gui.modulos.abm.GuiABMCondicionDeVenta','Administrar Condiciones de venta',0),
 (17,'ar.com.textillevel.gui.modulos.abm.portal.GuiABMPerfil','Administrar Perfiles de usuario',0),
 (18,'ar.com.textillevel.gui.modulos.abm.portal.GuiABMUsuarioSistema','Administrar usuarios',0),
 (19,'main.acciones.facturacion.AgregarRemitoEntradaAction','Agregar Remito de Entrada',0),
 (20,'main.acciones.facturacion.AgregarRemitoSalidaAction','Agregar Remito de Salida',0),
 (21,'main.acciones.facturacion.AgregarFacturaAction','Agregar factura',0),
 (22,'main.acciones.facturacion.AgregarCorreccionAction','Agregar nota de débito o crédito',0),
 (23,'main.acciones.facturacion.AgregarReciboAction','Agregar Recibo',0),
 (24,'main.acciones.facturacion.ConsultarFacturaAction','Consultar factura',0), 
 (25,'main.acciones.facturacion.ConsultarCorreccionesAction','Consultar notas de crédito y débito',0),
 (26,'main.acciones.facturacion.ConsultarRemitoEntradaAction','Consultar remito de entrada',0),
 (27,'main.acciones.facturacion.ConsultarRemitoSalidaAction','Consultar remito de salida',0),
 (28,'main.acciones.facturacion.ConsultarReciboAction','Consultar recibo',0),
 (29,'main.acciones.cuentas.VerMovimientosCuentaAction','Ver movimientos de cuentas',0),
 (30,'main.acciones.parametrosgenerales.ModificarParametrosGeneralesAction','Modificar parametros generales',0),
 (31,'ar.com.textillevel.gui.modulos.eventos.ModuloVisorEventos','Visor de eventos',0),
 (32,'ar.com.textillevel.gui.modulos.abm.materiaprima','Administrar Materias primas',0),
 (33,'ar.com.textillevel.gui.modulos.abm.materiaprima.GuiABMTipoAnilina','Administrar tipos de anilina',0),
 (34,'ar.com.textillevel.gui.modulos.abm.GuiABMListaDePreciosProveedor','Administrar Lista de Precios',0),
 (35,'main.acciones.informes.InformeIVAVentasAction','IVA - Ventas',0),
 (36,'main.acciones.compras.AgregarRemitoEntradaProveedorAction','Agregar remito de entrada de proveedor',0),
 (37,'main.acciones.compras.ConsultarRemitoEntradaProveedorAction','Consultar remito de Entrada de proveedor',0),
 (38,'main.triggers.TriggerParametrosGenerales','Configurar Parametros Generales faltantes',1),
 (39,'main.triggers.TriggerActualizarChequesVencidos','Actualizar Cheques Vencidos',1),
 (40,'main.triggers.TriggerMostrarChequesPorVencer','Mostrar Cheques por vencer',1),
 (41,'ar.com.textillevel.gui.modulos.stock.ModuloStock','Controlar Stock',0);
 (42,'main.acciones.compras.AgregarFacturaProveedorAction','Agregar Factura de Proveedor',0),
 (43,'main.acciones.facturacion.AccionAgregarFacturaSinRemitoAction','Agregar factura sin remito',0),
 (44,'main.acciones.compras.ConsultarFacturaProveedorAction','Consultar Factura de Proveedor',0),
 (45,'main.acciones.cuentas.VerMovimientosCuentaProveedorAction','Ver movimientos de cuentas de proveedores',0),
 (46,'main.acciones.pagos.CargarOrdenDePagoAction','Cargar orden de pago',0),
 (47,'main.acciones.compras.AgregarRemitoSalidaProveedorAction','Agregar remito de salida de proveedor',0),
 (48,'main.acciones.pagos.ConsultarOrdenDePagoAction','Consultar órden de pago',0),
 (49,'main.acciones.compras.AgregarNotaCreditoProveedorAction','Agregar nota de crédito de proveedor',0),
 (50,'ar.com.textillevel.gui.modulos.abm.materiaprima.GuiABMContenedoresMatPrima','Administrar Contenedores',0),
 (51,'main.acciones.pagos.CargarOrdenDeDepositoAction','Cargar órden de depósito',0),
 (52,'main.acciones.pagos.ConsultarOrdenDeDepositoAction','Consultar órden de depósito',0),
 (53,'main.acciones.cuentas.VerMovimientosCuentaBancoAction','Ver movimientos de cuentas de bancos',0),
 (54,'ar.com.textillevel.gui.modulos.abm.GuiABMImpuestos','Administrar Impuestos',0),
 (55,'main.acciones.stock.RealizarBajaDeStockAction','Realizar baja de stock',0),
 (56,'main.acciones.informes.InformeProduccionAction','Informe de producción',0),
 (57,'main.triggers.TriggerMostrarClientesConMuchaDeuda','Ver clientes con mucha deuda',1),
 (58,'main.acciones.informes.InformeMorososAction','Informe de clientes con deuda',0),
 (59,'main.acciones.informes.InformeIVAComprasAction','IVA - Compras',0),
 (60,'main.acciones.informes.InformeDeudasConProveedoresAction','Informe de deudas con proveedores',0),
 (61,'main.acciones.informes.InformeStockAction','Informe de stock',0), 
 (62,'main.acciones.pagos.EditarOrdenDePagoAction','Editar orden de pago',0),
 (63,'main.acciones.compras.AgregarFacturaSinRemitoProveedorAction','Factura sin remito',0),
 (64,'main.acciones.informes.InformeRecibosAction','Informe de Recibos',0),
 (65,'ar.com.textillevel.gui.modulos.abm.portal.GuiABMAccionesPorPerfil','Seguridad - Administrar Acciones por Perfil', 0),
 (66,'main.acciones.pagos.CargarOrdenDePagoAPersonaAction','Pagos - Cargar órden de pago a persona',0),
 (67,'main.acciones.cuentas.VerMovimientosCuentaPersonaAction','Cuentas - Ver movimientos de cuentas de personas',0),
 (68,'main.acciones.facturacion.AgregarRemitoSalidaStockAction','Facturación - Agregar Remito de Salida de 01',0),
 (69,'main.acciones.cuentas.VerMovimientosCuentaArticuloAction','Cuentas - Ver movimientos de cuentas de telas',0),
 (70,'main.acciones.facturacion.AgregarRemitoSalidaStockAction','Facturación - Agregar Remito de Entrada de 01',0),
 (71,'main.acciones.facturacion.CompletarPiezasRemitoEntradaStockAction','Facturación - Completar Piezas de Remito de Entrada',0),
 (72,'main.acciones.pagos.CargarFacturaDePersonaAction','Pagos - Cargar factura a persona',0),
 (73,'main.acciones.facturacion.AgregarRemitoEntradaVentaTelaAction','Facturación - Agregar Remito de Entrada (Venta de Tela)',0),
 (74,'main.acciones.facturacion.AgregarRemitoSalidaVentaTelaAction','Facturación - Agregar Remito de Salida (Venta de Tela)',0)
 (75,'ar.com.textillevel.gui.modulos.abm.GuiABMServicios','Configuración - Administrar Servicios',0),
 (76,'main.acciones.compras.AgregarFacturaProveedorServicioAction','Compras - Agregar Factura de Servicios',0),
 (77,'ar.com.textillevel.gui.modulos.personal.abm.empleados.GuiABMEmpleados','Personal - Administrar empleados',0),
 (78,'ar.com.textillevel.gui.modulos.personal.abm.configuracion.GuiABMArt','Personal - Administrar ARTs',0),
 (79,'ar.com.textillevel.gui.modulos.personal.abm.configuracion.GuiABMEmpresaSeguros','Personal - Administrar empresas de seguro',0),
 (80,'ar.com.textillevel.gui.modulos.personal.abm.calenlaboral.GuiAdministrarCalendarioLaboral','Personal - Administrar calendario laboral',0),
 (81,'ar.com.textillevel.gui.modulos.personal.abm.tareas.GuiABMSindicato','Personal - Administrar sindicatos',0),
 (82,'ar.com.textillevel.gui.modulos.personal.abm.tareas.GuiABMPuesto','Personal - Administrar puestos',0),
 (83,'ar.com.textillevel.gui.modulos.personal.abm.tareas.GuiABMCategoria','Personal - Administrar categorías',0),
 (84,'ar.com.textillevel.gui.modulos.personal.modulos.legajos.ModuloLegajos','Personal - Administrar legajos',0),
 (85,'ar.com.textillevel.gui.modulos.personal.abm.tareas.GuiABMObraSocial','Personal - Administrar Obras Sociales',0),
 (86,'ar.com.textillevel.gui.modulos.personal.abm.tareas.GuiABMConfigValorHoraCategoria','Personal - Configurar Valor Hora por Puesto',0),
 (87,'ar.com.textillevel.gui.modulos.personal.abm.contribuciones.GuiABMContribuciones','Personal - Administrar Contribuciones',0),
 (88,'ar.com.textillevel.gui.modulos.personal.abm.antiguedad.GuiABMConfiguracionAntiguedades','Personal - Configurar Antigüedades',0),
 (89,'ar.com.textillevel.gui.modulos.personal.abm.conceptosrecibosueldo.GuiABMConceptosReciboSueldo','Personal - Configurar conceptos recibo de sueldo',0),
 (90,'ar.com.textillevel.gui.modulos.personal.abm.asignaciones.GuiABMAsignaciones','Personal - Administrar Asignaciones',0),
 (91,'main.acciones.personal.parametrosgenerales.ModificarParametrosGeneralesPersonalAction','Personal - Modificar parametros generales',0),
 (92,'main.triggers.personal.TriggerParametrosGeneralesPersonal','Aviso despues de login - Personal - Configurar Parametros Generales faltantes',1),
 (93,'main.triggers.personal.TriggerContratosPorVencer','Aviso despues de login - Personal - Ver contratos por vencer',1),
 (94,'ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.ModuloReciboSueldo','Personal - Recibos de Sueldo',0),
 (95,'main.acciones.cuentas.CorregirCuentaClienteAction','Cuentas - Corregir cuentas de clientes',0),
 (96,'ar.com.textillevel.gui.modulos.personal.abm.presentismo.GuiABMPresentismo','Personal - Adminitrar Presentismo',0),
 (97,'ar.com.textillevel.gui.modulos.personal.modulos.vales.ModuloValesAnticipo','Personal - Adminitrar vales anticipo',0),
 (98,'ar.com.textillevel.gui.modulos.personal.abm.vacaciones.GuiABMConfiguracionVacaciones','Personal - Configurar vacaciones',0),
 (99,'main.acciones.personal.informes.InformesVacacionesPersonalAction','Personal - Informe de vacaciones',0),
 (100,'main.triggers.TriggerVencimientoNumeracionFactura','Aviso despues de login - Verificar vencimientos de numeraciones de factura',1),
 (101,'main.acciones.personal.informes.PlanificacionVacacionesPersonalAction','Personal - Planificación de vacaciones',0),
 (102,'ar.com.textillevel.gui.modulos.odt.acciones.ModuloODT','ODT - Administrar ODTs',0),
 (103,'main.acciones.odt.VerEstadoProduccionAction','ODT - Ver Estado Producción',0),
 (104,'ar.com.textillevel.gui.modulos.odt.gui.GuiABMTipoMaquina','ODT - Administrar Tipos de Máquina',0),
 (105,'ar.com.textillevel.gui.modulos.odt.gui.GuiABMMaquina','ODT - Administrar Máquinas',0),
 (106,'ar.com.textillevel.gui.modulos.odt.gui.tenido.VerAdministrarFormulasAction','ODT - Administrar Fórmulas de Teñido por Cliente',0),
 (107,'ar.com.textillevel.gui.modulos.odt.gui.secuencias.GuiABMSecuencias','ODT - Administrar Secuencias de Trabajo',0),
 (108,'main.servicios.alertas.ServicioAlertas','Servicios - Servicio de Alertas',0,1),
 (109,'ar.com.textillevel.gui.modulos.abm.portal.GuiAsignacionAlertasPerfil','Seguridad - Administrar Alertas para Perfiles de usuario',0,0),
 (110,'main.acciones.cuentas.VerFacturasSinAutorizacionAFIP','Cuentas - Visualizar facturas sin autorizacion de AFIP',0,0),
 (111,'main.acciones.cuentas.VerEstadoServerAFIPAction','Cuentas - Visualizar estado de servicios AFIP',0,0)
 ;

 
/*!40000 ALTER TABLE 'T_MODULO' ENABLE KEYS */;


--
-- Definition of table 'T_PERFIL'
--

DROP TABLE IF EXISTS T_PERFIL;
CREATE TABLE 'T_PERFIL' (
  'P_ID' int(11) NOT NULL auto_increment,
  'A_NOMBRE' varchar(255) NOT NULL,
  PRIMARY KEY  ('P_ID')
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'T_PERFIL'
--

/*!40000 ALTER TABLE 'T_PERFIL' DISABLE KEYS */;
INSERT INTO T_PERFIL ('P_ID','A_NOMBRE') VALUES 
 (1,'admin'),
 (2,'usuario');
/*!40000 ALTER TABLE 'T_PERFIL' ENABLE KEYS */;


--
-- Definition of table 'T_PERFIL_MODULO'
--

DROP TABLE IF EXISTS T_PERFIL_MODULO;
CREATE TABLE 'T_PERFIL_MODULO' (
  'F_PERFIL_P_ID' int(11) NOT NULL,
  'F_MODULO_P_ID' int(11) NOT NULL,
  KEY 'FKAE220B9E3F7D47B7' ('F_PERFIL_P_ID'),
  KEY 'FKAE220B9EA55AA1F7' ('F_MODULO_P_ID'),
  CONSTRAINT 'FKAE220B9EA55AA1F7' FOREIGN KEY ('F_MODULO_P_ID') REFERENCES T_MODULO ('P_ID'),
  CONSTRAINT 'FKAE220B9E3F7D47B7' FOREIGN KEY ('F_PERFIL_P_ID') REFERENCES T_PERFIL ('P_ID')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'T_PERFIL_MODULO'
--

/*!40000 ALTER TABLE 'T_PERFIL_MODULO' DISABLE KEYS */;
INSERT INTO T_PERFIL_MODULO ('F_PERFIL_P_ID','F_MODULO_P_ID') VALUES 
 (1,8),
 (1,15),
 (1,1),
 (1,2),
 (1,12),
 (1,16),
 (1,14),
 (1,13),
 (1,7),
 (1,10),
 (1,17),
 (1,4),
 (1,6),
 (1,3),
 (1,11),
 (1,18),
 (1,5),
 (1,21),
 (1,22),
 (1,23),
 (1,19),
 (1,20),
 (1,24),
 (1,25),
 (1,28),
 (1,26),
 (1,27),
 (1,30),
 (1,29),
 (2,2),
 (2,4),
 (2,3),
 (2,5);
/*!40000 ALTER TABLE 'T_PERFIL_MODULO' ENABLE KEYS */;


--
-- Definition of table 'T_USUARIO_SISTEMA'
--

DROP TABLE IF EXISTS T_USUARIO_SISTEMA;
CREATE TABLE T_USUARIO_SISTEMA (
  'P_ID' int(11) NOT NULL auto_increment,
  'A_PASSWORD' varchar(255) NOT NULL,
  'A_USR_NAME' varchar(255) NOT NULL,
  'F_PERFIL_P_ID' int(11) NOT NULL,
  PRIMARY KEY  ('P_ID'),
  KEY 'FKB30D3BE63F7D47B7' ('F_PERFIL_P_ID'),
  CONSTRAINT 'FKB30D3BE63F7D47B7' FOREIGN KEY ('F_PERFIL_P_ID') REFERENCES T_PERFIL ('P_ID')
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'T_USUARIO_SISTEMA'
--

/*!40000 ALTER TABLE 'T_USUARIO_SISTEMA' DISABLE KEYS */;
INSERT INTO T_USUARIO_SISTEMA ('P_ID','A_PASSWORD','A_USR_NAME','F_PERFIL_P_ID') VALUES 
 (1,'!#/)zW¥§C‰JJ€Ã','admin',1),
 (3,'ø-\\®=âÎÈ‡ó•ìšj','usuario',2);
/*!40000 ALTER TABLE 'T_USUARIO_SISTEMA' ENABLE KEYS */;

/*Insert de tipo de evento*/

INSERT INTO t_tipoevento VALUES(1,1,'Alta Genérica'),
INSERT INTO t_tipoevento VALUES(2,1,'Baja Genérica'),
INSERT INTO t_tipoevento VALUES(3,3,'Modif. Genérica'),
INSERT INTO t_tipoevento VALUES(4,4,'Anulación');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

