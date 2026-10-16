-- Facturas asociadas a notas de credito Y de debito (AFIP/ARCA exige CbtesAsoc en las ND).
--
-- Antes: la relacion NC/ND -> FC era la columna T_FACTURA.F_NOTA_CREDITO_P_ID (una factura solo podia
-- estar en UNA correccion). Ahora: tabla intermedia T_CORRECCION_FACTURA_REL (muchos a muchos),
-- mapeada en CorreccionFactura.getFacturasRelacionadas().
--
-- Correr ANTES de desplegar la version nueva (tambien funciona despues: es idempotente).
-- Las FKs a T_FACTURA (con sus indices) las agrega hbm2ddl (update) al desplegar.
-- La columna F_NOTA_CREDITO_P_ID NO se borra ni se vacia: queda sin mapear y permite volver a la version
-- anterior. Se eliminara (junto con sus FKs) en un script posterior.

CREATE TABLE IF NOT EXISTS T_CORRECCION_FACTURA_REL (
	F_CORRECCION_P_ID INT(11) NOT NULL,
	F_FACTURA_P_ID INT(11) NOT NULL,
	PRIMARY KEY (F_CORRECCION_P_ID, F_FACTURA_P_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

START TRANSACTION;

INSERT INTO T_CORRECCION_FACTURA_REL (F_CORRECCION_P_ID, F_FACTURA_P_ID)
SELECT f.F_NOTA_CREDITO_P_ID, f.P_ID
FROM T_FACTURA f
WHERE f.F_NOTA_CREDITO_P_ID IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM T_CORRECCION_FACTURA_REL r
                  WHERE r.F_CORRECCION_P_ID = f.F_NOTA_CREDITO_P_ID AND r.F_FACTURA_P_ID = f.P_ID);

COMMIT;

-- Control: vinculos_columna = vinculos_migrados y faltantes = 0
SELECT
	(SELECT COUNT(*) FROM T_FACTURA WHERE F_NOTA_CREDITO_P_ID IS NOT NULL) AS vinculos_columna,
	(SELECT COUNT(*) FROM T_CORRECCION_FACTURA_REL r
	   JOIN T_FACTURA f ON f.P_ID = r.F_FACTURA_P_ID AND f.F_NOTA_CREDITO_P_ID = r.F_CORRECCION_P_ID) AS vinculos_migrados,
	(SELECT COUNT(*) FROM T_FACTURA f
	   WHERE f.F_NOTA_CREDITO_P_ID IS NOT NULL
	     AND NOT EXISTS (SELECT 1 FROM T_CORRECCION_FACTURA_REL r
	                     WHERE r.F_CORRECCION_P_ID = f.F_NOTA_CREDITO_P_ID AND r.F_FACTURA_P_ID = f.P_ID)) AS faltantes;

-- Detalle por tipo de correccion
SELECT c.TIPO, COUNT(*) AS facturas_asociadas
FROM T_CORRECCION_FACTURA_REL r JOIN T_FACTURA c ON c.P_ID = r.F_CORRECCION_P_ID
GROUP BY c.TIPO;
