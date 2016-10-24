update t_usuario_sistema set a_codigo_usuario = 100 where a_usr_name = 'admin';
update t_usuario_sistema set a_codigo_usuario = 101 where a_usr_name = 'Gaby';
update t_usuario_sistema set a_codigo_usuario = 102 where a_usr_name = 'Diego';
update t_usuario_sistema set a_codigo_usuario = 103 where a_usr_name = 'usu';
update t_usuario_sistema set a_codigo_usuario = 104 where a_usr_name = 'nicolas84522';
update t_usuario_sistema set a_codigo_usuario = 105 where a_usr_name = 'Leila';

ALTER TABLE `gtl`.`t_usuario_sistema` ADD UNIQUE INDEX `IX_UNIQUE_CODIGO`(`A_CODIGO_USUARIO`);
