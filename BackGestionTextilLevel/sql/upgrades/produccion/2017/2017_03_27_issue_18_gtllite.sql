-- en A y B
update t_modulo_terminal set a_requiere_login=1;

-- sólo en A
insert into t_modulo_terminal values (5, 'main.acciones.ProcesarODTEnSectorHumedoAction', 'Sector Teñido', 0);
insert into t_modulo_terminal values (6, 'main.acciones.ProcesarODTEnSectorSecoAction', 'Sector Seco', 0);
insert into t_modulo_terminal values (7, 'main.acciones.ProcesarODTEnSectorEstampadoAction', 'Sector Estampería', 0);

-- en A y B
ALTER TABLE t_transicion_odt MODIFY COLUMN F_USUARIO_P_ID INT(11) DEFAULT NULL;
ALTER TABLE t_cambio_avance_odt MODIFY COLUMN F_USUARIO_P_ID INT(11) DEFAULT NULL;


