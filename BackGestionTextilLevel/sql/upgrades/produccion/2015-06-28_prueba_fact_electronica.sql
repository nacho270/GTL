update t_factura f
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura =f.a_nro_factura*(-1),
    rem.a_nro_factura = f.a_nro_factura*(-1);

update t_factura f set f.a_nro_factura = f.a_nro_factura*(-1) where f.a_nro_factura > 0;

update T_PARAM_GENERALES set A_NRO_COMIENZO_FACTURA = 0, A_NRO_SUCURSAL=2;

update t_remito r set a_nro_factura=(-1)*a_nro_factura where a_nro_factura > 0;

ALTER TABLE `gtl`.`t_factura` DROP INDEX `IX_UNIQUE_NRO_FC`;
ALTER TABLE `gtl`.`t_factura` ADD UNIQUE INDEX `IX_UNIQUE_NRO_FC`(`A_ID_TIPO_FACTURA`, `TIPO`, `A_NRO_FACTURA`);
