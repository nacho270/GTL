update t_modulo set a_nombre = concat('Cheques - ',a_nombre) where p_id = 1
update t_modulo set a_nombre = concat('Contactos - ',a_nombre) where p_id in (2,3,4,5)
update t_modulo set a_nombre = concat('Productos - ',a_nombre) where p_id between 6 and 8
update t_modulo set a_nombre = concat('Configuración - ',a_nombre) where p_id between 9 and 16
update t_modulo set a_nombre = concat('Configuración - ',a_nombre) where p_id in (30,54)
update t_modulo set a_nombre = concat('Seguridad - ',a_nombre) where p_id in (17,18,31)
update t_modulo set a_nombre = concat('Cuentas - ',a_nombre) where p_id in (29,45,53)
update t_modulo set a_nombre = concat('Informes - ',a_nombre) where p_id in (35,56,58,59,60,61,64)
update t_modulo set a_nombre = concat('Aviso después de login - ',a_nombre) where p_id in (38,39,40,57)
update t_modulo set a_nombre = concat('Materias primas y stock - ',a_nombre) where p_id in (32,33,34,41,50,55)
update t_modulo set a_nombre = concat('Facturación - ',a_nombre) where p_id in (19,20,21,22,23,24,25,26,27,28,43)
update t_modulo set a_nombre = concat('Compras - ',a_nombre) where p_id in (36,37,42,44,47,49,63)
update t_modulo set a_nombre = concat('Pagos - ',a_nombre) where p_id in (46,48,51,52,62)