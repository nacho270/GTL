-- ejecutar en A y en B

CALL FIX_FECHAS_ULT_SECTOR_ODT();

-- para chequear 

select * from tmp_trans_borrar;
select * from tmp_cambios_estado_borrar;

-- borrar 
drop table tmp_trans_borrar;
drop table tmp_cambios_estado_borrar;