update t_factura set a_nro_sucursal = 1 where a_nro_factura < 0;
update t_factura set a_nro_sucursal = 2 where a_nro_factura > 0;

update t_remito r set a_nro_sucursal = 1 where a_nro_factura is not null and a_nro_factura < 0;
update t_remito r set a_nro_sucursal = 2 where a_nro_factura is not null and a_nro_factura > 0;

update t_factura f
inner join t_remito rem on rem.f_factura_p_id = f.p_id
set f.a_nro_factura = f.a_nro_factura*(-1),
    rem.a_nro_factura = f.a_nro_factura*(-1)
 where f.a_nro_sucursal = 1 and f.a_id_tipo_factura = 1;

update t_factura f set f.a_nro_factura = f.a_nro_factura*(-1)
where f.a_nro_factura < 0 and f.a_nro_sucursal = 1 and f.a_id_tipo_factura = 1;

ALTER TABLE `gtl`.`t_factura` DROP INDEX `IX_UNIQUE_NRO_FC`;
ALTER TABLE `gtl`.`t_factura` ADD UNIQUE INDEX `IX_UNIQUE_NRO_FC`(`A_ID_TIPO_FACTURA`, `TIPO`, `A_NRO_FACTURA`, `A_NRO_SUCURSAL`);
