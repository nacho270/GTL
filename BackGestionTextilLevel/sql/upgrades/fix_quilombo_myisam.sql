SELECT CONCAT('ALTER TABLE ',table_schema,'.',table_name,' engine=InnoDB;')
FROM information_schema.tables
WHERE engine = 'MyISAM' and (table_schema = 'gtl' or table_schema = 'gtltest');