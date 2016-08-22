delete from t_pieza_rem_sal_pieza_odt where f_pieza_rem_sal_p_id in (
  select pr.p_id from t_pieza_remito pr where pr.f_remito_p_id in (
    select r.p_id from t_remito r where r.tipo = 'SAL' and r.f_factura_p_id is null
  )
);

delete from t_pieza_remito where f_remito_p_id in (
  select p_id from t_remito where tipo = 'SAL' and f_factura_p_id is null
);

delete from t_remito_salida_odt where f_remito_salida_p_id  in (
    select p_id from t_remito where tipo = 'SAL' and f_factura_p_id is null
);

delete from t_movs_stock where f_remito_sal_prov_p_id in (
    select p_id from t_remito where tipo = 'SAL' and f_factura_p_id is null
);

delete from t_item_remito_salida_prov where f_remito_sal_prov_p_id in (
  select p_id from t_remito where tipo = 'SAL' and f_factura_p_id is null
);

delete from t_remito where tipo = 'SAL' and f_factura_p_id is null;