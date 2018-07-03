-- INSERT DE LOS MODULOS DISPONIBLES
INSERT INTO T_MODULO_TERMINAL (P_ID, A_CLASE, a_NOMBRE) VALUES(1, 'main.acciones.VerDialogoOrdenarPiezasODTAction', 'Cosido');
INSERT INTO T_MODULO_TERMINAL (P_ID, A_CLASE, a_NOMBRE) VALUES(2, 'main.acciones.VerLectorODTAction', 'Asignar metros piezas ODT');
INSERT INTO T_MODULO_TERMINAL (P_ID, A_CLASE, a_NOMBRE) VALUES(3, 'main.acciones.VerEntregaReingresoDocumentosAction', 'Entrega/Reingreso documentos');
INSERT INTO T_MODULO_TERMINAL (P_ID, A_CLASE, a_NOMBRE) VALUES(4, 'main.acciones.VerDialogoEntregarMercaderiaRemitoSalidaAction', 'Control de Mercadería');


-- INSERT DE LAS TERMINALES
INSERT INTO T_TERMINAL VALUES(1, 'TERMINAL COSIDO', 'por definir', NULL, '02');
INSERT INTO T_TERMINAL VALUES(2, 'TERMINAL ENTREGA', 'por definir', NULL, '03');
INSERT INTO T_TERMINAL VALUES(3, 'TERMINAL MATA', '192.168.1.106', 2, '01');
INSERT INTO T_TERMINAL VALUES(4, 'TERMINAL CONTROL DE SALIDA', 'por definir', NULL, '04');


-- INSERT DEL MODULO DE ASIGNACION DE TERMINALES
INSERT INTO T_MODULO VALUES (114,'ar.com.textillevel.gui.modulos.abm.GuiABMTerminal','Terminal - Administrar módulos de terminales',0,0,0);
